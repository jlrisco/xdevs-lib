package xdevs.lib.students.mips;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class GND extends Atomic {
    // Nombres de los puertos de entrada/salida. En este caso, al tratarse de
    // un GND, sólo tenemos un puerto de salida.

    protected Port<Integer> out = new Port<>("out");
    protected double delay;

    public GND(String name, double delay) {
        super(name);
        super.addOutPort(out);
        this.delay = delay;
    }

    public GND(String name) {
        this(name, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void lambda() {
        out.addValue(0);
    }

    // La función deltext no es necesaria porque este elemento no tiene
    // puertos de entrada.
    @Override
    public void deltext(double e) {
        super.passivate();
    }

    @Override
    public void initialize() {
        super.holdIn("active", delay);
    }

    @Override
    public void exit() {
    }
}

