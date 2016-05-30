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

package xdevs.lib.tfgs.c1516.hwsw.adder;

import java.util.Random;
import xdevs.core.modeling.Coupled;
import xdevs.core.simulation.Coordinator;

/**
 * 
 * @author Miguel Higuera Romero
 */
public class AddTest extends Coupled{
    protected AdderMatrix matx;
    protected Adder adder;
    
    public AddTest(String name){
        super(name);
        
        adder = new Adder("adder");
        addComponent(adder);
        matx = new AdderMatrix("matrix");
        addComponent(matx);
        
        addCoupling(matx.portA, adder.portA);
        addCoupling(matx.portB, adder.portB);
        addCoupling(adder.portC, matx.portC);
        
    }
    
    public void addOperands(int a, int b){
        matx.addOperands(a, b);
    }
    
    public static void main(String[] args) {
        AddTest test = new AddTest("test");
        Random rdm = new Random();
        for(int i = 0; i < 10; i++)
           test.addOperands(Math.abs(rdm.nextInt() % 15), Math.abs(rdm.nextInt() % 15));
        Coordinator coor = new Coordinator(test);
        coor.initialize();
        coor.simulate(Long.MAX_VALUE);
        coor.exit();
    }
}
