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
public class IC7410 extends Atomic {

    public Port<Integer> iPin1 = new Port<>("pin1");
    public Port<Integer> iPin2 = new Port<>("pin2");
    public Port<Integer> iPin3 = new Port<>("pin3");
    public Port<Integer> iPin4 = new Port<>("pin4");
    public Port<Integer> iPin5 = new Port<>("pin5");
    public Port<Integer> iPin7 = new Port<>("pin7");
    public Port<Integer> iPin9 = new Port<>("pin9");
    public Port<Integer> iPin10 = new Port<>("pin10");
    public Port<Integer> iPin11 = new Port<>("pin11");
    public Port<Integer> iPin13 = new Port<>("pin13");
    public Port<Integer> iPin14 = new Port<>("pin14");

    public Port<Integer> oPin6 = new Port<>("pin6");
    public Port<Integer> oPin8 = new Port<>("pin8");
    public Port<Integer> oPin12 = new Port<>("pin12");

    protected Integer valueAtPin1 = null;
    protected Integer valueAtPin2 = null;
    protected Integer valueAtPin3 = null;
    protected Integer valueAtPin4 = null;
    protected Integer valueAtPin5 = null;
    protected Integer valueAtPin7 = null;
    protected Integer valueAtPin9 = null;
    protected Integer valueAtPin10 = null;
    protected Integer valueAtPin11 = null;
    protected Integer valueAtPin13 = null;
    protected Integer valueAtPin14 = null;

    protected Integer valueToPin6 = null;
    protected Integer valueToPin8 = null;
    protected Integer valueToPin12 = null;

    protected double delay;

    public IC7410(String name, double delay) {
        super(name);
        super.addInPort(iPin1);
        super.addInPort(iPin2);
        super.addInPort(iPin3);
        super.addInPort(iPin4);
        super.addInPort(iPin5);
        super.addOutPort(oPin6);
        super.addInPort(iPin7);
        super.addOutPort(oPin8);
        super.addInPort(iPin9);
        super.addInPort(iPin10);
        super.addInPort(iPin11);
        super.addOutPort(oPin12);
        super.addInPort(iPin13);
        super.addInPort(iPin14);
        this.delay = delay;
    }

    public IC7410(double delay) {
        this(IC7410.class.getName(), delay);
    }

    public IC7410(String name) {
        this(name, 192 * 1e-12);
    }

    public IC7410() {
        this(IC7410.class.getName());
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
        valueToPin6 = null;
        valueToPin8 = null;
        valueToPin12 = null;
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        Integer tempValueAtPin1 = (iPin1.isEmpty())? null : iPin1.getSingleValue();
        Integer tempValueAtPin2 = (iPin2.isEmpty())? null : iPin2.getSingleValue();
        Integer tempValueAtPin3 = (iPin3.isEmpty())? null : iPin3.getSingleValue();
        Integer tempValueAtPin4 = (iPin4.isEmpty())? null : iPin4.getSingleValue();
        Integer tempValueAtPin5 = (iPin5.isEmpty())? null : iPin5.getSingleValue();
        Integer tempValueAtPin7 = (iPin7.isEmpty())? null : iPin7.getSingleValue();
        Integer tempValueAtPin9 = (iPin9.isEmpty())? null : iPin9.getSingleValue();
        Integer tempValueAtPin10 = (iPin10.isEmpty())? null : iPin10.getSingleValue();
        Integer tempValueAtPin11 = (iPin11.isEmpty())? null : iPin11.getSingleValue();
        Integer tempValueAtPin13 = (iPin13.isEmpty())? null : iPin13.getSingleValue();
        Integer tempValueAtPin14 = (iPin14.isEmpty())? null : iPin14.getSingleValue();
        if (tempValueAtPin7 != null && !tempValueAtPin7.equals(valueAtPin7)) {
            valueAtPin7 = tempValueAtPin7;
        }
        if (tempValueAtPin14 != null && !tempValueAtPin14.equals(valueAtPin14)) {
            valueAtPin14 = tempValueAtPin14;
        }

        boolean activate = false;
        if (tempValueAtPin1 != null && !tempValueAtPin1.equals(valueAtPin1)) {
            valueAtPin1 = tempValueAtPin1;
            activate = true;
        }
        if (tempValueAtPin2 != null && !tempValueAtPin2.equals(valueAtPin2)) {
            valueAtPin2 = tempValueAtPin2;
            activate = true;
        }
        if (tempValueAtPin13 != null && !tempValueAtPin13.equals(valueAtPin13)) {
            valueAtPin13 = tempValueAtPin13;
            activate = true;
        }
        if (activate && valueAtPin14.equals(1) && valueAtPin7.equals(0)) {
            if (valueAtPin1 != null && valueAtPin2 != null && valueAtPin13 != null) {
                valueToPin12 = 1 - (valueAtPin1 & valueAtPin2 & valueAtPin13);
                super.holdIn("active", delay);
            }
        }

        activate = false;
        if (tempValueAtPin3 != null && !tempValueAtPin3.equals(valueAtPin3)) {
            valueAtPin3 = tempValueAtPin3;
            activate = true;
        }
        if (tempValueAtPin4 != null && !tempValueAtPin4.equals(valueAtPin4)) {
            valueAtPin4 = tempValueAtPin4;
            activate = true;
        }
        if (tempValueAtPin5 != null && !tempValueAtPin5.equals(valueAtPin5)) {
            valueAtPin5 = tempValueAtPin5;
            activate = true;
        }
        if (activate && valueAtPin14.equals(1) && valueAtPin7.equals(0)) {
            if (valueAtPin3 != null && valueAtPin4 != null && valueAtPin5 != null) {
                valueToPin6 = 1 - (valueAtPin3 & valueAtPin4 & valueAtPin5);
                super.holdIn("active", delay);
            }
        }

        activate = false;
        if (tempValueAtPin9 != null && !tempValueAtPin9.equals(valueAtPin9)) {
            valueAtPin9 = tempValueAtPin9;
            activate = true;
        }
        if (tempValueAtPin10 != null && !tempValueAtPin10.equals(valueAtPin10)) {
            valueAtPin10 = tempValueAtPin10;
            activate = true;
        }
        if (tempValueAtPin11 != null && !tempValueAtPin11.equals(valueAtPin11)) {
            valueAtPin11 = tempValueAtPin11;
            activate = true;
        }
        if (activate && valueAtPin14.equals(1) && valueAtPin7.equals(0)) {
            if (valueAtPin9 != null && valueAtPin10 != null && valueAtPin11 != null) {
                valueToPin8 = 1 - (valueAtPin9 & valueAtPin10 & valueAtPin11);
                super.holdIn("active", delay);
            }
        }
    }

    @Override
    public void lambda() {
        if (valueToPin6 != null) {
            oPin6.addValue(valueToPin6);
        }
        if (valueToPin8 != null) {
            oPin8.addValue(valueToPin8);
        }
        if (valueToPin12 != null) {
            oPin12.addValue(valueToPin12);
        }
    }
}
