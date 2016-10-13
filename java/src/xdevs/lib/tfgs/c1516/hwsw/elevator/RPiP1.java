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
import xdevs.lib.general.sources.Constant;
import xdevs.lib.general.sources.StimulusFile;
import xdevs.lib.logic.combinational.ics.IC7404;
import xdevs.lib.logic.combinational.ics.IC7410;
import xdevs.lib.logic.sequential.Clock;
import xdevs.lib.logic.sequential.ics.IC74169;

/**
 *
 * @author Miguel Higuera Romero
 */
public class RPiP1 extends Coupled{
    public RPiP1(String name, String stimulusFilePath) {
        // First we add the Stimulus generator
        StimulusFile stimulus = new StimulusFile("TEST", stimulusFilePath);
        super.addComponent(stimulus);
        // Now we add the clock:
        Clock clock = new Clock("CLOCK", 1, 0);
        super.addComponent(clock);
        // Now VCC
        Constant<Integer> vcc = new Constant<>("VCC", 1);
        super.addComponent(vcc);
        // Now GND
        Constant<Integer> gnd = new Constant<>("GND", 0);
        super.addComponent(gnd);
        // Now the counter
        IC74169 chipA = new IC74169("A");
        super.addComponent(chipA);
        // Adder
        MDCosim1 chipBe = new MDCosim1("B");
        super.addComponent(chipBe);
        // 3-input NANDs
        IC7410 chipC = new IC7410("C");
        super.addComponent(chipC);
        // NOT gates
        IC7404 chipD = new IC7404("D");
        super.addComponent(chipD);
        // Consoles:
        Console cQ2 = new Console("Q2");
        super.addComponent(cQ2);
        Console cQ1 = new Console("Q1");
        super.addComponent(cQ1);
        Console cQ0 = new Console("Q0");
        super.addComponent(cQ0);

        // Now we connect everything
        // Stimulus:
        super.addCoupling(stimulus.getPortByName("sw7"), chipA.iPin9);
        super.addCoupling(stimulus.getPortByName("sw2"), chipD.iPin13);
        super.addCoupling(stimulus.getPortByName("sw1"), chipD.iPin11);
        super.addCoupling(stimulus.getPortByName("sw0"), chipD.iPin9);
        super.addCoupling(stimulus.getPortByName("stop"), clock.iStop);
        // Clock:
        super.addCoupling(clock.oClk, chipA.iPin2);
        // VCC:
        super.addCoupling(vcc.oOut, chipA.iPin16);
        super.addCoupling(vcc.oOut, chipC.iPin13);
        super.addCoupling(vcc.oOut, chipC.iPin14);
        super.addCoupling(vcc.oOut, chipD.iPin14);
        // GND:
        super.addCoupling(gnd.oOut, chipA.iPin3);
        super.addCoupling(gnd.oOut, chipA.iPin4);
        super.addCoupling(gnd.oOut, chipA.iPin5);
        super.addCoupling(gnd.oOut, chipA.iPin6);
        super.addCoupling(gnd.oOut, chipA.iPin8);
        super.addCoupling(gnd.oOut, chipC.iPin7);
        super.addCoupling(gnd.oOut, chipD.iPin7);
        // ChipA
        super.addCoupling(chipA.oPin12, cQ2.iIn);
        super.addCoupling(chipA.oPin12, chipBe.iPin14);
        super.addCoupling(chipA.oPin13, cQ1.iIn);
        super.addCoupling(chipA.oPin13, chipBe.iPin3);
        super.addCoupling(chipA.oPin14, cQ0.iIn);
        super.addCoupling(chipA.oPin14, chipBe.iPin5);
        // ChipB extern
        super.addCoupling(chipBe.oPin1, chipD.iPin3);
        super.addCoupling(chipBe.oPin4, chipD.iPin5);
        super.addCoupling(chipBe.oPin10, chipA.iPin1);
        super.addCoupling(chipBe.oPin13, chipD.iPin1);
        // ChipC
        super.addCoupling(chipC.oPin6, chipC.iPin1);
        super.addCoupling(chipC.oPin6, chipC.iPin2);
        super.addCoupling(chipC.oPin12, chipA.iPin7);
        super.addCoupling(chipC.oPin12, chipA.iPin10);
        // ChipD
        super.addCoupling(chipD.oPin2, chipC.iPin3);
        super.addCoupling(chipD.oPin4, chipC.iPin4);
        super.addCoupling(chipD.oPin6, chipC.iPin5);
        super.addCoupling(chipD.oPin8, chipBe.iPin6);
        super.addCoupling(chipD.oPin10, chipBe.iPin2);
        super.addCoupling(chipD.oPin12, chipBe.iPin15);
    }

    public static void main(String[] args) {
        DevsLogger.setup(Level.INFO);
        RPiP1 rasp = new RPiP1("Raspberry", "." + File.separator + "lib" + File.separator + "test1.txt");
        RTCentralCoordinator coordinator = new RTCentralCoordinator(rasp);
        //Coordinator coordinator = new Coordinator(rasp);
        coordinator.initialize();
        coordinator.simulate(15.0);
        coordinator.exit();
    }
}
