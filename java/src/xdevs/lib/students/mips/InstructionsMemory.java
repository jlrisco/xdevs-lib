/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.students.mips;

import java.util.ArrayList;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;


/**
 *
 * @author jlrisco
 */
public class InstructionsMemory extends Atomic {

    public static final String inADDRName = "ADDR";
    public static final String outDRName = "DR";
    public static final String outStopName = "stop";
    protected Port<Integer> ADDR = new Port<Integer>(inADDRName);
    protected Port<String> DR = new Port<String>(outDRName);
    protected Port<Integer> stop = new Port<Integer>(outStopName);

    protected double delay;
    protected ArrayList<String> instructions;
    protected String currentInstructionInBinary;
    protected String currentInstructionInAssembler;

    protected Integer valueAtADDR = null;
    
    public InstructionsMemory(String name, ArrayList<String> instructions, double delay) {
        super(name);
        super.addInPort(ADDR);
        super.addOutPort(DR);
        super.addOutPort(stop);
        this.instructions = instructions;
        this.delay = delay;
        this.currentInstructionInBinary = null;
        super.passivate();
    }

     public InstructionsMemory(String name, ArrayList<String> instructions) {
        this(name, instructions, 0.0);
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
        Integer tempValueAtADDR = ADDR.getSingleValue();
        if(tempValueAtADDR!=null) {
            valueAtADDR = tempValueAtADDR / 4;
            if(valueAtADDR<instructions.size()) {
                String currentInstruction = instructions.get(valueAtADDR);
                int pos = currentInstruction.indexOf(":");
                this.currentInstructionInBinary = currentInstruction.substring(0, pos);
                currentInstructionInAssembler = currentInstruction.substring(pos+1);
                super.holdIn(currentInstructionInAssembler, delay);
            }
            else {
                super.holdIn(outStopName, 0.0);
            }
        }
    }

    @Override
    public void lambda() {
        // Se supone que nos nos salimos del array instructions.
        if(super.phaseIs(outStopName)) {
            stop.addValue(1);
        }
        else {
            DR.addValue(currentInstructionInBinary);
            System.out.println(4*valueAtADDR + ": " + currentInstructionInAssembler);
        }
    }

}
