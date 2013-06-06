import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

/**
 * 
 * @author James
 *
 *This method is called from the PhotoSenderListener class and the class just sends the photoData
 *it is given, down the socket it is given. This class is made to be instantiated multiple times
 *for multiple images.
 *
 *This method of sending the pictures is being used as when sending buffered images from PC
 * to android, problems occur when trying to read or write to a socket after the image and the data
 * corrupting, so this we the socket is used to send the data then the image then closed.
 */

public class PhotoSender implements Runnable{
	private Socket acceptSocket = null;
	private PictureData pictureData = null;
	
	public PhotoSender(Socket s, PictureData picData) throws IOException{
		
		acceptSocket = s;
		acceptSocket.setKeepAlive(true);
		pictureData = picData;
		
	}
	
	public void run(){
		DataOutputStream out;
		try {
			
			out = new DataOutputStream(acceptSocket.getOutputStream());
			System.out.println("Sending Name: " + pictureData.name);
			out.writeUTF(pictureData.name);
			System.out.println("Sending DefAct: " + pictureData.defaultaction);
			out.writeUTF(pictureData.defaultaction);
			System.out.println("Sending Group: " + pictureData.group);
			out.writeUTF(pictureData.group);
			System.out.println("Sending Image");
			ImageIO.write(pictureData.img,"PNG", out);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
