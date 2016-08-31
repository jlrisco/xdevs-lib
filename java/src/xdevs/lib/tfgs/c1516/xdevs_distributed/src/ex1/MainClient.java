package ex1;

import xdevs.core.simulation.distributed.Client;

public class MainClient {

	
	 public static void main(String[] args) {
		 	Client client = new Client("localhost",5000);//"7.26.208.49"
		    new Thread(client.sh).start();	//start the server handler

		    

	 }

}
