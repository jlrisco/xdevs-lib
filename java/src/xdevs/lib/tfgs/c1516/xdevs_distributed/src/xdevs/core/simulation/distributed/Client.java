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
 *  - José Luis Risco Martín <jlrisco@ucm.es>
 *  - Saurabh Mittal <smittal@duniptech.com>
 */
/**
 *
 * @author Guillermo Llorente de la Cita
 * @author Luis L�zaro-Carrasco Hern�ndez
 */
package xdevs.core.simulation.distributed;
import java.util.ArrayList;

public class Client{
	public ServerHandler sh;
	public CoordinatorDistributed coordinator;
	public ArrayList<Integer> lamworks;
	public ArrayList<Integer> delworks;
	public Client(String serverDir,int port){
		sh = new ServerHandler(serverDir,port,this);
	}
	
	public void setCoordinator(CoordinatorDistributed coordinator){
		this.coordinator = coordinator;
	}
	public void RunLambda(){
		coordinator.lambda(lamworks);
		sh.sendToServer(Orders.setModel, coordinator);

		
	}
	
	public void RunDelta(){
		coordinator.deltfcn(delworks);
		coordinator.setTL(coordinator.getClock().getTime());
		coordinator.setTN(coordinator.getTL() + coordinator.ta());
		sh.sendToServer(Orders.setModel,coordinator);

	}



	
	public CoordinatorDistributed getModel(){
		return this.coordinator;
	}
	public void setLamWorks(ArrayList<Integer> lam){
		this.lamworks=lam;
	}
	public void setDelWorks(ArrayList<Integer> del){
		this.delworks=del;
	}


	

}
