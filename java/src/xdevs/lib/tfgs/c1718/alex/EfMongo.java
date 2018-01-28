package xdevs.lib.tfgs.c1718.alex;

public class EfMongo extends CoupledMongo {
	protected PortMongo<Boolean> iStart = new PortMongo<>("iStart");
	protected PortMongo<Integer> iIn = new PortMongo<>("iIn");
	protected PortMongo<Integer> oOut = new PortMongo<>("oOut");
	
	public EfMongo(String name, double period, double observationTime){
		super(name);
		
		super.addInPort(iStart);
		super.addInPort(iIn);
		super.addOutPort(oOut);
		
		GeneratorMongo generator = new GeneratorMongo("generator", period);
		TransducerMongo transducer = new TransducerMongo("trandsucer", observationTime);
		super.addComponent(generator);
		super.addComponent(transducer);
		
		super.addCoupling(generator.oOut, transducer.iArrived);
		super.addCoupling(generator.oOut, this.oOut);
		super.addCoupling(transducer.oStop, generator.iStop);
		super.addCoupling(this.iStart, generator.iStart);
		super.addCoupling(this.iIn, transducer.iSolved);
	}
}
