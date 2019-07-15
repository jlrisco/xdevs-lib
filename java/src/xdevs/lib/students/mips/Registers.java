/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 * Banco de registros: Implementa el banco de registros del MIPS. TODO: Aún no
 * lo he chequeado, así que no sé si está todo correcto.
 *
 * @author José L. Risco-Martín
 */
public class Registers extends Atomic {

    public static enum WRITE_MODE {
        PERIOD, SEMI_PERIOD
    };

    public static final String inCLKName = "CLK";
    public static final String inRegWriteName = "RegWrite";
    public static final String inRAName = "RA";
    public static final String inRBName = "RB";
    public static final String inRWName = "RW";
    public static final String inBusWName = "busW";
    public static final String outBusAName = "busA";
    public static final String outBusBName = "busB";

    protected Port<Integer> CLK = new Port<Integer>(inCLKName);
    protected Port<Integer> RegWrite = new Port<Integer>(inRegWriteName);
    protected Port<Integer> RA = new Port<Integer>(inRAName);
    protected Port<Integer> RB = new Port<Integer>(inRBName);
    protected Port<Integer> RW = new Port<Integer>(inRWName);
    protected Port<Integer> busW = new Port<Integer>(inBusWName);
    protected Port<Integer> busA = new Port<Integer>(outBusAName);
    protected Port<Integer> busB = new Port<Integer>(outBusBName);

    protected WRITE_MODE writeMode;
    protected Double delayRead;
    protected Double delayWrite;
    protected Integer valueAtCLK;
    protected Integer valueAtRegWrite;
    protected Integer valueAtRA;
    protected Integer valueAtRB;
    protected Integer valueAtRW;
    protected Integer valueAtBusW;
    protected Integer[] data;

    public Registers(String name, WRITE_MODE writeMode, Double delayRead, Double delayWrite) {
        super(name);
        super.addInPort(CLK);
        super.addInPort(RegWrite);
        super.addInPort(RA);
        super.addInPort(RB);
        super.addInPort(RW);
        super.addInPort(busW);
        super.addOutPort(busA);
        super.addOutPort(busB);
        this.writeMode = writeMode;
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtCLK = null;
        valueAtRegWrite = null;
        valueAtRA = null;
        valueAtRB = null;
        valueAtRW = null;
        valueAtBusW = null;
        data = new Integer[32];
        for (int i = 0; i < data.length; ++i) {
            data[i] = 0;
        }
        super.passivate();
    }

    public Registers(String name, WRITE_MODE writeMode) {
        this(name, writeMode, 0.0, 0.0);
    }

    public Registers(String name) {
        this(name, WRITE_MODE.PERIOD, 0.0, 0.0);
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
        // Primero procesamos los valores de las entradas, lectura asíncrona.

        if (!RA.isEmpty()) {
            Integer tempValueAtRA = RA.getSingleValue();
            if (tempValueAtRA != valueAtRA) {
                valueAtRA = tempValueAtRA;
                super.holdIn("Read", delayRead); // phase = Read, sigma = delayRead
            }
        }

        if (!RB.isEmpty()) {
            Integer tempValueAtRB = RB.getSingleValue();
            if (tempValueAtRB != valueAtRB) {
                valueAtRB = tempValueAtRB;
                super.holdIn("Read", delayRead);
            }
        }

        if (!RW.isEmpty()) {
            valueAtRW = RW.getSingleValue();
        }

        if (!RegWrite.isEmpty()) {
            valueAtRegWrite = RegWrite.getSingleValue();
        }

        if (!busW.isEmpty()) {
            valueAtBusW = busW.getSingleValue();
        }

        // Ahora el reloj, que gobierna la escritura:
        if (!CLK.isEmpty()) {
            Integer tempValueAtCLK = CLK.getSingleValue();
            boolean write = false;
            if (writeMode == WRITE_MODE.SEMI_PERIOD) { // Se escribe si es un paso de 0 a 1
                if (valueAtRegWrite != null && valueAtRegWrite == 1 && valueAtCLK != null && valueAtCLK == 0 && tempValueAtCLK == 1) {
                    write = true;
                }
            } else if (writeMode == WRITE_MODE.PERIOD) { // Se escribe si es un paso de 1 a 0
                if (valueAtRegWrite != null && valueAtRegWrite == 1 && valueAtCLK != null && valueAtCLK == 1 && tempValueAtCLK == 0) {
                    write = true;
                }
            }
            if (write) {
                if (valueAtRW != null && valueAtBusW != null) {
                    data[valueAtRW] = valueAtBusW;
                    System.out.println("BR[" + valueAtRW + "] = " + data[valueAtRW]);
                }
                // Aunque me llegue una petición de lectura y escritura simultáneas,
                // lo que salga en la lectura no será estable mientras no se escriba en el
                // banco de registros.
                if (valueAtRA == valueAtRW || valueAtRB == valueAtRW) {
                    super.holdIn("Write", delayWrite);
                }
                // Nota: Esto se puede aproximar mejor haciendo lo siguiente
                // Si phase == Read =>
                // - Si RW es distinto de RA y RB, sigma = delayRead (RA y RB son estables)
                // - De lo contrario sigma = delayRead
            }
            valueAtCLK = tempValueAtCLK;
        }
    }

    @Override
    public void lambda() {
        if (valueAtRA != null) {
            busA.addValue(data[valueAtRA]);
        }

        if (valueAtRB != null) {
            busB.addValue(data[valueAtRB]);
        }
    }

}
