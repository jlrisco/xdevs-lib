package xdevs.lib.core.modeling;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.Collection;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class AtomicView extends Atomic {

    protected Atomic atomic;
    protected mxCell block;
    protected mxGraph graph;
    protected ArrayList<mxCell> inportViews;
    protected ArrayList<mxCell> outportViews;

    public AtomicView(Atomic atomic) {
        super(atomic.getName());
        this.atomic = atomic;
        this.block = new mxCell(this);
        mxGeometry geometry = new mxGeometry(0, 0, 100, 100);
        geometry.setRelative(true);
        this.block.setGeometry(geometry);
        this.block.setVertex(true);
        this.graph = null;
        this.inportViews = new ArrayList<>();
        this.outportViews = new ArrayList<>();

        int numPorts, count;

        Collection<Port<?>> ports = atomic.getInPorts();
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

        ports = atomic.getOutPorts();
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

        super.holdIn(atomic.getPhase(), atomic.getSigma());
    }

    @Override
    public void initialize() {
        atomic.initialize();
        super.holdIn(atomic.getPhase(), atomic.getSigma());
    }

    public void setBounds(double x, double y, double w, double h) {
        mxGeometry geometry = block.getGeometry();
        geometry.setX(x);
        geometry.setY(y);
        geometry.setWidth(w);
        geometry.setHeight(h);
    }

    public Object getInportView(String portName) {
        for (mxCell portView : inportViews) {
            if (((PortView) portView.getValue()).getPort().getName().equals(portName)) {
                return portView;
            }
        }
        return null;
    }

    public Object getOutportView(String portName) {
        for (mxCell portView : outportViews) {
            if (((PortView) portView.getValue()).getPort().getName().equals(portName)) {
                return portView;
            }
        }
        return null;
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

    @Override
    public void deltint() {
        atomic.deltint();
        super.holdIn(atomic.getPhase(), atomic.getSigma());
        Object[] objs = {block};
        graph.setCellStyle("fillColor=yellow;", objs);
        graph.repaint(new mxRectangle(block.getGeometry().getRectangle()));
        graph.refresh();
    }

    @Override
    public void deltext(double e) {
        inportViews.forEach((port) -> {
            graph.repaint(new mxRectangle(port.getGeometry().getRectangle()));
        });
        atomic.setSigma(atomic.getSigma() - e);
        atomic.deltext(e);
        super.holdIn(atomic.getPhase(), atomic.getSigma());
        Object[] objs = {block};
        graph.setCellStyle("fillColor=green;", objs);
        graph.repaint(new mxRectangle(block.getGeometry().getRectangle()));
        graph.refresh();
    }

    @Override
    public void lambda() {
        atomic.lambda();
        Object[] objs = {block};
        graph.setCellStyle("fillColor=red;", objs);
        graph.repaint(new mxRectangle(block.getGeometry().getRectangle()));
        outportViews.forEach((port) -> {
            graph.repaint(new mxRectangle(port.getGeometry().getRectangle()));
        });
        graph.refresh();
    }

    @Override
    public void deltcon(double e) {
        atomic.setSigma(atomic.getSigma() - e);
        deltint();
        deltext(0);
        Object[] objs = {block};
        graph.setCellStyle("fillColor=orange;", objs);
        graph.repaint(new mxRectangle(block.getGeometry().getRectangle()));
        graph.refresh();
    }

    @Override
    public void exit() {
        atomic.exit();
    }

    @Override
    public String toString() {
        return super.getName() + "\n" + atomic.getPhase() + "\n" + atomic.getSigma();
    }
}
