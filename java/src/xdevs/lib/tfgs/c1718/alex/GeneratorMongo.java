package xdevs.lib.tfgs.c1718.alex;

import xdevs.core.util.Constants;

public class GeneratorMongo extends AtomicMongo {

    protected PortMongo<Boolean> iStart = new PortMongo<>("iStart");
    protected PortMongo<Boolean> iStop = new PortMongo<>("iStop");
    protected PortMongo<Integer> oOut = new PortMongo<>("oOut");
    private int jobs;
    private double period;

    public GeneratorMongo(String name, double period) {
        super(name);
        super.addInPort(iStart);
        super.addInPort(iStop);
        super.addOutPort(oOut);
        this.period = period;
    }

    @Override
    public void initialize() {
        super.passivate();
        jobs = 1;
    }

    @Override
    public void deltext(double e) {
        if (phaseIs(Constants.PHASE_PASSIVE) && !iStart.isEmpty() && iStart.getSingleValue())
            super.activate();
        else if (phaseIs(Constants.PHASE_ACTIVE) && !iStop.isEmpty() && iStop.getSingleValue())
            super.passivate();
    }

    @Override
    public void lambda() {
        oOut.addValue(jobs);
    }

    @Override
    public void deltint() {
        jobs++;
        this.holdIn(Constants.PHASE_ACTIVE, period);
    }

    @Override
    public void exit() {
    }
}
