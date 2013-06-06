import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author James
 * This  class is used to send all the photos and photo data to the mobile app.
 * This class is called from inside the ServerConnections class and is sent the photo
 * data to be sent to the mobile app. The phone then makes multiple connections to the
 * listening socket and is sent 1 photo and its data each connection until it has 
 * received all its photos.
 * 
 */

public class PhotoSenderListener implements Runnable{
	private PictureData[] allPicData = null;
	private int picDataLength = 0;
	private Socket[] theSockets = null;
	
	public PhotoSenderListener(PictureData[] picData, int length){
		allPicData = picData;
		picDataLength = length;
		theSockets = new Socket[length];
	}

	public void run(){
		int clientport = 55556;
		boolean succeed = false;
		
		//This variable is used incase an exception is thrown, so that it can carry on from the same point
		int count = 0; 
		while(!succeed){
			try {
				ServerSocket listeningSocket = new ServerSocket(clientport);
				listeningSocket.setSoTimeout(0);
				succeed = true;
				System.out.println("Listening on " + listeningSocket.getLocalPort());
				for (int i = count; i<picDataLength; i++){
					System.out.println("On loop " + i);
					//theSockets[i] = new Socket();
					theSockets[i] = listeningSocket.accept();
					System.out.println("Starting thread for photo " + (i+1));
					Thread u = new Thread(new PhotoSender(theSockets[i], allPicData[i]));
					u.start();
					count = i;
		        }
				listeningSocket.close();
				System.out.println("Photo Sending Finished");
				System.out.println("--------------------------END--------------------------");
			}catch(IOException e){
				succeed=false;
				e.printStackTrace();
			}
		}
	}
}
