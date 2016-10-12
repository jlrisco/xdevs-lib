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

import java.util.logging.Level;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;

/**
 *
 * @author jlrisco
 */
public class Mygpt extends Coupled {

    public Mygpt(String name, double period, double observationTime) {
    	super(name);
        Mygen generator = new Mygen("generator", period);
        super.addComponent(generator);
        Myprocessor processor = new Myprocessor("processor", 3*period);
        super.addComponent(processor);
        Mytransductor transducer = new Mytransductor("transducer", observationTime);
        super.addComponent(transducer);

        super.addCoupling(generator, generator.oOut, processor, processor.iIn);
        super.addCoupling(generator, generator.oOut, transducer, transducer.iArrived);
        super.addCoupling(processor, processor.oOut, transducer, transducer.iSolved);
        super.addCoupling(transducer, transducer.oOut, generator, generator.iStop);
    }

    public static void main(String args[]) {
        DevsLogger.setup(Level.INFO);
        Mygpt gpt = new Mygpt("gpt", 1, 100);
        //CoordinatorParallel coordinator = new CoordinatorParallel(gpt);
        Coordinator coordinator = new Coordinator(gpt);
        //RTCentralCoordinator coordinator = new RTCentralCoordinator(gpt);
        coordinator.initialize();
        coordinator.simulate(Long.MAX_VALUE);
    }

}
