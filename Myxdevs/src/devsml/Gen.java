package devsml;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author José Luis Risco Martín TODO: I must also modify this class, according
 * to the source code implemented by Saurabh, a iStart input port must be added.
 */
public class Gen extends Atomic {

    protected Port<MyJob> iStart = new Port<MyJob>("iStart");
    protected Port<MyJob> iStop = new Port<MyJob>("iStop");
    protected Port<MyJob> oOut = new Port<MyJob>("oOut");
    protected int jobCounter;
    protected double period;
    

    public Gen(String name, double period) {
        super(name);
        super.addInPort(iStop);
        super.addInPort( iStart);
        super.addOutPort(oOut);
        this.period = period;
    }

    @Override
    public void initialize() {
        jobCounter = 1;
        this.holdIn("active", period);
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        jobCounter++;
        this.holdIn("active", period);
    }

    @Override
    public void deltext(double e) {
        super.passivate();
    }

    @Override
    public void lambda() {
        MyJob job = new MyJob("" + jobCounter + "");
        oOut.addValue(job);
    }
}
