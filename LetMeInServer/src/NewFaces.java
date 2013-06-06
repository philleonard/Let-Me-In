import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvResetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

/**
 * 
 * @author James
 *
 * This class is used when a new face is to be added to the database. This class detects the face and resizes
 * the image so that all images stored are of the same size.
 * 
 */

public class NewFaces {

	//This method takes in a buffered image, resizes it to 200 x 200 pixels and returns it
	public static BufferedImage resizeFace(BufferedImage newPhoto) {
		
		BufferedImage resizedImage = new BufferedImage(200, 200, newPhoto.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(newPhoto, 0, 0, 200, 200, null);
		g.dispose();
	 
		return resizedImage;
	}
	
	//This method takes an image, detects the biggest face in the image and just returns the face
	public static BufferedImage detectFace(BufferedImage newPhoto) {
		IplImage faceImage = IplImage.createFrom(newPhoto);
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad("res/haarcascade_frontalface_default.xml"));
		CvMemStorage storage = CvMemStorage.create();
		CvSeq sign = cvHaarDetectObjects(faceImage, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);
		
		//if not faces detected, returns null.
		if (sign.total() == 0){
			newPhoto = null;
			System.out.println("No Face");
		}
		
		else {
			IplImage newImage; //IplImage used to temporarily hold the face
			int biggest = 0; //location of biggest face
			int biggestSize = 0; //height of biggest face
			CvRect r;
			for (int i = 0; i < sign.total(); i++){
				r = new CvRect(cvGetSeqElem(sign, i));
				
				if (r.height() > biggestSize){
					biggest = i;
					biggestSize = r.height();
				}
	
			}
			cvResetImageROI(faceImage);
			r = new CvRect(cvGetSeqElem(sign, biggest));
			cvSetImageROI(faceImage, r);
			//sets size of newImage to same as face
			newImage = cvCreateImage(cvGetSize(faceImage), faceImage.depth(), faceImage.nChannels());
			//Copies the face into newImage
			cvCopy(faceImage, newImage);
			cvResetImageROI(faceImage);
			//Converts back to BufferedImage
			newPhoto = newImage.getBufferedImage();
		}
		//If null = no faces detected
		return newPhoto;
	}
	
}
