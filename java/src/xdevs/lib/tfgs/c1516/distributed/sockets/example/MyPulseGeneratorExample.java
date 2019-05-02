package xdevs.lib.tfgs.c1516.distributed.sockets.example;

import xdevs.core.modeling.Coupled;

public class MyPulseGeneratorExample extends Coupled {

    /**
     *
     */
    private static final long serialVersionUID = -2358243267068712108L;

    public MyPulseGeneratorExample() {
        super("PulseGeneratorExample");
        MyPulseGenerator pulse = new MyPulseGenerator("Pulse", 10, 3, 5, 5);
        super.addComponent(pulse);
        MyCsvConsole scope = new MyCsvConsole("CSV");
        super.addComponent(scope);
        super.addCoupling(pulse.oOut, scope.iIn);
    }
}
