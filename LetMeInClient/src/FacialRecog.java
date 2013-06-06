import java.awt.image.BufferedImage;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
/**
 * @author Philip Leonard
 */

public class FacialRecog implements Runnable {
	
	/*
	 * FacialRecog thread, handles recognising faces and handles the webcam device.
	 * When a face is recognised, the photos of the faces are sent to the server
	 */
	
	static FrameGrabber grab = new VideoInputFrameGrabber(0);
	static ClientHome clientHome;
	
	public FacialRecog(ClientHome clientHome) {
		this.clientHome = clientHome;
	}

	@Override
	public void run(){
		clientHome.getConsole().append(clientHome.console() + "Setting up camera device\n");
		
		clientHome.getConsole().append(clientHome.console() + "Starting frame grabber\n");
		try {
			grab.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Thread for refreshing webcam image on client GUI
		Thread li = new Thread(new LiveImage(clientHome, grab));
		li.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		clientHome.getConsole().append(clientHome.console() + "Starting face detection\n");
		
		//Thread for sending photos of whole webcam image to the phone
		Thread md = new Thread(new MyDoor(grab, clientHome));
		md.start();
		
		while (true) {
			if (listen()) {
				clientHome.getConsole().append(clientHome.console() + "Face(s) detected...\n");
				
				//If a face is detected then the face images are sent tp the server in listen class, after which a response is expected
				Thread res = new Thread(new Result(clientHome));
				res.start();
				try {
					res.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			} 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static boolean listen() {
		try {
			
			IplImage image, newImage;
			image = grab.grab();
			if (image != null) {
				
				//Detecting number of faces present on the image
				CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad("res/haarcascade_frontalface_default.xml"));
				CvMemStorage storage = CvMemStorage.create();
				CvSeq sign = cvHaarDetectObjects(image, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
				cvClearMemStorage(storage);
				
				int numberFaces = sign.total();
				
				if (numberFaces >= 1) {
					clientHome.getConsole().append(clientHome.console() + numberFaces + " faces detected \n");
    				
    				Object[] imageArray = new Object[numberFaces];
    				for (int x = 0; x < numberFaces; x++) {
    					//Get all faces from the image
    					
    					CvRect r = new CvRect(cvGetSeqElem(sign, x));
    					cvSetImageROI(image, r);
    					newImage = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
    					cvCopy(image, newImage);
    					cvResetImageROI(image);
    					//display image on frame
    					BufferedImage face = newImage.getBufferedImage();
    					
    					//Detected faces are displayed on the GUI
    					if (x == 0)
    						clientHome.setFace1(face);
    					if (x == 1)
    						clientHome.setFace2(face);
    					if (x == 2)
    						clientHome.setFace3(face);
    					if (x == 3)
    						clientHome.setFace4(face);
    					if (x == 4)
    						clientHome.setFace5(face);
    					if (x == 5)
    						clientHome.setFace6(face);
    					
    					imageArray[x] = newImage;
    					
    				}
    				
    				clientHome.getConsole().append(clientHome.console() + "Sending image data to server for recognition...\n");
    				
    				//Sending images to the server from another thread.
    				SendAndRecv sendImage = new SendAndRecv();
          			sendImage.sendToServ(imageArray, clientHome.uname);
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
