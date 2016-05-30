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
package xdevs.lib.tfgs.c1516.hwsw.elevator;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.lib.tfgs.c1516.hwsw.DriverManager;

/**
 *
 * @author Miguel Higuera Romero
 */
public class MDCosim1 extends Atomic{
    
    public Port<Integer> oPin1 = new Port<>("pin1");  //S2
    public Port<Integer> iPin2 = new Port<>("pin2");
    public Port<Integer> iPin3 = new Port<>("pin3");
    public Port<Integer> oPin4 = new Port<>("pin4");  //S1
    public Port<Integer> iPin5 = new Port<>("pin5");
    public Port<Integer> iPin6 = new Port<>("pin6");
    public Port<Integer> oPin10 = new Port<>("pin10");    //S4
    public Port<Integer> oPin13 = new Port<>("pin13");    //S3
    public Port<Integer> iPin14 = new Port<>("pin14");
    public Port<Integer> iPin15 = new Port<>("pin15");

    protected Integer valueToPin1 = null;
    protected Integer valueToPin4 = null;
    protected Integer valueToPin10 = null;
    protected Integer valueToPin13 = null;
    
    protected Integer valueAtPin2 = null;
    protected Integer valueAtPin3 = null;
    protected Integer valueAtPin5 = null;
    protected Integer valueAtPin6 = null;
    protected Integer valueAtPin14 = null;
    protected Integer valueAtPin15 = null;
    
    private final String DEVICE_FILE_PATH = "/proc/elevator";
    public static final double ADDER_DELAY = 24*1e-9;
    private DriverManager driver;

    protected double delay;
    
    public MDCosim1(String name, double delay){
        super(name);
        super.addOutPort(oPin1);
        super.addInPort(iPin2);
        super.addInPort(iPin3);
        super.addOutPort(oPin4);
        super.addInPort(iPin5);
        super.addInPort(iPin6);
        super.addOutPort(oPin10);
        super.addOutPort(oPin13);
        super.addInPort(iPin14);
        super.addInPort(iPin15);
        this.delay = delay;
        this.driver = new DriverManager(DEVICE_FILE_PATH);
    }
    
    public MDCosim1(double delay) {
        this(MDCosim1.class.getName(), delay);
    }

    public MDCosim1(String name) {
        this(name, ADDER_DELAY);
    }

    public MDCosim1() {
        this(MDCosim1.class.getName());
    }

    @Override
    public void initialize() {
        super.passivate();
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        valueToPin10 = null;
        valueToPin13 = null;
        valueToPin1 = null;
        valueToPin4 = null;
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        Integer tempValueAtPin2 = (iPin2.isEmpty()) ? null : iPin2.getSingleValue();
        Integer tempValueAtPin3 = (iPin3.isEmpty()) ? null : iPin3.getSingleValue();
        Integer tempValueAtPin5 = (iPin5.isEmpty()) ? null : iPin5.getSingleValue();
        Integer tempValueAtPin6 = (iPin6.isEmpty()) ? null : iPin6.getSingleValue();
        Integer tempValueAtPin14 = (iPin14.isEmpty()) ? null : iPin14.getSingleValue();
        Integer tempValueAtPin15 = (iPin15.isEmpty()) ? null : iPin15.getSingleValue();
        
        boolean activate = false;
        
        if (tempValueAtPin15 != null && !tempValueAtPin15.equals(this.valueAtPin15)) { // B3
            activate = true;
            valueAtPin15 = tempValueAtPin15;
        }
        if (tempValueAtPin2 != null && !tempValueAtPin2.equals(this.valueAtPin2)) {  // B2
            activate = true;
            valueAtPin2 = tempValueAtPin2;
        }
        if (tempValueAtPin6 != null && !tempValueAtPin6.equals(this.valueAtPin6)) {  //B1
            activate = true;
            valueAtPin6 = tempValueAtPin6;
        }
        if (tempValueAtPin14 != null && !tempValueAtPin14.equals(this.valueAtPin14)) { //A3
            activate = true;
            valueAtPin14 = tempValueAtPin14;
        }
        if (tempValueAtPin3 != null && !tempValueAtPin3.equals(this.valueAtPin3)) {  //A2
            activate = true;
            valueAtPin3 = tempValueAtPin3;
        }
        if (tempValueAtPin5 != null && !tempValueAtPin5.equals(this.valueAtPin5)) {  //A1
            activate = true;
            valueAtPin5 = tempValueAtPin5;
        }
        
        if (activate && valueAtPin5 != null && valueAtPin6 != null && valueAtPin2 != null && valueAtPin3 != null && valueAtPin14 != null && valueAtPin15 != null) {
            String cmd = "0b" + valueAtPin15 + valueAtPin3 + valueAtPin6 + valueAtPin14 + valueAtPin2 + valueAtPin5;
            driver.write(cmd);
            super.holdIn("active", delay);
        }else{
            super.passivate();
        }
    }

    @Override
    public void lambda() {
        if(super.phaseIs("active")){
            String result = driver.read();
            Integer floor = Integer.parseInt(result, 2);
            int mask = 1 << 3;
            valueToPin10 = (floor & mask) != 0 ? 1 : 0;
            mask = 1 << 2;
            valueToPin13 = (floor & mask) != 0 ? 1 : 0;
            mask = 1 << 1;
            valueToPin1 = (floor & mask) != 0 ? 1 : 0;
            mask = 1;
            valueToPin4 = (floor & mask) != 0 ? 1 : 0;
        }
        this.oPin1.addValue(this.valueToPin1);
        this.oPin10.addValue(this.valueToPin10);
        this.oPin13.addValue(this.valueToPin13);
        this.oPin4.addValue(this.valueToPin4);
    }
    
}
