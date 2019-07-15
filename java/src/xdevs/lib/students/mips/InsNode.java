/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;


/**
 * Este es el nodo que está justo después de la memoria de instrucciones.
 * Separa la instrucción originalmente en binario en varias partes, traduciento
 * la información a tipo entero
 * @author jlrisco
 */
public class InsNode extends Atomic {

    public static final String inInName = "in";
    public static final String outOut3126Name = "out3126";
    public static final String outOut2521Name = "out2521";
    public static final String outOut2016Name = "out2016";
    public static final String outOut1511Name = "out1511";
    public static final String outOut1500Name = "out1500";
    public static final String outOut0500Name = "out0500";
    public static final String outOut2500Name = "out2500";

    protected Port<String> in = new Port<String>(InsNode.inInName);
    protected Port<Integer> out3126 = new Port<Integer>(InsNode.outOut3126Name);
    protected Port<Integer> out2521 = new Port<Integer>(InsNode.outOut2521Name);
    protected Port<Integer> out2016 = new Port<Integer>(InsNode.outOut2016Name);
    protected Port<Integer> out1511 = new Port<Integer>(InsNode.outOut1511Name);
    protected Port<Integer> out1500 = new Port<Integer>(InsNode.outOut1500Name);
    protected Port<Integer> out0500 = new Port<Integer>(InsNode.outOut0500Name);
    protected Port<Integer> out2500 = new Port<Integer>(InsNode.outOut2500Name);

    protected String valueAtIn = null;
    
    public InsNode(String name) {
        super(name);
        super.addInPort(in);
        super.addOutPort(out3126);
        super.addOutPort(out2521);
        super.addOutPort(out2016);
        super.addOutPort(out1511);
        super.addOutPort(out1500);
        super.addOutPort(out0500);
        super.addOutPort(out2500);
        super.passivate();
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
        if(in.getSingleValue()!=null) {
            valueAtIn = in.getSingleValue();
            super.holdIn("active", 0.0);
        }
    }

    @Override
    public void lambda() {
        out3126.addValue(Integer.parseInt(valueAtIn.substring(0, 6), 2)); //salida 1 de node1
        out2521.addValue(Integer.parseInt(valueAtIn.substring(6, 11), 2)); // salida2 de node1
        out2016.addValue(Integer.parseInt(valueAtIn.substring(11, 16), 2));// salida 3 de node1
        out1511.addValue(Integer.parseInt(valueAtIn.substring(16, 21), 2)); // salida a ALU de node2
        // Hay que hacer la transformación del inmediato a complemento a 2
        int sum = 0;
        if(valueAtIn.substring(16, 32).charAt(0)=='1')
            sum = -((int)Math.pow(2, 16)); // Hay que restar 2 veces 2^15
        Integer valueTo1500 = Integer.parseInt(valueAtIn.substring(16, 32), 2) + sum;
        out1500.addValue(valueTo1500); //  salida 4 del node
        out0500.addValue(Integer.parseInt(valueAtIn.substring(26, 32), 2)); //  salida 5 del node
        
        int sum2 = 0;
        if(valueAtIn.substring(6, 32).charAt(0)=='1')
            sum2 = -((int)Math.pow(2, 26)); // Hay que restar 2 veces 2^25
        Integer valueTo2500 = Integer.parseInt(valueAtIn.substring(6, 32), 2) + sum2;
        out2500.addValue(valueTo2500); //  salida 4 del node
    }

}
