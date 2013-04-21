package testcode;

import java.net.Socket;



public class threadConnectTest implements Runnable {
	
	public static void main(String [] args){
		threadConnectTest serv1 = new threadConnectTest();
        Thread t1 = new Thread(serv1);
        threadConnectTest serv2 = new threadConnectTest();
        Thread t2 = new Thread(serv2);
		t1.start();
		t2.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String serverName = "127.0.0.1";
	    int port = 8080;
	    try {
		    System.out.println("Connecting to: " + serverName + "Port: " + port);
		    Socket client = new Socket(serverName, port);
		    System.out.println("Connected to: " + client.getRemoteSocketAddress());
		    client.close();
		    System.out.println("Connection Closed");
	    }catch(Exception e) {}
	}
}
