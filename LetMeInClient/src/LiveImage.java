import java.awt.image.BufferedImage;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
/**
 * @author Philip Leonard
 */

public class LiveImage implements Runnable {

	/*
	 * LiveImage thread takes image from webcam input and sets it on the GUI every 0.1 seconds
	 */
	
	static ClientHome clientHome;
	static FrameGrabber grab;
	
	public LiveImage(ClientHome clientHome, FrameGrabber grab) {
		this.clientHome = clientHome;
		this.grab = grab;
	}
	
	@Override
	public void run() {
		while(true) {
			IplImage faceImage = null;
			try {
				faceImage = grab.grab();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			BufferedImage clientLive = faceImage.getBufferedImage();
			clientHome.setImage(clientLive);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
