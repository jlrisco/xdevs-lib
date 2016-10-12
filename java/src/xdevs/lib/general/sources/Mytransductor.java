/*
 * Copyright (C) 2014-2015 Jos� Luis Risco Mart�n <jlrisco@ucm.es> and 
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
 *  - Jos� Luis Risco Mart�n
 */
package xdevs;

import java.util.LinkedList;
import java.util.logging.Logger;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;
import xdevs.core.modeling.Port;

/**
 *
 * @author Jos� Luis Risco Mart�n TODO: I keep the Transducer atomic model for
 * the end ...
 */
public class Mytransductor extends Atomic {

    private static final Logger LOGGER = Logger.getLogger(Mytransductor.class.getName());

    protected Port<Myjob> iArrived = new Port<Myjob>("iArrived");
    protected Port<Myjob> iSolved = new Port<Myjob>("iSolved");
    protected Port<Myjob> oOut = new Port<Myjob>("oOut");

    protected LinkedList<Myjob> jobsArrived = new LinkedList<Myjob>();
    protected LinkedList<Myjob> jobsSolved = new LinkedList<Myjob>();
    protected double observationTime;
    protected double totalTa;
    protected double clock;

    public Mytransductor(String name, double observationTime) {
        super(name);
        super.addInPort((InPort<?>) iArrived);
        super.addInPort((InPort<?>) iSolved);
        super.addOutPort((OutPort<?>) oOut);
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
            Myjob job = null;
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
            Myjob job = new Myjob("null");
            oOut.addValue(job);
        }
    }
}