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
package xdevs.lib.projects.slide.atomic;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.OutPort;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class HostMonitoringPolicy extends Atomic {
    public OutPort<Object> oOut = new OutPort<>("out");
    
    protected double period;
    protected double delay;
    
    public HostMonitoringPolicy(double period, double delay) {
        super();
        super.addOutPort(oOut);
        this.period = period;
        this.delay = delay;
    }
    
    public HostMonitoringPolicy(double period) {
        this(period, 0.0);
    }
    
    @Override
    public void initialize() {
        super.holdIn("active", delay);
    }
    
    @Override
    public void exit() {        
    }
    
    @Override
    public void deltint() {
        super.holdIn("active", period);
    }
    
    @Override
    public void deltext(double e) {        
    }
    
    @Override
    public void lambda() {
        oOut.addValue(new Double(1));
    }
}
