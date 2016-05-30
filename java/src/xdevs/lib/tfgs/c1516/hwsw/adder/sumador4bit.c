/*
 * Copyright (C) 2016 Miguel Higuera Romero
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/string.h>
#include <linux/gpio.h>
#include <asm-generic/uaccess.h>
#include <linux/delay.h>

// Maximum buffer length for the user-space buffer
#define BUF_LEN 100
// States for the GPIO pines
#define OFF_ST 0
#define ON_ST 1
// True and false constants
#ifndef TRUE
#define TRUE 1
#endif
#ifndef FALSE
#define FALSE 0
#endif
// Sum states and results constants
#define S4B_PROCESSING -40
#define S4B_ERROR -50
// Success constant
#ifndef SUCCESS
#define SUCCESS 0
#endif

/* Module data */
MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("Sumador4bit Kernel Module - FDI - Universidad Complutense de Madrid");
MODULE_AUTHOR("Miguel Higuera Romero");

/* Proc entry for the driver */
static struct proc_dir_entry *proc_entry_sumador4bit;

/* Result value */
int sum;

/* GPIO pin constants */
const unsigned int Apin[4] = {26,19,13,06};
const unsigned int Bpin[4] = {17,27,22,14};
const unsigned int Cpin[5] = {05,15,18,23,24};

/* Function prototypes */
void decimalToArray(int num, int *Arr);
void readC(void);
int getC(void);
void sendValues(int *A, int *B);
short init_gpio_pines(void);
void free_all_gpio(void);

/* Sets the given binary array (previously set with 0) with the given integer converted to binary */
void decimalToArray(int num, int *Arr){
	int index = 0, aux = num;
	while(aux > 0 && index < 4){
		Arr[index] = (aux % 2);
		aux = aux / 2;
		index++;
	}
}

/* Wait a microsecond to the adder and reads C value */
void readC(void){
	/* Wait for the sum */
	udelay(1);	//Retardo de 1 microsegundo
	/* Read C */
	sum = getC();
}

/**** GPIO FUNCTIONS ****/
/* Reads the C value from the GPIO pines and returns the integer value */
int getC(void){
	int index = 0, aux, num = 0, i;
	while(index < 5){
		if(gpio_get_value(Cpin[index]) == ON_ST){
			aux = 1;
			for(i=0; i < index; i++){
				aux *= 2;
			}
			num += aux;
		}
		index++;
	}
	return num;
}

/* Sends the A and B values to the GPIO pines */
void sendValues(int *A, int *B){
	int i;
	for(i=0; i<4; i++){
		gpio_set_value(Apin[i], A[i]);
		gpio_set_value(Bpin[i], B[i]);
	}
}

/* Initializes the linux GPIO pines
 * Returns TRUE if GPIO pines are requested or FALSE otherwise
 */
short init_gpio_pines(void){
	short i = 0;
	// Init A
	while( i < 4){
		if(gpio_request(Apin[i], "GPIOA") != 0 || gpio_direction_output(Apin[i], OFF_ST) != 0){
			return FALSE;
		}
		i++;
	}
	// Init B /
	i = 0;
	while(i < 4){
		if(gpio_request(Bpin[i], "GPIOB") != 0 || gpio_direction_output(Bpin[i], OFF_ST) != 0){
			return FALSE;
		}
		i++;
	}
	// Init C /
	i = 0;
	while(i < 5){
		if(gpio_request(Cpin[i], "GPIOC") != 0 || gpio_direction_input(Cpin[i]) != 0){
			return FALSE;
		}
		i++;
	}

	return TRUE;
}

/* Free al GPIO pines */
void free_all_gpio(void){
	int i;
	for(i=0; i < 4; i++){
		gpio_free(Apin[i]);
		gpio_free(Bpin[i]);
		gpio_free(Cpin[i]);
	}
	gpio_free(Cpin[4]);
}

