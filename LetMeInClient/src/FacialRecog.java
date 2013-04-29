import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;


public class FacialRecog {
	
	static FrameGrabber grab = new VideoInputFrameGrabber(0);
	
	public static void facialRecog() throws InterruptedException, Exception {
		//Camera takes photos constantly, if face is detected then 
		grab.start();
		
		//Thread for sending photos of whole webcam image to the phone
		Thread md = new Thread(new MyDoor(grab));
		md.start();
		
		while (true) {
			if (listen()) {
				Thread cap = new Thread(new Capture(grab));
				cap.start();
				cap.join();
				//Wait for capture to return (i.e. to send images)
				
				Thread res = new Thread(new Result());
				res.start();
				res.join();
				//Then start result thread and wait for the server to return a result
				
			}
			else
				Thread.sleep(500);
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
