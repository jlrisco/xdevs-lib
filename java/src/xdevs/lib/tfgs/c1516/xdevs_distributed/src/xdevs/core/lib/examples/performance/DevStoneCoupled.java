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
package xdevs.core.lib.examples.performance;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;
import xdevs.core.simulation.Coordinator;
import xdevs.core.simulation.distributed.ClientHandler;
import xdevs.core.simulation.distributed.CoordinatorDistributed;
import xdevs.core.simulation.distributed.Server;
import xdevs.core.simulation.parallel.CoordinatorParallel;
import xdevs.core.util.DevsLogger;

/**
 * Coupled model to study the performance using DEVStone
 *
 * @author José Luis Risco Martín
 */
public abstract class DevStoneCoupled extends Coupled {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8642069539153929810L;

	private static final Logger logger = Logger.getLogger(DevStoneCoupled.class.getName());

    public InPort<Integer> iIn = new InPort<>("in");
    public OutPort<Integer> oOut = new OutPort<>("out");

    public DevStoneCoupled(String name) {
        super(name);
        addInPort(iIn);
        addOutPort(oOut);
    }

    public abstract int getNumDeltExts(int maxEvents, int width, int depth);

    public abstract int getNumDeltInts(int maxEvents, int width, int depth);

    public abstract long getNumOfEvents(int maxEvents, int width, int depth);

