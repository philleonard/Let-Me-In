import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import com.googlecode.javacv.cpp.opencv_core.IplImage;
import java.io.Serializable;

/**
 * 
 * @author James
 * @description This class is the main class that is used. The class is instantiated and threaded
 * from the Listening class. This class is used to communicate with the Phone App and PC Client
 * and calls all of the other classes for the server (except TheServer and ListeningClass).
 *
 */

public class ServerConnections implements Runnable, Serializable{

	//This is the socket that accepts the connection from the listening class
	private Socket acceptSocket = new Socket();
	private DataInputStream in;
	private DataOutputStream out;
	private String username;
	private String password;
	private String userID;
	
	private int LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = 1;
	
	//This is the port that the Phone app listens on
	private final int PHONE_LISTENING_PORT = 55557;
	
	/**
	These numbers are used to identify what type of connection it is and are sent first before any data
	The numbers are normally sent as first CLIENT or PHONE, then the requested function.
	
	identifier = 0 Distinguishes that it is the client connecting
	identifier = 1 Distinguishes that it is the phone connecting
	
	Commands:
	command = 0 	Login Request
	command = 1		Signup request
	command = 2		Sends the list of photos and photo data
	command = 3		Adds new photo to users account
	command = 4		Deletes a photo from their account
	command = 5		Changes photo data for their account
	command = 6		Signs out from the phone app
	command = 7		Sends response to photo sent earlier
	command = 8		Pictures to be sent for facial recognition

	
	
	This is a response sent back to the user and changes depending on success or error:
	For Login: 	 0 = Correct
				 1 = User doesnt exist
				 2 = Password Incorrect
				 
	For Sign Up: 0 = Correct
				 1 = Username exists
				 2 = Email exists
				 3 = Username and Email exists
	 
	 */
	
	
	
	/*This is the constructor or the class and gets passed the accepting socket
	of the listening class and then makes the socket keep alive
	*/
	public ServerConnections(Socket s) throws IOException {
		acceptSocket = s;
		acceptSocket.setKeepAlive(true);
		in = new DataInputStream(acceptSocket.getInputStream());
		out = new DataOutputStream(acceptSocket.getOutputStream());
	}

