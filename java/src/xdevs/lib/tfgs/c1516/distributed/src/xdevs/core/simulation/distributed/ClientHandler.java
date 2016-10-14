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
package xdevs.lib.tfgs.c1516.distributed.src.xdevs.core.simulation.distributed;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class ClientHandler implements Runnable{
	private Socket cliente;
	private Server server;
	ObjectInputStream in=null;
	ObjectOutputStream out=null;
	Object orden;
	Object lista;
	Double ta;
	public ClientHandler(Socket so,Server server){
		this.cliente=so;
		this.server=server;
	}
	
	public void sendToClient(Orders o){
		try {
			if(out==null)out = new ObjectOutputStream(cliente.getOutputStream());
			out.writeObject(o);
		} catch (IOException e) {
			
		}
	}
	public void sendToClient(Orders o,Object param){
		try {
			if(out==null)out = new ObjectOutputStream(cliente.getOutputStream());
			out.writeObject(o);
			out.writeObject(param);
		} catch (IOException e) {
			
		}
	}
	public void sendToClient(Orders o,Object param1, Object param2){
		try {
			if(out==null)out = new ObjectOutputStream(cliente.getOutputStream());
			out.writeObject(o);
			out.writeObject(param1);
			out.writeObject(param2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void run() {
		while(!Thread.currentThread().isInterrupted()){
			//readOrder
			try {
				if(in==null) in = new ObjectInputStream(cliente.getInputStream());
				orden = in.readObject();
				if(orden.equals(Orders.setModel)){
					CoordinatorDistributed model = (CoordinatorDistributed) in.readObject();
					this.server.setModel(model);
					synchronized (server) {
						this.server.notify();
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				//nothing
			}
		}
		try {
			in.close();
			out.close();
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Socket getSocket(){
		return this.cliente;
	}
	public void endTask(){
		Thread.currentThread().interrupt();
	}
	

}
