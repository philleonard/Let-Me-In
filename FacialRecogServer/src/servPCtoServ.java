import java.net.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

import com.googlecode.javacv.CanvasFrame;

//Lines of code that end with // will be deleted in the final version, these are for test purposes

public class servPCtoServ implements Runnable{
   private Socket acceptSocket;
   
   
   
   public servPCtoServ(Socket s) throws IOException
   {
	   acceptSocket = s;
   }

   public void run()
   {
     CanvasFrame canvas = new CanvasFrame("Received Face");//
     canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);//
     try
     {
        System.out.println("Connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
        System.out.println("Receiving User ID");
        DataInputStream in = new DataInputStream(acceptSocket.getInputStream());
        int userID = in.readInt();
        System.out.println("Attempting retreival of photo");
        BufferedImage image = ImageIO.read(in);
        System.out.println("Received Image");
        try {//
        	System.out.println("Attempting Save");//
            File outputfile = new File("received.jpg");//
            ImageIO.write(image, "JPG", outputfile);//
            System.out.println("Successfully saved");//
        } catch (IOException e) {}//
        acceptSocket.close();
		canvas.showImage(image);//
		System.out.println("ID: " + userID);
		
		BufferedImage grayimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = grayimage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose(); 
		
		//c:\(userid)\(photoid).pgm
		//String[] array = sqlConnections.getPicsAndIDs(userid); //this stores the location of the pictures
		//int recognised = faceRec(array);
		
		
		//String picid = facerecognition(image, userid);
		
		
		/**
		 * if (recognised == -1) {
		 * 		Send photo to phone and say that it is at the door.
		 * 		//this will be in new class and at the end the facerec needs to be retrained
		 * }
		 * else {
		 *		search db for that photoid then send to phone
		 *		//this will be in new class
		 * }
		 *
		 */
		//Once recoged, how does it know phone ip? in DB or just variable
        
     }catch(SocketTimeoutException s)
     {
        System.out.println("Socket timed out!");
     }catch(IOException e){}
   }
}
