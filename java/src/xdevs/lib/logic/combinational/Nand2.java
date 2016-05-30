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
package xdevs.lib.logic.combinational;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author José Luis Risco Martín
 */
public class Nand2 extends Atomic {

    public Port<Integer> iIn0 = new Port<>("In0");
    public Port<Integer> iIn1 = new Port<>("In1");
    public Port<Integer> oOut = new Port<>("Out");

    protected double delay;
    protected Integer valueToOut = null;
    protected Integer valueAtIn0 = null;
    protected Integer valueAtIn1 = null;

    public Nand2(String name, double delay) {
        super(name);
        super.addInPort(iIn0);
        super.addInPort(iIn1);
        super.addOutPort(oOut);
        this.delay = delay;
    }

    public Nand2(String name) {
        this(name, 132e-12);
    }

    public Nand2() {
        this(Nand2.class.getName());
    }

    @Override
    public void initialize() {
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
        boolean activate = false;
        
        Integer tempValueAtIn0 = (iIn0.isEmpty())? null : iIn0.getSingleValue();
        if (tempValueAtIn0 != null && !tempValueAtIn0.equals(valueAtIn0)) {
            valueAtIn0 = tempValueAtIn0;
            activate = true;
        }
        Integer tempValueAtIn1 = (iIn1.isEmpty())? null : iIn1.getSingleValue();
        if (tempValueAtIn1 != null && !tempValueAtIn1.equals(valueAtIn1)) {
            valueAtIn1 = tempValueAtIn1;
            activate = true;
        }
        if (activate && valueAtIn0 != null && valueAtIn1 != null) {
            valueToOut = ~(valueAtIn0 & valueAtIn1);
            super.holdIn("active", delay);
        }
    }

    @Override
    public void lambda() {
        oOut.addValue(valueToOut);
    }
}
