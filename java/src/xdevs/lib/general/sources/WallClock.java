/*
 * Copyright (C) 2014-2016 José Luis Risco Martín <jlrisco@ucm.es>
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
 *  - José Luis Risco Martín
 */
package xdevs.lib.general.sources;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class WallClock extends Atomic {

    public Port<Object> iStop = new Port<>("iStop");
    public Port<Double> oClk = new Port<>("oClk");

    protected double period;
    protected double currentTime = 0.0;   // Output of the function

    public WallClock(String name, double period, double initialTime) {
        super(name);
        super.addInPort(iStop);
        super.addOutPort(oClk);
        this.period = period;
        this.currentTime = initialTime;
    }

    public WallClock(String name, double period) {
        this(name, period, 0.0);
    }

    public WallClock(String name) {
        this(name, 1.0);
    }

    public WallClock() {
        this(WallClock.class.getName());
    }

    @Override
    public void initialize() {
        super.activate();
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltext(double e) {
        currentTime += e;
        if (!iStop.isEmpty()) {
            super.passivate();
        }
    }

    @Override
    public void deltint() {
        currentTime += super.getSigma();
        super.holdIn("active", period);
    }

    @Override
    public void lambda() {
        oClk.addValue(currentTime);
    }

}
