package xdevs.lib.tfgs.c1718.alex;

import java.util.logging.Level;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;

public class Efp extends Coupled {
    protected Port<Boolean> iStart = new Port<>("iStart");

    public Efp(String name, double period, double observationTime) {
        super(name);
        super.addInPort(iStart);
        Ef ef = new Ef("ef", period, observationTime);
        Processor processor = new Processor("processor", period * 2);
        super.addComponent(ef);
        super.addComponent(processor);

        super.addCoupling(ef.oOut, processor.iIn);
        super.addCoupling(processor.oOut, ef.iIn);
        super.addCoupling(this.iStart, ef.iStart);
    }

    public static void main(String args[]) {
        DevsLogger.setup(Level.INFO);
        Efp efp = new Efp("efp", 1, 100);
        Coordinator coordinator = new Coordinator(efp);
        coordinator.initialize();
        coordinator.simInject(efp.iStart, true);
        coordinator.simulate(Long.MAX_VALUE);
    }
}
