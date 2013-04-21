import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
public class serverEg extends Thread{
   private ServerSocket serverSocket;
   
   public serverEg(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(100000);
   }

   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to "+ server.getRemoteSocketAddress());
            System.out.println("Attempting retreival");
            DataInputStream in = new DataInputStream(server.getInputStream());
            BufferedImage image = ImageIO.read(in);
            
            try {
                // retrieve image
                File outputfile = new File("received.jpg");
                ImageIO.write(image, "jpg", outputfile);
            } catch (IOException e) {
                
            }
            
            
            /*System.out.println(in.readUTF());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to "+ server.getLocalSocketAddress() + "\nGoodbye!");
            */
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
   //public static void startRunning()
   {
      int port = 8080;//Integer.parseInt(args[0]);
      try
      {
         Thread t = new serverEg(port);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
