import com.googlecode.javacv.cpp.opencv_core.IplImage;
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * @author James, Philip
 */
public class SendAndRecv {
	
	/*
	 * This class is for sending the images of detected faces to the server
	 * in order to identify who they are. 
	 */
	public int sendToServ (Object[] imageArray, String username) {
		//Send image to server, return 0 if success, -1 else
		String serverName = "192.168.1.178";
	    int port = 55555;
	    try {
	    	    	
		    System.out.println("Connecting to: " + serverName + "Port: " + port);
		    Socket client = new Socket(serverName, port);
		    System.out.println("Connected to: " + client.getRemoteSocketAddress());
		    OutputStream outToServer = client.getOutputStream();
		    DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
		    dataOut.writeInt(0);
		    dataOut.writeInt(8);
		    System.out.println("Attempting to send username");
		    dataOut.writeUTF(username);
		    System.out.println("username Sent");
		    dataOut.flush();
		    System.out.println("Attempting img send");
		    
		    //Send images as byte arrays
		    dataOut.writeInt(imageArray.length);
		    for (int f = 0; f < imageArray.length; f++) {
		    	ByteArrayOutputStream byteArrayO = new ByteArrayOutputStream();
		    	BufferedImage image = ((IplImage) imageArray[f]).getBufferedImage();
		    	ImageIO.write(image,"PNG",byteArrayO);
		    	byte [] byteArray = byteArrayO.toByteArray();
		    	dataOut.writeInt(byteArray.length);
		    	dataOut.write(byteArray);
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