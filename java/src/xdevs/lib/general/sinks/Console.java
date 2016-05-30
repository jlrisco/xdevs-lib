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
package xdevs.lib.general.sinks;

import java.util.Collection;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author José Luis Risco Martín
 */
public class Console extends Atomic {

    //private static final Logger logger = Logger.getLogger(Console.class.getName());

    public Port<Object> iIn = new Port<>("iIn");
    // Parameters
    protected double time;

    /**
     * Console atomic model.
     *
     * @param name Name of the atomic model
     */
    public Console(String name) {
    	super(name);
        super.addInPort(iIn);
    }
    
    @Override
    public void initialize() {
        this.time = 0.0;
    	super.passivate();
    }
    
    @Override
    public void exit() {        
    }
    
    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        time += e;
        if (iIn.isEmpty()) {
            return;
        }
        StringBuilder message = new StringBuilder();
        message.append(name).append("::").append(time);
        Collection<Object> values = iIn.getValues();
        for (Object value : values) {
            message.append(":").append(value.toString());
        }
        System.out.println(message.toString());
    }

    @Override
    public void lambda() {
    }
}
