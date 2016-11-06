/*
 * Copyright (C) 2014-2015 José Luis Risco Martín <jlrisco@ucm.es> and 
 * Saurabh Mittal <smittal@duniptech.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * http://www.gnu.org/licenses/
 *
 * Contributors:
 *  - José Luis Risco Martín <jlrisco@ucm.es>
 *  - Saurabh Mittal <smittal@duniptech.com>
 */
/**
 *
 * @author Guillermo Llorente de la Cita
 * @author Luis L�zaro-Carrasco Hern�ndez
 */
package xdevs.lib.tfgs.c1516.distributed.sockets;

import java.util.ArrayList;

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;
import xdevs.core.simulation.SimulationClock;

public class CoordinatorDistributed extends Coordinator {

    /**
     *
     */
    private static final long serialVersionUID = 428330830561301426L;

    public CoordinatorDistributed() {
        super(null);
    }

    public CoordinatorDistributed(Coupled model) {
        super(model, true);
    }

    public CoordinatorDistributed(Coupled model, boolean flatten) {
        super(new SimulationClock(), model, flatten);
    }

    public CoordinatorDistributed(SimulationClock clock, Coupled model, boolean flatten) {
        super(clock, model, flatten);
    }

    public void lambda(ArrayList<Integer> lamb) {
        for (Integer x : lamb) {
            this.simulators.get(x).lambda();
        }
    }

    public void deltfcn(ArrayList<Integer> delt) {
        for (Integer x : delt) {
            this.simulators.get(x).deltfcn();
        }
    }

}
