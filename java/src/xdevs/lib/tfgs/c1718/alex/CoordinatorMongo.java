package xdevs.lib.tfgs.c1718.alex;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import xdevs.core.simulation.SimulationClock;
import xdevs.core.util.Constants;
import xdevs.core.util.Util;

public class CoordinatorMongo extends AbstractSimulatorMongo {

    private static final Logger LOGGER = Logger.getLogger(CoordinatorMongo.class.getName());

    protected CoupledMongo model;
    protected LinkedList<AbstractSimulatorMongo> simulators = new LinkedList<>();

    public CoordinatorMongo(SimulationClock clock, CoupledMongo model, boolean flatten) {
        super(clock);
        LOGGER.fine(model.getName() + "'s hierarchical...\n" + printCouplings(model));
        if (flatten) {
            this.model = model.flatten();
        } else {
            this.model = model;
        }
    }

    public CoordinatorMongo(SimulationClock clock, CoupledMongo model) {
        this(clock, model, false);
    }

    public CoordinatorMongo(CoupledMongo model, boolean flatten) {
        this(new SimulationClock(), model, flatten);
    }

    public CoordinatorMongo(CoupledMongo model) {
        this(model, true);
    }

    protected void buildHierarchy() {
        // Build hierarchy
        Collection<ComponentMongo> components = model.getComponents();
        for (ComponentMongo component : components) {
            if (component instanceof CoupledMongo) {
                CoordinatorMongo coordinator = new CoordinatorMongo(clock, (CoupledMongo) component, false);
                simulators.add(coordinator);
            } else if (component instanceof AtomicMongo) {
                SimulatorMongo simulator = new SimulatorMongo(clock, (AtomicMongo) component);
                simulators.add(simulator);
            }
        }
    }

    @Override
    public void initialize() {
        this.buildHierarchy();
        for (AbstractSimulatorMongo simulator : simulators) {
            simulator.initialize();
        }
        tL = clock.getTime();
        tN = tL + ta();
    }

    @Override
    public void exit() {
        for (AbstractSimulatorMongo simulator : simulators) {
            simulator.exit();
        }
    }

    public Collection<AbstractSimulatorMongo> getSimulators() {
        return simulators;
    }

    @Override
    public double ta() {
        double tn = Constants.INFINITY;
        for (AbstractSimulatorMongo simulator : simulators) {
            if (simulator.getTN() < tn) {
                tn = simulator.getTN();
            }
        }
        return tn - clock.getTime();
    }

    @Override
    public void lambda() {
        for (AbstractSimulatorMongo simulator : simulators) {
            simulator.lambda();
        }
        propagateOutput();
    }

    public void propagateOutput() {
        LinkedList<CouplingMongo<?>> ic = model.getIC();
        for (CouplingMongo<?> c : ic) {
            c.propagateValues();
        }

        LinkedList<CouplingMongo<?>> eoc = model.getEOC();
        for (CouplingMongo<?> c : eoc) {
            c.propagateValues();
        }
    }

    @Override
    public void deltfcn() {
        propagateInput();
        for (AbstractSimulatorMongo simulator : simulators) {
            simulator.deltfcn();
        }
        tL = clock.getTime();
        tN = tL + ta();
    }

    public void propagateInput() {
        LinkedList<CouplingMongo<?>> eic = model.getEIC();
        for (CouplingMongo<?> c : eic) {
            c.propagateValues();
        }
    }

    @Override
    public void clear() {
        for (AbstractSimulatorMongo simulator : simulators) {
            simulator.clear();
        }
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

    /**
     * Injects a value into the PortMongo "PortMongo", calling the transition function.
     *
     * @param e      elapsed time
     * @param port   input PortMongo to inject the set of values
     * @param values set of values to inject
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void simInject(double e, PortMongo port, Collection<Object> values) {
        double time = tL + e;
        if (time <= tN) {
            port.addValues(values);
            clock.setTime(time);
            deltfcn();
        } else {
            LOGGER.severe("Time: " + tL + " - ERROR input rejected: elapsed time " + e + " is not in bounds.");
        }
    }

    /**
     * @param port   input PortMongo to inject the set of values
     * @param values set of values to inject
     * @see xdevs.core.simulation.CoordinatorMongo#simInject(double, xdevs.core.modeling.PortMongo, java.util.Collection)
     */
    @SuppressWarnings("rawtypes")
    public void simInject(PortMongo port, Collection<Object> values) {
        simInject(0.0, port, values);
    }

    /**
     * Injects a single value in the given input PortMongo with elapsed time e.
     *
     * @param e
     * @param port
     * @param value
     * @see xdevs.core.simulation.CoordinatorMongo#simInject(double, xdevs.core.modeling.PortMongo, java.util.Collection)
     */
    @SuppressWarnings("rawtypes")
    public void simInject(double e, PortMongo port, Object value) {
        LinkedList<Object> values = new LinkedList<Object>();
        values.add(value);
        simInject(e, port, values);
    }

    /**
     * Injects a single value in the given input PortMongo with elapsed time e equal
     * to 0.
     *
     * @param port
     * @param value
     * @see xdevs.core.simulation.CoordinatorMongo#simInject(double, xdevs.core.modeling.PortMongo, java.util.Collection)
     */
    @SuppressWarnings("rawtypes")
    public void simInject(PortMongo port, Object value) {
        simInject(0.0, port, value);
    }

    public void simulate(long numIterations) {
        LOGGER.fine("START SIMULATION");
        clock.setTime(tN);
        long counter;
        for (counter = 1; counter < numIterations
                && clock.getTime() < Constants.INFINITY; counter++) {
            lambda();
            deltfcn();
            clear();
            clock.setTime(tN);
        }
    }

    public void simulate(double timeInterval) {
        LOGGER.fine("START SIMULATION");
        clock.setTime(tN);
        double tF = clock.getTime() + timeInterval;
        while (clock.getTime() < Constants.INFINITY && clock.getTime() < tF) {
            lambda();
            deltfcn();
            clear();
            clock.setTime(tN);
        }
    }

    @Override
    public CoupledMongo getModel() {
        return model;
    }

    private static String printCouplings(CoupledMongo model) {
        StringBuilder sb = new StringBuilder(" coupling: [");
        sb.append(Util.printLinkedList("\n\tEIC", model.getEIC()));
        sb.append(Util.printLinkedList("\n\tIC", model.getIC()));
        sb.append(Util.printLinkedList("\n\tEOC", model.getEOC()));
        sb.append("\n\t]");
        return sb.toString();
    }
}
