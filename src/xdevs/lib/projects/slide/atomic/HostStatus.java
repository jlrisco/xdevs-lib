/*
 * Copyright (C) 2015-2015 GreenDisc research group <http://greendisc.dacya.ucm.es/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
  */
package xdevs.lib.projects.slide.atomic;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class HostStatus extends Atomic {
    public InPort<Object> iHostMonitoringPolicy = new InPort<>("HostMonitoringPolicy");
    public OutPort<Object> oHostStatus = new OutPort<>("HostStatus");
    
    public enum HostPhase {ON, SUSPENDED, OFF, POWERING_ON, SUSPENDING, POWERING_OFF, FAILED}
    
  
    public HostStatus() {
        super();
        super.addInPort(iHostMonitoringPolicy);
    }

    @Override
    public void initialize() {
        super.passivateIn(HostPhase.OFF.toString());
    }

    @Override
    public void deltint() {
        super.passivateIn(HostPhase.OFF.toString());
    }

    @Override
    public void deltext(double e) {
        if(!iHostMonitoringPolicy.isEmpty()) {
            // TODO: Update the host model
            super.holdIn("SendHostStatus", 0.0);
        }
    }

    @Override
    public void lambda() {
        if(super.phaseIs("SendHostStatus")) {
            oHostStatus.addValue(new Double(1));
        }
    }
    
    @Override
    public void exit() {        
    }

}
