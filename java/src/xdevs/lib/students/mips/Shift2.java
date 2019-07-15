/*
 * 2bit-Shifter
 */
package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Alfonso San Miguel Sánchez
 */
public class Shift2 extends Atomic {

    public static final String inPortInName = "PortIn";
    public static final String outPortOutName = "PortOut";
    protected Port<Integer> inPortIn = new Port<Integer>(inPortInName);
    protected Port<Integer> outPortOut = new Port<Integer>(outPortOutName);
    protected Double delay;
    protected Integer valueAtIn;
    protected Integer valueAtOut;

    public Shift2(String name, Double delay) {
        super(name);
        super.addInPort(inPortIn);
        super.addOutPort(outPortOut);
        this.delay = delay;
        valueAtIn = null;
        valueAtOut = null;
        super.passivate();
    }

    public Shift2(String name) {
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
        Integer tempValueAtIn = inPortIn.getSingleValue();
        if (tempValueAtIn != null) {
            valueAtIn = tempValueAtIn;
            super.holdIn("active", delay);
        }

    }

    @Override
    public void lambda() {
        if (valueAtIn != null) {
            Integer result;
            result = valueAtIn * 4; // 2x shift right
            // COMPROBACION DE RANGO?
            outPortOut.addValue(result);
        }
    }
}
