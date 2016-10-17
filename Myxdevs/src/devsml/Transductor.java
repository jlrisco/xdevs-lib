package devsml;

import java.util.LinkedList;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author José Luis Risco Martín TODO: I keep the Transducer atomic model for
 * the end ...
 */
public class Transductor extends Atomic {

    private static final Logger LOGGER = Logger.getLogger(Transductor.class.getName());

    protected Port<MyJob> iArrived = new Port<MyJob>("iArrived");
    protected Port<MyJob> iSolved = new Port<MyJob>("iSolved");
    protected Port<MyJob> oOut = new Port<MyJob>("oOut");

    protected LinkedList<MyJob> jobsArrived = new LinkedList<MyJob>();
    protected LinkedList<MyJob> jobsSolved = new LinkedList<MyJob>();
    protected double observationTime;
    protected double totalTa;
    protected double clock;

    public Transductor(String name, double observationTime) {
        super(name);
        super.addInPort(iArrived);
        super.addInPort(iSolved);
        super.addOutPort(oOut);
        totalTa = 0;
        clock = 0;
        this.observationTime = observationTime;
    }

    @Override
    public void initialize() {
        super.holdIn("active", observationTime);
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        clock = clock + getSigma();
        double throughput;
        double avgTaTime;
        if (phaseIs("active")) {
            if (!jobsSolved.isEmpty()) {
                avgTaTime = totalTa / jobsSolved.size();
                if (clock > 0.0) {
                    throughput = jobsSolved.size() / clock;
                } else {
                    throughput = 0.0;
                }
            } else {
                avgTaTime = 0.0;
                throughput = 0.0;
            }
            LOGGER.info("End time: " + clock);
            LOGGER.info("Jobs arrived : " + jobsArrived.size());
            LOGGER.info("Jobs solved : " + jobsSolved.size());
            LOGGER.info("Average TA = " + avgTaTime);
            LOGGER.info("Throughput = " + throughput);
            holdIn("done", 0);
        } else {
            passivate();
        }
        //logger.info("####deltint: "+showState());
    }

    @Override
    public void deltext(double e) {
        clock = clock + e;
        if (phaseIs("active")) {
            MyJob job = null;
            if (!iArrived.isEmpty()) {
                job = iArrived.getSingleValue();
                LOGGER.fine("Start job " + job.id + " @ t = " + clock);
                job.time = clock;
                jobsArrived.add(job);
            }
            if (!iSolved.isEmpty()) {
                job = iSolved.getSingleValue();
                totalTa += (clock - job.time);
                LOGGER.fine("Finish job " + job.id + " @ t = " + clock);
                job.time = clock;
                jobsSolved.add(job);
            }
        }
        //logger.info("###Deltext: "+showState());
    }

    @Override
    public void lambda() {
        if (phaseIs("done")) {
            MyJob job = new MyJob("null");
            oOut.addValue(job);
        }
    }
}

