import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author James
 * 
 * This class is finished. It is called from the main server class and 
 * starts listening for connections from the pc's running the client software.
 * 
 * When it gets a connection, it creates a new socket to accept the connection
 * and passes that in to a new thread to hand the facial recognition etc.
 *
 */

public class ListeningClass implements Runnable {
	private static ServerSocket listeningSocket;

	public void run(){
		   int clientport = 8080;
		   try {
	    	 listeningSocket = new ServerSocket(clientport);
	         listeningSocket.setSoTimeout(0);
	         System.out.println("Listening on port: " +listeningSocket.getLocalPort());
	         while (true) {
	        	 Socket tempSock = new Socket();
	        	 tempSock = listeningSocket.accept();
	             //System.out.println("Connected to (tempsock): "+ tempSock.getRemoteSocketAddress());

	        	 Thread t = new Thread(new ConntoServ(tempSock));
	             t.start();
	             
	        	 
	         }
	         
	      }catch(IOException e){
	    	  
	      }
	   }
	   

}
