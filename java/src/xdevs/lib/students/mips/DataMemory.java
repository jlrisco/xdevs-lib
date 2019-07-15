/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.students.mips;

import java.util.HashMap;
import java.util.logging.Logger;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Jose Roldan Ramirez
 * @author José L. Risco Martín
 */
public class DataMemory extends Atomic {

    private static Logger logger = Logger.getLogger(DataMemory.class.getName());

    public static final String CLKName = "CLK";
    public static final String ADDRName = "ADDR";
    public static final String DWName = "DW";
    public static final String MemReadName = "MemRead";
    public static final String MemWriteName = "MemWrite";
    public static final String DRName = "DR";

    protected Port<Integer> CLK = new Port<Integer>(CLKName);
    protected Port<Integer> ADDR = new Port<Integer>(ADDRName);
    protected Port<Integer> DW = new Port<Integer>(DWName);
    protected Port<Integer> MemRead = new Port<Integer>(MemReadName);
    protected Port<Integer> MemWrite = new Port<Integer>(MemWriteName);
    protected Port<Integer> DR = new Port<Integer>(DRName);

    protected Integer valueAtCLK;
    protected Integer valueAtADDR;
    protected Integer valueAtDW;
    protected Integer valueAtMemRead;
    protected Integer valueAtMemWrite;
    protected Integer valueToDR;

    protected Double delayRead;
    protected Double delayWrite;

    protected HashMap<Integer, Integer> data;

    public HashMap<Integer, Integer> getData() {
        return data;
    }

    public DataMemory(String name, HashMap<Integer, Integer> data, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(CLK);
        super.addInPort(ADDR);
        super.addInPort(DW);
        super.addInPort(MemWrite);
        super.addInPort(MemRead);
        super.addOutPort(DR);

        valueAtCLK = null;
        valueAtADDR = null;
        valueAtDW = null;
        valueAtMemRead = null;
        valueAtMemWrite = null;
        valueToDR = null;

        this.data = data;
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;

        super.passivate();
    }

    public DataMemory(String name, HashMap<Integer, Integer> data) {
        this(name, data, 0.0, 0.0);
    }

    public DataMemory(String name) {
        this(name, new HashMap<Integer, Integer>());
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

        // Primero procesamos las señales de lectura asíncrona
        if (!ADDR.isEmpty()) {
            valueAtADDR = ADDR.getSingleValue();
        }

        if (!DW.isEmpty()) {
            valueAtDW = DW.getSingleValue();
        }

        if (!MemRead.isEmpty()) {
            valueAtMemRead = MemRead.getSingleValue();
            super.holdIn("Read", delayRead);
        }

        // Ahora las señales síncronas (escritura)
        if (!MemWrite.isEmpty()) {
            valueAtMemWrite = MemWrite.getSingleValue();
        }

        if (!CLK.isEmpty()) {
            Integer tempValueAtCLK = CLK.getSingleValue();
            if (valueAtMemWrite != null && valueAtMemWrite == 1 && valueAtCLK != null && valueAtCLK == 1 && tempValueAtCLK == 0) {
                if (valueAtADDR != null) {
                    data.put(valueAtADDR, valueAtDW);
                    System.out.println("MEM[" + valueAtADDR + "] = " + data.get(valueAtADDR));
                    super.holdIn("Write", delayWrite);
                }
            }
            valueAtCLK = tempValueAtCLK;
        }
    }

    public void lambda() {
        if (valueAtMemRead != null && valueAtMemRead == 1 && valueAtADDR != null) {
            // Se supone que no hay errores en la memoria de datos.
            valueToDR = data.get(valueAtADDR);
            if (valueToDR == null) {
                System.err.println("MEM[" + valueAtADDR + "] is null");
            }
            DR.addValue(valueToDR);
        }
    }

}
