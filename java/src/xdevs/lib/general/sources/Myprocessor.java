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

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;
import xdevs.core.modeling.Port;

/**
 *
 * @author jlrisco
 */
public class Myprocessor extends Atomic {

    protected Port<Myjob> iIn = new Port<Myjob>("iIn");
    protected Port<Myjob> oOut = new Port<Myjob>("oOut");
    protected Myjob currentJob = null;
    protected double processingTime;

    public Myprocessor(String name, double processingTime) {
        super(name);
        super.addInPort((InPort<?>) iIn);
        super.addOutPort((OutPort<?>) oOut);
        this.processingTime = processingTime;
    }

    @Override
    public void initialize() {
        super.passivate();
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        if (super.phaseIs("passive")) {
            Myjob job = iIn.getSingleValue();
            currentJob = job;
            super.holdIn("active", processingTime);
        }
    }

    @Override
    public void lambda() {
        oOut.addValue(currentJob);
    }
}
