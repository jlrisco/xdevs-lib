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
import xdevs.core.simulation.Coordinator;
import xdevs.lib.logic.sequential.Clock;
import xdevs.lib.util.ScopeMultiView;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class ScopeMulti extends Atomic {

    protected double time = 0.0;
    protected ScopeMultiView chart = null;
    
    public ScopeMulti(String yTitle, int numInPorts) {
        super(yTitle);
        chart = new ScopeMultiView("Scope", "Scope", "time", yTitle);
        for(int i=0; i<numInPorts; ++i) {
            InPort<Number> inPort = new InPort<>("i" + i);
            super.addInPort(inPort);
            chart.addSerie(inPort.getName());
        }
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
        for(InPort<?> inPort : inPorts) {
            if(!inPort.isEmpty()) {
                chart.addPoint(inPort.getName(), time, (Number)inPort.getSingleValue());
            }
        }
    }

    @Override
    public void lambda() {
    }

    public static void main(String[] args) {
        Coupled coupled = new Coupled("Test");
        Clock clock1 = new Clock("CLK1", 1, 0);
        coupled.addComponent(clock1);
        Clock clock2 = new Clock("CLK2", 1.5, 0);
        coupled.addComponent(clock2);
        Clock clock3 = new Clock("CLK3", 1.75, 0);
        coupled.addComponent(clock3);
        
        ScopeMulti scope = new ScopeMulti("CLOCK", 3);
        coupled.addComponent(scope);
        
        coupled.addCoupling(clock1, 0, scope, 0);
        coupled.addCoupling(clock2, 0, scope, 1);
        coupled.addCoupling(clock3, 0, scope, 2);
        
        Coordinator coordinator = new Coordinator(coupled);
        coordinator.initialize();
        coordinator.simulate(20.0);
        coordinator.exit();
    }
}
