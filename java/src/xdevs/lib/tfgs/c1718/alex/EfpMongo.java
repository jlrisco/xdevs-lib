package xdevs.lib.tfgs.c1718.alex;

import java.util.logging.Level;

import xdevs.core.util.DevsLogger;

public class EfpMongo extends CoupledMongo {
    protected PortMongo<Boolean> iStart = new PortMongo<>("iStart");

    public EfpMongo(String name, double period, double observationTime) {
        super(name);
        super.addInPort(iStart);
        EfMongo ef = new EfMongo("ef", period, observationTime);
        ProcessorMongo processor = new ProcessorMongo("processor", period * 2);
        super.addComponent(ef);
        super.addComponent(processor);

        super.addCoupling(ef.oOut, processor.iIn);
        super.addCoupling(processor.oOut, ef.iIn);
        super.addCoupling(this.iStart, ef.iStart);
    }

    public static void main(String args[]) {
        DevsLogger.setup(Level.INFO);
        EfpMongo efp = new EfpMongo("efp", 1, 100);
        CoordinatorMongo coordinator = new CoordinatorMongo(efp);
        coordinator.initialize();
        coordinator.simInject(efp.iStart, true);
        coordinator.simulate(Long.MAX_VALUE);
    }
}
