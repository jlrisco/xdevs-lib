/*
 * 4 to 1 Mux
 * 
 */
package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Alfonso San Miguel
 */
public class Mux4to1 extends Atomic {

    public static final String inCtrlName = "PortCtrl";
    public static final String inIn0Name = "PortIn0";
    public static final String inIn1Name = "PortIn";
    public static final String inIn2Name = "PortIn2";
    public static final String inIn3Name = "PortIn3";
    public static final String outOutName = "PortOut";
    protected Port<Integer> inPortCtrl = new Port<Integer>(inCtrlName);
    protected Port<Integer> inPortIn0 = new Port<Integer>(inIn0Name);
    protected Port<Integer> inPortIn1 = new Port<Integer>(inIn1Name);
    protected Port<Integer> inPortIn2 = new Port<Integer>(inIn2Name);
    protected Port<Integer> inPortIn3 = new Port<Integer>(inIn3Name);
    protected Port<Integer> outPortOut = new Port<Integer>(outOutName);
    protected Double delay;
    protected Integer valueAtCtrl;
    protected Integer valueAtIn0;
    protected Integer valueAtIn1;
    protected Integer valueAtIn2;
    protected Integer valueAtIn3;
    protected Integer valueToOut;

    public Mux4to1(String name, Double delay) {
        super(name);
        super.addInPort(inPortCtrl);
        super.addInPort(inPortIn0);
        super.addInPort(inPortIn1);
        super.addInPort(inPortIn2);
        super.addInPort(inPortIn3);
        super.addOutPort(outPortOut);
        this.delay = delay;
        valueAtCtrl = null;
        valueAtIn0 = null;
        valueAtIn1 = null;
        valueAtIn2 = null;
        valueAtIn3 = null;
        valueToOut = null;
        super.passivate();
    }

    public Mux4to1(String name) {
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
        if (!inPortCtrl.isEmpty()) {
            Integer tempValueAtCtrl = inPortCtrl.getSingleValue();
            if (tempValueAtCtrl != null && tempValueAtCtrl != valueAtCtrl) {
                valueAtCtrl = tempValueAtCtrl;
                super.holdIn("active", delay);
            }
        }

        if (!inPortIn0.isEmpty()) {
            Integer tempValueAtIn0 = inPortIn0.getSingleValue();
            if (tempValueAtIn0 != null && tempValueAtIn0 != valueAtIn0) {
                valueAtIn0 = tempValueAtIn0;
                super.holdIn("active", delay);
            }
        }

        if (!inPortIn1.isEmpty()) {
            Integer tempValueAtIn1 = inPortIn1.getSingleValue();
            if (tempValueAtIn1 != null && tempValueAtIn1 != valueAtIn1) {
                valueAtIn1 = tempValueAtIn1;
                super.holdIn("active", delay);
            }
        }
        if (!inPortIn2.isEmpty()) {
            Integer tempValueAtIn2 = inPortIn2.getSingleValue();
            if (tempValueAtIn2 != null && tempValueAtIn2 != valueAtIn2) {
                valueAtIn2 = tempValueAtIn2;
                super.holdIn("active", delay);
            }
        }
        if (!inPortIn3.isEmpty()) {
            Integer tempValueAtIn3 = inPortIn3.getSingleValue();
            if (tempValueAtIn3 != null && tempValueAtIn3 != valueAtIn3) {
                valueAtIn3 = tempValueAtIn3;
                super.holdIn("active", delay);
            }
        }

        // Después pre-calculamos la salida
        if (super.phaseIs("active") && valueAtCtrl != null) {
            if (valueAtCtrl == 0) {
                valueToOut = valueAtIn0;
            } else if (valueAtCtrl == 1) {
                valueToOut = valueAtIn1;
            } else if (valueAtCtrl == 2) {
                valueToOut = valueAtIn2;
            } else if (valueAtCtrl == 3) {
                valueToOut = valueAtIn3;
            }
        }

    }

    @Override
    public void lambda() {
        outPortOut.addValue(valueToOut);
    }
}