	/*
	This is the method that is run when the thread is created. This method is used to
	read in the identifier and command then calls the appropiate method from there.
	*/
	public void run() {
		
		try {
			//TODO do we need to send identifer for everything or just login?
			System.out.println("-------------------------START-------------------------");
			System.out.println("Connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
			int identifier = in.readInt();
			int command = in.readInt();
			switch (command) {
				case 0: login(identifier);
						break;
				case 1:	signUp();
						break;
				case 2:	readList();
						break;
				case 3:	addNewPhoto();
						break;
				case 4:	deletePhoto();
						break;
				case 5:	changePhoto();
						break;
				case 6:	signOut();
						break;
				case 7:	phonesResponse();
						break;
				case 8:	faceRecognition();
						break;
			}
			acceptSocket.close();
		}catch(SocketTimeoutException s)
		{
			System.out.println("Socket timed out!");
			try {
				acceptSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	/*
	This method is used for logins, it reads in the username and password from the socket and trys to
	authenticate them with the database. It then sends its response back.
	*/
	private void login(int identifier){
		if (identifier == 0)
			System.out.print("CLIENT-LOGIN\n");
		else 
			System.out.print("PHONE-LOGIN\n");
		System.out.println("Receiving Username and password");
		try {
			username = in.readUTF();
			password = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Username: " + username + "\nPassword: " + password + "\nIP: " + acceptSocket.getInetAddress().getHostAddress());
		LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = SQLConnections.loginAuthentication(username, password, acceptSocket.getInetAddress().getHostAddress(), identifier);
		
		
		try {
			out.writeInt(LOGIN_SIGNUP_AUTHENTICATION_RESPONSE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------------END--------------------------");
	}
	
	/*
	This method is used to sign up for a new account. It reads the username, password and email from the socket
	and passes them to the signUp method in the SQLConnections class. This method then returns the response from
	signup attempt.
	*/
	private void signUp(){
		String emailAddress = null;
		System.out.print("-SIGN_UP\n");
		System.out.println("Receiving Username and Password and Email");
		try {
			username = in.readUTF();
			password = in.readUTF();
			emailAddress = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = SQLConnections.signUp(username, password, emailAddress);
		
		try {
			out.writeInt(LOGIN_SIGNUP_AUTHENTICATION_RESPONSE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------------END--------------------------");
	}
	
	
	/*
	This method is called by the phone and is used to retrieve all the photos and data
	for the user so that they can appear in the list on the phone. This method then starts
	a new thread of PhotoSenderListener to send all the photos.
	This method has been used due to the problems we faced when trying to send more that one
	photo at a time from pc to android.
	*/
	private void readList(){
		System.out.print("-READ_LIST\n");
		System.out.println("Receiving Username For Photos");
		try {
			username = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Getting Id From Username");
		userID = SQLConnections.getIDFromUsername(username);
		System.out.println("ID: " + userID + ", now getting the number of photos this user has");
		int noOfPhotos = SQLConnections.getNoPhotos(userID);

		PictureData[] allPicData;
		
		System.out.println("Getting the object array");
		allPicData = SQLConnections.getPhotos(userID, noOfPhotos);
		
		System.out.println("Sending Number Of Photos");
		
		try {
			out.writeInt(noOfPhotos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread s = new Thread(new PhotoSenderListener(allPicData, noOfPhotos));
		s.start();
		System.out.println("Picture Sending Started");
	}
	
	/*
	This method is used to add a new photo. It reads in the username which it sends to the SQLConnections class to
	get the matching id. It then reads in the Picture Name, Group and Default Action. It then reads the actual photo
	in by using a bytestream. The photo is then sent to the NewFaces class to detect the face and resize it.
	It is then added to the database and a response to confirm success sent to the phone.
	*/
	private void addNewPhoto(){
		System.out.print("-ADD_NEW_PHOTO\n");
		System.out.println("Reading in Username");
		String name = null;
		String group = null;
		String defAct = null;
		try {
			username = in.readUTF();
			userID = SQLConnections.getIDFromUsername(username);
			System.out.println("Reading in Picture Name");
			name = in.readUTF();
			System.out.println("Reading in Group");
			group = in.readUTF();
			System.out.println("Reading in Default Action");
			defAct  = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Reading in Picture");
		BufferedImage newPhoto = null;
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bufSize = in.readInt();
			byte buffer[] = new byte[bufSize];
			
			int s;
			while ((s=in.read(buffer)) != -1){
				baos.write(buffer, 0, s);
			}
			byte[] byteArray = baos.toByteArray();
	
			System.out.println("Fully Received Photo");
			
			newPhoto = ImageIO.read(new ByteArrayInputStream(byteArray));

		}catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Detecting Face");
		newPhoto = NewFaces.detectFace(newPhoto);
		try {
			if (newPhoto == null){
				out.writeBoolean(false);
			}
			else {
				System.out.println("Resizing Face");
				newPhoto = NewFaces.resizeFace(newPhoto);
				System.out.println("Adding Photo");
				SQLConnections.addPicture(userID, name, defAct, group, newPhoto);
				System.out.println("Photo Added");
				out.writeBoolean(true);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("--------------------------END--------------------------");		
	}
	
	/*
	This method is used to delete a photo. It reads in the username and photo name from the socket, gets the
	userid from the database and then sends the userID and photo name to the SQLConnections class for deletion. 
	*/
	private void deletePhoto(){
		try {
			System.out.print("-DELETE_PHOTO\n");
			System.out.println("Reading in Username");
			username = in.readUTF();
			userID = SQLConnections.getIDFromUsername(username);
			System.out.println("Reading in Picture Name");
			String name = in.readUTF();
			System.out.println("Deleting " + name + " from " + username + "'s account");
			SQLConnections.removePicture(userID, name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------------END--------------------------");		
	}
	
	/*
	This method is used to change photo data. This method receives all the photo data back from the 
	phone so that the processing can be done on the server side. All of the photo data is then sent to
	the SQLConnections class to be update the database. 
	*/
	private void changePhoto(){
		System.out.print("-CHANGE_PHOTO\n");
		System.out.println("Reading in Username");
		
		try {
			username = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		userID = SQLConnections.getIDFromUsername(username);
		int noOfPhotos = SQLConnections.getNoPhotos(userID);
		PictureData[] newPicData = new PictureData[noOfPhotos];
		for (int i = 0; i<noOfPhotos; i++){
			try {
				String name = in.readUTF();
				String defact = in.readUTF();
				String group = in.readUTF();
				newPicData[i] = new PictureData(name, defact, group, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Finished reading in all photo data");
		SQLConnections.changePhotos(userID, newPicData);
		System.out.println("--------------------------END--------------------------");		
	}
	
	/*
	This method is used by the phone to signout. This prevents it from receiving notifications by
	removing the phoneIP from the database. 
	*/
	private void signOut(){
		System.out.print("-SIGN_OUT\n");
		System.out.println("Reading in Username");
		try {
			username = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		userID = SQLConnections.getIDFromUsername(username);
		SQLConnections.signOut(userID);
		System.out.println("--------------------------END--------------------------");		
	}
	
	/*
	This method is used during the facial recognition process. This is used when the pc
	needs a response from the phone. This method handles this and forwards the response to the pc. 
	*/
	private void phonesResponse(){
		System.out.print("-PHONE_RESPONSE\n");
		System.out.println("Reading in Username");
		try {
			username = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Reading in Response");
		boolean response = false;
		try {
			response = in.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
		userID = SQLConnections.getIDFromUsername(username);
		String clientIP = SQLConnections.getClientIP(userID);
		System.out.println("Connecting to: " + clientIP);
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(clientIP, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Connected");
		try {
			new DataOutputStream(clientSocket.getOutputStream()).writeBoolean(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Response Sent");
		System.out.println("--------------------------END--------------------------");		
	}
	
	
	/*
	This method handles the facial recognition. Further comments will explain each step. 
	*/
	private void faceRecognition(){
		System.out.print("-FACE_RECOGNITION\n");
		System.out.println("Receiving Username");
		int noOfPhotos = 0;
		BufferedImage[] image = null;
		InetAddress clientIP = null;
		//This reads in the photos to be recognised.
		try {
			username = in.readUTF();
			clientIP = acceptSocket.getInetAddress(); 
			System.out.println("Clients IP: " + clientIP);
			noOfPhotos = in.readInt();
			image = new BufferedImage[noOfPhotos];
			System.out.println("Attempting retreival of photos");
			for (int i=0; i<noOfPhotos; i++){
				int noBytesToRead = in.readInt();
	            byte[] byteArray = new byte[noBytesToRead];
	            int noBytesRead = 0;
	            int nBytesLeftToRead = noBytesToRead;
	            while(nBytesLeftToRead > 0){ 
	                int read =in.read(byteArray, noBytesRead, nBytesLeftToRead);
	                if(read < 0)
	                    break;
	                noBytesRead += read; // accumulate bytes read
	                nBytesLeftToRead -= read;
	            }
	            //Converting the image
	            ByteArrayInputStream byteArrayI = new ByteArrayInputStream(byteArray);
	            image[i] = ImageIO.read(byteArrayI);
	            System.out.println("received screen " + i);
			
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Retreived Photos");

		
		//This converts them to IpLs for the face recognition.
		System.out.println("Converting to IPL's");
		IplImage[] comparisonImages = new IplImage[noOfPhotos];
		for (int i=0; i<noOfPhotos; i++){
			image[i] = NewFaces.resizeFace(image[i]);
			comparisonImages[i] = IplImage.createFrom(image[i]);
		}
		System.out.println("Converted");
		
		userID = SQLConnections.getIDFromUsername(username);
		
		//This performs facial recognition on each photo.
		System.out.println("Starting Facial Recognition");
		String[] pictureIDs = new String[noOfPhotos];
		for (int i=0; i<noOfPhotos; i++){
			System.out.println("FaceRec: " + i);
			pictureIDs[i] = Integer.toString(FacialRecognition.recognise(userID, comparisonImages[i]));
		}
		
		//This gets information about each photo.
		System.out.println("Getting Photo Informations");
		PictureData[] photoData = new PictureData[noOfPhotos];
		SQLConnections.getPicInfoFromID(userID, pictureIDs, noOfPhotos, photoData);
		
		
		//This checks to see if door should just be opened or if there is another default action
		System.out.println("Checking for non-open's");
		Boolean waitForPhone = false;
		for (int i=0; i<noOfPhotos; i++){
			if(!photoData[i].defaultaction.equals("Open")){
				waitForPhone = true;
			}
		}
		
		//This reconnects to the client to tell them the response, open or wait for phone.
		DataOutputStream clientOut = null;
		Socket newClient = null;
		try {
			System.out.println("Attempting new connection to client");
			
			newClient = new Socket(clientIP, 55555);
		
			clientOut = new DataOutputStream(newClient.getOutputStream());
			System.out.println("Sending noofphotos");
			clientOut.writeInt(noOfPhotos);
			System.out.println("Telling Client open or phone");
			if (waitForPhone){
				System.out.println("Phone");
				clientOut.writeUTF("phone");
			}
			else {
				System.out.println("Open");
				clientOut.writeUTF("open");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//This gets the phone ip and tells the client if they are signed in on phone or not.
		String phoneIP = null;
		try {
			phoneIP = SQLConnections.getPhoneIP(userID);	
			if (phoneIP.equals("")) {
				clientOut.writeUTF("offline");
				System.out.println("User is logged out of phone");
			}
			else {
				System.out.println("Telling Client, phone is logged in");
				try{
				clientOut.writeUTF("online");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
		//This sends the names of the people in the picture to client to display on screen.
		try {
			System.out.println("Sending Picture Names");
			for (int i=0; i<noOfPhotos; i++){
				clientOut.writeUTF(photoData[i].name);
			}
			System.out.println("All sent");
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		//This is for if the client needs to wait for a phone response.
		if (waitForPhone) {
			//This finds the biggest face to send to the phone.
			int biggestSize = 0;
			int biggestLoc = -1;
			for (int i=0; i<noOfPhotos; i++){
				if (photoData[i].defaultaction.equals("Notify me")){
					if (image[i].getHeight()>biggestSize)
						biggestLoc = i;
				}
			}
			
			if (biggestLoc>-1){
				
				if(!phoneIP.equals("")){
					System.out.println("Persons Name is:" + photoData[biggestLoc].name);
					System.out.println("Connecting To Phone at IP: " + phoneIP);
					try{
						Thread.sleep(5000);
						Socket phoneSocket = new Socket(phoneIP, PHONE_LISTENING_PORT);
						System.out.println("Connected to Phone");
						DataOutputStream phoneOut = new DataOutputStream(phoneSocket.getOutputStream());
						System.out.println("Writing Name");
						phoneOut.writeUTF(photoData[biggestLoc].name);
						System.out.println("Writing Image");
						ImageIO.write(image[biggestLoc], "PNG", phoneOut);
						System.out.println("Complete");									
						phoneSocket.close();
					}catch(Exception e){
						try {
							String clientsip = SQLConnections.getClientIP(userID);
							System.out.println("Connecting to: " + clientsip);
							Socket clientSocket = new Socket(clientsip, 55556);
							new DataOutputStream(clientSocket.getOutputStream()).writeBoolean(false);
							System.out.println("Failed Response Sent");
							clientSocket.close();
						}catch(IOException e1){
							e.printStackTrace();
						}
					}
				}
				
			}
		}
		else{
			System.out.println("The Person At The Door Has Access");
		}
		try {
			newClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------------END--------------------------");		
	}
}
