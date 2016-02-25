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
package xdevs.lib.general.sinks;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.InPort;
import xdevs.core.simulation.realtime.RTCentralCoordinator;
import xdevs.lib.logic.sequential.Clock;
import xdevs.lib.util.ScopeJFreeChart;

/**
 *
 * @author José Luis Risco Martín
 */
public class Scope extends Atomic {

    public InPort<Number> iIn = new InPort<>("iIn");
    protected double time;
    protected ScopeJFreeChart chart;

    public Scope(String yTitle) {
        super("Scope");
        super.addInPort(iIn);
        chart = new ScopeJFreeChart("Scope", "Scope", "time", yTitle);
        this.time = 0.0;
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
        time += super.getSigma();
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        time += e;
        if (!iIn.isEmpty()) {
            chart.addPoint(time, iIn.getSingleValue().doubleValue());
        }
    }

    @Override
    public void lambda() {
    }
    
    public static void main(String[] args) {
        Coupled coupled = new Coupled("Test");
        Clock clock = new Clock("CLK");
        coupled.addComponent(clock);
        Scope scope = new Scope("CLOCK");
        coupled.addComponent(scope);
        coupled.addCoupling(clock.oClk, scope.iIn);
        RTCentralCoordinator coordinator = new RTCentralCoordinator(coupled);
        coordinator.initialize();
        coordinator.simulate(20.0);
        coordinator.exit();
    }
}
