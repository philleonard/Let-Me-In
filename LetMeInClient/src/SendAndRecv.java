import com.googlecode.javacv.cpp.opencv_core.IplImage;
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * @author James
 */
public class SendAndRecv {
	public int sendToServ (IplImage image, String username, String password) {
		//Send image to server, return 0 if success, -1 else
		String serverName = "127.0.0.1";
	    int port = 8080;
	    try {
	    	    	
		    System.out.println("Connecting to: " + serverName + "Port: " + port);
		    Socket client = new Socket(serverName, port);
		    System.out.println("Connected to: " + client.getRemoteSocketAddress());
		    OutputStream outToServer = client.getOutputStream();
		    DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
		    System.out.println("Attempting to send username");
		    dataOut.writeUTF(username);
		    System.out.println("username Sent");
		    dataOut.writeUTF(password);
		    System.out.println("pass Sent");
		    dataOut.flush();
		    System.out.println("Attempting img send");
		    BufferedImage buferedimg = image.getBufferedImage();
		    ImageIO.write(buferedimg, "JPG", outToServer);
		    System.out.println("Image Sent");
		    client.close();
		    System.out.println("Connection Closed");
	    	
	    }catch(Exception e) {
	    	return -1;
	    }
	    return 0;
	}
}