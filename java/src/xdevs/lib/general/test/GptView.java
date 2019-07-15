/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.general.test;

import xdevs.core.test.efp.Gpt;
import xdevs.lib.core.modeling.CoupledView;
import xdevs.lib.core.simulation.CoordinatorView;

/**
 *
 * @author jlrisco
 */
public class GptView {
    


    public static void main(String[] args) {
        Gpt gpt = new Gpt("Gpt", 1, 10);
        CoupledView coupledView = new CoupledView(gpt);
        coupledView.setBounds("Gpt",0, 0, 700, 500);
        coupledView.setBounds("generator", 0.1, 0.1, 100, 100);
        coupledView.setBounds("transducer", 0.1, 0.5, 100, 100);
        coupledView.setBounds("processor", 0.7, 0.1, 100, 100);
        CoordinatorView coordinator = new CoordinatorView(coupledView);
        coordinator.initialize();
        coordinator.setVisible(true);
    }
}
