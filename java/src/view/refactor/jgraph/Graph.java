package view.refactor.jgraph;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

public class Graph extends JGraph {
	private static final long serialVersionUID = 1L;
	public Graph() {
		super();
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model, new DefaultCellViewFactory());
		super.setModel(model);
		super.setGraphLayoutCache(view);
	}
}
