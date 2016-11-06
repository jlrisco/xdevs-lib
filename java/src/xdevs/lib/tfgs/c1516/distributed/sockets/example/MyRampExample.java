package xdevs.lib.tfgs.c1516.distributed.sockets.example;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;

public class MyRampExample extends Coupled {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 549090665895758789L;

	public MyRampExample() {
	    super("MyRampExample");
	    MyRamp ramp = new MyRamp("MyRamp", 2, 10, 2, 0.1);
	    super.addComponent(ramp);    
	    MyCsvConsole scope = new MyCsvConsole("CSV");
	    super.addComponent(scope);
	    super.addCoupling(ramp, ramp.oOut, scope, scope.iIn);
	  }
	  
	  public static void main(String[] args) {
	    MyRampExample example = new MyRampExample();
	    Coordinator coordinator = new Coordinator(example);
	    coordinator.initialize();
	    coordinator.simulate(30.0);
	    coordinator.exit();
	  }
}