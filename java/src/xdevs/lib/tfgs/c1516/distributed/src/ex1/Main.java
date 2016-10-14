package xdevs.lib.tfgs.c1516.distributed.src.ex1;

import java.util.ArrayList;

import xdevs.lib.tfgs.c1516.distributed.src.xdevs.core.simulation.distributed.ClientHandler;
import xdevs.lib.tfgs.c1516.distributed.src.xdevs.core.simulation.distributed.CoordinatorDistributed;
import xdevs.lib.tfgs.c1516.distributed.src.xdevs.core.simulation.distributed.Server;

public class Main {

    public static void main(String[] args) {
        /*Guide
		 	 * You have to distribute the tasks to the workers
         */
        //Here you have the model with the coordinator//
        MyPulseGeneratorExample pulseExample = new MyPulseGeneratorExample();
        CoordinatorDistributed coordinator = new CoordinatorDistributed(pulseExample, true);

        ArrayList<ClientHandler> clientList;	//List of workers

        /*Declaration of works*/
        ArrayList<Integer> lam1 = new ArrayList<>();	//lista de lambdas c1
        ArrayList<Integer> del1 = new ArrayList<>();	//lista de deltas c1
        ArrayList<Integer> lam2 = new ArrayList<>(); //lista de lambdas c2
        ArrayList<Integer> del2 = new ArrayList<>(); //lista de deltas c2
        /*Here you have to choose what work goes to what worker*/
 /*And add them to the works*/
        lam1.add(0);
        del1.add(0);
        lam2.add(1);
        del2.add(1);
        //Initialize the coordinator
        coordinator.initialize();
        //						   port,numClients,coordinator,flatten
        Server server = new Server(5000, 2, coordinator, true);

        server.connect();	//This waits until the clients get connected

        ////////////////Here we send to the client the works ////////////////////
        /*If the worker have on lam1 {0,4}  will execute the lambda of the simulator on the position 0 and 4*/
        server.sendLamWork(0, lam1);
        server.sendDelWork(0, del1);
        server.sendLamWork(1, lam2);
        server.sendDelWork(1, del2);
        /////////////////////////////////////////////////////////////////////////
        server.simulate(10.0);

    }

}
