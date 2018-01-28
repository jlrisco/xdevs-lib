package xdevs.lib.tfgs.c1718.alex;

public class CouplingMongo<E> {
		
	protected PortMongo<E> portFrom;
    protected PortMongo<E> portTo;

	public CouplingMongo(PortMongo<E> portFrom, PortMongo<E> portTo) {
		this.portFrom = portFrom;
        this.portTo = portTo;
	}
	
	@Override
    public String toString() {
        return "(" + portFrom + "->" + portTo + ")";
    }
	
	public void propagateValues() {
		portTo.addValues(portFrom.getValues());
    }
	
    public PortMongo<E> getPortFrom() {
        return portFrom;
    }

    public PortMongo<E> getPortTo() {
        return portTo;
    }

}
