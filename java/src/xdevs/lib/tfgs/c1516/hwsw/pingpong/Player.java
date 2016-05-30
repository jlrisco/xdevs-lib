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

package xdevs.lib.tfgs.c1516.hwsw.pingpong;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class Player extends Atomic{

    protected Port<Integer> iReceive = new Port<>("Receiver");
    protected Port<Integer> oSend = new Port<>("Sender");
    private double delay;

    public Player(String name, double delay){
        super(name);
        this.delay = delay;
        super.addInPort(iReceive);
        super.addOutPort(oSend);
    }

    @Override
    public void initialize() {
        if(super.getName().equals("A")){
            super.holdIn("Send", 0);
        }else{
            super.passivateIn("Wait");
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public void deltint() {
        if(phaseIs("Send")){
            super.holdIn("Wait", delay);
        }else{
            super.passivate();
        }
    }

    @Override
    public void deltext(double d) {
        if(!phaseIs("Send")){
            if(!iReceive.isEmpty() && iReceive.getSingleValue() != null){
                System.out.println("Player " + super.getName() + " received the ball");
                super.holdIn("Send", delay);
            }else{
                super.passivate();
            }
        }
    }

    @Override
    public void lambda() {
        if(phaseIs("Send")){
            System.out.println("Player " + super.getName() + " sent the ball");
            oSend.addValue(1);
        }else{
            oSend.addValue(null);
        }
    }
}
