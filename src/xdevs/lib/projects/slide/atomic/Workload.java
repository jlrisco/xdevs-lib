/*
 * Copyright (C) 2015-2016 GreenDisc research group <http://greendisc.dacya.ucm.es/>
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

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.OutPort;
import xdevs.lib.projects.slide.util.WorkloadTrace;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class Workload extends Atomic {

    public OutPort<Double> oOut = new OutPort<>("out");

    protected LinkedList<WorkloadTrace> workloadTraces = new LinkedList<>();
    protected WorkloadTrace currentTrace = null;
    protected Double nextTime = null;
    protected Double nextValue = null;

    public Workload(Collection<String> filePaths) throws IOException {
        super(Workload.class.getName());
        super.addOutPort(oOut);
        for (String filePath : filePaths) {
            workloadTraces.add(new WorkloadTrace(filePath));
        }
    }

    @Override
    public void initialize() {
        // Read the first event:
        if (!workloadTraces.isEmpty()) {
            currentTrace = workloadTraces.remove();
            nextTime = currentTrace.getNextTime();
            nextValue = currentTrace.getNextValue();
            if (nextTime != null && nextValue != null) {
                super.holdIn("active", nextTime);
            }
        } else {
            super.passivate();
        }
    }

    @Override
    public void deltint() {
        nextTime = currentTrace.getNextTime();
        nextValue = currentTrace.getNextValue();
        if (nextTime != null && nextValue != null) {
            super.holdIn("active", nextTime);
        } else if (!workloadTraces.isEmpty()) {
            currentTrace = workloadTraces.remove();
            nextTime = currentTrace.getNextTime();
            nextValue = currentTrace.getNextValue();
            if (nextTime != null && nextValue != null) {
                super.holdIn("active", nextTime);
            }
            else {
                super.passivate();
            }
        } else {
            super.passivate();
        }
    }

    @Override
    public void deltext(double e) {
    }

    @Override
    public void lambda() {
        oOut.addValue(nextValue);
    }

    @Override
    public void exit() {
    }
}
