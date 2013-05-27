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



public class NewFaces {

	public static BufferedImage resizeFace(BufferedImage newPhoto) {
		
		BufferedImage resizedImage = new BufferedImage(200, 200, newPhoto.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(newPhoto, 0, 0, 200, 200, null);
		g.dispose();
	 
		return resizedImage;
	}
	
	public static BufferedImage detectFace(BufferedImage newPhoto) {
		IplImage faceImage = IplImage.createFrom(newPhoto);
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad("res/haarcascade_frontalface_default.xml"));
		CvMemStorage storage = CvMemStorage.create();
		CvSeq sign = cvHaarDetectObjects(faceImage, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
		cvClearMemStorage(storage);
		if (sign.total() == 0){
			newPhoto = null;
			System.out.println("No Face");
		}
		else {
			IplImage newImage;
			int biggest = 0;
			int biggestSize = 0;
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
			newImage = cvCreateImage(cvGetSize(faceImage), faceImage.depth(), faceImage.nChannels());
			cvCopy(faceImage, newImage);
			cvResetImageROI(faceImage);
			newPhoto = newImage.getBufferedImage();
		}
		//If null = no faces detected
		return newPhoto;
	}
	
	public static void main(String[] args){
		try {
			detectFace(ImageIO.read(new File("test.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
