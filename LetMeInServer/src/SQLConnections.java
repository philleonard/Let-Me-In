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
 */


public class SQLConnections implements Serializable{ 
	private static Statement statement = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;

	private static Connection connect(){
		//System.out.println("MySQL Connect Example.");
		Connection conn = null;
		String url = "jdbc:mysql://localhost:1337/";
		String dbName = "facerecsch";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root"; 
		String password = "root";

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName,userName,password);
			//System.out.println("Connected to the database");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return conn;
		}

	}

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
			} catch (SQLException e ) {} 
		}
		disconnect(conn);
		
		
		return phoneIP;
		
	}

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
			} catch (SQLException e ) {} 
		}
		disconnect(conn);
		return Integer.parseInt(noOfPhotos);
	}

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
			} catch (SQLException e ) {} 
		}
		disconnect(conn);
		return userid;
	}

	public static String getPicNameFromId(int picID){
		System.out.println("PICID to Identify: " + picID);
		String picName = null;
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT personname FROM photos WHERE photoid = \""+picID+"\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					picName = rs.getString("personname");
					System.out.println(picName);
				}
			} catch (SQLException e ) {} 

		}
		disconnect(conn);
		return picName;
	}
	
	public static boolean getNotify(int picID){
		boolean notify = false;
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT defaultaction FROM photos WHERE photoid = \""+picID+"\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					if (rs.getString("defaultaction").equals("Notify me")){
						notify = true;
					}
				}
			} catch (SQLException e ) {} 

		}
		disconnect(conn);
		return notify;
	}

	

	public static void addPicture(String userID, String name, String defAct, String group, BufferedImage newPhoto) {
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			try {
				ps = conn.prepareStatement("INSERT into photos (userid, personname, location, defaultaction, persongroup) VALUES (?, ?, ?, ?, ?)");
				ps.setString(1, userID);
				ps.setString(2, name);
				ps.setString(3, "temp");
				ps.setString(4, defAct);
				ps.setString(5, group);
				ps.addBatch();
				ps.executeBatch();
				System.out.println("Photo Added");
				
				String query = "SELECT photoid FROM photos WHERE personname = \""+name+"\" and userid = \"" + userID + "\"";
			
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				int picID = 0;
				while (rs.next()) {
					picID = Integer.parseInt(rs.getString("photoid"));
				}
				 
	            File outputfile = new File("Photos/" + userID + "/" + picID + ".bmp");
	            ImageIO.write(newPhoto, "PNG", outputfile);
			      
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


			} catch (SQLException e ) {} 
		}
		disconnect(conn);
	}
	
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
				while (rs.next()) {
					ps = conn.prepareStatement("UPDATE photos SET name = ?, persongroup = ?, defaultaction = ? WHERE photoid = ?");
					ps.setString(1, newPicData[i].name);
					ps.setString(2, newPicData[i].group);
					ps.setString(3, newPicData[i].defaultaction);
					ps.setString(4, rs.getString("photoid"));
					ps.addBatch();
					i++;
				}
				ps.executeBatch();

			} catch (SQLException e ) {} 
		}
		disconnect(conn);
	}

	public static int loginAuthentication (String username, String password, String ipAddress, int identifier){
		//identify 0 = client, 1 = phone
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
						System.out.println("Else result 2");

					}
				}//endif rs.next
				else {
					result = 1;
				}
			} catch (SQLException e ) {
				e.printStackTrace();
			} 


		}//end else
		disconnect(conn);
		return result;
	}

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
					//System.out.println("Username Exists");
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



	//test method
	public static void main(String[] args) throws Exception{
		//signUp("james", "bob", "tit");
		//loginAuthentication("jamestrever", "password", "ipAddress", 0);
		//System.out.println(getIDFromUsername("chuck"));
		//System.out.println(getPicNameFromId(1));
		//System.out.println(getNoPhotos(getIDFromUsername("phil")));
		//File outputfile = new File("Photos/userid/photoID.png");
		//getPhotos("1", 2);
		//getPhoneIP("1");
		getPicNameFromId(7);
	}

	

}
