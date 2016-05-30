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
public class MDCosim3 extends Atomic{
    
    public Port<Integer> iB2 = new Port<>("B2");
    public Port<Integer> iA2 = new Port<>("A2");
    public Port<Integer> iA1 = new Port<>("A1");
    public Port<Integer> iB1 = new Port<>("B1");
    public Port<Integer> oUD = new Port<>("UD");
    public Port<Integer> oEN = new Port<>("S3");
    public Port<Integer> iA3 = new Port<>("A3");
    public Port<Integer> iB3 = new Port<>("B3");

    protected Integer valueToUD = null;
    protected Integer valueToEN = null;
    
    protected Integer valueAtB2 = null;
    protected Integer valueAtA2 = null;
    protected Integer valueAtA1 = null;
    protected Integer valueAtB1 = null;
    protected Integer valueAtA3 = null;
    protected Integer valueAtB3 = null;
    
    private final String DEVICE_FILE_PATH = "/proc/elevator";
    public static final double ADDER_DELAY = 24*1e-9;
    public static final double NOTS_DELAY = 15*1e-9;
    public static final double NANDS_DELAY = 15*1e-9;
    private DriverManager driver;

    protected double delay;
    
    public MDCosim3(String name, double delay){
        super(name);
        super.addInPort(iB2);
        super.addInPort(iA2);
        super.addInPort(iA1);
        super.addInPort(iB1);
        super.addOutPort(oUD);
        super.addOutPort(oEN);
        super.addInPort(iA3);
        super.addInPort(iB3);
        this.delay = delay;
        this.driver = new DriverManager(this.DEVICE_FILE_PATH);
    }
    
    public MDCosim3(double delay) {
        this(MDCosim1.class.getName(), delay);
    }

    public MDCosim3(String name) {
        this(name, ADDER_DELAY + 2*NOTS_DELAY + 2*NANDS_DELAY);
    }

    public MDCosim3() {
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
        valueToUD = null;
        valueToEN = null;
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        Integer tempValueAtB2 = (iB2.isEmpty()) ? null : iB2.getSingleValue();
        Integer tempValueAtA2 = (iA2.isEmpty()) ? null : iA2.getSingleValue();
        Integer tempValueAtA1 = (iA1.isEmpty()) ? null : iA1.getSingleValue();
        Integer tempValueAtB1 = (iB1.isEmpty()) ? null : iB1.getSingleValue();
        Integer tempValueAtA3 = (iA3.isEmpty()) ? null : iA3.getSingleValue();
        Integer tempValueAtB3 = (iB3.isEmpty()) ? null : iB3.getSingleValue();
        
        boolean activate = false;
        
        if (tempValueAtB3 != null && !tempValueAtB3.equals(this.valueAtB3)) { // B3
            activate = true;
            valueAtB3 = tempValueAtB3;
        }
        if (tempValueAtB2 != null && !tempValueAtB2.equals(this.valueAtB2)) {  // B2
            activate = true;
            valueAtB2 = tempValueAtB2;
        }
        if (tempValueAtB1 != null && !tempValueAtB1.equals(this.valueAtB1)) {  //B1
            activate = true;
            valueAtB1 = tempValueAtB1;
        }
        if (tempValueAtA3 != null && !tempValueAtA3.equals(this.valueAtA3)) { //A3
            activate = true;
            valueAtA3 = tempValueAtA3;
        }
        if (tempValueAtA2 != null && !tempValueAtA2.equals(this.valueAtA2)) {  //A2
            activate = true;
            valueAtA2 = tempValueAtA2;
        }
        if (tempValueAtA1 != null && !tempValueAtA1.equals(this.valueAtA1)) {  //A1
            activate = true;
            valueAtA1 = tempValueAtA1;
        }
        
        if (activate && valueAtA1 != null && valueAtB1 != null && valueAtB2 != null && valueAtA2 != null && valueAtA3 != null && valueAtB3 != null) {
            String cmd = "0b" + valueAtB3 + valueAtA2 + valueAtB1 + valueAtA3 + valueAtB2 + valueAtA1;
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
            int mask = 1 << 1;
            valueToUD = (floor & mask) != 0 ? 1 : 0;
            mask = 1;
            valueToEN = (floor & mask) != 0 ? 1 : 0;
        }
        this.oUD.addValue(this.valueToUD);
        this.oEN.addValue(this.valueToEN);
    }
    
}
