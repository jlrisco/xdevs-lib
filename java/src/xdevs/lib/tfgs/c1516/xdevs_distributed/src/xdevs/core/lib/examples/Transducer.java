/*
 * Copyright (C) 2014-2015 José Luis Risco Martín <jlrisco@ucm.es> and 
 * Saurabh Mittal <smittal@duniptech.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * http://www.gnu.org/licenses/
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package xdevs.core.lib.examples;

import java.util.LinkedList;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;

/**
 *
 * @author José Luis Risco Martín TODO: I keep the Transducer atomic model for
 * the end ...
 */
public class Transducer extends Atomic {

    private static final Logger logger = Logger.getLogger(Transducer.class.getName());

    protected InPort<Job> iArrived = new InPort<>("iArrived");
    protected InPort<Job> iSolved = new InPort<>("iSolved");
    protected OutPort<Job> oOut = new OutPort<>("oOut");
    protected OutPort<Result> oResult = new OutPort<>("oResult");

    protected LinkedList<Job> jobsArrived = new LinkedList<>();
    protected LinkedList<Job> jobsSolved = new LinkedList<>();
    protected double observationTime;
    protected double totalTa;
    protected double clock;
    protected Result result;

    public Transducer(String name, double observationTime) {
        super(name);
        super.addInPort(iArrived);
        super.addInPort(iSolved);
        super.addOutPort(oOut);
        super.addOutPort(oResult);
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
            logger.info("End time: " + clock);
            logger.info("Jobs arrived : " + jobsArrived.size());
            logger.info("Jobs solved : " + jobsSolved.size());
            logger.info("Average TA = " + avgTaTime);
            logger.info("Throughput = " + throughput);
            result = new Result(throughput, avgTaTime, jobsArrived.size(), jobsSolved.size());
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
            Job job = null;
            if (!iArrived.isEmpty()) {
                job = iArrived.getSingleValue();
                logger.fine("Start job " + job.id + " @ t = " + clock);
                job.time = clock;
                jobsArrived.add(job);
            }
            if (!iSolved.isEmpty()) {
                job = iSolved.getSingleValue();
                totalTa += (clock - job.time);
                logger.fine("Finish job " + job.id + " @ t = " + clock);
                job.time = clock;
                jobsSolved.add(job);
            }
        }
        //logger.info("###Deltext: "+showState());
    }

    @Override
    public void lambda() {
        if (phaseIs("done")) {
            Job job = new Job("null");
            oOut.addValue(job);
            oResult.addValue(result);
            logger.info("" + result);
        }
    }
}
