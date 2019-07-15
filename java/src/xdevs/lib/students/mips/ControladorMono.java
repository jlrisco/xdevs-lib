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
 * @author José L. Risco-Martín
 */
public class ControladorMono extends Atomic {

    public static final String inOpName = "op";
    public static final String inFunctName = "funct";
    public static final String outPCWriteName = "PCWrite";
    public static final String outRegDstName = "regDst";
    public static final String outALUSrcName = "ALUSrc";
    public static final String outMemtoRegName = "memtoReg";
    public static final String outRegWriteName = "regWrite";
    public static final String outMemReadName = "memRead";
    public static final String outMemWriteName = "memWrite";
    public static final String outBranchName = "branch";
    public static final String outALUOpName = "ALUOp";
    public static final String outJumpReg = "JumpReg";
    public static final String outRegData = "RegData";
    public static final String outMuxSlt = "MuxSlt";

    protected Port<Integer> portOp = new Port<Integer>(ControladorMono.inOpName);
    protected Port<Integer> portFunct = new Port<Integer>(ControladorMono.inFunctName);
    protected Port<Integer> portPcWrite = new Port<Integer>(ControladorMono.outPCWriteName);
    protected Port<Integer> portRegDst = new Port<Integer>(ControladorMono.outRegDstName);
    protected Port<Integer> portALUSrc = new Port<Integer>(ControladorMono.outALUSrcName);
    protected Port<Integer> portMemtoReg = new Port<Integer>(ControladorMono.outMemtoRegName);
    protected Port<Integer> portRegWrite = new Port<Integer>(ControladorMono.outRegWriteName);
    protected Port<Integer> portMemRead = new Port<Integer>(ControladorMono.outMemReadName);
    protected Port<Integer> portMemWrite = new Port<Integer>(ControladorMono.outMemWriteName);
    protected Port<Integer> portBranch = new Port<Integer>(ControladorMono.outBranchName);
    protected Port<Integer> portALUOp = new Port<Integer>(ControladorMono.outALUOpName);
    protected Port<Integer> portJumpReg = new Port<Integer>(ControladorMono.outJumpReg);
    protected Port<Integer> portRegData = new Port<Integer>(ControladorMono.outRegData);
    protected Port<Integer> portMuxSlt = new Port<Integer>(ControladorMono.outMuxSlt);

    protected Double delay;
    protected Integer valueAtOp;
    protected Integer valueAtFunct;

    public ControladorMono(String name, Double delay) {
        super(name);
        super.addInPort(portOp);
        super.addInPort(portFunct);
        super.addOutPort(portPcWrite);
        super.addOutPort(portRegDst);
        super.addOutPort(portALUSrc);
        super.addOutPort(portMemtoReg);
        super.addOutPort(portRegWrite);
        super.addOutPort(portMemRead);
        super.addOutPort(portMemWrite);
        super.addOutPort(portBranch);
        super.addOutPort(portALUOp);
        super.addOutPort(portJumpReg);
        super.addOutPort(portRegData);
        super.addOutPort(portMuxSlt);

        this.delay = delay;
        valueAtOp = null;
        valueAtFunct = null;

        super.passivate();
    }

    public ControladorMono(String name) {
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
        //valueAtOp = null;
        //valueAtFunct = null;
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        // Primero procesamos los valores de las entradas.
        if (portOp.getSingleValue() != null) {
            valueAtOp = portOp.getSingleValue();
        }
        if (portFunct.getSingleValue() != null) {
            valueAtFunct = portFunct.getSingleValue();
        }
        // Si las dos son estables lanzamos salida:
        if(valueAtOp!=null && valueAtFunct!=null)
            super.holdIn("active", delay);
    }

    @Override
    public void lambda() {
        Integer PCWrite = 1;
        Integer RegDst = 0;
        Integer Branch = 0;
        Integer MemRead = 0;
        Integer MemToReg = 0;
        Integer ALUOp = 0;
        Integer MemWrite = 0;
        Integer ALUSrc = 0;
        Integer RegWrite = 0;
        Integer JumpReg = 0;
        Integer RegData = 0;

        if ((valueAtOp == 0) && (valueAtFunct == 8)) { //jr (000000)
            JumpReg = 1;
        }
        else if (valueAtOp == 0) {// tipo-R (000000)
            RegDst = 1;
            RegWrite = 1;
            ALUOp = 2;
        } else if (valueAtOp == 4) {// beq (000100)
            Branch = 1;
            ALUOp = 1;
        } else if (valueAtOp == 43) {// sw (101011)
            ALUSrc = 1;
            MemWrite = 1;
        } else if (valueAtOp == 35) {// lw(100011)
            ALUSrc = 1;
            MemToReg = 1;
            RegWrite = 1;
            MemRead = 1;
        } else if (valueAtOp == 9) {// addiu(001001)
            ALUSrc = 1;
            RegWrite = 1;
        } else if (valueAtOp == 10) {// slti(001010)
            ALUOp = 1;
            ALUSrc = 1;
            RegWrite = 1;
            RegData = 1;
        } else if (valueAtOp == 2) {// j(000010)
            JumpReg = 2;
        } else {
            System.err.println("OpCode not implemented yet: " + valueAtOp);
            PCWrite = 0;

        }

        this.portPcWrite.addValue(PCWrite);
        this.portRegDst.addValue(RegDst);
        this.portALUSrc.addValue(ALUSrc);
        this.portMemtoReg.addValue(MemToReg);
        this.portRegWrite.addValue(RegWrite);
        this.portMemRead.addValue(MemRead);
        this.portMemWrite.addValue(MemWrite);
        this.portBranch.addValue(Branch);
        this.portALUOp.addValue(ALUOp);
        this.portJumpReg.addValue(JumpReg);
        this.portRegData.addValue(RegData);
    }
}
