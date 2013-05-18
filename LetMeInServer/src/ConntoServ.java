import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import java.io.Serializable;

/**
 * 
 * @author James
 *
 */
//Lines of code that end with // will be deleted in the final version, these are for test purposes

public class ConntoServ implements Runnable, Serializable{
	private Socket acceptSocket = new Socket();


	public ConntoServ(Socket s) throws IOException
	{
		acceptSocket = s;
	}

	public void run()
	{
		
		try
		{
			int result = 1;
			String username;
			String password;
			System.out.println("-------------------------------------------------------");
			System.out.println("Connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
			DataInputStream in = new DataInputStream(acceptSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(acceptSocket.getOutputStream());
			int identifier = in.readInt();
			System.out.println("Identifier 1: " + identifier);
			//first identifier indetifies whether it is phone or client; client = 0, phone = 1.
			//Second identifier indentifies whether it is a login request or signup; login = 0, signup = 1;
			if (identifier == 0) { 
				identifier = in.readInt();
				System.out.println("Identifier 2: " + identifier);
				if (identifier == 0) {
					System.out.println("Receiving Username and password");
					username = in.readUTF();
					password = in.readUTF();
					String clientIP = acceptSocket.getInetAddress().getHostAddress();
					System.out.println("Username: " + username + "\nPassword: " + password + "\nIP: " + clientIP);
					//if ip is not used again, remove variable and just put ^^ in method below
					result = SQLConnections.loginAuthentication(username, password, clientIP, 0);
					//sends 0 if correct, 1 if no username, 2 if password wrong
					out.writeInt(result);
				}
				else {
					System.out.println("Receiving Username and password and Email");
					username = in.readUTF();
					password = in.readUTF();
					String emailAddress = in.readUTF();
					result = SQLConnections.signUp(username, password, emailAddress);
					out.writeInt(result);
				}
			}

			else if (identifier == 1) {
				identifier = in.readInt();
				System.out.println("Identifier 2: " + identifier);
				if (identifier == 0) {

					System.out.println("Receiving Username and password for Login");
					username = in.readUTF();
					password = in.readUTF();
					String phoneIP = acceptSocket.getInetAddress().getHostAddress();
					System.out.println("Username: " + username + "\nPassword: " + password + "\nIP: " + phoneIP);
					//if ip is not used again, remove variable and just put ^^ in method below
					result = SQLConnections.loginAuthentication(username, password, phoneIP, 1);
					//sends 0 if worked, 1 if fail
					out.writeInt(result);
				}
				else if (identifier==1){
					System.out.println("Receiving Username and password and Email For Signup");
					username = in.readUTF();
					password = in.readUTF();
					String emailAddress = in.readUTF();
					result = SQLConnections.signUp(username, password, emailAddress);
					out.writeInt(result);
				}
				else if (identifier==2){
					System.out.println("Receiving Username For Photos");
					username = in.readUTF();
					System.out.println("Getting Id From Username");
					String userID = SQLConnections.getIDFromUsername(username);
					System.out.println("ID: " + userID + ", now getting the number of photos this user has");
					int noOfPhotos = SQLConnections.getNoPhotos(userID);

					PictureData[] allPicData;
					
					System.out.println("Sending Number Of Photos");
					out.writeInt(noOfPhotos);

					System.out.println("Getting the object array");
					allPicData = SQLConnections.getPhotos(userID, noOfPhotos);
					
					System.out.println("Sending " + noOfPhotos + " PictureDatas");
					
					for (int i = 0; i<noOfPhotos; i++) {
						
						CanvasFrame canvas = new CanvasFrame("Sending Face" + (i+1));//
						canvas.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);//
						String name = allPicData[i].name;
						String defact = allPicData[i].defaultaction;
						String group = allPicData[i].group;
						BufferedImage img = allPicData[i].img;
						canvas.showImage(img);
						
						

						System.out.println("Sending Name: " + name);
						out.writeUTF(name);
						System.out.println("Sending DefAct: " + defact);
						out.writeUTF(defact);
						System.out.println("Sending Group: " + group);
						out.writeUTF(group);
						System.out.println("Sending Image");
						ImageIO.write(img,"JPG", out);
						out.flush();
						System.out.println("PictureData " + (i+1) + " all sent");
					}
					
				}

			}



			else {
				System.out.println("ERROR with identifier");
			}


			/*while (result==0) {
     		//stuff to do then send exit signal
	     	System.out.println("Attempting retreival of photo");
	        BufferedImage image = ImageIO.read(in);
	        System.out.println("Received Image");
	        try {//
	        	System.out.println("Attempting Save");//
	            File outputfile = new File("received.jpg");//
	            ImageIO.write(image, "JPG", outputfile);//
	            System.out.println("Successfully saved");//
	        } catch (IOException e) {}//
			//canvas.showImage(image);//

	        IplImage comparisonImage = IplImage.createFrom(image);
			username = "false";
	        String userID = SQLConnections.getIDFromUsername(username);

			int picID = FacialRecognition.recognise(userID, comparisonImage);

			if (picID < 0) {
				//not found
			}
			else {
				String picName = SQLConnections.getPicNameFromId(picID);
			}*/



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
			//Once recoged, how does it know phone ip? in DB or just variable*/
			//}
			 

		}catch(SocketTimeoutException s)
		{
			System.out.println("Socket timed out!");
			try {
				acceptSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(IOException e){}
	}
}
