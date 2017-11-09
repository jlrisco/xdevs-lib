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

import xdevs.lib.general.sinks.Console;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;

/**
 *
 * @author jlrisco
 */
public class Ramp extends Atomic {

    public Port<Double> portOut = new Port<>("portOut");
    protected double startTime;
    protected double slope;
    protected double sampleTime;
    protected double nextOutput;

    public Ramp(String name, double initialOutput, double startTime, double slope, double sampleTime) {
    	super(name);
        super.addOutPort(portOut);
        this.nextOutput = initialOutput;
        this.startTime = startTime;
        this.slope = slope;
        this.sampleTime = sampleTime;
    }
    
    @Override
    public void initialize() {
        super.holdIn("initialOutput", 0.0);    	
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        if (super.phaseIs("initialOutput")) {
            super.holdIn("startTime", startTime);
        } else {
            nextOutput += slope * sampleTime;
            super.holdIn("active", sampleTime);
        }
    }

    @Override
    public void deltext(double e) {
    }

    @Override
    public void lambda() {
        portOut.addValue(nextOutput);
    }

    public static void main(String[] args) {
        Coupled example = new Coupled("example");
        Ramp ramp = new Ramp("Ramp",2, 10, 2, 0.1);
        example.addComponent(ramp);
        Console console = new Console("console");
        example.addComponent(console);
        example.addCoupling(ramp.portOut, console.iIn);
        Coordinator coordinator = new Coordinator(example);
        coordinator.initialize();
        coordinator.simulate(30.0);
        coordinator.exit();
    }
}
