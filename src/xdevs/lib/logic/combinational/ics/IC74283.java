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
package xdevs.lib.logic.combinational.ics;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author jlrisco
 */
public class IC74283 extends Atomic {

    public Port<Integer> oPin1 = new Port<>("pin1");
    public Port<Integer> iPin2 = new Port<>("pin2");
    public Port<Integer> iPin3 = new Port<>("pin3");
    public Port<Integer> oPin4 = new Port<>("pin4");
    public Port<Integer> iPin5 = new Port<>("pin5");
    public Port<Integer> iPin6 = new Port<>("pin6");
    public Port<Integer> iPin7 = new Port<>("pin7");
    public Port<Integer> iPin8 = new Port<>("pin8");
    public Port<Integer> oPin9 = new Port<>("pin9");
    public Port<Integer> oPin10 = new Port<>("pin10");
    public Port<Integer> iPin11 = new Port<>("pin11");
    public Port<Integer> iPin12 = new Port<>("pin12");
    public Port<Integer> oPin13 = new Port<>("pin13");
    public Port<Integer> iPin14 = new Port<>("pin14");
    public Port<Integer> iPin15 = new Port<>("pin15");
    public Port<Integer> iPin16 = new Port<>("pin16");

    protected Integer valueToPin1 = null;
    protected Integer valueAtPin2 = null;
    protected Integer valueAtPin3 = null;
    protected Integer valueToPin4 = null;
    protected Integer valueAtPin5 = null;
    protected Integer valueAtPin6 = null;
    protected Integer valueAtPin7 = null;
    protected Integer valueAtPin8 = null;
    protected Integer valueToPin9 = null;
    protected Integer valueToPin10 = null;
    protected Integer valueAtPin11 = null;
    protected Integer valueAtPin12 = null;
    protected Integer valueToPin13 = null;
    protected Integer valueAtPin14 = null;
    protected Integer valueAtPin15 = null;
    protected Integer valueAtPin16 = null;

    protected double delay;

    public IC74283(String name, double delay) {
        super(name);
        super.addOutPort(oPin1);
        super.addInPort(iPin2);
        super.addInPort(iPin3);
        super.addOutPort(oPin4);
        super.addInPort(iPin5);
        super.addInPort(iPin6);
        super.addInPort(iPin7);
        super.addInPort(iPin8);
        super.addOutPort(oPin9);
        super.addOutPort(oPin10);
        super.addInPort(iPin11);
        super.addInPort(iPin12);
        super.addOutPort(oPin13);
        super.addInPort(iPin14);
        super.addInPort(iPin15);
        super.addInPort(iPin16);
        this.delay = delay;
    }

    public IC74283(double delay) {
        this(IC74283.class.getName(), delay);
    }

    public IC74283(String name) {
        this(name, 16 * 1e-9);
    }

    public IC74283() {
        this(IC74283.class.getName());
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
        valueToPin1 = null;
        valueToPin4 = null;
        valueToPin9 = null;
        valueToPin10 = null;
        valueToPin13 = null;
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        Integer tempValueAtPin2 = (iPin2.isEmpty())? null : iPin2.getSingleValue();
        Integer tempValueAtPin3 = (iPin3.isEmpty())? null : iPin3.getSingleValue();
        Integer tempValueAtPin5 = (iPin5.isEmpty())? null : iPin5.getSingleValue();
        Integer tempValueAtPin6 = (iPin6.isEmpty())? null : iPin6.getSingleValue();
        Integer tempValueAtPin7 = (iPin7.isEmpty())? null : iPin7.getSingleValue();
        Integer tempValueAtPin8 = (iPin8.isEmpty())? null : iPin8.getSingleValue();
        Integer tempValueAtPin11 = (iPin11.isEmpty())? null : iPin11.getSingleValue();
        Integer tempValueAtPin12 = (iPin12.isEmpty())? null : iPin12.getSingleValue();
        Integer tempValueAtPin14 = (iPin14.isEmpty())? null : iPin14.getSingleValue();
        Integer tempValueAtPin15 = (iPin15.isEmpty())? null : iPin15.getSingleValue();
        Integer tempValueAtPin16 = (iPin16.isEmpty())? null : iPin16.getSingleValue();
        if (tempValueAtPin8 != null && !tempValueAtPin8.equals(valueAtPin8)) {
            valueAtPin8 = tempValueAtPin8;
        }
        if (tempValueAtPin16 != null && !tempValueAtPin16.equals(valueAtPin16)) {
            valueAtPin16 = tempValueAtPin16;
        }

        boolean activate = false;
        if (tempValueAtPin2 != null && !tempValueAtPin2.equals(valueAtPin2)) {
            valueAtPin2 = tempValueAtPin2;
            activate = true;
        }
        if (tempValueAtPin3 != null && !tempValueAtPin3.equals(valueAtPin3)) {
            valueAtPin3 = tempValueAtPin3;
            activate = true;
        }
        if (tempValueAtPin5 != null && !tempValueAtPin5.equals(valueAtPin5)) {
            valueAtPin5 = tempValueAtPin5;
            activate = true;
        }
        if (tempValueAtPin6 != null && !tempValueAtPin6.equals(valueAtPin6)) {
            valueAtPin6 = tempValueAtPin6;
            activate = true;
        }
        if (tempValueAtPin7 != null && !tempValueAtPin7.equals(valueAtPin7)) {
            valueAtPin7 = tempValueAtPin7;
            activate = true;
        }
        if (tempValueAtPin11 != null && !tempValueAtPin11.equals(valueAtPin11)) {
            valueAtPin11 = tempValueAtPin11;
            activate = true;
        }
        if (tempValueAtPin12 != null && !tempValueAtPin12.equals(valueAtPin12)) {
            valueAtPin12 = tempValueAtPin12;
            activate = true;
        }
        if (tempValueAtPin14 != null && !tempValueAtPin14.equals(valueAtPin14)) {
            valueAtPin14 = tempValueAtPin14;
            activate = true;
        }
        if (tempValueAtPin15 != null && !tempValueAtPin15.equals(valueAtPin15)) {
            valueAtPin15 = tempValueAtPin15;
            activate = true;
        }

        if ((valueAtPin16 - valueAtPin8) > 0) {
            if (activate && valueAtPin2 != null && valueAtPin3 != null
                    && valueAtPin5 != null && valueAtPin6 != null && valueAtPin7 != null
                    && valueAtPin11 != null && valueAtPin12 != null && valueAtPin14 != null
                    && valueAtPin15 != null) {
                int sum = valueAtPin7 + (valueAtPin5 + valueAtPin6)
                        + 2 * (valueAtPin2 + valueAtPin3) + 4 * (valueAtPin14 + valueAtPin15)
                        + 8 * (valueAtPin11 + valueAtPin12);
                int mask = 1 << 4;
                valueToPin9 = (sum & mask) != 0 ? 1 : 0;
                mask = 1 << 3;
                valueToPin10 = (sum & mask) != 0 ? 1 : 0;
                mask = 1 << 2;
                valueToPin13 = (sum & mask) != 0 ? 1 : 0;
                mask = 1 << 1;
                valueToPin1 = (sum & mask) != 0 ? 1 : 0;
                mask = 1;
                valueToPin4 = (sum & mask) != 0 ? 1 : 0;
                super.holdIn("active", delay);
            }
            else {
                super.passivate();
            }
        }
    }

    @Override
    public void lambda() {
        oPin1.addValue(valueToPin1);
        oPin4.addValue(valueToPin4);
        oPin9.addValue(valueToPin9);
        oPin10.addValue(valueToPin10);
        oPin13.addValue(valueToPin13);
    }
}
