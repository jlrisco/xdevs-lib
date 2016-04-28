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
package xdevs.lib.projects.slide.coupled;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.OutPort;
import xdevs.lib.projects.slide.atomic.HostStatus;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class Host extends Coupled {
    public OutPort<Object> oHostStatus = new OutPort<>("HostStatus");
    
    public Host() {
        super();
        super.addOutPort(oHostStatus);
    }
    
    public void addComponents(HostManager hostManager, HostStatus hostStatus) {
        super.addComponent(hostManager);
        super.addComponent(hostStatus);
        super.addCoupling(hostManager.oHostMonitoringPolicy, hostStatus.iHostMonitoringPolicy);
        super.addCoupling(hostStatus.oHostStatus, oHostStatus);
    }
}
