package xdevs.lib.tfgs.c1718.alex;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;

public class Generator extends Atomic {
	
	protected Port<Boolean> iStart = new Port<>("iStart");
    protected Port<Boolean> iStop = new Port<>("iStop");
    protected Port<Integer> oOut = new Port<>("oOut");
    private int jobs;
    private double period;
    
    public Generator(String name, double period){
    	super(name);
    	super.addInPort(iStart);
    	super.addInPort(iStop);
    	super.addOutPort(oOut);
    	this.period = period;
    }
    
	@Override
	public void initialize() {
		jobs = 1;
	}

	@Override
	public void deltext(double e) {
		if(!iStart.isEmpty() && iStart.getSingleValue())
			super.activate();
		else if(!iStop.isEmpty() && iStop.getSingleValue())
			super.passivate();
	}
	
	@Override
	public void lambda() {
		oOut.addValue(jobs);
	}

	@Override
	public void deltint() {
		jobs++;
		this.holdIn(Constants.PHASE_ACTIVE, period);
	}
	
	@Override
	public void exit() {		
	}    
}
