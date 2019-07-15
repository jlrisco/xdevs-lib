package xdevs.lib.core.modeling;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Component;
import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Coupling;
import xdevs.core.modeling.Port;

public class CoupledView extends Coupled {

    protected Coupled coupled;
    protected mxCell block;
    protected mxGraph graph;
    protected ArrayList<mxCell> inportViews;
    protected ArrayList<mxCell> outportViews;

    /**
     * Constructor.
     * @param coupled
     */
    public CoupledView(Coupled coupled) {
        super(coupled.getName());
        this.coupled = coupled;
        this.block = new mxCell(name);
        mxGeometry geometry = new mxGeometry(0, 0, 100, 100);
        geometry.setRelative(true);
        this.block.setGeometry(geometry);
        this.block.setVertex(true);
        this.graph = null;
        this.inportViews = new ArrayList<>();
        this.outportViews = new ArrayList<>();

        // Components
        coupled.getComponents().forEach((component) -> {
            if (component instanceof Coupled) {
                components.add(new CoupledView((Coupled) component));
            } else if (component instanceof Atomic) {
                components.add(new AtomicView((Atomic) component));
            }
        });

        // Ports
        int numPorts, count;

        Collection<Port<?>> ports = coupled.getInPorts();
        numPorts = ports.size();
        count = 1;
        for (Port port : ports) {
            super.addInPort(port);
            mxCell portView = new mxCell(new PortView(port));
            mxGeometry portGeometry = new mxGeometry(0, (1.0 / (numPorts + 1)) * count, 10, 10);
            portGeometry.setRelative(true);
            portGeometry.setOffset(new mxPoint(-5, -5));
            portView.setGeometry(portGeometry);
            portView.setVertex(true);
            inportViews.add(portView);
            count++;
        }

        ports = coupled.getOutPorts();
        numPorts = ports.size();
        count = 1;
        for (Port port : ports) {
            super.addOutPort(port);
            mxCell portView = new mxCell(new PortView(port));
            mxGeometry portGeometry = new mxGeometry(1.0, (1.0 / (numPorts + 1)) * count, 10, 10);
            portGeometry.setRelative(true);
            portGeometry.setOffset(new mxPoint(-5, -5));
            portView.setGeometry(portGeometry);
            portView.setVertex(true);
            outportViews.add(portView);
            count++;
        }

        // Couplings
        coupled.getEIC().forEach((c) -> {
            super.addCoupling(c.getPortFrom(), c.getPortTo());
        });

        coupled.getIC().forEach((c) -> {
            super.addCoupling(c.getPortFrom(), c.getPortTo());
        });

        coupled.getEOC().forEach((c) -> {
            super.addCoupling(c.getPortFrom(), c.getPortTo());
        });
    }

    private Component getViewAdapter(String componentName) {
        if (this.getName().equals(componentName)) {
            return this;
        }
        for (Component component : components) {
            if (component.getName().equals(componentName)) {
                return component;
            }
        }
        return null;
    }

    public void setBounds(String componentName, double x, double y, double w, double h) {
        if (super.getName().equals(componentName)) {
            mxGeometry geometry = block.getGeometry();
            geometry.setX(x);
            geometry.setY(y);
            geometry.setWidth(w);
            geometry.setHeight(h);
            return;
        }
        for (Component component : components) {
            if (component instanceof CoupledView) {
                CoupledView coupledView = (CoupledView) component;
                coupledView.setBounds(componentName, x, y, w, h);
            } else if (component instanceof AtomicView) {
                AtomicView atomicView = (AtomicView) component;
                if (atomicView.getName().equals(componentName)) {
                    mxGeometry geometry = atomicView.block.getGeometry();
                    geometry.setX(x);
                    geometry.setY(y);
                    geometry.setWidth(w);
                    geometry.setHeight(h);
                    return;
                }
            }
        }

    }

