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

import java.util.logging.Level;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;

/**
 *
 * @author jlrisco
 */
public class Gpt extends Coupled {

    public Gpt(String name, double period, double observationTime) {
    	super(name);
        Generator generator = new Generator("generator", period);
        addComponent(generator);
        Processor processor = new Processor("processor", 3*period);
        addComponent(processor);
        Transducer transducer = new Transducer("transducer", observationTime);
        addComponent(transducer);

        addCoupling(generator, generator.oOut, processor, processor.iIn);
        addCoupling(generator, generator.oOut, transducer, transducer.iArrived);
        addCoupling(processor, processor.oOut, transducer, transducer.iSolved);
        addCoupling(transducer, transducer.oOut, generator, generator.iStop);
    }

    public static void main(String args[]) {
        DevsLogger.setup(Level.ALL);
        Gpt gpt = new Gpt("gpt", 1, 100);
        //CoordinatorParallel coordinator = new CoordinatorParallel(gpt);
        Coordinator coordinator = new Coordinator(gpt);
        coordinator.initialize();
        coordinator.simulate(Long.MAX_VALUE);
    }

}
