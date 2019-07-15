/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author jlrisco
 */
public class ALU extends Atomic {

    public static final String inCtrlName = "InCtrl";
    public static final String inOpAName = "InOpA";
    public static final String inOpBName = "InOpB";
    public static final String outZeroName = "outZero";
    public static final String outLessThanName = "outLessThan";
    public static final String outOutName = "outOut";
    protected Port<Integer> inPortCtrl = new Port<>(inCtrlName);
    protected Port<Integer> inPortOpA = new Port<>(inOpAName);
    protected Port<Integer> inPortOpB = new Port<>(inOpBName);
    protected Port<Integer> outPortZero = new Port<>(outZeroName);
    protected Port<Integer> outLessThan = new Port<>(outLessThanName);
    protected Port<Integer> outPortOut = new Port<>(outOutName);
    protected Double delay;
    protected Integer valueAtCtrl;
    protected Integer valueAtOpA;
    protected Integer valueAtOpB;
    protected Integer valueToOut;

    public ALU(String name, Double delay) {
        super(name);
        super.addInPort(inPortCtrl);
        super.addInPort(inPortOpA);
        super.addInPort(inPortOpB);
        super.addOutPort(outPortZero);
        super.addOutPort(outLessThan);
        super.addOutPort(outPortOut);
        this.delay = delay;
        valueAtCtrl = null;
        valueAtOpA = null;
        valueAtOpB = null;
        valueToOut = null;
    }

    public ALU(String name) {
        this(name, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        // Primero procesamos los valores de las entradas.
        if (!inPortCtrl.isEmpty()) {
            valueAtCtrl = inPortCtrl.getSingleValue();
            super.holdIn("active", delay);
        }

        if (!inPortOpA.isEmpty()) {
            valueAtOpA = inPortOpA.getSingleValue();
            super.holdIn("active", delay);
        }

        if (!inPortOpB.isEmpty()) {
            valueAtOpB = inPortOpB.getSingleValue();
            super.holdIn("active", delay);
        }
    }

    @Override
    public void lambda() {
        if (valueAtCtrl != null && valueAtOpA != null && valueAtOpB != null) {
            Integer result;
            if (valueAtCtrl == 0) // and
            {
                result = valueAtOpA & valueAtOpB;
            } else if (valueAtCtrl == 1) // or
            {
                result = valueAtOpA | valueAtOpB;
            } else if (valueAtCtrl == 2) // suma
            {
                result = valueAtOpA + valueAtOpB;
            } else if (valueAtCtrl == 6 || valueAtCtrl == 7) // resta, slt
            {
                result = valueAtOpA - valueAtOpB;
            } else {
                result = 0;
            }
            outPortOut.addValue(result);

            if (result == 0) {
                outPortZero.addValue(1);
                outLessThan.addValue(0);
            } else if (result > 0) {
                outPortZero.addValue(0);
                outLessThan.addValue(0);
            } else if (result < 0) {
                outPortZero.addValue(0);
                outLessThan.addValue(1);
            }

        }
    }

    @Override
    public void initialize() {
        super.passivate();
    }

    @Override
    public void exit() {
    }
}
