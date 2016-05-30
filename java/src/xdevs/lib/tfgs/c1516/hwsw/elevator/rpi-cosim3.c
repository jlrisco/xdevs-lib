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
// Success constant
#ifndef SUCCESS
#define SUCCESS 0
#endif

/* Module data */
MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("Elevator Kernel Module - FDI - Universidad Complutense de Madrid");
MODULE_AUTHOR("Miguel Higuera Romero");

/* Proc entry for the driver */
static struct proc_dir_entry *proc_entry_elevator;

/* GPIO pin constants */
const unsigned int dataSPin[6] = {18,15,14,7,8,25}; // {A1,A2,A3,B1,B2,B3}
const unsigned int dataRPin[2] = {16,12}; // {EN,UD}

/* Function prototypes */
short init_gpio_pines(void);
void free_all_gpio(void);

/**** GPIO FUNCTIONS ****/

/* Initializes the linux GPIO pines
 * Returns TRUE if GPIO pines are requested or FALSE otherwise
 */
short init_gpio_pines(void){
	short i = 0;
	// Init data sender
	while( i < 6){
		if(gpio_request(dataSPin[i], "GPIOPINS") != 0 || gpio_direction_output(dataSPin[i], OFF_ST) != 0){
			return FALSE;
		}
		i++;
	}
	// Init data receiver
	i = 0;
	while(i < 2){
		if(gpio_request(dataRPin[i], "GPIOPINR") != 0 || gpio_direction_input(dataRPin[i]) != 0){
			return FALSE;
		}
		i++;
	}
	return TRUE;
}

/* Free al GPIO pines */
void free_all_gpio(void){
	int i;
	for(i=0; i < 6; i++){
		gpio_free(dataSPin[i]);
	}
	for(i=0; i < 2; i++)
		gpio_free(dataRPin[i]);
}


/**** PROC ENTRY FUNCTIONS ****/

/* Reads the A and B values from the user space and manages the communication with the extern circuit */
static ssize_t elevator_write(struct file *filp, const char __user *buf, size_t len, loff_t *off) {
    char kbuff[BUF_LEN];	     //Copia de buffer de usuario
	int value, rest, temp, i, data;

	/* Checks the buffer len */
    if (len > BUF_LEN - 1) {
        printk(KERN_INFO "Elevator: not enough space!!\n");
        return -ENOSPC;
    }

    /* Transfer data from user to kernel space */
    if (copy_from_user( &kbuff[0], buf, len ))
        return -EFAULT;

    kbuff[len] = '\0'; 	/* Add the '\0' */
    *off+=len;            /* Update the file pointer */

	/* Parse the buffer */
    if(sscanf(&kbuff[0],"0b%d", &value)){
		temp = value;
		i=0;
		while(i < 6){
			rest = temp % 10;
			data = (rest == 0) ? 0 : 1;
			gpio_set_value(dataSPin[i], data);
			temp /= 10;
			i++;
		}
        printk(KERN_INFO "Elevator: Sent 0b%i\n", value);

    } else {
        printk(KERN_INFO "Elevator: Invalid option - %s", kbuff);
        return -EINVAL;
    }
    return len;
}

/* Returns the C value to the user space */
static ssize_t elevator_read(struct file *filp, char __user *buf, size_t len, loff_t *off) {
    int nr_bytes;
    char kbuff[BUF_LEN];
    kbuff[0] = '\0';

	if(*off > 0)
		return 0;

    /* Charges the C value */
    sprintf(kbuff, "%i%i", gpio_get_value(dataRPin[1]), gpio_get_value(dataRPin[0])); //UD y EN
    nr_bytes = strlen(kbuff);

    /* Send data to the user space */
    if (copy_to_user(buf, kbuff, nr_bytes))
        return -EINVAL;

    (*off)+=nr_bytes;  /* Update the file pointer */

    /* Inform */
    printk(KERN_INFO "Elevator: Received %s.\n", kbuff);

    return nr_bytes;
}

static int elevator_open (struct inode *nodo, struct file *fich){
	/* Avoid the module exit */
	try_module_get(THIS_MODULE);
	return SUCCESS;
}

static int elevator_release (struct inode *nodo, struct file *fich){
	/* Allow the module exit */
	module_put(THIS_MODULE);
	return SUCCESS;
}

static const struct file_operations proc_entry_elevator_fops = {
	.open = elevator_open,
	.release = elevator_release,
    .read = elevator_read,
    .write = elevator_write,
};

/**** MODULE FUNCTIONS ****/

/* Initializes the module */
int init_elevator_module( void )
{
    /* Create the proc entry */
    proc_entry_elevator = proc_create( "elevator", 0666, NULL, &proc_entry_elevator_fops);
    if (proc_entry_elevator == NULL) {
        printk(KERN_INFO "Elevator: Can't create /proc entry\n");
        return -ENOMEM;
    }

    /* Inits the GPIO pines */
    if(!init_gpio_pines()){
    	remove_proc_entry("elevator", NULL);
    	free_all_gpio();
        printk(KERN_INFO "Elevator: GPIO error\n");
        return -EBUSY;
    }

    printk(KERN_INFO "Elevator: Module loaded\n");

    return SUCCESS;
}

/* Finalizes the module */
void exit_elevator_module( void )
{
	/* Remove proc entry */
    remove_proc_entry("elevator", NULL);

    /* Free GPIO pines */
	free_all_gpio();

    /* Inform */
    printk(KERN_INFO "Elevator: Module unloaded.\n");
}

module_init( init_elevator_module );
module_exit( exit_elevator_module );
