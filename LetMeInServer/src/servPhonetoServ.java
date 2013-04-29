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
    	int result = 1;
    	int LogSignUp = -1;
    	int exit = 0;
    	String username;
    	String password;
    	String emailAddress;
    	System.out.println("Connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
    	DataInputStream in = new DataInputStream(acceptSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(acceptSocket.getOutputStream());
        LogSignUp=in.readInt();
    	if (LogSignUp == 0) {
	    	System.out.println("Receiving Username and password");
	        username = in.readUTF();
	        password = in.readUTF();
	        String phoneIP = acceptSocket.getInetAddress().getHostAddress();
	        System.out.println("Username: " + username + "\nPassword: " + password + "\nIP: " + phoneIP);
	        //if ip is not used again, remove variable and just put ^^ in method below
	        result = sqlConnections.loginAuthentication(username, password, phoneIP, 1);
	        //sends 0 if worked, 1 if fail
	        out.writeInt(result);
	    }
    	else if (LogSignUp==1){
    		System.out.println("Receiving Username and password and Email");
	        username = in.readUTF();
	        password = in.readUTF();
	        emailAddress = in.readUTF();
	        result = sqlConnections.signUp(username, password, emailAddress);
	        out.writeInt(result);
	        exit=1;
    	}
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
