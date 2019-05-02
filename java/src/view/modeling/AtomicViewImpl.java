package view.modeling;

import view.refactor.jgraph.NodeView;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public abstract class AtomicViewImpl extends Atomic {
	protected NodeView nodeView;
	public AtomicViewImpl(String name) {
		super(name);
		nodeView = new NodeView(name);
	}
	
        @Override
	public void addInPort(Port port) {
		super.addInPort(port);
		nodeView.addInPort();
	}
	
        @Override
	public void addOutPort(Port port) {
		super.addOutPort(port);
		nodeView.addOutPort();
	}
	
	// Graphic properties
	public NodeView getNodeView() {
		return nodeView;
	}
	
	public void setBounds(int x, int y, int w, int h) {
		nodeView.setBounds(x, y, w, h);
	}
	
}
