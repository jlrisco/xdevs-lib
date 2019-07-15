/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.core.simulation;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import xdevs.core.simulation.Coordinator;
import xdevs.lib.core.modeling.CoupledView;

/**
 *
 * @author jlrisco
 */
public class CoordinatorView extends JFrame {

    protected mxGraph graph;
    protected CoupledView coupledView;
    protected Coordinator coordinator;
    protected boolean firstStep;

    public CoordinatorView(CoupledView coupledView) {
        super();

        this.graph = new mxGraph();
        this.coupledView = coupledView;
        try {
            coupledView.buildNodes(graph.getDefaultParent(), graph);
            coupledView.buildEdges(graph);
        } catch (Exception ee) {
            System.err.println(ee.getLocalizedMessage());
        }
        coordinator = new Coordinator(coupledView, false);
        this.firstStep = true;
    }

    public void initialize() {
        coordinator.initialize();
        // PANEL 1
        mxGraphComponent graphPanel = new mxGraphComponent(graph);

        // PANEL 2
        // Button step
        JButton buttonStep = new JButton();
        buttonStep.setText("Step");
        buttonStep.setEnabled(true);
        buttonStep.addActionListener((java.awt.event.ActionEvent e) -> {
            //JButton button = (JButton) e.getSource();
            if (!firstStep) {
                coordinator.clear();
            } else {
                firstStep = false;
            }
            System.out.println("Current tN: " + coordinator.getTN());
            coordinator.getClock().setTime(coordinator.getTN());
            buttonStep.setText("t <- " + coordinator.getClock().getTime() + " s.");
            coordinator.lambda();
            coordinator.deltfcn();
        });
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.add(buttonStep, null);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(graphPanel);
        content.add(panelButtons);

        super.setContentPane(content);
        super.pack();
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setTitle("xDEVS Simulator");
    }

}
