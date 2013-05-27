import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;


public class PhotoSender implements Runnable{
	private Socket acceptSocket = null;
	private PictureData pictureData = null;
	
	public PhotoSender(Socket s, PictureData picData) throws IOException
	{
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
}
