package testcode;
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

import com.googlecode.javacv.CanvasFrame;

public class oldServerCode extends Thread{
   private static ServerSocket listeningSocket;
   
   public oldServerCode() throws IOException
   {
   }

   public void run()
   {
      CanvasFrame canvas = new CanvasFrame("Received Face");
      canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
      while(true)
      {
         try
         {
            System.out.println("Listening on port: " +listeningSocket.getLocalPort());
            Socket acceptSocket = listeningSocket.accept();
            System.out.println("Connected to: "+ acceptSocket.getRemoteSocketAddress());
            System.out.println("Receiving User ID");
            DataInputStream in = new DataInputStream(acceptSocket.getInputStream());
            int userID = in.readInt();
            System.out.println("Attempting retreival of photo");
            BufferedImage image = ImageIO.read(in);
            System.out.println("Received Image");
            try {
            	System.out.println("Attempting Save");
                File outputfile = new File("received.jpg");
                ImageIO.write(image, "JPG", outputfile);
                System.out.println("Successfully saved");
            } catch (IOException e) {}
            acceptSocket.close();
			canvas.showImage(image);
			System.out.println("ID: " + userID);
			//Create new thread calling facial recognition.
            
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e){break;}
      }
   }
   public static void main(String [] args)
   {
      int clientport = 8080;
      try
      {
    	 listeningSocket = new ServerSocket(clientport);
         listeningSocket.setSoTimeout(0);
         Thread t = new oldServerCode();
         t.start();
         /*
          * Change so that two listening ports are opened here, and a new thread
          * is started everytime something is accepted.
          * 
          * client thread:
          * does as above then passes id num and picture to recog.
          * Once recoged, how does it know phone ip? in DB or just variable
          * 
          * 
          * phone thread:
          * listens and once accepted it needs to receive id and command
          * delete, rename, refresh IP, 
          * 
          */
         
      }catch(IOException e){}
   }
}
