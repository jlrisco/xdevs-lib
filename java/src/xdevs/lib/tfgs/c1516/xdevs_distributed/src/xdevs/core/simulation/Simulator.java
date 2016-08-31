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
package xdevs.core.simulation;

import java.io.Serializable;
import java.util.Collection;

import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;
import xdevs.core.modeling.api.AtomicInterface;
import xdevs.core.simulation.api.SimulationClock;

/**
 *
 * @author José Luis Risco Martín
 */
public class Simulator extends AbstractSimulator implements Serializable {

	private static final long serialVersionUID = 1843734699149406496L;
	protected AtomicInterface model;
	
	public Simulator(){
		super(null);
		this.model = null;		
	}
    public Simulator(SimulationClock clock, AtomicInterface model) {
        super(clock);
        this.model = model;
    }

    @Override
    public void initialize() {
        model.initialize();
        tL = clock.getTime();
        tN = tL + model.ta();
    }
    
    @Override
    public void exit() {
        model.exit();
    }

    @Override
    public double ta() {
        return model.ta();
    }

    @Override
    public void deltfcn() {
        double t = clock.getTime();
        boolean isInputEmpty = model.isInputEmpty();
        if (isInputEmpty && t != tN) {
            return;
        } else if (!isInputEmpty && t == tN) {
            double e = t - tL;
            model.setSigma(model.getSigma() - e);
            model.deltcon(e);
        } else if (isInputEmpty && t == tN) {
            model.deltint();
        } else if (!isInputEmpty && t != tN) {
            double e = t - tL;
            model.setSigma(model.getSigma() - e);
            model.deltext(e);
        }
        tL = t;
        tN = tL + model.ta();
    }

    @Override
    public void lambda() {
        if (clock.getTime() == tN) {
            model.lambda();
        }
    }

    @Override
    public void clear() {
        Collection<InPort<?>> inPorts;
        inPorts = model.getInPorts();
        for (InPort<?> port : inPorts) {
            port.clear();
        }
        Collection<OutPort<?>> outPorts;
        outPorts = model.getOutPorts();
        for (OutPort<?> port : outPorts) {
            port.clear();
        }
    }
    @Override
    public AtomicInterface getModel() {
        return model;
    }
    public void setModel(AtomicInterface at){
    	this.model = at;
    }


}
