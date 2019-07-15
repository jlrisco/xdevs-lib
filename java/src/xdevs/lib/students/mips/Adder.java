/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;


/**
 *
 * @author Jose Roldan Ramirez
 */
public class Adder extends Atomic {
    public static final String inOpAName = "opA";
    public static final String inOpBName = "opB";
    public static final String outOutName = "out";

    protected Port<Integer> opA;
    protected Port<Integer> opB;
    protected Port<Integer> out;

    protected Double delay;
    protected Integer valueAtOpA;
    protected Integer valueAtOpB;
    protected Integer valueAtOut;

    public Adder(String name, Double delay){

        super(name);
        opA = new Port<>(Adder.inOpAName);
        opB = new Port<>(Adder.inOpBName);
        out = new Port<>(Adder.outOutName);
        super.addInPort(opA);
        super.addInPort(opB);
        super.addOutPort(out);
        this.delay = delay;
        valueAtOpA = null;
        valueAtOpB = null;
        valueAtOut = null;
        super.passivate();
    }

    public Adder(String name) {
        this(name, 0.0);
    }
    
    @Override
    public void initialize() {        
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
        // Primero procesamos los valores de las entradas.

        if(!opA.isEmpty()) {
            valueAtOpA = opA.getSingleValue();
            super.holdIn("active", delay);
        }

        if(!opB.isEmpty()) {
            valueAtOpB = opB.getSingleValue();
            super.holdIn("active", delay);
        }
    }

    @Override
    public void lambda() {
        if(valueAtOpA!=null && valueAtOpB!=null) {
            Integer result;
            result = valueAtOpA + valueAtOpB;
            out.addValue(result);
        }
    }

}
