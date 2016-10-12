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
package xdevs;

import java.util.logging.Level;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;

/**
 *
 * @author jlrisco
 */
public class Myefp extends Coupled {

    protected Port<Myjob> iStart = new Port<Myjob>("iStart");

    public Myefp(String name, double generatorPeriod, double processorPeriod, double transducerPeriod) {
        super(name);
        super.addInPort((InPort<?>) iStart);

        Myef ef = new Myef("ef", generatorPeriod, transducerPeriod);
        super.addComponent(ef);
        Myprocessor processor = new Myprocessor("processor", processorPeriod);
        super.addComponent(processor);

        super.addCoupling(ef.oOut, processor.iIn);
        super.addCoupling(processor.oOut, ef.iIn);
        super.addCoupling(this.iStart, ef.iStart);
    }

    public static void main(String args[]) {
        DevsLogger.setup(Level.INFO);
        Myefp efp = new Myefp("efp", 1, 3, 100);
        Coordinator coordinator = new Coordinator(efp);
        coordinator.initialize();
        coordinator.simulate(Long.MAX_VALUE);
        coordinator.exit();
    }

}
