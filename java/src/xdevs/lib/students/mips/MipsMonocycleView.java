/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.students.mips;

import java.io.File;
import xdevs.lib.core.modeling.CoupledView;
import xdevs.lib.core.simulation.CoordinatorView;

/**
 * 
 * @author José L. Risco-Martín
 */
public class MipsMonocycleView extends MipsMonocycle {

    public MipsMonocycleView(String name, String filePath) {
        super(name, filePath);
    }

    public static void main(String[] args) {
        MipsMonocycleView mips = null;
        CoupledView mipsView = null;
        try {
            mips = new MipsMonocycleView("MIPS", "test" + File.separator + "foo.dis");
            mipsView = new CoupledView(mips);
            mipsView.setBounds("MIPS", 0, 0, 1800, 900);
            mipsView.setBounds(mips.clock.getName(), 0, 0, 20, 20);
            mipsView.setBounds(mips.ctrl.getName(), 0.05, 0.00, 110, 250);
            mipsView.setBounds(mips.aluCtrl.getName(), 0.35, 0.00, 80, 80);
            mipsView.setBounds(mips.and2.getName(), 0.45, 0.00, 80, 80);
            mipsView.setBounds(mips.muxjr.getName(), 0.02, 0.35, 120, 80);
            mipsView.setBounds(mips.pc.getName(), 0.02, 0.50, 100, 80);
            mipsView.setBounds(mips.muxPCSrc.getName(), 0.02, 0.65, 100, 80);
            mipsView.setBounds(mips.pcAdder.getName(), 0.15, 0.35, 80, 80);
            mipsView.setBounds(mips.shif2j.getName(), 0.17, 0.10, 80, 80);
            mipsView.setBounds(mips.nodej.getName(), 0.25, 0.10, 90, 80);
            mipsView.setBounds(mips.branchAdder.getName(), 0.25, 0.20, 90, 80);
            mipsView.setBounds(mips.shif2.getName(), 0.25, 0.30, 90, 80);
            mipsView.setBounds(mips.signExt.getName(), 0.25, 0.40, 90, 80);
            mipsView.setBounds(mips.insMem.getName(), 0.12, 0.5, 320, 100);
            mipsView.setBounds(mips.insNode.getName(), 0.12, 0.70, 300, 120);
            mipsView.setBounds(mips.muxRegDst.getName(), 0.35, 0.60, 80, 80);
            mipsView.setBounds(mips.muxRegData.getName(), 0.35, 0.70, 80, 80);
            mipsView.setBounds(mips.gnd.getName(), 0.30, 0.85, 40, 40);
            mipsView.setBounds(mips.vcc.getName(), 0.30, 0.90, 40, 40);
            mipsView.setBounds(mips.muxSlt.getName(), 0.35, 0.80, 80, 80);
            mipsView.setBounds(mips.registers.getName(), 0.45, 0.45, 180, 300);
            mipsView.setBounds(mips.muxALUSrc.getName(), 0.60, 0.40, 80, 80);
            mipsView.setBounds(mips.alu.getName(), 0.65, 0.50, 180, 90);
            mipsView.setBounds(mips.dataMemory.getName(), 0.65, 0.65, 180, 150);
            mipsView.setBounds(mips.muxMemToReg.getName(), 0.80, 0.75, 100, 100);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        if (mipsView == null) {
            return;
        }
        CoordinatorView coordinator = new CoordinatorView(mipsView);
        coordinator.initialize();
        coordinator.setVisible(true);
        //Coordinator coordinator = new Coordinator(mips);
        //coordinator.simulate(Long.MAX_VALUE);
    }
}
