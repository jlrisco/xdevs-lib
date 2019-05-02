package xdevs.lib.tfgs.c1718.alex;

import xdevs.core.simulation.SimulationClock;

public abstract class AbstractSimulatorMongo {

    protected SimulationClock clock;
    protected double tL; // Time of last event
    protected double tN; // Time of next event

    public AbstractSimulatorMongo(SimulationClock clock) {
        this.clock = clock;
    }

    public abstract void initialize();

    public abstract void exit();

    public abstract double ta();

    public abstract void lambda();

    public abstract void deltfcn();

    public abstract void clear();

    public abstract ComponentMongo getModel();

    public double getTL() {
        return tL;
    }

    public void setTL(double tL) {
        this.tL = tL;
    }

    public double getTN() {
        return tN;
    }

    public void setTN(double tN) {
        this.tN = tN;
    }

    public SimulationClock getClock() {
        return clock;
    }
}
