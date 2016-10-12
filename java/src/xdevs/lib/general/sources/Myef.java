/*
 * Copyright (C) 2014-2015 José Luis Risco Martín <jlrisco@ucm.es> and 
 * Saurabh Mittal <smittal@duniptech.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * http://www.gnu.org/licenses/
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package xdevs;

import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.InPort;
import xdevs.core.modeling.OutPort;
import xdevs.core.modeling.Port;

/**
 *
 * @author José Luis Risco Martín
 */
public class Myef extends Coupled {

  protected Port<Myjob> iStart = new Port<Myjob>("iStart");
  protected Port<Myjob> iIn = new Port<Myjob>("iIn");
  protected Port<Myjob> oOut = new Port<Myjob>("oOut");

  public Myef(String name, double period, double observationTime) {
	  super(name);
    super.addInPort((InPort<?>) iIn);
    super.addInPort((InPort<?>) iStart);
    super.addOutPort((OutPort<?>) oOut);
    Mygen generator = new Mygen("generator", period);
    super.addComponent(generator);
    Mytransductor transducer = new Mytransductor("transducer", observationTime);
    super.addComponent(transducer);
    
    super.addCoupling(this.iIn, transducer.iSolved);
    super.addCoupling(generator.oOut, this.oOut);
    super.addCoupling(generator.oOut, transducer.iArrived);
    super.addCoupling(transducer.oOut, generator.iStop);
    super.addCoupling(this.iStart, generator.iStart);
  }
}
