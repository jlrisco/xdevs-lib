package devsml;
/*
 * Copyright (C) 2014-2016 Jos� Luis Risco Mart�n <jlrisco@ucm.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - Jos� Luis Risco Mart�n
 */

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author Jos� Luis Risco Mart�n
 */
public class MyGenerator extends Atomic {
    // Comentario

    protected Port<MyJob> iStart = new Port<MyJob>("iStart");
    protected Port<MyJob> iStop = new Port<MyJob>("iStop");
    protected Port<MyJob> oOut = new Port<MyJob>("oOut");
    protected int jobCounter;
    protected double period;

    public MyGenerator(String name, double period) {
        super(name);
        super.addInPort(iStop);
        super.addInPort(iStart);
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