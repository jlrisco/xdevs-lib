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
package xdevs.lib.logic.sequential.ics;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author jlrisco
 */
public class IC74169 extends Atomic {

    public Port<Integer> iPin1 = new Port<>("pin1");
    public Port<Integer> iPin2 = new Port<>("pin2");
    public Port<Integer> iPin3 = new Port<>("pin3");
    public Port<Integer> iPin4 = new Port<>("pin4");
    public Port<Integer> iPin5 = new Port<>("pin5");
    public Port<Integer> iPin6 = new Port<>("pin6");
    public Port<Integer> iPin7 = new Port<>("pin7");
    public Port<Integer> iPin8 = new Port<>("pin8");
    public Port<Integer> iPin9 = new Port<>("pin9");
    public Port<Integer> iPin10 = new Port<>("pin10");
    public Port<Integer> oPin11 = new Port<>("pin11");
    public Port<Integer> oPin12 = new Port<>("pin12");
    public Port<Integer> oPin13 = new Port<>("pin13");
    public Port<Integer> oPin14 = new Port<>("pin14");
    public Port<Integer> oPin15 = new Port<>("pin15");
    public Port<Integer> iPin16 = new Port<>("pin16");

    protected Integer valueAtPin1 = null;
    protected Integer valueAtPin2 = null;
    protected Integer valueAtPin3 = null;
    protected Integer valueAtPin4 = null;
    protected Integer valueAtPin5 = null;
    protected Integer valueAtPin6 = null;
    protected Integer valueAtPin7 = null;
    protected Integer valueAtPin8 = null;
    protected Integer valueAtPin9 = null;
    protected Integer valueAtPin10 = null;
    protected Integer valueToPin11 = 0;
    protected Integer valueToPin12 = 0;
    protected Integer valueToPin13 = 0;
    protected Integer valueToPin14 = 0;
    protected Integer valueToPin15 = 0;
    protected Integer valueAtPin16 = null;

    protected double delay;

    public IC74169(String name, double delay) {
        super(name);
        super.addInPort(iPin1);
        super.addInPort(iPin2);
        super.addInPort(iPin3);
        super.addInPort(iPin4);
        super.addInPort(iPin5);
        super.addInPort(iPin6);
        super.addInPort(iPin7);
        super.addInPort(iPin8);
        super.addInPort(iPin9);
        super.addInPort(iPin10);
        super.addOutPort(oPin11);
        super.addOutPort(oPin12);
        super.addOutPort(oPin13);
        super.addOutPort(oPin14);
        super.addOutPort(oPin15);
        super.addInPort(iPin16);
        this.delay = delay;
    }

    public IC74169(double delay) {
        this(IC74169.class.getName(), delay);
    }

    public IC74169(String name) {
        this(name, 100 * 1e-12);
    }

    public IC74169() {
        this(IC74169.class.getName());
    }

    @Override
    public void initialize() {
        valueToPin11 = 0;
        valueToPin12 = 0;
        valueToPin13 = 0;
        valueToPin14 = 0;
        valueToPin15 = 1;
        super.holdIn("initialize", 0);
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
        Integer tempValueAtPin1 = (iPin1.isEmpty()) ? null : iPin1.getSingleValue();
        Integer tempValueAtPin2 = (iPin2.isEmpty()) ? null : iPin2.getSingleValue();
        Integer tempValueAtPin3 = (iPin3.isEmpty()) ? null : iPin3.getSingleValue();
        Integer tempValueAtPin4 = (iPin4.isEmpty()) ? null : iPin4.getSingleValue();
        Integer tempValueAtPin5 = (iPin5.isEmpty()) ? null : iPin5.getSingleValue();
        Integer tempValueAtPin6 = (iPin6.isEmpty()) ? null : iPin6.getSingleValue();
        Integer tempValueAtPin7 = (iPin7.isEmpty()) ? null : iPin7.getSingleValue();
        Integer tempValueAtPin8 = (iPin8.isEmpty()) ? null : iPin8.getSingleValue();
        Integer tempValueAtPin9 = (iPin9.isEmpty()) ? null : iPin9.getSingleValue();
        Integer tempValueAtPin10 = (iPin10.isEmpty()) ? null : iPin10.getSingleValue();
        Integer tempValueAtPin16 = (iPin16.isEmpty()) ? null : iPin16.getSingleValue();
        if (tempValueAtPin8 != null) {
            valueAtPin8 = tempValueAtPin8;
        }
        if (tempValueAtPin16 != null) {
            valueAtPin16 = tempValueAtPin16;
        }

        if (tempValueAtPin1 != null) {
            valueAtPin1 = tempValueAtPin1;
        }
        if (tempValueAtPin3 != null) {
            valueAtPin3 = tempValueAtPin3;
        }
        if (tempValueAtPin4 != null) {
            valueAtPin4 = tempValueAtPin4;
        }
        if (tempValueAtPin5 != null) {
            valueAtPin5 = tempValueAtPin5;
        }
        if (tempValueAtPin6 != null) {
            valueAtPin6 = tempValueAtPin6;
        }
        if (tempValueAtPin7 != null) {
            valueAtPin7 = tempValueAtPin7;
        }
        if (tempValueAtPin8 != null) {
            valueAtPin8 = tempValueAtPin8;
        }
        if (tempValueAtPin9 != null) {
            valueAtPin9 = tempValueAtPin9;
        }
        if (tempValueAtPin10 != null) {
            valueAtPin10 = tempValueAtPin10;
        }

        // Miramos si hay flanco de reloj
        if (tempValueAtPin2 != null) {
            if (tempValueAtPin2 == 1 && (valueAtPin2.equals(0)) && valueAtPin16.equals(1) && valueAtPin8.equals(0)) {
                // Is there a load operation?
                if (valueAtPin9.equals(0)) {
                    valueToPin14 = valueAtPin3;
                    valueToPin13 = valueAtPin4;
                    valueToPin12 = valueAtPin5;
                    valueToPin11 = valueAtPin6;
                    super.holdIn("load", delay);
                } else {
                    // Check the current state
                    int currentState = 1 * valueToPin14 + 2 * valueToPin13 + 4 * valueToPin12 + 8 * valueToPin11;
                    // Update the state if both CEP and CET are equal to 0
                    if (valueAtPin7.equals(0) && valueAtPin10.equals(0)) {
                        valueToPin15 = 1;
                        // up counter:
                        if (valueAtPin1.equals(1)) {
                            currentState++;
                            if (currentState == 16) {
                                currentState = 0;
                                valueToPin15 = 0;
                            }
                        } else {
                            currentState--;
                            if (currentState == -1) {
                                currentState = 15;
                                valueToPin15 = 0;
                            }
                        }
                        int mask = 1 << 3;
                        valueToPin11 = (currentState & mask) != 0 ? 1 : 0;
                        mask = 1 << 2;
                        valueToPin12 = (currentState & mask) != 0 ? 1 : 0;
                        mask = 1 << 1;
                        valueToPin13 = (currentState & mask) != 0 ? 1 : 0;
                        mask = 1;
                        valueToPin14 = (currentState & mask) != 0 ? 1 : 0;
                        super.holdIn("update", delay);
                    }
                }
            }
            valueAtPin2 = tempValueAtPin2;
        }
    }

    @Override
    public void lambda() {
        oPin11.addValue(valueToPin11);
        oPin12.addValue(valueToPin12);
        oPin13.addValue(valueToPin13);
        oPin14.addValue(valueToPin14);
        oPin15.addValue(valueToPin15);
    }
}
