import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import static com.googlecode.javacv.cpp.opencv_contrib.*;

import java.io.File;
import java.io.FilenameFilter;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

/**
 * 
 * @author James
 * 
 * This class takes the users id and the image they want to identify
 * and returns the photo id of the matching person. This class is called from the 
 * ServerConnections class.
 *	
 */
public class FacialRecognition {
    public static void main(String[] args) {
    	//recognise("C:\\Users\\James\\Desktop\\Folder\\training", cvLoadImage("C:\\Users\\James\\Desktop\\Folder\\james.png"));
    	
    	CanvasFrame canvas = new CanvasFrame("VideoCanvas"); 
    	   
    	  //Set Canvas frame to close on exit
    	     canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);   
    	      
    	     //Declare FrameGrabber to import video from "video.mp4"
    	     FrameGrabber grabber = new VideoInputFrameGrabber(0);    	      
    	      
    	     try {      
    	      //Start grabber to capture video
    	      grabber.start();      
    	   
    	      //Declare img as IplImage
    	      IplImage img;
    	       Thread.sleep(4000);
    	       boolean bob = true;
    	   while (bob){
    	        
    	       //inser grabed video fram to IplImage img
    	       img = grabber.grab();
    	        
    	       
    	       
    	       CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad("res/haarcascade_frontalface_default.xml"));
    			CvMemStorage storage = CvMemStorage.create();
    			CvSeq sign = cvHaarDetectObjects(img, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
    			cvClearMemStorage(storage);
    			if (sign.total() == 0){
    				System.out.println("No Face");
    				canvas.setCanvasSize(img.width(), img.height()); 
    				canvas.showImage(img);    
    			}
    			else {
    				IplImage newImage;
    				int biggest = 0;
    				int biggestSize =0;
    				CvRect r;
    				
    				for (int i = 0; i < sign.total(); i++){
    					r = new CvRect(cvGetSeqElem(sign, i));
    					
    					
						if (r.height() > biggestSize){
    						biggest = i;
    						biggestSize = r.height();
    					}
    		
    				}
    				cvResetImageROI(img);
    				
    				
    				r = new CvRect(cvGetSeqElem(sign, biggest));
    				cvSetImageROI(img, r);
    				newImage = cvCreateImage(cvGetSize(img), img.depth(), img.nChannels());
    				cvCopy(img, newImage);
    				bob = false;
    			
    			 
    				
    				newImage = IplImage.createFrom(NewFaces.resizeFace(newImage.getBufferedImage())); 
					canvas.showImage(newImage);
    				recognise("1", newImage);
    			}
    	       
    	       
    	       
    	       
    	       
    	       
    	       
    	       
    	       
    	      
    	       }
    	      }
    	     catch (Exception e) {  
    	    	 e.printStackTrace();
    	     }
    	
    	
    	
    	
    	
    	//recognise("1", testImage);
    	
    }
    
    public static int recognise(String userID, IplImage testImage){
        String trainingDir = "Photos/" + userID + "/"; //Where the photos to be compared with are stored
        CanvasFrame canvas = new CanvasFrame("Face To Recognise");//
        canvas.showImage(testImage);//
        
        File root = new File(trainingDir); //Creates a File Object of where the photos are stored
        
        //This filter is used so only the .bmp images are selected
        FilenameFilter pngFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
            	return name.toLowerCase().endsWith(".bmp");
            }
        };
        
        //This selects all the photo files
        File[] imageFiles = root.listFiles(pngFilter);

        MatVector images = new MatVector(imageFiles.length);

        
        int[] labels = new int[imageFiles.length];

        int counter = 0;
        int label;

        IplImage img;
        IplImage grayImg;

        for (File image : imageFiles) {
            img = cvLoadImage(image.getAbsolutePath());
            
            label = Integer.parseInt(image.getName().split("\\.")[0]);

            grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);

            cvCvtColor(img, grayImg, CV_BGR2GRAY);

            images.put(counter, grayImg);

            labels[counter] = label;

            counter++;
        }

        IplImage greyTestImage = IplImage.create(testImage.width(), testImage.height(), IPL_DEPTH_8U, 1);
        
        Double THRESHHOLD = 150d;
        
        //Alternate facerecognition algorithms
        //FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        // FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        // FaceRecognizer faceRecognizer = createEigenFaceRecognizer(0, THRESHHOLD);
         FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();

        faceRecognizer.train(images, labels);

        cvCvtColor(testImage, greyTestImage, CV_BGR2GRAY);

        int predictedLabel = faceRecognizer.predict(greyTestImage);

        final int[] prediction = new int[1];
        final double[] confidence = new double[1];
        faceRecognizer.predict(greyTestImage, prediction, confidence);
        int name = 0;
        if (prediction[0] >= 0) {
            name = prediction[0];
            System.out.println("Rough Name: " + name );
            double confidence1 = 100*(THRESHHOLD - confidence[0])/THRESHHOLD;
            double confidence2 = confidence[0];
            //if ((confidence2-confidence1)>50)
            if(confidence1<0){
            	//name = 0;
            }
            System.out.println("\nName: " + name + "\nConfidence: " + confidence1 + "\nConfidence 2: " + confidence2 + "\nConfidence 3: " + confidence2 + "/" + THRESHHOLD);
          }

        System.out.println("Predicted Picture ID: " + predictedLabel);
        return name;
    }
}