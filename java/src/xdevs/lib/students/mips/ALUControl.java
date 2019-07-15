/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Francisco Calvo
 */
public class ALUControl extends Atomic {

    public static final String ALUopName = "ALUop";
    public static final String FunctName = "funct";
    public static final String ALUCtrlName = "ALUCtrl";
    protected Port<Integer> ALUop;
    protected Port<Integer> Funct;
    protected Port<Integer> ALUCtrl;
    protected Double delay;
    protected Integer valueAtFunct;
    protected Integer valueAtALUop;
    protected Integer valueAtALUCtrl;

    public ALUControl(String name, Double delay) {
        super(name);
        ALUop = new Port<>(ALUopName);
        Funct = new Port<>(FunctName);
        ALUCtrl = new Port<>(ALUCtrlName);
        super.addInPort(ALUop);
        super.addInPort(Funct);
        super.addOutPort(ALUCtrl);
        this.delay = delay;
    }

    public ALUControl(String name) {
        this(name, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {

        // Primero procesamos los valores de las entradas.
        if (!Funct.isEmpty()) {
            valueAtFunct = Funct.getSingleValue();
            super.holdIn("active", delay);
        }
        if (!ALUop.isEmpty()) {
            valueAtALUop = ALUop.getSingleValue();
            super.holdIn("active", delay);
        }
    }

    @Override
    public void lambda() {
        if (valueAtALUop != null) {
            Integer valueToALUCtrl = 0;

            if (valueAtALUop == 0) {// lw / sw / addiu
                valueToALUCtrl = 2;
            } else if (valueAtALUop == 1) {// beq
                valueToALUCtrl = 6;
            } else if (valueAtALUop == 2) {// tipo-R
                if (valueAtFunct == 32) {// add
                    valueToALUCtrl = 2;
                } else if (valueAtFunct == 33) {// addu
                    valueToALUCtrl = 2;
                } else if (valueAtFunct == 34) {// sub
                    valueToALUCtrl = 6;
                } else if (valueAtFunct == 36) {// and
                    valueToALUCtrl = 0;
                } else if (valueAtFunct == 37) {// or
                    valueToALUCtrl = 1;
                } else if (valueAtFunct == 42) {// slt
                    valueToALUCtrl = 7;
                } else {
                    System.err.println("(AluOp, funct) not implemented yet: (" + valueAtALUop + ", " + valueAtFunct + ")");
                }
            } else if (valueAtALUop == 8) {// slti
                valueToALUCtrl = 6;
            } else {
                System.err.println("AluOp not implemented yet: " + valueAtALUop);
            }
            this.ALUCtrl.addValue(valueToALUCtrl);
        }
    }

    @Override
    public void initialize() {
        valueAtFunct = null;
        valueAtALUop = null;
        valueAtALUCtrl = null;
        super.passivate();
    }

    @Override
    public void exit() {
    }
}
