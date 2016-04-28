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
package xdevs.lib.projects.slide.coupled;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.OutPort;
import xdevs.lib.projects.slide.atomic.HostMonitoringPolicy;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class HostManager extends Coupled {
    public OutPort<Object> oHostMonitoringPolicy = new OutPort<>("HostMonitoringPolicy");
    
    public HostManager() {
        super();
        super.addOutPort(oHostMonitoringPolicy);
    }
    
    public void addHostMonitoringPolicy(double period, double delay) {
        HostMonitoringPolicy hostMonitoringPolicy = new HostMonitoringPolicy(period, delay);
        super.addComponent(hostMonitoringPolicy);
        super.addCoupling(hostMonitoringPolicy.oOut, oHostMonitoringPolicy);
    }
}
