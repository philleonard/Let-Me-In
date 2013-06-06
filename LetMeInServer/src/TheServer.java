import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author James
 * 
 * This is the main server class that is ran and starts listening for connections 
 * from the pc's running the client software.
 * 
 * When it gets a connection, it creates a new socket to accept the connection
 * and passes that in to a new thread to hand the facial recognition etc.
 *
 */

public class TheServer extends Thread {
	private static ServerSocket listeningSocket;

	public static void main(String[] args) { 
		

	   int clientport = 55555;
	   try {
		 listeningSocket = new ServerSocket(clientport);
	     listeningSocket.setSoTimeout(0);
	     System.out.println("Listening on port: " +listeningSocket.getLocalPort());
	     while (true) {
	    	 Socket tempSock = new Socket();
	    	 tempSock = listeningSocket.accept();	
	    	 Thread t = new Thread(new ServerConnections(tempSock));
	         t.start();
	         
	    	 
	     }
	     
	  }catch(IOException e){
		  e.printStackTrace();
	  }
		
	}
}
