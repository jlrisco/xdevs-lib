package view.modeling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import view.refactor.jgraph.Graph;

public class CoupledViewImpl extends AtomicViewImpl implements CoupledView {
	private Set<AtomicView> components;
	private ArrayList<Coupling> ic;
	private ArrayList<Coupling> eic;
	private ArrayList<Coupling> eoc;
	protected Graph graph;
		
	public CoupledViewImpl(String name) {
		super(name);
		graph = new Graph();
	}
	
	//==========================================================================
	public void addCoupling(AtomicView componentFrom, String portFrom, AtomicView componentTo, String portTo) {
		Coupling coupling = new CouplingImpl(componentFrom, portFrom, componentTo, portTo);
		// Add to connections
		if(componentFrom==this) {
			if(componentFrom.containsInPort(portFrom) && componentTo.containsInPort(portTo))
				eic.add(coupling);
		}
		else if(componentTo==this) {
			if(componentFrom.containsOutPort(portFrom) && componentTo.containsOutPort(portTo))
				eoc.add(coupling);
		}
		else {
			if(componentFrom.containsOutPort(portFrom) && componentTo.containsInPort(portTo))
				ic.add(coupling);
		}
	}
	
	public Collection<Coupling> getICs() { return ic; }
	
	//public void addComponent(AtomicView) {
		
	//}
	
}
