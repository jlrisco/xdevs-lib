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

import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;

/**
 *
 * @author Miguel Higuera Romero
 */
public class PingPong extends Coupled{

    public PingPong(String name){
        super(name);
        Player pA = new Player("A", 1e-9);
        Player pB = new Player("B", 1e-9);

        super.addComponent(pA);
        super.addComponent(pB);

        super.addCoupling(pA.oSend, pB.iReceive);
        super.addCoupling(pB.oSend, pA.iReceive);
    }

    public static void main(String[] args) {
        PingPong game = new PingPong("Game");
        Coordinator coor = new Coordinator(game);
        coor.initialize();
        coor.simulate(100);
        coor.exit();
    }
}
