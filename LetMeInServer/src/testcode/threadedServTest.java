package testcode;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class threadedServTest implements Runnable {
	private static ServerSocket listeningSocket;
	private Socket acceptSocket;
	private int id;
	public threadedServTest (Socket s, int i) {
		acceptSocket = s;
		id = i;
	}
	
	public static void main(String [] args)
	   {
	      int clientport = 8080;
	      try
	      {
	    	 int i = 1;
	    	 listeningSocket = new ServerSocket(clientport);
	         listeningSocket.setSoTimeout(0);
	         System.out.println("Listening on port: " +listeningSocket.getLocalPort());
	         while (i<3) {
	        	 Socket tempSock = new Socket();
	        	 tempSock = listeningSocket.accept();
	             System.out.println("Connected to (tempsock): "+ tempSock.getRemoteSocketAddress());
	        	 
	             Thread t = new Thread(new threadedServTest(tempSock, i));
	             t.start();
	             i++;
	        	 
	         }
	      }catch(IOException e){}
	   }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//test with 2 concurrent connections to this and keep them both open indefinitely
        System.out.println("ID: " + id +" is connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
		for (int j=0; j<100; j++) {
			System.out.println("ID - " + id + ": " + j);
			try {
				Thread.sleep(500);
			}catch(InterruptedException e){} 
		}
		try {
			acceptSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