/**** PROC ENTRY FUNCTIONS ****/
/* Reads the A and B values from the user space and manages the communication with the extern circuit */
static ssize_t sumador4bit_write(struct file *filp, const char __user *buf, size_t len, loff_t *off) {
    char kbuff[BUF_LEN];	     //Copia de buffer de usuario
	int numA, numB;
	int A[4] = {0,0,0,0}, B[4] = {0,0,0,0};

    /* The application can write in this entry just once */
    if ((*off) > 0)
        return 0;

	/* Checks the buffer len */
    if (len > BUF_LEN - 1) {
        printk(KERN_INFO "Sumador4bit: not enough space!!\n");
        return -ENOSPC;
    }

    /* Transfer data from user to kernel space */
    if (copy_from_user( &kbuff[0], buf, len ))
        return -EFAULT;

    kbuff[len] = '\0'; 	/* Add the '\0' */
    *off+=len;            /* Update the file pointer */

    /* Sets the sum to processing */
    sum = S4B_PROCESSING;

	/* Parse the buffer */
    if(sscanf(&kbuff[0],"add %i %i\n", &numA, &numB)){
		/* Check errors */
        if(numA > 15 || numB > 15 || numA < 0 || numB < 0){
			sum = S4B_ERROR;
			printk(KERN_INFO "Sumador4bit: Invalid values");
        	return -EINVAL;
        }
        /* Send A and B */
    	decimalToArray(numA, A);
    	decimalToArray(numB, B);
    	sendValues(A, B);
    	/* Read C */
    	readC();
        /* Inform kernel */
        printk(KERN_INFO "Sumador4bit: Sent %i + %i, received %i\n", numA, numB, sum);

    } else {
		/* Set sum with ERROR */
    	sum = S4B_ERROR;
        printk(KERN_INFO "Sumador4bit: Invalid option - %s", kbuff);
        return -EINVAL;
    }
    return len;
}

/* Returns the C value to the user space */
static ssize_t sumador4bit_read(struct file *filp, char __user *buf, size_t len, loff_t *off) {
    int nr_bytes;
    char kbuff[BUF_LEN];
    kbuff[0] = '\0';

	/* Tell the application that there is nothing left to read */
    if ((*off) > 0)
        return 0;

    /* Charges the C value */
    sprintf(kbuff, "%i", sum);
    nr_bytes = strlen(kbuff);

    /* Send data to the user space */
    if (copy_to_user(buf, kbuff, nr_bytes))
        return -EINVAL;

    (*off)+=nr_bytes;  /* Update the file pointer */

    /* Inform */
    printk(KERN_INFO "Sumador4bit: Read C value.\n");

    return nr_bytes;
}

/* An application opens the device file */
static int sumador4bit_open (struct inode *nodo, struct file *fich){
	/* Avoid the module exit */
	try_module_get(THIS_MODULE);
	return SUCCESS;
}

/* An application closes the device file */
static int sumador4bit_release (struct inode *nodo, struct file *fich){
	/* Allow the module exit */
	module_put(THIS_MODULE);
	return SUCCESS;
}

/* Proc operations implemented */
static const struct file_operations proc_entry_sumador4bit_fops = {
	.open = sumador4bit_open,
	.release = sumador4bit_release,
    .read = sumador4bit_read,
    .write = sumador4bit_write,
};

/**** MODULE FUNCTIONS ****/
/* Initializes the module */
int init_sumador4bit_module( void )
{
    /* Create the proc entry */
    proc_entry_sumador4bit = proc_create( "sumador4bit", 0666, NULL, &proc_entry_sumador4bit_fops);
    if (proc_entry_sumador4bit == NULL) {
        printk(KERN_INFO "Sumador4bit: Can't create /proc entry\n");
        return -ENOMEM;
    }

    /* Inits the GPIO pines */
    if(!init_gpio_pines()){
    	remove_proc_entry("sumador4bit", NULL);
    	free_all_gpio();
        printk(KERN_INFO "Sumador4bit: GPIO error\n");
        return -EBUSY;
    }

	/* Inits */
	sum = OFF_ST;

    printk(KERN_INFO "Sumador4bit: Module loaded\n");
    return SUCCESS;
}

/* Finalizes the module */
void exit_sumador4bit_module( void )
{
	/* Remove proc entry */
    remove_proc_entry("sumador4bit", NULL);

    /* Free GPIO pines */
	free_all_gpio();

    /* Inform */
    printk(KERN_INFO "Sumador4bit: Module unloaded.\n");
}

module_init( init_sumador4bit_module );
module_exit( exit_sumador4bit_module );
