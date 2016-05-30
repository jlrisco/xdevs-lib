/*
 * Copyright (C) 2016 Miguel Higuera Romero
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
 */
package xdevs.lib.tfgs.c1516.hwsw.elevator;

import java.io.File;
import java.util.logging.Level;
import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.core.simulation.realtime.RTCentralCoordinator;
import xdevs.core.util.DevsLogger;
import xdevs.lib.general.sinks.Console;
import xdevs.lib.general.sources.StimulusFile;
import xdevs.lib.logic.sequential.Clock;

/**
 * 
 * @author Miguel Higuera Romero
 */

public class RPiP4 extends Coupled {
    public RPiP4(String name, String stimulusFilePath) {
        // First we add the Stimulus generator
        StimulusFile stimulus = new StimulusFile("TEST", stimulusFilePath);
        super.addComponent(stimulus);
        // Now we add the clock:
        Clock clock = new Clock("CLOCK", 1, 0);
        super.addComponent(clock);
        // Consoles:
        Console cQ2 = new Console("Q2");
        super.addComponent(cQ2);
        Console cQ1 = new Console("Q1");
        super.addComponent(cQ1);
        Console cQ0 = new Console("Q0");
        super.addComponent(cQ0);
        
        // Extern elevator
        MDCosim4S chipExtS = new MDCosim4S("elevatorS");
        super.addComponent(chipExtS);
        MDCosim4R chipExtR = new MDCosim4R("elevatorR");
        super.addComponent(chipExtR);
        
        // Now we connect everything
        // Stimulus:
        super.addCoupling(stimulus.getPortByName("sw7"), chipExtS.iIni);
        super.addCoupling(stimulus.getPortByName("sw2"), chipExtS.iX3);
        super.addCoupling(stimulus.getPortByName("sw1"), chipExtS.iX2);
        super.addCoupling(stimulus.getPortByName("sw0"), chipExtS.iX1);
        super.addCoupling(stimulus.getPortByName("stop"), clock.iStop);
        // Clock:
        super.addCoupling(clock.oClk, chipExtR.iClk);
        
        // ChipA
        super.addCoupling(chipExtR.oQ3, cQ2.iIn);
        super.addCoupling(chipExtR.oQ2, cQ1.iIn);
        super.addCoupling(chipExtR.oQ1, cQ0.iIn);
        
    }
    
    public static void main(String[] args) {
        DevsLogger.setup(Level.INFO);
        RPiP4 rasp = new RPiP4("Raspberry", "." + File.separator + "lib" + File.separator + "test1.txt");
        RTCentralCoordinator coordinator = new RTCentralCoordinator(rasp);
        //Coordinator coordinator = new Coordinator(rasp);
        coordinator.initialize();
        coordinator.simulate(15.0);
        coordinator.exit();
    }
    
}
