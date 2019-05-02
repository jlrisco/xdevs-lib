package xdevs.lib.tfgs.c1516.distributed.sockets.example;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class MyStep extends Atomic {

    /**
     *
     */
    private static final long serialVersionUID = -136335389373588549L;
    public Port<Double> oOut = new Port<>("out");
    protected double initialValue;
    protected double stepTime;
    protected double finalValue;

    public MyStep(String name, double initialValue, double stepTime, double finalValue) {
        super(name);
        super.addOutPort(oOut);
        this.initialValue = initialValue;
        this.stepTime = stepTime;
        this.finalValue = finalValue;
    }

    public void deltext(double arg0) {
        // TODO Auto-generated method stub
        System.out.print("final value" + this.finalValue);
    }

    @Override
    public void deltint() {
        if (super.phaseIs("initialValue")) {
            super.holdIn("finalValue", stepTime);
        } else if (super.phaseIs("finalValue")) {
            super.passivate();
        }
        System.out.print("final value" + this.finalValue);

    }

    @Override
    public void lambda() {
        if (super.phaseIs("initialValue")) {
            oOut.addValue(initialValue);
        } else if (super.phaseIs("finalValue")) {
            oOut.addValue(finalValue);
        }
        System.out.print("final value" + this.finalValue);

    }

    @Override
    public void exit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initialize() {
        super.holdIn("initialValue", 0.0);
        System.out.print("final value" + this.finalValue);
    }

}
