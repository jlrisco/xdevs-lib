/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdevs.lib.util;

import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

/**
 *
 * @author josueportiz
 */
public class DataBuffer extends Atomic {
    public Port<Boolean> iIn = new Port<>("In");
    public Port<Double> oOut = new Port<>("Out");

    protected Double valueToOut = null;

    public DataBuffer (String name){
        super(name);
        super.addInPort(iIn);
        super.addOutPort(oOut);
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
    public void deltext(double d) {
        Boolean tempValuaeAtIn = iIn.getSingleValue();
        if (tempValuaeAtIn != null) {
            valueToOut = (tempValuaeAtIn) ? 1.0 : 0.0;
        }
        super.activate();
    }

    @Override
    public void lambda() {
        oOut.addValue(valueToOut);
    }
}
