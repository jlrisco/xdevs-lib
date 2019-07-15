/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.students.mips;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import xdevs.core.simulation.Coordinator;

/**
 * 
 * @author José L. Risco-Martín
 */
public class MipsMonocycle extends MipsAbstract {
    protected Clock clock;
        protected Register pc;
        protected ConstantAdder pcAdder;
        protected InstructionsMemory insMem;
        protected InsNode insNode;
        protected ControladorMono ctrl;
        protected Mux2to1 muxRegDst; // mux of the Registers Bank
        protected Registers registers;// Register Bank creation
        protected SignExtender signExt;
        protected ALUControl aluCtrl;
        protected Mux2to1 muxALUSrc;
        protected ALU alu; // creation of the ALU
        protected Shift2 shif2;
        protected Adder branchAdder;
        protected And2 and2;
        protected Mux2to1 muxPCSrc;
        protected DataMemory dataMemory;
        protected Mux2to1 muxMemToReg;
        protected Mux4to1 muxjr;
        protected Shift2 shif2j;
        protected NodeJ nodej;
        protected Mux2to1 muxRegData;
        protected Mux2to1 muxSlt;
        protected GND gnd;
        protected VCC vcc;

        public MipsMonocycle(String name, String filePath) {
        super(name);
        ArrayList<String> instructions = new ArrayList<String>();
        try {
            instructions = super.loadDisassembledFile(filePath);
        }
        catch(Exception ee) {
            ee.printStackTrace();
        }
        // Components:
        clock = new Clock("Clock",600.0);
        //clock.setLoggerActive(true);
        super.addComponent(clock);

        pc = new Register("PC");
        super.addComponent(pc);

        pcAdder = new ConstantAdder("PCAdder", 4, 100.0);
        //pcAdder.setLoggerActive(true);
        super.addComponent(pcAdder);
        
        // Añadimos una instrucción artificialmente, que cargue en el registro ra(31)
        // el número de instrucciones. De esta forma, al llegar a jr, sólo ejecutará la instrucción siguiente
        // al jr y acabará el programa. Recuerda que el código generado es para
        // saltos retardados.
        Integer destinyAsInt = 4*instructions.size();
        String destinyAsStr = Integer.toBinaryString(destinyAsInt);
        while(destinyAsStr.length()!=16)
            destinyAsStr = "0" + destinyAsStr;
        instructions.add(0, "0010010000011111" + destinyAsStr + ":addiu ra,zero," + destinyAsInt); // addiu ra, zero, instructions.size*4
        insMem = new InstructionsMemory("InsMem", instructions, 200.0);
        //insMem.setLoggerActive(true);
        super.addComponent(insMem);

        insNode = new InsNode("Node");
        //insNode.setLoggerActive(true);
        super.addComponent(insNode);

        ctrl = new ControladorMono("Ctrl");
        //ctrl.setLoggerActive(true);
        super.addComponent(ctrl);

        muxRegDst = new Mux2to1("MuxRegDst"); // mux of the Registers Bank
        //muxRegDst.setLoggerActive(true);
        super.addComponent(muxRegDst);

        registers = new Registers ("Registers", Registers.WRITE_MODE.PERIOD, 50.0, 50.0);// Register Bank creation
        super.addComponent(registers);

        signExt = new SignExtender("SignExt");
        //signExt.setLoggerActive(true);
        super.addComponent(signExt);

        aluCtrl = new ALUControl("ALUCtrl");
        //aluCtrl.setLoggerActive(true);
        super.addComponent(aluCtrl);

        muxALUSrc = new Mux2to1("MuxALUSrc");
        //muxALUSrc.setLoggerActive(true);
        super.addComponent(muxALUSrc);

        alu = new ALU("ALU", 100.0); // creation of the ALU
        super.addComponent(alu);

        shif2 = new Shift2("ShiftLeft2");
        //shif2.setLoggerActive(true);
        super.addComponent(shif2);

        branchAdder = new Adder("BranchAdder", 100.0);
        //branchAdder.setLoggerActive(true);
        super.addComponent(branchAdder);
                
        and2 = new And2("And");
        //and2.setLoggerActive(true);
        super.addComponent(and2);

        muxPCSrc = new Mux2to1("MuxPCSrc");
        //muxPCSrc.setLoggerActive(true);
        super.addComponent(muxPCSrc);

        dataMemory = new DataMemory("DataMem", new HashMap<Integer, Integer>(), 200.0, 200.0);
        super.addComponent(dataMemory);

        muxMemToReg = new Mux2to1("MuxMemToReg");
        //muxMemToReg.setLoggerActive(true);
        super.addComponent(muxMemToReg);

        muxjr = new Mux4to1("MuxJR");
        //muxjr.setLoggerActive(true);
        super.addComponent(muxjr);

        shif2j = new Shift2("ShiftLeft2j");
        //shif2.setLoggerActive(true);
        super.addComponent(shif2j);

        nodej = new NodeJ ("NodeJ");
        super.addComponent(nodej);

        muxRegData = new Mux2to1("muxRegData");
        //muxRegData.setLoggerActive(true);
        super.addComponent(muxRegData);

        muxSlt = new Mux2to1("muxSlt");
        //muxSlt.setLoggerActive(true);
        super.addComponent(muxSlt);

        gnd = new GND("Gnd");
        super.addComponent(gnd);

        vcc = new VCC("Vcc");
        super.addComponent(vcc);

        // Couplings
        // JOSE L.

        // Salidas del reloj:
        super.addCoupling(clock.out, pc.clk);
        super.addCoupling(clock.out, registers.CLK);
        super.addCoupling(clock.out, dataMemory.CLK);

        // Salidas del PC:
        super.addCoupling(pc.out, insMem.ADDR);
        super.addCoupling(pc.out, pcAdder.opA);
        
        // Salidas del sumador del PC
        super.addCoupling(pcAdder.out, branchAdder.opA);
        super.addCoupling(pcAdder.out, muxPCSrc.inPortIn0);

        // Salidas de la memoria de instrucciones
        super.addCoupling(insMem.DR, insNode.in);
        
        // Salidas del nodo de instrucción:
        super.addCoupling(insNode.out3126, ctrl.portOp);
        super.addCoupling(insNode.out0500, ctrl.portFunct);
        super.addCoupling(insNode.out2016, muxRegDst.inPortIn0);
        super.addCoupling(insNode.out1511, muxRegDst.inPortIn1);
        super.addCoupling(insNode.out2521, registers.RA);
        super.addCoupling(insNode.out2016, registers.RB);
        super.addCoupling(insNode.out1500, signExt.inPortIn);
        super.addCoupling(insNode.out0500, aluCtrl.Funct);
        super.addCoupling(insNode.out2500, shif2j.inPortIn);

        // Salidas del controlador
        super.addCoupling(ctrl.portPcWrite, pc.regWrite);
        super.addCoupling(ctrl.portMemRead, dataMemory.MemRead);
        super.addCoupling(ctrl.portMemWrite, dataMemory.MemWrite);
        super.addCoupling(ctrl.portRegWrite, registers.RegWrite);
        super.addCoupling(ctrl.portMemtoReg, muxMemToReg.inPortCtrl);
        super.addCoupling(ctrl.portALUSrc, muxALUSrc.inPortCtrl);
        super.addCoupling(ctrl.portRegDst, muxRegDst.inPortCtrl);
        super.addCoupling(ctrl.portBranch, and2.in0);
        super.addCoupling(ctrl.portALUOp, aluCtrl.ALUop);
        super.addCoupling(ctrl.portJumpReg, muxjr.inPortCtrl);
        super.addCoupling(ctrl.portRegData, muxRegData.inPortCtrl);

        // Salidas de los multiplexores de control / otros
        super.addCoupling(aluCtrl.ALUCtrl, alu.inPortCtrl);
        super.addCoupling(signExt.outPortOut, muxALUSrc.inPortIn1);
        super.addCoupling(signExt.outPortOut, shif2.inPortIn);
        super.addCoupling(muxRegDst.outPortOut, registers.RW);
        super.addCoupling(muxALUSrc.outPortOut, alu.inPortOpB);
        super.addCoupling(shif2.outPortOut, branchAdder.opB);
        super.addCoupling(branchAdder.out, muxPCSrc.inPortIn1);
        super.addCoupling(muxPCSrc.outPortOut,muxjr.inPortIn0);
        super.addCoupling(muxjr.outPortOut,pc.in);
        super.addCoupling(registers.busA, muxjr.inPortIn1);
        super.addCoupling(muxMemToReg.outPortOut, muxRegData.inPortIn0);
        super.addCoupling(and2.out, muxPCSrc.inPortCtrl);
        super.addCoupling(muxRegData.outPortOut, registers.busW);
        super.addCoupling(muxSlt.outPortOut, muxRegData.inPortIn1);
        
        // Salidas del banco de registros
        super.addCoupling(registers.busA, alu.inPortOpA);
        super.addCoupling(registers.busB, muxALUSrc.inPortIn0);
        super.addCoupling(registers.busB, dataMemory.DW);

        // Salidas de la ALU
        super.addCoupling(alu.outPortZero, and2.in1);
        super.addCoupling(alu.outPortOut, dataMemory.ADDR);
        super.addCoupling(alu.outPortOut, muxMemToReg.inPortIn0);
        super.addCoupling(alu.outLessThan, muxSlt.inPortCtrl);

        // Salidas de la memoria de datos
        super.addCoupling(dataMemory.DR, muxMemToReg.inPortIn1);

        // NodeJ
        super.addCoupling(shif2j.outPortOut, nodej.inPortIn1);
        super.addCoupling(pcAdder.out, nodej.inPortIn2);
        super.addCoupling(nodej.outPortOut, muxjr.inPortIn2);

        // Entradas del multiplexor para el slt
        super.addCoupling(gnd.out, muxSlt.inPortIn0);
        super.addCoupling(vcc.out, muxSlt.inPortIn1);
    }

    public static void main(String[] args) {
        bench1_fibonacci();
    }

    public static void test1() {
        // Primer test. c = a + b, con a = 3, b = 4
        // Examinando el ensamblado, el resultado lo almacena en MEM[8-24]
        MipsMonocycle mips = new MipsMonocycle("MIPS", "test" + File.separator + "foo.dis");
        Coordinator coordinator = new Coordinator(mips);
        coordinator.initialize();
        coordinator.simulate(580);
        coordinator.exit();
        System.out.println("Resultado: " + mips.dataMemory.getData().get(8 - 24));
    }

    public static void bench1_fibonacci() {

        /*
	Benchmark1: Función que calcula el término 32 de la serie de fibonacci
	Solución: f2 = 3524578, que está en MEM[-12]
        */
        MipsMonocycle mips = new MipsMonocycle("MIPS", "test" + File.separator + "bench1_fibonacci.dis");
        Coordinator coordinator = new Coordinator(mips);
        coordinator.initialize();
        coordinator.simulate(580);
        coordinator.exit();
        System.out.println("Resultado: " + mips.dataMemory.getData().get(-12));
    }
}
