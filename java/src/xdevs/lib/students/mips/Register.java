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
 * @author José L. Risco Martín
 */
public class Register extends Atomic {

    public static final String inClkName = "clk";
    public static final String inRegWriteName = "RegWrite";
    public static final String inInName = "in";
    public static final String outOutName = "out";

    protected Port<Integer> clk = new Port<Integer>(Register.inClkName);
    protected Port<Integer> regWrite = new Port<Integer>(Register.inRegWriteName);
    protected Port<Integer> in = new Port<Integer>(Register.inInName);
    protected Port<Integer> out = new Port<Integer>(Register.outOutName);

    protected Double delayRead;
    protected Double delayWrite;
    protected Integer valueAtClk;
    protected Integer valueAtRegWrite;
    protected Integer valueAtIn;
    protected Integer valueAtOut;

    public Register(String name, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(clk);
        super.addInPort(regWrite);
        super.addInPort(in);
        super.addOutPort(out);
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtClk = null;
        valueAtIn = null;
        valueAtOut = 0;
        super.holdIn("Read", delayRead);
    }

    public Register(String name) {
        this(name, 0.0, 0.0);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        super.passivate(); // sigma = infinito, phase = passive
    }

    @Override
    public void deltext(double e) {
        // Primero procesamos los valores de la entrada

        if (!in.isEmpty()) {
            valueAtIn = in.getSingleValue();
        }

        if (!regWrite.isEmpty()) {
            valueAtRegWrite = regWrite.getSingleValue();
        }

        // Ahora el reloj, que gobierna la escritura:
        if (!clk.isEmpty()) {
            Integer tempValueAtClk = clk.getSingleValue();
            if (valueAtRegWrite != null && valueAtRegWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                if (valueAtIn != null) {
                    valueAtOut = valueAtIn;
                }
                super.holdIn("Write", delayWrite);
            }
            valueAtClk = tempValueAtClk;
        }
    }

    @Override
    public void lambda() {
        if (valueAtOut != null) {
            out.addValue(valueAtOut);
        }
    }
}
