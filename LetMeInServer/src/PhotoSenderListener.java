import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class PhotoSenderListener implements Runnable{
	private PictureData[] allPicData = null;
	private int picDataLength = 0;
	private Socket[] theSocks = null;
	
	public PhotoSenderListener(PictureData[] picData, int length){
		allPicData = picData;
		picDataLength = length;
		System.out.println(picDataLength);
		theSocks = new Socket[length];
	}

	public void run(){
		int clientport = 55556;
		try {
			ServerSocket listeningSocket = new ServerSocket(clientport);
			listeningSocket.setSoTimeout(0);
			System.out.println("Listening on " + listeningSocket.getLocalPort());
			for (int i = 0; i<picDataLength; i++){
				System.out.println("On loop " + i);
	        	 theSocks[i] = new Socket();
	        	 theSocks[i] = listeningSocket.accept();
	             //System.out.println("Connected to (tempsock): "+ tempSock.getRemoteSocketAddress());
	        	 System.out.println("Starting thread for photo " + (i+1));
	        	 Thread u = new Thread(new PhotoSender(theSocks[i], allPicData[i]));
	             u.start();
	             
	        	 
	         }
			listeningSocket.close();
			System.out.println("Photo Sending Finished");
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
