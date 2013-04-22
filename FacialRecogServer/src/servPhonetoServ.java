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
     try {
    	int result = 0;
    	System.out.println("Connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
    	DataInputStream in = new DataInputStream(acceptSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(acceptSocket.getOutputStream());
    	while (result == 0) {
	    	System.out.println("Receiving Username and password");
	        String username = in.readUTF();
	        String password = in.readUTF();
	        String phoneIP = acceptSocket.getInetAddress().getHostAddress();
	        System.out.println("Username: " + username + "\nPassword: " + password + "\nIP: " + phoneIP);
	        //if ip is not used again, remove variable and just put ^^ in method below
	        result = sqlConnections.loginAuthentication(username, password, phoneIP, 1);
	        //sends 0 if fail, 1 if worked
	        out.writeInt(result);
	    }
    	int exit = 0;
    	while (exit==0) {
    		//stuff to do then send exit signal
    	}
    	acceptSocket.close();
		
        
     }catch(SocketTimeoutException s)
     {
        System.out.println("Socket timed out!");
     }catch(IOException e){}
   }
}
