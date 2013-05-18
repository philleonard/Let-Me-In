import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_contrib.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FacialRecognition {
    public static void main(String[] args) {
    	//recognise("C:\\Users\\James\\Desktop\\Folder\\training", cvLoadImage("C:\\Users\\James\\Desktop\\Folder\\james.png"));
    	
    	BufferedImage bufimg = null;
    	IplImage testImage = null;
		try {
			bufimg = ImageIO.read(new File("Photos/james.png"));
	        testImage = IplImage.createFrom(bufimg);

		} catch (IOException e) {
			e.printStackTrace();
		}   
    	
    	recognise("test", testImage);
    	
    }
    
    public static int recognise(String userID, IplImage comparisonImage){
    	//training dir can just be the dir where a users photos are stored
        String trainingDir = "Photos/" + userID + "/"; //need location +userid     
        IplImage testImage = comparisonImage;

        File root = new File(trainingDir);
        
        FilenameFilter pngFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(pngFilter);

        MatVector images = new MatVector(imageFiles.length);

        
        int[] labels = new int[imageFiles.length];

        int counter = 0;
        int label;

        IplImage img;
        IplImage grayImg;

        for (File image : imageFiles) {
            img = cvLoadImage(image.getAbsolutePath());
            
            //for format 1.png
            label = Integer.parseInt(image.getName().split("\\.")[0]);
            
            //for format 1-james.png
            //label = Integer.parseInt(image.getName().split("\\-")[0]);

            grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);

            cvCvtColor(img, grayImg, CV_BGR2GRAY);

            images.put(counter, grayImg);

            labels[counter] = label;

            counter++;
        }

        IplImage greyTestImage = IplImage.create(testImage.width(), testImage.height(), IPL_DEPTH_8U, 1);

        Double THRESHHOLD = 150d;
        
        //FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
         //FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        // FaceRecognizer faceRecognizer = createEigenFaceRecognizer(0, THRESHHOLD);
         FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();

        faceRecognizer.train(images, labels);

        cvCvtColor(testImage, greyTestImage, CV_BGR2GRAY);

        int predictedLabel = faceRecognizer.predict(greyTestImage);

        final int[] prediction = new int[1];
        final double[] confidence = new double[1];
        faceRecognizer.predict(greyTestImage, prediction, confidence);
        if (prediction[0] >= 0) {
            int name = prediction[0];
            double confidence1 = 100*(THRESHHOLD - confidence[0])/THRESHHOLD;
            double confidence2 = confidence[0];
            System.out.println("Name: " + name + "\nConfidence: " + confidence1 + "\nConfidence 2: " + confidence2);
          }

        System.out.println("Predicted Picture ID: " + predictedLabel);
        return predictedLabel;
    }
}