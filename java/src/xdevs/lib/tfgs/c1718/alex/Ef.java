package xdevs.lib.tfgs.c1718.alex;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;

public class Ef extends Coupled {
    protected Port<Boolean> iStart = new Port<>("iStart");
    protected Port<Integer> iIn = new Port<>("iIn");
    protected Port<Integer> oOut = new Port<>("oOut");

    public Ef(String name, double period, double observationTime) {
        super(name);

        super.addInPort(iStart);
        super.addInPort(iIn);
        super.addOutPort(oOut);

        Generator generator = new Generator("generator", period);
        Transducer transducer = new Transducer("trandsucer", observationTime);
        super.addComponent(generator);
        super.addComponent(transducer);

        super.addCoupling(generator.oOut, transducer.iArrived);
        super.addCoupling(generator.oOut, this.oOut);
        super.addCoupling(transducer.oStop, generator.iStop);
        super.addCoupling(this.iStart, generator.iStart);
        super.addCoupling(this.iIn, transducer.iSolved);
    }
}