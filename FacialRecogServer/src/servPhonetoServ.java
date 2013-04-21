import java.net.*;
import java.io.*;
import javax.imageio.ImageIO;



public class servPhonetoServ implements Runnable{
   private Socket acceptSocket;
    
   
   public servPhonetoServ(Socket s) throws IOException
   {
	   acceptSocket = s;
   }

   public void run()
   {
     /*try {
    	System.out.println("Connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
        System.out.println("Receiving User ID");
        DataInputStream in = new DataInputStream(acceptSocket.getInputStream());
        int userID = in.readInt();
        System.out.println("Attempting retreival of photo");
        System.out.println("Received Image");
        acceptSocket.close();
		System.out.println("ID: " + userID);
		
        
     }catch(SocketTimeoutException s)
     {
        System.out.println("Socket timed out!");
     }catch(IOException e){}*/
   }
}
