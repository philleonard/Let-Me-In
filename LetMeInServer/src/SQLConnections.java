import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
/**
 * 
 * @author James
 *
 *This static class is called from ServerConnections whenever data needs to manipulated 
 *in the database. This class controls all the connections to the database.
 *
 *To set of a database that works with this project, it has to be of the following type:
 *Address: localhost Port: 1337
 *Database name: facerecsch
 *Username: root
 *Password: root
 *
 *Schema
 *users table:
 *userid			Int				PK, NN, AI
 *username			VarChar(45)		PK, NN
 *password			VarChar(45)		NN
 *clientip			VarChar(16)
 *phoneip			VarChar(16)	
 *email				VarChar(45)		PK, NN
 *
 *photos table: 
 *userid			Int				PK, NN
 *photoid			Int				PK, NN, AI
 *personname		VarChar(45)		
 *location			VarChar(45)		PK, NN
 *defaultaction		VarChar(10)
 *persongroup		VarChar(15)
 */


public class SQLConnections implements Serializable{ 
	private static Statement statement = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;

	
	//This method is called in every method as it all the details to connect to the db
	private static Connection connect(){
		Connection conn = null;
		String url = "jdbc:mysql://localhost:1337/";
		String dbName = "facerecsch";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root"; 
		String password = "root";

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);
			System.out.println("Connected to the database");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return conn;
		}

	}
	
	//This method is called at the end of every method as the closes the connections and statements
	private static void disconnect(Connection connect) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
			if (ps != null){
				ps.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	This method is used to get all the photos for a perticular user.
	The method is sent the user's id and the number of photos that belongs to them
	The number of photos they have is retreived in another method.
	*/
	public static PictureData[] getPhotos(String userID, int noOfPhotos){
		PictureData[] allPicData = new PictureData[noOfPhotos];
		int i = 0;
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT * FROM photos WHERE userid = \"" + userID + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					String imageLoc = rs.getString("location");
					BufferedImage img = null;
					try {
						img = ImageIO.read(new File(imageLoc));
					} catch (IOException e) {
						System.out.println("Image Finder Error");
					}
					
					PictureData aPicture = new PictureData(rs.getString("personname"), rs.getString("defaultaction"), rs.getString("persongroup"), img);
					allPicData[i] = aPicture;
					
					i++;
					System.out.println("Object " + i + " contains Name: " + aPicture.name + " DefAct: " + aPicture.defaultaction + " Group: " + aPicture.group);
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
		return allPicData;
	}
	
	
	//This method is used to get the IP of the user's mobile phone from the data base
	public static String getPhoneIP(String userID){
		String phoneIP = null;
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT phoneip FROM users WHERE userid = \"" + userID + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					phoneIP = rs.getString("phoneip");
					System.out.println("Phone IP of user " + userID + " is " + phoneIP);
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
		return phoneIP;
	}
	
	/*
	This method is used to get the IP of the user's computer that has the client
	running on it, from the data base
	*/
	public static String getClientIP(String userID){
		String clientIP = null;
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT clientip FROM users WHERE userid = \"" + userID + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					clientIP = rs.getString("clientip");
					System.out.println("Client IP of user " + userID + " is " + clientIP);
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
		return clientIP;
	}

	/*
	This method is used to get the number of photos that belong to a user. As this number is
	used quite a lot, it has its own method for this.
	*/
	public static int getNoPhotos(String userID){
		String noOfPhotos = "0";
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT userid, COUNT(*) FROM photos WHERE userid = \"" + userID + "\" GROUP BY userid";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					noOfPhotos = rs.getString("COUNT(*)");
					System.out.println(noOfPhotos + " photos belong to user with ID: " + userID);
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
		return Integer.parseInt(noOfPhotos);
	}

	/*
	This method is used to get the userid of a user from their username. This is because a user 
	only knows there username and the userid is just something used in the database for determining
	who each photo belongs to.
	*/
	public static String getIDFromUsername(String username){
		Connection conn = connect();
		String userid = null;
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT userid FROM users WHERE username = \""+username+"\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					userid = rs.getString("userid");
					System.out.println("Username: " + username + " ID is: " + userid);
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
		return userid;
	}

	
	

	
	//This method is used to add a new picture to a user's account
	public static void addPicture(String userID, String name, String defAct, String group, BufferedImage newPhoto) {
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			try {
				//Here all the details of the photo are added to the db
				//except for the location as the photo has not been saved yet.
				//This is done first though as the name of the file must be the photoid,
				//but this is auto-generated when you add the entry to the db.
				ps = conn.prepareStatement("INSERT into photos (userid, personname, location, defaultaction, persongroup) VALUES (?, ?, ?, ?, ?)");
				ps.setString(1, userID);
				ps.setString(2, name);
				ps.setString(3, "temp");
				ps.setString(4, defAct);
				ps.setString(5, group);
				ps.addBatch();
				ps.executeBatch();
				System.out.println("Photo Added");
				
				//Here the photo id of the new photo is retreived.
				String query = "SELECT photoid FROM photos WHERE personname = \""+name+"\" and userid = \"" + userID + "\"";
			
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				int picID = 0;
				while (rs.next()) {
					picID = Integer.parseInt(rs.getString("photoid"));
				}
				 
				//Here the file is saved with the photoid just retreived
	            File outputfile = new File("Photos/" + userID + "/" + picID + ".bmp");
	            ImageIO.write(newPhoto, "PNG", outputfile);
			     
	            //Here the db is updated to include the location of where the photo has just been saved.
	            ps = conn.prepareStatement("UPDATE photos SET location = ? WHERE photoid = ?");
				ps.setString(1, "Photos/" + userID + "/" + picID + ".bmp");
				ps.setString(2, Integer.toString(picID));
				ps.addBatch();
				ps.executeBatch();
				
			} catch (SQLException | IOException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
	}

	/*
	This method is used to remove a photo, it first gets the images location from the db,
	then deletes the file from storage, then updates the database.
	*/
	public static void removePicture(String userID, String name) {
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT location FROM photos WHERE userid = \"" + userID + "\" and personname = \"" + name + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					String location = rs.getString("location");
					File ftd = new File(location);
					ftd.delete();
					System.out.println("Photo File Deleted");
				}
				
				ps = conn.prepareStatement("DELETE from photos WHERE userid = ? and personname = ?");
				ps.setString(1, userID);
				ps.setString(2, name);
				ps.addBatch();
				ps.executeBatch();
				System.out.println("Photo Data Deleted from Database");


			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
	}
	
	/*
	This method is used to update the photo data in the database.
	To make it easier on the phone side, when changes are made, the phone just sends
	back all the data, including stuff that hasn't been update. This means we update
	all data in the database, even if nothing has changed.
	*/
	public static void changePhotos(String userID, PictureData[] newPicData){
		Connection conn = connect();
		int i = 0;
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT * FROM photos WHERE userid = \"" + userID + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				System.out.println("Starting to overwrite photo data");
				while (rs.next()) {
					ps = conn.prepareStatement("UPDATE photos SET personname = ?, persongroup = ?, defaultaction = ? WHERE photoid = ?");
					ps.setString(1, newPicData[i].name);
					ps.setString(2, newPicData[i].group);
					ps.setString(3, newPicData[i].defaultaction);
					ps.setString(4, rs.getString("photoid"));
					ps.addBatch();
					i++;
				}
				System.out.println("Executing Batch");
				ps.executeBatch();
				System.out.println("PhotoData Updated");


			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
	}
	
	/*
	This method is used to authenticate login attempts. As the user will login from both the phone
	app and the pc client, there needs to be a way to distinguish them as there user's ip address is
	stored in this method for later contact.
	*/
	public static int loginAuthentication (String username, String password, String ipAddress, int identifier){
		//identify: 0 = client, 1 = phone
		Connection conn = connect();
		String query;
		System.out.println("Sign In Request");
		int result = 1;
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			query = "SELECT password, phoneip, clientip FROM users WHERE username = \"" + username + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);			  
				if(rs.next()){
					if (rs.getString("password").equals(password)){
						result = 0;
						System.out.println("Sign In Successful");
						if (identifier == 1) {
							if (!ipAddress.equals(rs.getString("phoneip"))){

								ps = conn.prepareStatement("UPDATE users SET phoneip = ? WHERE username = ?");
								ps.setString(1, ipAddress);
								ps.setString(2, username);
								ps.addBatch();
								ps.executeBatch();

							}
						}

						else {
							if (!ipAddress.equals(rs.getString("clientip"))){

								ps = conn.prepareStatement("UPDATE users SET clientip = ? WHERE username = ?");
								ps.setString(1, ipAddress);
								ps.setString(2, username);
								ps.addBatch();
								ps.executeBatch();

							}
						}
					}//endif password check
					else {
						result = 2;
						System.out.println("Incorrect Password Entered");
					}
				}//endif rs.next/find matching username
				else {
					result = 1;
					System.out.println("Username not found");
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 


		}//end else
		disconnect(conn);
		return result;
	}

	/*
	This method is used to signup for a new account. This method checks that the username or 
	email address entered does not already exist
	Result:	-1 = Initialising State
				0 = Success
				1 = Username already exists
				2 = Email already exists
				3 = Both already exist
	*/
	public static int signUp (String username, String password, String emailAddress){
		Connection conn = connect();
		String query;
		int result = -1;
		System.out.println("Sign Up Attempt");
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			query = "SELECT * FROM users WHERE username = \"" + username + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);			  
				if(rs.next()){
					System.out.println("Username Exists");
					result = 1;
				}

				query = "SELECT * FROM users WHERE email = \"" + emailAddress + "\"";
				rs=statement.executeQuery(query);

				if(rs.next()){
					if (result==1){
						System.out.println("Username and Email Exists");
						result = 3;
					}
					else{
						System.out.println("Email Exists");
						result = 2; 
					}
				}

				else {
					if (result == 1){
						System.out.println("Username Exists");
					}
					else {
						System.out.println("Adding new user");
						result = 0;

						ps = conn.prepareStatement("INSERT into users (username, password, email) VALUES (?, ?, ?)");
						ps.setString(1, username);
						ps.setString(2, password);
						ps.setString(3, emailAddress);
						ps.addBatch();
						ps.executeBatch();
						System.out.println("User Added");

					}
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 


		}//end else
		disconnect(conn);
		return result;
	}

	/*
	This method is used to sign out of the phone app.
	This prevents them getting notifications also.
	*/
	public static void signOut(String userID){
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			try {
				ps = conn.prepareStatement("UPDATE users SET phoneip = ? WHERE userid = ?");
				ps.setString(1, "");
				ps.setString(2, userID);
				ps.addBatch();

				ps.executeBatch();
				System.out.println("User " + userID + " has signed out");


			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
	}

	/*
	This method is used to get all the information about the images from their id's.
	The method first sets the name and default action to defaults in case the person is a stranger.
	In then finds the rest of the information.
	*/
	public static void getPicInfoFromID(String userID, String[] pictureIDs, int noOfPhotos, PictureData[] allPicData) {
		for(int i = 0; i<noOfPhotos; i++){
			allPicData[i] = new PictureData("Unidentified", "Notify me", null, null);
		}
		
		
		
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT * FROM photos WHERE userid = \""+userID+"\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				for (int i = 0; i<noOfPhotos; i++){
					while (rs.next()) {
						if(rs.getString("photoid").equals(pictureIDs[i])){
							allPicData[i].name = rs.getString("personname");
							allPicData[i].defaultaction = rs.getString("defaultaction");
							rs.afterLast();
						}
					}
					rs.beforeFirst();
				}
			} catch (SQLException e ) {} 

		}
		disconnect(conn);
	}	

}
