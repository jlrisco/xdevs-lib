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

package xdevs.lib.tfgs.c1516.hwsw.adder;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.lib.tfgs.c1516.hwsw.DriverManager;

/**
 * 
 * @author Miguel Higuera Romero
 */
public class Adder extends Atomic {

    protected Port<Integer> portA = new Port<>("pA");
    protected Port<Integer> portB = new Port<>("pB");
    protected Port<Integer> portC = new Port<>("pC");

    public static final String ADDER_FILE = "/proc/sumador4bit";
    private DriverManager driver;
    
    private Integer valA;
    private Integer valB;
    private Integer valC;

    private double delay;

    public Adder(String name, double delay){
        super(name);
        super.addInPort(portA);
        super.addInPort(portB);
        super.addOutPort(portC);
        valA = null;
        valB = null;
        valC = null;
        this.delay = delay;
        this.driver = new DriverManager(Adder.ADDER_FILE);
    }
    
    public Adder(String name) {
        this(name, 16 * 1e-9);
    }

    public Adder() {
        this(Adder.class.getName());
    }
    
    public Adder(double delay){
        this(Adder.class.getName(), delay);
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
        
        if(phaseIs("active")){
            valC = null;
            super.passivate();
        }else if(phaseIs("read")){
            this.valA = null;
            this.valB = null;
            String result = driver.read();
            valC = Integer.parseInt(result);
            super.holdIn("active", delay);
        }
    }

    @Override
    public void deltext(double d) {
        boolean activate = false;
        if(!portA.isEmpty() && portA.getSingleValue() != null){
            activate = true;
            valA = portA.getSingleValue();
        }
        if(!portB.isEmpty() && portB.getSingleValue() != null){
            activate = true;
            valB = portB.getSingleValue();
        }
        if(activate){
            String cmd = "add " + this.valA + " " + this.valB + "\n";
            driver.write(cmd);
            super.holdIn("read", delay);
        }
    }

    @Override
    public void lambda() {
        portC.addValue(valC);
    }
}
