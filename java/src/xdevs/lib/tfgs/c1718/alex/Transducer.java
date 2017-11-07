package xdevs.lib.tfgs.c1718.alex;

import java.util.HashMap;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;
import xdevs.core.util.Constants;

public class Transducer extends Atomic {
	
	private static final Logger LOGGER = Logger.getLogger(Transducer.class.getName());
	
	protected Port<Integer> iArrived = new Port<>("iArrived");
	protected Port<Integer> iSolved = new Port<>("iSolved");
	protected Port<Boolean> oStop = new Port<>("oStop");
	
	private HashMap<Integer,Double> jobs = new HashMap<Integer,Double>();
	private double observationTime;
	private int arrived;
	private int solved;
	private double clock;
	private double totalTa;
	
	
	public Transducer(String name, double observationTime){
		super(name);
		super.addInPort(iArrived);
		super.addInPort(iSolved);
		super.addOutPort(oStop);
		this.observationTime = observationTime;
	}

	@Override
	public void initialize() {
		super.holdIn(Constants.PHASE_ACTIVE, observationTime);
		arrived = 0;
		solved = 0;
		clock = 0;
		totalTa = 0;
	}

	@Override
	public void deltext(double e) {
		clock = clock + e;
		if(phaseIs(Constants.PHASE_ACTIVE)){
			if(!iArrived.isEmpty()){
				arrived++;
				int job = iArrived.getSingleValue();
				jobs.put(job,clock);
				LOGGER.fine("Start job" + job + " @ t = " + clock);
			}
			if(!iSolved.isEmpty()){
				solved++;
				int job = iSolved.getSingleValue();
				totalTa += clock - jobs.get(job);
				LOGGER.fine("Finish job " + job + " @ t = " + clock);
			}
		}
	}

	@Override
	public void lambda() {
		if(phaseIs("Stop Generator"))
			oStop.addValue(true);
	}
	
	@Override
	public void deltint() {
		clock = clock + getSigma();
		if(phaseIs(Constants.PHASE_ACTIVE)){
			double throughput = 0.0;
			double avgTa = 0.0;
			if(solved != 0){
				avgTa = totalTa / solved;
				if(clock > 0)
					throughput = solved / clock;
			}
			LOGGER.info("End time: " + clock);
			LOGGER.info("Jobs arrived: " + arrived);
			LOGGER.info("Jobs Solved: " + solved);
			LOGGER.info("Average Ta: " + avgTa);
			LOGGER.info("Throughput: " + throughput);
			holdIn("Stop Generator", 0);
			
		} else if(phaseIs("Stop Generator")){
			passivate();
		}
	}

	@Override
	public void exit() {		
	}
}
