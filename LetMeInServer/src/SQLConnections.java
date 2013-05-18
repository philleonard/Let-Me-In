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
		System.out.println("MySQL Connect Example.");
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
					
					PictureData aPicture = new PictureData(rs.getString("name"), rs.getString("defaultaction"), rs.getString("group"), img);
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
		String picName = null;
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT name FROM photos WHERE photoid = \""+picID+"\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					picName = rs.getString("name");
					System.out.println(picName);
				}
			} catch (SQLException e ) {} 

		}
		disconnect(conn);
		return picName;
	}

	/*public static String[] getPicsAndIDs (int userid) throws Exception {
	  Connection conn = connect();
	  String[] addList = null;
	  if (conn == null) {
		  System.out.println("NULL");
	  }
	  else {
		  List<String> myList = new ArrayList<String>(); 
		  String query = "SELECT location FROM photos WHERE userid = "+userid;
		  try {
			  Boolean results = false;
			  statement = conn.createStatement();
			  rs = statement.executeQuery(query);
			  while (rs.next()) {
				  String location = rs.getString("location");
				  myList.add(location);
				  System.out.println(location);
				  results = true;
			  }
			  if (!results){
				 myList.add("0"); 
			  }
			  addList = (String[]) myList.toArray();
		  } catch (SQLException e ) {} 

	  }
	  disconnect(conn);
	  return addList;
  }*/

	//save actual photo first
	public static int addPicture(String userID, String name, String location, String defAct, String group) {
		Connection conn = connect();
		int success = 0;
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			try {
				ps = conn.prepareStatement("INSERT into photos (userid, name, location, defaultaction, group) VALUES (?, ?, ?, ?, ?)");
				ps.setString(1, userID);
				ps.setString(2, name);
				ps.setString(3, location);
				ps.setString(4, defAct);
				ps.setString(5, group);
				ps.addBatch();
				ps.executeBatch();
				System.out.println("Photo Added");
				success=1;
			} catch (SQLException e ) {
				e.printStackTrace();
			} 
		}
		disconnect(conn);
		return success;
	}

	public static int removePicture(String userID, String name) {
		Connection conn = connect();
		if (conn == null) {
			System.out.println("NULL");
		}
		else {
			String query = "SELECT location FROM photos WHERE userid = \"" + userID + "\" and name = \"" + name + "\"";
			try {
				statement = conn.createStatement();
				rs = statement.executeQuery(query);
				while (rs.next()) {
					String location = rs.getString("location");
					File ftd = new File(location);
					ftd.delete();
				}



			} catch (SQLException e ) {} 
		}
		disconnect(conn);
		return 0;
	}

	//needs full testing, not yet tested
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
						System.out.println("Esle result 2");

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
		getPhotos("1", 2);

	}

}
