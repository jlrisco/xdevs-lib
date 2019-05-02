package view.modeling;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;
import xdevs.core.modeling.Coupling;
import xdevs.core.modeling.Port;

public class CouplingViewImpl extends Coupling {
	protected DefaultEdge edge;
	public CouplingViewImpl(Port pFrom, Port pTo) {
		super(pFrom, pTo);
		edge = new DefaultEdge();
		edge.setSource(pFrom);
		edge.setTarget(pTo);
		GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
		GraphConstants.setEndFill(edge.getAttributes(), true);
	}
}
