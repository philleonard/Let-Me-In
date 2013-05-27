import com.googlecode.javacv.cpp.opencv_core.IplImage;
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * @author James
 */
public class SendAndRecv {
	public int sendToServ (Object[] imageArray, String username) {
		//Send image to server, return 0 if success, -1 else
		String serverName = "192.168.100.6";
	    int port = 55555;
	    try {
	    	    	
		    System.out.println("Connecting to: " + serverName + "Port: " + port);
		    Socket client = new Socket(serverName, port);
		    System.out.println("Connected to: " + client.getRemoteSocketAddress());
		    OutputStream outToServer = client.getOutputStream();
		    DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
		    dataOut.writeInt(0);
		    dataOut.writeInt(2);
		    System.out.println("Attempting to send username");
		    dataOut.writeUTF(username);
		    System.out.println("username Sent");
		    dataOut.flush();
		    System.out.println("Attempting img send");
		    
		    dataOut.writeInt(imageArray.length);
		    for (int f = 0; f < imageArray.length; f++) {
		    	BufferedImage image = ((IplImage) imageArray[f]).getBufferedImage();
			    ImageIO.write(image, "JPG", outToServer);
		    }
		    
		    System.out.println("Image(s) Sent");
		    client.close();
		    System.out.println("Connection Closed");
	    	
	    }catch(Exception e) {
	    	return -1;
	    }
	    return 0;
	}
}