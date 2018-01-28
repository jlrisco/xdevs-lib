package xdevs.lib.tfgs.c1718.alex;

import xdevs.core.util.Constants;

public abstract class AtomicMongo extends ComponentMongo {

	// DevsAtomic attributes
    protected String phase = Constants.PHASE_PASSIVE;
    protected double sigma = Constants.INFINITY;
    
	public AtomicMongo(String name) {
		super(name);
	}
	
	public AtomicMongo() {
        this(AtomicMongo.class.getSimpleName());
    }
	
	// DevsAtomic methods
    public double ta() {
        return sigma;
    }

    public abstract void deltint();

    public abstract void deltext(double e);

    public void deltcon(double e) {
        deltint();
        deltext(0);
    }

    public abstract void lambda();

    public void holdIn(String phase, double sigma) {
        this.phase = phase;
        this.sigma = sigma;
    }

    public void activate() {
        this.phase = Constants.PHASE_ACTIVE;
        this.sigma = 0;
    }

    public void passivate() {
        this.phase = Constants.PHASE_PASSIVE;
        this.sigma = Constants.INFINITY;
    }

    public void passivateIn(String phase) {
        this.phase = phase;
        this.sigma = Constants.INFINITY;
    }

    public boolean phaseIs(String phase) {
        return this.phase.equals(phase);
    }

    public String getPhase() {
        return phase;
    }

    public final void setPhase(String phase) {
        this.phase = phase;
    }

    public double getSigma() {
        return sigma;
    }

    public final void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public String showState() {
        StringBuilder sb = new StringBuilder(name + ":[");
        sb.append("\tstate: ").append(phase);
        sb.append("\t, sigma: ").append(sigma);
        sb.append("]");
        return sb.toString();
    }
}
