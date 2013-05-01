import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
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

public class Capture implements Runnable {
    
    FrameGrabber grabber;
    public Capture(FrameGrabber grab) {
		this.grabber = grab;
	}

	public void run() {
        int i=0;
        try {
            IplImage image, newImage;
            System.out.println("FaceRecogModule: Gathering face images");
            while (i < 4) {
            	newImage = null;
                image = grabber.grab();
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
    					
    					SendAndRecv sendImage = new SendAndRecv();
              			sendImage.sendToServ(newImage, "username", "password");
    				}
                    
                }
                Thread.sleep(500);
                i++;
            }
        
        } catch (Exception e) {
        	System.out.println("FaceRecogModule: Capture failed");
        }
    
    }
   
}