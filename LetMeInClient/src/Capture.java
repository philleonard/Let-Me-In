import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FFmpegFrameGrabber;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
/**
 * @author Philip Leonard
 */
public class Capture implements Runnable {
    
    FrameGrabber grabber;
    ClientHome clientHome;
    
    public Capture(ClientHome clientHome, FrameGrabber grab) {
		this.grabber = grab;
		this.clientHome = clientHome;
	}

	public void run() {
        try {
            IplImage image, newImage;
            System.out.println("FaceRecogModule: Gathering face images");
            	newImage = null;
                image = grabber.grab();
                clientHome.resetFaces();
                if (image != null) {
                	CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad("res/haarcascade_frontalface_default.xml"));
    				CvMemStorage storage = CvMemStorage.create();
    				CvSeq sign = cvHaarDetectObjects(image, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
    				cvClearMemStorage(storage);
    				
    				int numberFaces = sign.total();
    		
    				for (int x = 0; x < numberFaces; x++) {
    					CvRect r = new CvRect(cvGetSeqElem(sign, x));
    					cvSetImageROI(image, r);
    					newImage = cvCreateImage(cvGetSize(image), image.depth(), image.nChannels());
    					cvCopy(image, newImage);
    					cvResetImageROI(image);
    					//display image on frame
    					BufferedImage face = newImage.getBufferedImage();
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
    					
    					SendAndRecv sendImage = new SendAndRecv();
              			sendImage.sendToServ(newImage, clientHome.uname);
    				}
                    
                }
        
        } catch (Exception e) {
        	System.out.println("FaceRecogModule: Capture failed");
        }
    
    }
   
}