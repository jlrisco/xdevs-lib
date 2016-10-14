package xdevs.lib.tfgs.c1516.distributed.src.ex1;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class MyCsvConsole extends Atomic {

    private static final long serialVersionUID = -8768429093916352817L;
    public Port<Number> iIn = new Port<>("in");
    protected double time;

    public MyCsvConsole(String csvPath) {
        super("CsvConsole");
        super.addInPort(iIn);
    }

    @Override
    public void initialize() {
        this.time = 0.0;
        super.passivate();
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        time += super.getSigma();
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        time += e;
        if (!iIn.isEmpty()) {
            System.out.println(time + ";" + iIn.getSingleValue().doubleValue());
        } else {
            System.out.println("in esta vacio");
        }
    }

    @Override
    public void lambda() {

    }
}
