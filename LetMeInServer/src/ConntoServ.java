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
	
	private final int CLIENT = 0;
	private final int LOGIN = 0;
	private final int SIGN_UP = 1;
	private final int FACE_RECOGNITION = 2;

	
	private final int PHONE = 1;
	//LOGIN and SIGN_UP are also used on the phone application
	private final int READ_LIST = 2;
	private final int ADD_NEW_PHOTO = 3;
	private final int DELETE_PHOTO = 4;
	private final int CHANGE_PHOTO = 5;
	
	private int LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = 1;
	private final int PHONE_LISTENING_PORT = 55557;
	

	public ConntoServ(Socket s) throws IOException {
		acceptSocket = s;
		acceptSocket.setKeepAlive(true);
	}

	public void run() {
		
		try {
			
			String username;
			String password;
			System.out.println("-------------------------------------------------------");
			System.out.println("Connected to (acceptSock): "+ acceptSocket.getRemoteSocketAddress());
			DataInputStream in = new DataInputStream(acceptSocket.getInputStream());
			DataOutputStream out = new DataOutputStream(acceptSocket.getOutputStream());
			int identifier = in.readInt();
			
/************************************************** CLIENT **************************************/
			if (identifier == CLIENT) {
				System.out.print("CLIENT");
				identifier = in.readInt();
				if (identifier == LOGIN) {
					System.out.print("-LOGIN\n");
					System.out.println("Receiving Username and password");
					username = in.readUTF();
					password = in.readUTF();
					String clientIP = acceptSocket.getInetAddress().getHostAddress();
					System.out.println("Username: " + username + "\nPassword: " + password + "\nIP: " + clientIP);
					//if ip is not used again, remove variable and just put ^^ in method below
					LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = SQLConnections.loginAuthentication(username, password, clientIP, 0);
					//sends 0 if correct, 1 if no username, 2 if password wrong
					out.writeInt(LOGIN_SIGNUP_AUTHENTICATION_RESPONSE);
				}
				else if(identifier == SIGN_UP){
					System.out.print("-SIGN_UP\n");
					System.out.println("Receiving Username and Password and Email");
					username = in.readUTF();
					password = in.readUTF();
					String emailAddress = in.readUTF();
					LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = SQLConnections.signUp(username, password, emailAddress);
					out.writeInt(LOGIN_SIGNUP_AUTHENTICATION_RESPONSE);
				}
				else if (identifier == FACE_RECOGNITION){
					System.out.print("-FACE_RECOGNITION\n");
					System.out.println("Receiving Username");
					username = in.readUTF();
					System.out.println("Attempting retreival of photo");
			        BufferedImage image = ImageIO.read(in);
			        System.out.println("Received Image");
			        /*try {//
			        	System.out.println("Attempting Save");//
			            File outputfile = new File("received.jpg");//
			            ImageIO.write(image, "JPG", outputfile);//
			            System.out.println("Successfully saved");//
			        } catch (IOException e) {}//*/
			        //CanvasFrame canvas = new CanvasFrame("Full");
			        
			        IplImage comparisonImage = IplImage.createFrom(image);
			        String userID = SQLConnections.getIDFromUsername(username);
			        
					int picID = FacialRecognition.recognise(userID, comparisonImage);
					String phoneIP = SQLConnections.getPhoneIP(userID);
					Socket phoneSocket;
					DataOutputStream phoneOut;
					if (picID < 0) {
						System.out.println("Cannot Identify Person");
						phoneSocket = new Socket(phoneIP, PHONE_LISTENING_PORT);
						phoneOut = new DataOutputStream(phoneSocket.getOutputStream());
						phoneOut.writeUTF("Stranger");
						ImageIO.write(image, "PNG", phoneOut);
					}
					else {
						System.out.println("Identified Person");
						System.out.println("Checking Whether To Notify");
						Boolean notify = SQLConnections.getNotify(picID);
						if (notify){
							System.out.println("Notifying, Getting Name Of Photo");
							String picName = SQLConnections.getPicNameFromId(picID);
							System.out.println("Persons Name is:" + picName);
							System.out.println("Connecting To Phone at IP: " + phoneIP);
							try{
								phoneSocket = new Socket(phoneIP, PHONE_LISTENING_PORT);
								System.out.println("Connected to Phone");
								phoneOut = new DataOutputStream(phoneSocket.getOutputStream());
								System.out.println("Writing Name");
								phoneOut.writeUTF(picName);
								System.out.println("Writing Image");
								ImageIO.write(image, "PNG", phoneOut);
								System.out.println("Complete");
							}catch(SocketTimeoutException e){
								e.printStackTrace();
							}catch (UnknownHostException e) {
								e.printStackTrace();
							} 
						}
					}
					
				}
			}

/*****************************************PHONE APP ********************************************/
			
			else if (identifier == PHONE) {
				System.out.print("PHONE");
				identifier = in.readInt();
				if (identifier == LOGIN) {
					System.out.print("-LOGIN\n");
					System.out.println("Receiving Username and password for Login");
					username = in.readUTF();
					password = in.readUTF();
					String phoneIP = acceptSocket.getInetAddress().getHostAddress();
					System.out.println("Username: " + username + "\nPassword: " + password + "\nIP: " + phoneIP);
					//if ip is not used again, remove variable and just put ^^ in method below
					LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = SQLConnections.loginAuthentication(username, password, phoneIP, 1);
					//sends 0 if worked, 1 if fail
					out.writeInt(LOGIN_SIGNUP_AUTHENTICATION_RESPONSE);
					
					
				}
				else if (identifier == SIGN_UP){
					System.out.print("-SIGN_UP\n");
					System.out.println("Receiving Username and password and Email For Signup");
					username = in.readUTF();
					password = in.readUTF();
					String emailAddress = in.readUTF();
					LOGIN_SIGNUP_AUTHENTICATION_RESPONSE = SQLConnections.signUp(username, password, emailAddress);
					out.writeInt(LOGIN_SIGNUP_AUTHENTICATION_RESPONSE);
				}
				else if (identifier == READ_LIST){
					System.out.print("-READ_LIST\n");
					System.out.println("Receiving Username For Photos");
					username = in.readUTF();
					System.out.println("Getting Id From Username");
					String userID = SQLConnections.getIDFromUsername(username);
					System.out.println("ID: " + userID + ", now getting the number of photos this user has");
					int noOfPhotos = SQLConnections.getNoPhotos(userID);

					PictureData[] allPicData;
					
					System.out.println("Getting the object array");
					allPicData = SQLConnections.getPhotos(userID, noOfPhotos);
					
					System.out.println("Sending Number Of Photos");
					out.writeInt(noOfPhotos);
					
					Thread s = new Thread(new PhotoSenderListener(allPicData, noOfPhotos));
					s.start();
					System.out.println("Picture Sending Started");
				}
				
				else if (identifier == ADD_NEW_PHOTO){
					System.out.print("-ADD_NEW_PHOTO\n");
					System.out.println("Reading in Username");
					username = in.readUTF();
					String userID = SQLConnections.getIDFromUsername(username);
					System.out.println("Reading in Picture Name");
					String name = in.readUTF();
					System.out.println("Reading in Group");
					String group = in.readUTF();
					System.out.println("Reading in Default Action");
					String defAct  = in.readUTF();
					
					
					System.out.println("Reading in Picture");
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int bufSize = in.readInt();
					byte buffer[] = new byte[bufSize];
					
					int s;
					while ((s=in.read(buffer)) != -1){
						baos.write(buffer, 0, s);
					}
					byte[] byteArray = baos.toByteArray();

					System.out.println("Fully Received Photo");
					
					BufferedImage newPhoto = ImageIO.read(new ByteArrayInputStream(byteArray));
					CanvasFrame canvas = new CanvasFrame("Picture");
					canvas.showImage(newPhoto);
					System.out.println("Detecting Face");
					newPhoto = NewFaces.detectFace(newPhoto);
					if (newPhoto == null){
						out.writeBoolean(false);
					}
					else {
						canvas = new CanvasFrame("Just Face");
						canvas.showImage(newPhoto);
						System.out.println("Resizing Face");
						newPhoto = NewFaces.resizeFace(newPhoto);
						canvas = new CanvasFrame("Resized Face");
						canvas.showImage(newPhoto);
						System.out.println("Adding Photo");
						SQLConnections.addPicture(userID, name, defAct, group, newPhoto);
						System.out.println("Photo Added");
						out.writeBoolean(true);
					}
				
					
				}
				else if (identifier == DELETE_PHOTO){
					System.out.print("-DELETE_PHOTO\n");
					System.out.println("Reading in Username");
					username = in.readUTF();
					String userID = SQLConnections.getIDFromUsername(username);
					System.out.println("Reading in Picture Name");
					String name = in.readUTF();
					System.out.println("Deleting " + name + " from " + username + "'s account");
					SQLConnections.removePicture(userID, name);
				}
				
				else if (identifier == CHANGE_PHOTO){
					System.out.print("-CHANGE_PHOTO\n");
					System.out.println("Reading in Username");
					username = in.readUTF();
					String userID = SQLConnections.getIDFromUsername(username);
					int noOfPhotos = SQLConnections.getNoPhotos(userID);
					PictureData[] newPicData = new PictureData[noOfPhotos];
					for (int i = 0; i<noOfPhotos; i++){
						String name = in.readUTF();
						String defact = in.readUTF();
						String group = in.readUTF();
						newPicData[i] = new PictureData(name, defact, group, null);
					}
					SQLConnections.changePhotos(userID, newPicData);
				}

			}



			else {
				System.out.println("ERROR with identifier");
			}
			 

		}catch(SocketTimeoutException s)
		{
			System.out.println("Socket timed out!");
			try {
				acceptSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(IOException e){}
	}
}
