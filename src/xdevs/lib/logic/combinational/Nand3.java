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
 * @author José Luis Risco Martín
 */
public class Nand3 extends Atomic {

    public Port<Integer> iIn0 = new Port<>("iIn0");
    public Port<Integer> iIn1 = new Port<>("iIn1");
    public Port<Integer> iIn2 = new Port<>("iIn2");
    public Port<Integer> oOut = new Port<>("Out");

    protected Integer valueAtIn0 = null;
    protected Integer valueAtIn1 = null;
    protected Integer valueAtIn2 = null;
    protected Integer valueToOut = null;

    protected double delay;

    public Nand3(String name, double delay) {
        super(name);
        super.addInPort(iIn0);
        super.addInPort(iIn1);
        super.addInPort(iIn2);
        super.addOutPort(oOut);
        this.delay = delay;
    }

    public Nand3(String name) {
        this(name, 192e-12);
    }

    public Nand3() {
        this(Nand3.class.getName());
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
        // Primero procesamos los valores de las entradas.
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
        Integer tempValueAtIn2 = (iIn2.isEmpty())? null : iIn2.getSingleValue();
        if (tempValueAtIn2 != null && !tempValueAtIn2.equals(valueAtIn2)) {
            valueAtIn2 = tempValueAtIn2;
            activate = true;
        }

        if (activate && valueAtIn0 != null && valueAtIn1 != null && valueAtIn2 != null) {
            valueToOut = ~(valueAtIn0 & valueAtIn1 & valueAtIn2);
            super.holdIn("active", delay);
        }
    }

    @Override
    public void lambda() {
        oOut.addValue(valueToOut);
    }
}
