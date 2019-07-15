/*
 * Node J
 */

package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Francisco Calvo
 */
public class NodeJ extends Atomic {

    public static final String inPortIn1Name = "PortIn1";
    public static final String inPortIn2Name = "PortIn2";
    public static final String outPortOutName = "PortOut";

    protected Port<Integer> inPortIn1 = new Port<Integer>(inPortIn1Name);
    protected Port<Integer> inPortIn2 = new Port<Integer>(inPortIn2Name);
    protected Port<Integer> outPortOut = new Port<Integer>(outPortOutName);

    protected Double delay;
    protected Integer valueAtIn1;
    protected Integer valueAtIn2;
    protected Integer valueAtOut;

    public NodeJ(String name, Double delay) {
        super(name);
        super.addInPort(inPortIn1);
        super.addInPort(inPortIn2);
        super.addOutPort(outPortOut);
        this.delay = delay;
        valueAtIn1 = null;
        valueAtIn2 = null;
        valueAtOut = null;
        super.passivate();
    }

    public NodeJ(String name) {
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
        if(!inPortIn1.isEmpty()) {
            valueAtIn1 = inPortIn1.getSingleValue();
            super.holdIn("active", delay);
        }
        if(!inPortIn2.isEmpty()) {
            valueAtIn2 = inPortIn2.getSingleValue();
            super.holdIn("active", delay);
        }

}

    @Override
    public void lambda() {
        if ((valueAtIn1 != null)&&(valueAtIn2 != null)) {
            outPortOut.addValue(valueAtIn1 + valueAtIn2);// No se como hacerlo
        }
    }


}
