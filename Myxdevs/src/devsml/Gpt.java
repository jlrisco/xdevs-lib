package devsml;
import java.util.logging.Level;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;

/**
 *
 * @author jlrisco
 */
public class Gpt extends Coupled {

    @SuppressWarnings("deprecation")
	public Gpt(String name, double period, double observationTime) {
    	super(name);
        Gen generator = new Gen("generator", period);
        super.addComponent(generator);
        Processador processor = new Processador("processor", 3*period);
        super.addComponent(processor);
        Transductor transducer = new Transductor("transducer", observationTime);
        super.addComponent(transducer);

        super.addCoupling(generator, generator.oOut, processor, processor.iIn);
        super.addCoupling(generator, generator.oOut, transducer, transducer.iArrived);
        super.addCoupling(processor, processor.oOut, transducer, transducer.iSolved);
        super.addCoupling(transducer, transducer.oOut, generator, generator.iStop);
    }

    public static void main(String args[]) {
        DevsLogger.setup(Level.INFO);
        Gpt gpt = new Gpt("gpt", 1, 100);
        //CoordinatorParallel coordinator = new CoordinatorParallel(gpt);
        Coordinator coordinator = new Coordinator(gpt);
        //RTCentralCoordinator coordinator = new RTCentralCoordinator(gpt);
        coordinator.initialize();
        coordinator.simulate(Long.MAX_VALUE);
    }

}

