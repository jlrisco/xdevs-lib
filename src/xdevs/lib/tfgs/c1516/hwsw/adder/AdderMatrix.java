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

import java.util.ArrayList;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 * 
 * @author Miguel Higuera Romero
 */
public class AdderMatrix extends Atomic{

    protected Port<Integer> portC = new Port<>("pC");
    protected Port<Integer> portA = new Port<>("pA");
    protected Port<Integer> portB = new Port<>("pB");

    private ArrayList<Integer> arrA;
    private ArrayList<Integer> arrB;

    private Integer valC = null;
    private Integer valA = null;
    private Integer valB = null;
    private int current = 0;

    private double delay;


    /* Initializes the object */
    AdderMatrix(String name, double delay){
        super(name);
        super.addInPort(portC);
        super.addOutPort(portA);
        super.addOutPort(portB);
        arrA = new ArrayList<>();
        arrB = new ArrayList<>();
        this.delay = delay;
    }

    public AdderMatrix(String name) {
        this(name, 16 * 1e-9);
    }

    public AdderMatrix() {
        this(AdderMatrix.class.getName());
    }

    public AdderMatrix(double delay){
        this(AdderMatrix.class.getName(), delay);
    }

    /* Adds the operand for a new Add operation */
    public boolean addOperands(int opA, int opB){
        return arrA.add(opA) && arrB.add(opB);
    }

    @Override
    public void initialize() {
        super.holdIn("active", delay);
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        if(phaseIs("active")){
            if(current < arrA.size()){
                super.holdIn("send", delay);
            }else{
                super.passivate();
            }
        }else if(phaseIs("send")){
            valA = arrA.get(current);
            valB = arrB.get(current);
            System.out.println("Sent " + valA + " + " + valB);
            super.holdIn("wait", delay);
        }else if(phaseIs("wait")){
            valA = null;
            valB = null;
            super.passivate();
        }else if(phaseIs("read")){
            current++;
            super.holdIn("active", delay);
        }
    }

    @Override
    public void deltext(double d) {
        if(!portC.isEmpty() && portC.getSingleValue() != null){
            valC = portC.getSingleValue();
            System.out.println("Received " + valC);
            super.holdIn("read", delay);
        }
    }

    @Override
    public void lambda() {
        portA.addValue(valA);
        portB.addValue(valB);
    }
}
