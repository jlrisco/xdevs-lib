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
 *  - Marina Zapater Sancho
 */
package xdevs.lib.projects.slide.coupled.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;
import xdevs.lib.projects.slide.atomic.HostStatus;
import xdevs.lib.projects.slide.atomic.Workload;
import xdevs.lib.projects.slide.coupled.Host;
import xdevs.lib.projects.slide.coupled.HostManager;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class SimpleDataCenter extends Coupled {

    public SimpleDataCenter() throws IOException {
        super(SimpleDataCenter.class.getName());
        // First we add the workload
        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("test" + File.separator + "devs-slide" + File.separator + "clarknet");
        // Workload
        Workload workload = new Workload(filePaths);
        super.addComponent(workload);
        // Host
        Host host = new Host();
        HostManager hostManager = new HostManager();
        hostManager.addHostMonitoringPolicy(5*60, 0);
        HostStatus hostStatus = new HostStatus();
        host.addComponents(hostManager, hostStatus);
        super.addComponent(host);
    }

    public static void main(String args[]) {
        try {
            DevsLogger.setup(Level.INFO);
            SimpleDataCenter dataCenter = new SimpleDataCenter();
            Coordinator devsSimulator = new Coordinator(dataCenter);
            devsSimulator.initialize();
            devsSimulator.simulate(30.0*60);
        } catch (IOException ex) {
            Logger.getLogger(SimpleDataCenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
