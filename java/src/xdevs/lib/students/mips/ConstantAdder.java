/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;



/**
 *
 * @author José L. Risco-Martín
 */
public class ConstantAdder extends Atomic {
    public static final String inOpAName = "opA";
    public static final String outOutName = "out";

    protected Port<Integer> opA = new Port<Integer>(ConstantAdder.inOpAName);
    protected Port<Integer> out = new Port<Integer>(ConstantAdder.outOutName);

    protected Integer constant;
    protected Double delay;
    protected Integer prevValueAtOpA;
    protected Integer prevValueAtOut;

    public ConstantAdder(String name, Integer constant, Double delay){
        super(name);
        super.addInPort(opA);
        super.addOutPort(out);
        this.constant = constant;
        this.delay = delay;
        prevValueAtOpA = null;
        prevValueAtOut = null;
        super.passivate();
    }

    public ConstantAdder(String name, Integer constant) {
        this(name, constant, 0.0);
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

        Integer tempValueAtOpA = opA.getSingleValue();
        if(tempValueAtOpA!=null) {
            prevValueAtOpA = tempValueAtOpA;
            super.holdIn("active", delay);
        }
    }

    @Override
    public void lambda() {
        Integer result;
        result = prevValueAtOpA + constant;
        out.addValue(result);
    }

}
