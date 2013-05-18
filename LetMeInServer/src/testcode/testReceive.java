package testcode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class testReceive implements Runnable {
	private static ServerSocket listeningSocket;

	public testReceive(){
		
	}
	
	public void run(){
		   int clientport = 8080;
		   try {
			   while (true){
	    	 listeningSocket = new ServerSocket(clientport);
	         listeningSocket.setSoTimeout(0);
	         Socket client = new Socket("localhost", clientport);
	         System.out.println("Listening on port: " +listeningSocket.getLocalPort());
			 

	         Socket tempSock = new Socket();
	         tempSock = listeningSocket.accept();
	         System.out.println(tempSock.getRemoteSocketAddress());
	         
	         DataInputStream in = new DataInputStream(tempSock.getInputStream());
	         ObjectInputStream obin = new ObjectInputStream(tempSock.getInputStream());
			 DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
			    ObjectOutputStream obOut = new ObjectOutputStream(client.getOutputStream());


			 System.out.println("sending int");
			 dataOut.writeInt(5);
			 dataOut.flush();
	         
	         int num = in.readInt(); 
	         System.out.println("Num: " + num);
	         
	         
	         ourObject[] recv = new ourObject[num]; 
	         
	         ourObject[] ourOb = new ourObject[2];
			    ourOb[0] = new ourObject("james", "1", "1", null);
			    ourOb[1] = new ourObject("sarah", "1", "1", null);
			    obOut.writeObject(ourOb);
			    obOut.flush();
	         
	         
	         try {
				recv = (ourObject[]) obin.readObject();
				System.out.println(recv[0].name + "   " + recv[1].name);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	     	
	         
	         
	         
	         
			   }
	         
	      }catch(IOException e){
	    	  
	      }
	}
}
