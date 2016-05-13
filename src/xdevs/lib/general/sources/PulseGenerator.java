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
 * Generate square wave pulses at regular intervals. The Pulse Generator block
 * generates square wave pulses at regular intervals. The block's waveform
 * parameters, Amplitude, Pulse Width, Period, and Phase delay, determine the
 * shape of the output waveform.
 *
 * @author José L. Risco Martín
 */
public class PulseGenerator extends Atomic {

    public Port<Object> iStop = new Port<>("iStop");
    public Port<Double> oVal = new Port<>("oVal");
    /**
     * The pulse amplitude. The default is 1.
     */
    protected double amplitude = 1;
    /**
     * The duration of the pulse period that the signal is on. Default is 0.5.
     */
    protected double pulseWidth = 0.5;
    /**
     * The pulse period specified in seconds. Default is 1.
     */
    protected double period;
    /**
     * The delay before the pulse is generated specified in seconds. Default is
     * 0.
     */
    protected double phaseDelay;

    /**
     *
     * @param name Name of the component.
     * @param amplitude The pulse amplitude.
     * @param pulseWidth The duration of the pulse period that the signal is on.
     * @param period The pulse period specified in seconds.
     * @param phaseDelay The delay before the pulse is generated specified in
     * seconds.
     */
    public PulseGenerator(String name, double amplitude, double pulseWidth, double period, double phaseDelay) {
        super(name);
        super.addInPort(iStop);
        super.addOutPort(oVal);
        this.amplitude = amplitude;
        this.pulseWidth = pulseWidth;
        this.period = period;
        this.phaseDelay = phaseDelay;
    }

    public PulseGenerator(String name) {
        this(name, 1, 0.5, 1, 0);
    }

    public PulseGenerator() {
        this(PulseGenerator.class.getName());
    }

    @Override
    public void initialize() {
        if (phaseDelay > 0) {
            super.holdIn("delay", 0);
        }
        else {
            super.holdIn("high", 0);
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        if (super.phaseIs("delay")) {
            super.holdIn("high", phaseDelay);
        } else if (super.phaseIs("high")) {
            super.holdIn("low", pulseWidth);
        } else if (super.phaseIs("low")) {
            super.holdIn("high", period - pulseWidth);
        }
    }

    @Override
    public void deltext(double e) {
        if (!iStop.isEmpty()) {
            super.passivate();
        }
    }

    @Override
    public void lambda() {
        if (super.phaseIs("delay")) {
            oVal.addValue(0.0);
        } else if (super.phaseIs("high")) {
            oVal.addValue(amplitude);
        } else if (super.phaseIs("low")) {
            oVal.addValue(0.0);
        }
    }

    public static void main(String[] args) {
        Coupled pulseExample = new Coupled("pulseExample");
        PulseGenerator pulse = new PulseGenerator("pulse", 10, 3, 5, 5);
        pulseExample.addComponent(pulse);
        Console console = new Console("console");
        pulseExample.addComponent(console);
        pulseExample.addCoupling(pulse, pulse.oVal, console, console.iIn);
        Coordinator coordinator = new Coordinator(pulseExample);
        coordinator.initialize();
        coordinator.simulate(30.0);
        coordinator.exit();
    }
}
