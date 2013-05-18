package testcode;

import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class testSend implements Runnable{
	int port = 8080;
	
	public testSend(){
		
	}
	public void run(){
	    try {
	    	    	
		    Socket client = new Socket("localhost", port);
		    System.out.println("Connected to: " + client.getRemoteSocketAddress());

		    DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
		    System.out.println("sending int");
		    dataOut.writeInt(5);
		    dataOut.flush();
		    Thread.sleep(10000);
		    ObjectOutputStream obOut = new ObjectOutputStream(client.getOutputStream());
		    ourObject[] ourOb = new ourObject[2];
		    ourOb[0] = new ourObject("james", "1", "1", null);
		    ourOb[1] = new ourObject("sarah", "1", "1", null);
		    obOut.writeObject(ourOb);
		    obOut.flush();
		    client.close();
		    System.out.println("Connection Closed");
	    	
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
}
