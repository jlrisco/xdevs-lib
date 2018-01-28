package xdevs.lib.tfgs.c1718.alex;

import xdevs.core.util.Constants;

public class ProcessorMongo extends AtomicMongo {
	
	protected PortMongo<Integer> iIn = new PortMongo<>("iIn");
	protected PortMongo<Integer> oOut = new PortMongo<>("oOut");
	private int currentJob = 0;
	private double processingTime;
	
	public ProcessorMongo(String name, double processingTime){
		super(name);
		super.addInPort(iIn);
		super.addOutPort(oOut);
		this.processingTime = processingTime;
	}

	@Override
	public void initialize() {
		super.passivate();		
	}
	
	@Override
	public void deltext(double e) {
		// Only accept new jobs if not currently processing a job
		if(super.phaseIs(Constants.PHASE_PASSIVE)){ 
			currentJob = iIn.getSingleValue();
			super.holdIn(Constants.PHASE_ACTIVE, processingTime);
		}
	}
	
	@Override
	public void lambda() {
		if(super.phaseIs(Constants.PHASE_ACTIVE))
			oOut.addValue(currentJob);
	}
	
	@Override
	public void deltint() {
		super.passivate();
	}

	@Override
	public void exit() {
	}
}
