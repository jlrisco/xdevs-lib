package xdevs.lib.tfgs.c1718.alex;

import java.util.logging.Level;
import java.util.logging.Logger;

import xdevs.core.util.DevsLogger;

public class GptMongo extends CoupledMongo {

    protected PortMongo<Boolean> iStart = new PortMongo<>("iStart");

    public GptMongo(String name, double period, double observationTime) {
        super(name);
        super.addInPort(iStart);

        GeneratorMongo generator = new GeneratorMongo("generator", period);
        ProcessorMongo processor = new ProcessorMongo("processor", 3 * period);
        TransducerMongo transducer = new TransducerMongo("trandsucer", observationTime);
        super.addComponent(generator);
        super.addComponent(processor);
        super.addComponent(transducer);

        super.addCoupling(generator.oOut, processor.iIn);
        super.addCoupling(generator.oOut, transducer.iArrived);
        super.addCoupling(processor.oOut, transducer.iSolved);
        super.addCoupling(transducer.oStop, generator.iStop);
        super.addCoupling(this.iStart, generator.iStart);
    }

    public static void main(String args[]) {
        DevsLogger.setup(Level.FINER);
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        GptMongo gpt = new GptMongo("gpt", 1, 100);
        CoordinatorMongo coordinator = new CoordinatorMongo(gpt);
        coordinator.initialize();
        coordinator.simInject(gpt.iStart, true);
        coordinator.simulate(Long.MAX_VALUE);
    }
}