    public static void main(String[] args) {
        DevStoneProperties properties = new DevStoneProperties();
        /*Declaration of works*/
	    ArrayList<Integer> lam1 = new ArrayList<Integer>();	//lista de lambdas c1
	    ArrayList<Integer> del1 = new ArrayList<Integer>();	//lista de deltas c1
	    ArrayList<Integer> lam2 = new ArrayList<Integer>(); //lista de lambdas c2
	    ArrayList<Integer> del2 = new ArrayList<Integer>(); //lista de deltas c2*/
	    /*Here you have to choose what work goes to what worker*/
	    /*And add them to the works*/
	    lam1.add(0);del1.add(0);
	    lam2.add(1);del2.add(1);
	    
        if (args.length == 1) {
            properties = new DevStoneProperties(args[0]);
        }
        else if(args.length>1) {
            properties.loadFromCommandLine(args);
        }
        DevsLogger.setup(properties.getProperty(DevStoneProperties.LOGGER_PATH), Level.INFO);
        int numTrials = properties.getPropertyAsInteger(DevStoneProperties.NUM_TRIALS);
        int[] widthsAsArray = properties.getPropertyAsArrayOfInteger(DevStoneProperties.WIDTH);
        int[] depthsAsArray = properties.getPropertyAsArrayOfInteger(DevStoneProperties.DEPTH);
        int[] maxEventsAsArray = properties.getPropertyAsArrayOfInteger(DevStoneProperties.GENERATOR_MAX_EVENTS);
        for (int depth = depthsAsArray[0]; depth < depthsAsArray[2]; depth += depthsAsArray[1]) {
            for (int width = widthsAsArray[0]; width < widthsAsArray[2]; width += widthsAsArray[1]) {
                for (int maxEvents = maxEventsAsArray[0]; maxEvents < maxEventsAsArray[2]; maxEvents += maxEventsAsArray[1]) {
                    for (int currentTrial = 0; currentTrial < numTrials; ++currentTrial) {
                        DevStoneAtomic.NUM_DELT_INTS = 0;
                        DevStoneAtomic.NUM_DELT_EXTS = 0;
                        DevStoneAtomic.NUM_OF_EVENTS = 0;

                        Coupled framework = new Coupled("DevStone" + properties.getProperty(DevStoneProperties.BENCHMARK_NAME));

                        DevStoneGenerator generator = new DevStoneGenerator("Generator", properties, maxEvents);
                        framework.addComponent(generator);

                        DevStoneCoupled stoneCoupled = null;
                        String benchmarkName = properties.getProperty(DevStoneProperties.BENCHMARK_NAME);
                        if (benchmarkName.equals(DevStoneProperties.BenchMarkType.LI.toString())) {
                            stoneCoupled = new DevStoneCoupledLI("C", width, depth, properties);
                        } else if (benchmarkName.equals(DevStoneProperties.BenchMarkType.HI.toString())) {
                            stoneCoupled = new DevStoneCoupledHI("C", width, depth, properties);
                        } else if (benchmarkName.equals(DevStoneProperties.BenchMarkType.HO.toString())) {
                            stoneCoupled = new DevStoneCoupledHO("C", width, depth, properties);
                        } else if (benchmarkName.equals(DevStoneProperties.BenchMarkType.HOmem.toString())) {
                            stoneCoupled = new DevStoneCoupledHOmem("C", width, depth, properties);
                        } else if (benchmarkName.equals(DevStoneProperties.BenchMarkType.HOmod.toString())) {
                            stoneCoupled = new DevStoneCoupledHOmod("C", width, depth, properties);
                        }
                        
                        // Theoretical values
                        int numDeltInts = stoneCoupled.getNumDeltInts(maxEvents, width, depth);
                        int numDeltExts = stoneCoupled.getNumDeltExts(maxEvents, width, depth);
                        long numOfEvents = stoneCoupled.getNumOfEvents(maxEvents, width, depth);
                        if(properties.getPropertyAsInteger(DevStoneProperties.MAX_NUMBER_OF_EVENTS)>0 && (numOfEvents/maxEvents)>properties.getPropertyAsInteger(DevStoneProperties.MAX_NUMBER_OF_EVENTS)) {
                            String stats = (currentTrial + 1) + ";" + maxEvents + ";" + width + ";" + depth + ";" + numDeltInts + ";" + numDeltExts + ";" + numOfEvents + ";-1.0";
                            logger.info(stats);
                            continue;
                        }

                        framework.addComponent(stoneCoupled);
                        framework.addCoupling(generator.oOut, stoneCoupled.iIn);
                        if (benchmarkName.equals(DevStoneProperties.BenchMarkType.HO.toString())) {
                            framework.addCoupling(generator.oOut, ((DevStoneCoupledHO) stoneCoupled).iInAux);
                        } else if (benchmarkName.equals(DevStoneProperties.BenchMarkType.HOmem.toString())) {
                            framework.addCoupling(generator.oOut, ((DevStoneCoupledHOmem) stoneCoupled).iInAux);
                        } else if (benchmarkName.equals(DevStoneProperties.BenchMarkType.HOmod.toString())) {
                            framework.addCoupling(generator.oOut, ((DevStoneCoupledHOmod) stoneCoupled).iInAux);
                        }
                        CoordinatorParallel coordinator = new CoordinatorParallel(framework);
                        //CoordinatorDistributed coordinator = new CoordinatorDistributed(framework, true);
                        //Coordinator coordinator = new Coordinator(framework,properties.getPropertyAsBoolean(DevStoneProperties.FLATTEN));
                        ArrayList<ClientHandler> clientList;	//List of workers
            		    
                        /*coordinator.initialize();
                        
                        Server server = new Server(5000,    2	  ,coordinator,true);

            		    server.connect();*/	//This waits until the two clients get connected
            		    
            		    
            		    ////////////////Here we send to the client the works ////////////////////
            		    //If the worker have on lam1 {0,4}  will execute the lambda of the simulator on the position 0 and 4
            		    /*server.sendLamWork(0, lam1);
            		    server.sendDelWork(0, del1);
            		    server.sendLamWork(1, lam2);
            		    server.sendDelWork(1, del2);*/
                        
            		    
                        long start = System.currentTimeMillis();
                        coordinator.simulate(Long.MAX_VALUE);
                        //server.simulate(Long.MAX_VALUE);
                        long end = System.currentTimeMillis();
                        double time = (end - start) / 1000.0;
                        String stats;
                        //if(DevStoneAtomic.NUM_DELT_INTS==numDeltInts && DevStoneAtomic.NUM_DELT_EXTS==numDeltExts && DevStoneAtomic.NUM_OF_EVENTS==numOfEvents) {
                            stats = (currentTrial + 1) + ";" + maxEvents + ";" + width + ";" + depth + ";" /*+ DevStoneAtomic.NUM_DELT_INTS + ";" + DevStoneAtomic.NUM_DELT_EXTS + ";" + DevStoneAtomic.NUM_OF_EVENTS + ";"*/ + time;
                        //}
                        //else {
                        //   stats = "ERROR: NumDeltInts or NumDeltExts or NumOfEvents do not match the theoretical values (between brackets): " + DevStoneAtomic.NUM_DELT_INTS + ";[" + numDeltInts + "];" + DevStoneAtomic.NUM_DELT_EXTS + ";[" + numDeltExts + "];" + DevStoneAtomic.NUM_OF_EVENTS + ";[" + numOfEvents + "];" + time;
                        //}
                        logger.info(stats);
                    }
                }
            }
        }
    }
}