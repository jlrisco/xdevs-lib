package view.refactor.jgraph;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

public class NodeView extends DefaultGraphCell {

	private static final long serialVersionUID = 1L;
	
	// Puertos de entrada
	protected ArrayList<DefaultPort> inPorts;
	protected ArrayList<DefaultPort> outPorts;
	
	public NodeView(String name) {
		super(name);
		GraphConstants.setBorderColor(super.getAttributes(), Color.BLACK);
		inPorts = new ArrayList<DefaultPort>();
		outPorts = new ArrayList<DefaultPort>();
	}
	
	public void setBounds(int x, int y, int w, int h) {
		GraphConstants.setBounds(super.getAttributes(), new Rectangle2D.Double(x,y,w,h));
	}
	
	public void addInPort() {
		DefaultPort port = new DefaultPort();
		inPorts.add(port);
		super.add(port);
		// Reubico los puertos
		double dy = 1000/(inPorts.size()+1);
		for(int i = 0; i<inPorts.size(); ++i) {
			port = inPorts.get(i);
			GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(0, (i+1)*dy));
		}

	}

	public void addOutPort() {
		DefaultPort port = new DefaultPort();
		outPorts.add(port);
		super.add(port);
		// Reubico los puertos
		double dy = 1000/(outPorts.size()+1);
		for(int i = 0; i<outPorts.size(); ++i) {
			port = outPorts.get(i);
			GraphConstants.setOffset(port.getAttributes(), new Point2D.Double(1000, (i+1)*dy));
		}
	}
}
