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

public class MDCosim4R extends Atomic{
    
    public Port<Integer> iClk = new Port<>("CLK");
    public Port<Integer> oQ3 = new Port<>("Q3");
    public Port<Integer> oQ2 = new Port<>("Q2");
    public Port<Integer> oQ1 = new Port<>("Q1");

    protected Integer valueToQ3 = 0;
    protected Integer valueToQ2 = 0;
    protected Integer valueToQ1 = 0;
    protected Integer valueAtClk = 0;
    
    private final String DEVICE_FILE_PATH = "/proc/elevator";
    public static final double ADDER_DELAY = 24e-9;
    public static final double NOT_DELAY = 15e-9;
    public static final double NAND_DELAY = 15e-9;
    public static final double COUNTER_DELAY = 23e-9;
    private DriverManager driver;
    
    protected double delay;
    
    public MDCosim4R(String name, double delay){
        super(name);
        super.addOutPort(oQ3);
        super.addOutPort(oQ2);
        super.addOutPort(oQ1);
        super.addInPort(iClk);
        this.delay = delay;
        this.driver = new DriverManager(this.DEVICE_FILE_PATH);
    }
    
    public MDCosim4R(double delay) {
        this(MDCosim1.class.getName(), delay);
    }

    public MDCosim4R(String name) {
        this(name, COUNTER_DELAY + ADDER_DELAY + NOT_DELAY + 2*NAND_DELAY);
    }

    public MDCosim4R() {
        this(MDCosim1.class.getName());
    }
    
    @Override
    public void initialize() {
        super.activate();
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        if(!iClk.isEmpty()){
            valueAtClk = iClk.getSingleValue();
            driver.write("clk " + valueAtClk);
            super.holdIn("active", delay);
        }
    }

    @Override
    public void lambda() {
        if(phaseIs("active")){
            String result = driver.read();
            Integer floor = Integer.parseInt(result, 2);
            int mask = 1 << 2;
            Integer tvalueToQ3 = (floor & mask) != 0 ? 1 : 0;
            mask = 1 << 1;
            Integer tvalueToQ2 = (floor & mask) != 0 ? 1 : 0;
            mask = 1;
            Integer tvalueToQ1 = (floor & mask) != 0 ? 1 : 0;
            
            if(!tvalueToQ3.equals(valueToQ3) || !tvalueToQ2.equals(valueToQ2) || !tvalueToQ1.equals(valueToQ1)){
                valueToQ3 = tvalueToQ3;
                valueToQ2 = tvalueToQ2;
                valueToQ1 = tvalueToQ1;
                this.oQ3.addValue(valueToQ3);
                this.oQ2.addValue(valueToQ2);
                this.oQ1.addValue(valueToQ1);
            }
        }
    }
    
}
