import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
/**
 * @author Philip Leonard
 */

public class MyDoor implements Runnable {
	
	FrameGrabber grab = null;
	public MyDoor(FrameGrabber grab) {
		this.grab = grab;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		int port = 8100;
		
		System.out.println("MyDoorModule: Starting server socket");
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("MyDoorModule: Failed to listen on port " + port);
			return;
		}
		
		Socket clientSocket = null;
		while (true) {
			System.out.println("MyDoorModule: Waiting for android connection..");
			try {
				clientSocket = serverSocket.accept();
				System.out.println("MyDoorModule: Connection to android device successfull!");
				Thread si = new Thread(new sendImage(clientSocket, grab));
				si.start();		
			} catch (IOException e) {}
		}

	}

}

class sendImage implements Runnable {
	
	Socket clientSocket = null;
	private FrameGrabber doorGrab;
	public sendImage(Socket clientSocket, FrameGrabber grab) {
		this.clientSocket = clientSocket;
		this.doorGrab = grab;
	}

	@Override
	public void run() {

		IplImage doorView = null;
		try {
			doorView = doorGrab.grab();
			System.out.println("MyDoorModule: Webcam photo taken");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("MyDoorModule: Failed to get photo");
			return;
		}
		
		OutputStream toAndroidDev = null;
		try {
			toAndroidDev = clientSocket.getOutputStream();
		} catch (IOException e) {
			System.out.println("MyDoorModule: Failed to getOutputStream");
			return;
		}
		
		BufferedImage buferedimg = doorView.getBufferedImage();
		
		try {
			ImageIO.write(buferedimg, "JPG", toAndroidDev);
			System.out.println("MyDoorModule: Sent webcam photo successfully");
		} catch (IOException e) {
			System.out.println("MyDoorModule: Failed To send to android device");
			return;
		}
		
		try {
			clientSocket.close();
			System.out.println("MyDoorModule: Connection to android device closed");
		} catch (IOException e) {
			System.out.println("MyDoorModule: Failed to close connection to android device cleanly");
			return;
		}
		
	}
	
}
