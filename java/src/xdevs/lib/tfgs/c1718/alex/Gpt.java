package xdevs.lib.tfgs.c1718.alex;

import java.util.logging.Level;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;

public class Gpt extends Coupled {

    protected Port<Boolean> iStart = new Port<>("iStart");

    public Gpt(String name, double period, double observationTime) {
        super(name);
        super.addInPort(iStart);

        Generator generator = new Generator("generator", period);
        Processor processor = new Processor("processor", 3 * period);
        Transducer transducer = new Transducer("trandsucer", observationTime);
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
        Gpt gpt = new Gpt("gpt", 1, 100);
        Coordinator coordinator = new Coordinator(gpt);
        coordinator.initialize();
        coordinator.simInject(gpt.iStart, true);
        coordinator.simulate(Long.MAX_VALUE);
    }
}