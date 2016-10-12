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
///package xdevs.core.test.efp;
package xdevs;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;
import xdevs.core.modeling.Port;

/**
 *
 * @author José Luis Risco Martín TODO: I must also modify this class, according
 * to the source code implemented by Saurabh, a iStart input port must be added.
 */
public class Mygen extends Atomic {

    protected Port<Myjob> iStart = new Port<Myjob>("iStart");
    protected Port<Myjob> iStop = new Port<Myjob>("iStop");
    protected Port<Myjob> oOut = new Port<Myjob>("oOut");
    protected int jobCounter;
    protected double period;
    

    public Mygen(String name, double period) {
        super(name);
        super.addInPort((InPort<?>) iStop);
        super.addInPort((InPort<?>) iStart);
        super.addOutPort((OutPort<?>) oOut);
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
        Myjob job = new Myjob("" + jobCounter + "");
        oOut.addValue(job);
    }
}