    public void draw(Object parent) {
        inportViews.forEach((port) -> {
            graph.addCell(port, block);
        });
        outportViews.forEach((port) -> {
            graph.addCell(port, block);
        });
        graph.addCell(block, parent);
    }

    public void buildNodes(Object parent, mxGraph graph) throws Exception {
        this.graph = graph;
        this.draw(parent);
        for (Component component : components) {
            if (component instanceof CoupledView) {
                CoupledView coupledView = (CoupledView) component;
                coupledView.buildNodes(block, graph);
            } else if (component instanceof AtomicView) {
                AtomicView atomicView = (AtomicView) component;
                atomicView.graph = graph;
                atomicView.draw(block);
            } else {
                throw new Exception("Class " + component.getClass().getName() + " not supported.");
            }
        }
    }

    public void buildEdges(mxGraph graph) throws Exception {
        for (Component component : components) {
            if (component instanceof CoupledView) {
                CoupledView coupledView = (CoupledView) component;
                coupledView.buildEdges(graph);
            }
        }

        Object nodeFrom, nodeTo;

        Collection<Coupling<?>> couplings = this.getEIC();
        for (Coupling c : couplings) {
            nodeFrom = this.getInportView(c.getPortFrom().getName());
            nodeTo = null;
            if (c.getPortTo().getParent() instanceof CoupledView) {
                nodeTo = ((CoupledView) c.getPortTo().getParent()).getInportView(c.getPortTo().getName());
            } else if (c.getPortTo().getParent() instanceof AtomicView) {
                nodeTo = ((AtomicView) c.getPortTo().getParent()).getInportView(c.getPortTo().getName());
            }
            graph.insertEdge(block, null, "", nodeFrom, nodeTo);
        }

        couplings = this.getIC();
        for (Coupling c : couplings) {
            nodeFrom = null;
            nodeTo = null;
            if (c.getPortFrom().getParent() instanceof CoupledView) {
                nodeFrom = ((CoupledView) c.getPortFrom().getParent()).getOutportView(c.getPortFrom().getName());
            } else if (c.getPortFrom().getParent() instanceof AtomicView) {
                nodeFrom = ((AtomicView) c.getPortFrom().getParent()).getOutportView(c.getPortFrom().getName());
            }
            if (c.getPortTo().getParent() instanceof CoupledView) {
                nodeTo = ((CoupledView) c.getPortTo().getParent()).getInportView(c.getPortTo().getName());
            } else if (c.getPortTo().getParent() instanceof AtomicView) {
                nodeTo = ((AtomicView) c.getPortTo().getParent()).getInportView(c.getPortTo().getName());
            }
            graph.insertEdge(block, null, "", nodeFrom, nodeTo);
        }

        couplings = this.getEOC();
        for (Coupling c : couplings) {
            nodeFrom = null;
            nodeTo = this.getOutportView(c.getPortTo().getName());
            if (c.getPortFrom().getParent() instanceof CoupledView) {
                nodeFrom = ((CoupledView) c.getPortFrom().getParent()).getOutportView(c.getPortFrom().getName());
            } else if (c.getPortFrom().getParent() instanceof AtomicView) {
                nodeFrom = ((AtomicView) c.getPortFrom().getParent()).getOutportView(c.getPortFrom().getName());
            }
            graph.insertEdge(block, null, "", nodeFrom, nodeTo);
        }
    }

    public Object getInportView(String portName) {
        for (mxCell port : inportViews) {
            if (((Port) port.getValue()).getName().equals(portName)) {
                return port;
            }
        }
        return null;
    }

    public Object getOutportView(String portName) {
        for (mxCell port : outportViews) {
            if (((Port) port.getValue()).getName().equals(portName)) {
                return port;
            }
        }
        return null;
    }

    public static JFrame createFrame(CoupledView coupledView) throws Exception {
        JFrame frame = new JFrame();
        mxGraph graph = new mxGraph();
        coupledView.buildNodes(graph.getDefaultParent(), graph);
        coupledView.buildEdges(graph);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        return frame;
    }

}
