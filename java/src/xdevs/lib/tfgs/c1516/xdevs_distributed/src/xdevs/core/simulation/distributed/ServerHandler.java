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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import xdevs.core.simulation.api.SimulationClock;

public class ServerHandler implements Runnable{
	private Object order;
	private Client client;
	private Socket so;
	private Object recep;
	SimulationClock clock;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	public ServerHandler(String dir,int puerto,Client client){
		this.client = client;
		try {
			so = new Socket(dir,puerto);
		} 
		catch (IOException e) 
		{
			System.out.println("-> Failed to connect : ");
			System.exit(1);
		}
	}
	public void sendToServer(Orders o){
		try {
			if(out==null)out = new ObjectOutputStream(so.getOutputStream());
			out.writeObject(o);
		} catch (IOException e) {
			Thread.currentThread().interrupt();
		}
	}
	public void sendToServer(Orders o,Object param){
		try {
			if(out==null)out = new ObjectOutputStream(so.getOutputStream());
			out.writeObject(o);
			out.writeObject(param);
			
		} catch (IOException e) {
			Thread.currentThread().interrupt();
		}
	}
	public void setClient(Client client){
		this.client = client;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void run() {
		while(!Thread.currentThread().isInterrupted()){
			try {
				if(!so.isConnected()){
					so.close();
				}
				if(in==null)in = new ObjectInputStream(so.getInputStream());
				order = in.readObject();
				if(order.equals(Orders.runLambda)){
					this.client.RunLambda();
				}
				else if(order.equals(Orders.runDelta)){
					this.client.RunDelta();
				}
				else if(order.equals(Orders.sendLamWork)){
					recep = in.readObject();
					this.client.setLamWorks((ArrayList<Integer>)recep);
				}
				else if(order.equals(Orders.sendDelWork)){
					recep = in.readObject();
					this.client.setDelWorks((ArrayList<Integer>)recep);
				}
				else if(order.equals(Orders.setModel)){
					CoordinatorDistributed aux = (CoordinatorDistributed) in.readObject();
					this.client.setCoordinator(aux);
				}
				else if(order.equals(Orders.endTask)){
					Thread.currentThread().interrupt();
				}

			} catch (IOException | ClassNotFoundException e) {
				Thread.currentThread().interrupt();
			}
	    }
		try {
			this.in.close();
			this.out.close();
			this.so.close();
		} catch (IOException e) {
			
		}
		
		
	}

	
}
