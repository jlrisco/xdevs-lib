/*
 * And gate for the PCsource
 */
package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Francisco Calvo
 */
public class And2 extends Atomic {

    protected Port<Integer> in0 = new Port<>("in0");
    protected Port<Integer> in1 = new Port<>("in1");
    protected Port<Integer> out = new Port<>("out");
    protected Double delay;
    protected Integer valueAtIn0;
    protected Integer valueAtIn1;
    protected Integer valueToOut;

    public And2(String name, Double delay) {
        super(name);
        super.addInPort(in0);
        super.addInPort(in1);
        super.addOutPort(out);
        this.delay = delay;
        valueAtIn0 = null;
        valueAtIn1 = null;
        valueToOut = null;
    }

    public And2(String name) {
        this(name, 0.0);
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
        // Primero procesamos los valores de las entradas.
        if (!in0.isEmpty()) {
            Integer tempValueAtIn0 = in0.getSingleValue();
            if (tempValueAtIn0 != null && tempValueAtIn0 != valueAtIn0) {
                valueAtIn0 = tempValueAtIn0;
                super.holdIn("active", delay);
            }
        }
        if (!in1.isEmpty()) {
            Integer tempValueAtIn1 = in1.getSingleValue();
            if (tempValueAtIn1 != null && tempValueAtIn1 != valueAtIn1) {
                valueAtIn1 = tempValueAtIn1;
                super.holdIn("active", delay);
            }
        }

        if (super.phaseIs("active") && valueAtIn0 != null && valueAtIn1 != null) {
            valueToOut = valueAtIn0 * valueAtIn1;
        }

    }

    @Override
    public void lambda() {
        out.addValue(valueToOut);
    }
}
