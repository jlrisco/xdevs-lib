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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import xdevs.core.simulation.Coordinator;
import xdevs.core.util.Constants;

public class Server{
	private ServerSocket sc;
	private Socket so;
	public CoordinatorDistributed coord;
	public double tL,tN;
	private int numClients;
	private ArrayList<ClientHandler> clientList;
	private static final Logger logger = Logger.getLogger(Coordinator.class
            .getName());
	public Server(int port,int numClients,CoordinatorDistributed coord,boolean flatten){
		this.coord=coord;
		try {
			this.numClients = numClients;
			this.sc=new ServerSocket(port);
			this.clientList = new ArrayList<ClientHandler>();
		}
			catch (IOException e) 
			{
				System.out.println("-> Can�t create the server on port: " + port);
				System.exit(1);
			}
		System.out.println("-> Server created on port: " + port);
	}

    public void simulate(long numIterations) {
    	
        logger.fine("START SIMULATION");
        coord.getClock().setTime(tN);
        long counter;
        for (counter = 1; counter < numIterations
                && coord.getClock().getTime() < Constants.INFINITY; counter++) {
            lambda();
            propagateOutput();
            propagateInput();
            deltfcn();
            coord.clear();
            coord.getClock().setTime(coord.getTN());
        }
        for(ClientHandler c : clientList){
        	c.sendToClient(Orders.endTask);
        	c.endTask();
        }
        try {
			so.close();
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println("Simulation terminated");
        
        coord.exit();
    }

    public void simulate(double timeInterval) {
    	long timer = System.nanoTime();
        logger.fine("START SIMULATION");
        coord.getClock().setTime(tN);
        double tF = coord.getClock().getTime() + timeInterval;
        while (coord.getClock().getTime() < Constants.INFINITY && coord.getClock().getTime() < tF) {
            lambda();
            propagateOutput();
            propagateInput();
            deltfcn();
            coord.clear();
            coord.getClock().setTime(coord.getTN());
        }
        for(ClientHandler c : clientList){
        	c.sendToClient(Orders.endTask);
        	c.endTask();
        }
        try {
			so.close();
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Simulation terminated");
        System.out.println("It has taken "+(System.nanoTime()-timer)+ " nanoseconds");
        coord.exit();
        //System.exit(0);
        
        
    }
	

	public void connect(){
		for(int i=0;i<numClients;i++){
			so = new Socket();
			try {
				so = sc.accept();
				System.out.println("Client connected " + so.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clientList.add(new ClientHandler(so,this));	//Add clients ordered by time of connection
		}
		startThreads();
	}
	

	 public ArrayList<ClientHandler> getclientList(){
		 return this.clientList;
	}
	public void startThreads(){
		for(ClientHandler c:clientList){
	    	new Thread(c).start();
	    }
	}
	public void sendLamWork(int idclient,ArrayList<Integer> lam){
		clientList.get(idclient).sendToClient(Orders.sendLamWork,lam);
	}
	public void sendDelWork(int idclient,ArrayList<Integer> del){
		clientList.get(idclient).sendToClient(Orders.sendDelWork,del);
	}

	public synchronized void lambda() {
		for(ClientHandler c : clientList){
			c.sendToClient(Orders.setModel, this.coord);	//Send the model
			c.sendToClient(Orders.runLambda);	//Send the order
			try {								//Wait the answer
				wait();
			} catch (InterruptedException e) {
			}
		}
		
	}


	public synchronized void deltfcn() {
		for(ClientHandler c : clientList){
			c.sendToClient(Orders.setModel, this.coord);
			c.sendToClient(Orders.runDelta);
			try {								//Espero la respuesta
				wait();
			} catch (InterruptedException e) {
			}
		}
		
	}
	public void setModel(CoordinatorDistributed coord){
		this.coord = coord;
	}
	public void propagateOutput() {
		this.coord.propagateOutput();
    }
	public void propagateInput() {
        this.coord.propagateInput();
    }
}
