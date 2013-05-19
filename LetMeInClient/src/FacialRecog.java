import java.awt.image.BufferedImage;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
/**
 * @author Philip Leonard
 */

public class FacialRecog implements Runnable {
	
	static FrameGrabber grab = new VideoInputFrameGrabber(0);
	static ClientHome clientHome;
	
	public FacialRecog(ClientHome clientHome) {
		this.clientHome = clientHome;
	}

	@Override
	public void run(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			grab.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Thread for sending photos of whole webcam image to the phone
		Thread li = new Thread(new LiveImage(clientHome, grab));
		li.start();
		
		Thread md = new Thread(new MyDoor(grab));
		md.start();
		
		while (true) {
			if (listen()) {
				Thread cap = new Thread(new Capture(clientHome, grab));
				cap.start();
				try {
					cap.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Wait for capture to return (i.e. to send images)
				
				Thread res = new Thread(new Result());
				res.start();
				try {
					res.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//Then start result thread and wait for the server to return a result
				
			} 
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		
		
	}
	
	public static boolean listen() {
		try {
			IplImage faceImage;
			faceImage = grab.grab();
			if (faceImage != null) {
				
				CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad("res/haarcascade_frontalface_default.xml"));
				CvMemStorage storage = CvMemStorage.create();
				CvSeq sign = cvHaarDetectObjects(faceImage, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
				cvClearMemStorage(storage);
				
				int faces = sign.total();
				
				if (faces >= 1) {
					if (faces == 1)
						System.out.println("FaceRecogModule: " + faces + " face detected");
					else 
						System.out.println("FaceRecogModule: " + faces + " faces detected");
					return true;
				}
				else
					return false;
			}
		} catch (Exception e) {
			System.out.println("FaceRecogModule: Listen failed");
		}	
		return false;
	}

	

}
