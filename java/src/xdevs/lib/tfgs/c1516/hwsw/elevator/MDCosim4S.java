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
public class MDCosim4S extends Atomic{
    
    public Port<Integer> iX3 = new Port<>("X3");
    public Port<Integer> iX2 = new Port<>("X2");
    public Port<Integer> iX1 = new Port<>("X1");
    public Port<Integer> iIni = new Port<>("Ini");

    protected Integer valueAtX3 = null;
    protected Integer valueAtX2 = null;
    protected Integer valueAtX1 = null;
    protected Integer valueAtIni = null;
    
    private final String DEVICE_FILE_PATH = "/proc/elevator";
    private DriverManager driver;

    protected double delay;
    
    public MDCosim4S(String name, double delay){
        super(name);
        super.addInPort(iX3);
        super.addInPort(iX2);
        super.addInPort(iX1);
        super.addInPort(iIni);
        this.delay = delay;
        this.driver = new DriverManager(this.DEVICE_FILE_PATH);
    }
    
    public MDCosim4S(double delay) {
        this(MDCosim1.class.getName(), delay);
    }

    public MDCosim4S(String name) {
        this(name, 16 * 1e-9);
    }

    public MDCosim4S() {
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
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        Integer tempIni = (iIni.isEmpty()) ? null : iIni.getSingleValue();
        boolean activate = false;
        
        if(tempIni != null && tempIni == 0){   //Reset counter
            if(valueAtIni == null || valueAtIni == 1){
                valueAtIni = 0;
                driver.write("ini 0");
            }
        }else{
            if((tempIni != null && tempIni == 1) && (valueAtIni == null || valueAtIni == 0)){
                valueAtIni = 1;
                driver.write("ini 1");
            }
        }
        
        Integer tempX3 = (iX3.isEmpty()) ? null : iX3.getSingleValue();
        Integer tempX2 = (iX2.isEmpty()) ? null : iX2.getSingleValue();
        Integer tempX1 = (iX1.isEmpty()) ? null : iX1.getSingleValue();

        if(tempX3 != null && !tempX3.equals(valueAtX3)){
            activate = true;
            valueAtX3 = tempX3;
        }
        if(tempX2 != null && !tempX2.equals(valueAtX2)){
            activate = true;
            valueAtX2 = tempX2;
        }
        if(tempX1 != null && !tempX1.equals(valueAtX1)){
            activate = true;
            valueAtX1 = tempX1;
        }

        if(activate && valueAtX3 != null && valueAtX2 != null && valueAtX1 != null){
            driver.write("0b" + valueAtX3 + valueAtX2 + valueAtX1);
        }

        super.passivate();
    }

    @Override
    public void lambda() {
        
    }
    
}
