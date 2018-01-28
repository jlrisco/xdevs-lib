package xdevs.lib.tfgs.c1718.alex;

import java.util.Collection;

import xdevs.core.simulation.SimulationClock;

public class SimulatorMongo extends AbstractSimulatorMongo {

	//private MongoOperations<Object> mongo;
	protected AtomicMongo model;

	public SimulatorMongo(SimulationClock clock, AtomicMongo model) {
        super(clock);
        this.model = model;
    }

	@Override
	public void initialize() {
		model.initialize();
		tL = clock.getTime();
		tN = tL + model.ta();
	}

	@Override
	public void exit() {
		model.exit();
	}

	@Override
	public double ta() {
		return model.ta();
	}

	@Override
	public void deltfcn() {
		double t = clock.getTime();
		boolean isInputEmpty = model.isInputEmpty();
		if (isInputEmpty && t != tN) {
			return;
		} else if (!isInputEmpty && t == tN) {
			double e = t - tL;
			model.setSigma(model.getSigma() - e);
			model.deltcon(e);
		} else if (isInputEmpty && t == tN) {
			model.deltint();
		} else if (!isInputEmpty && t != tN) {
			double e = t - tL;
			model.setSigma(model.getSigma() - e);
			model.deltext(e);
			//mongo.readValues(model);
		}
		tL = t;
		tN = tL + model.ta();
	}

	@Override
	public void lambda() {
		if (clock.getTime() == tN) {
			model.lambda();
		}
	}

	@Override
	public void clear() {
		Collection<PortMongo<?>> inPorts;
		inPorts = model.getInPorts();
		for (PortMongo<?> port : inPorts) {
			port.clear();
		}
		Collection<PortMongo<?>> outPorts;
		outPorts = model.getOutPorts();
		for (PortMongo<?> port : outPorts) {
			port.clear();
		}
	}

	@Override
	public AtomicMongo getModel() {
		return model;
	}

}
