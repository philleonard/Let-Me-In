import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class sqlConnections{
	private static Statement statement = null;
	private static ResultSet rs = null;
	
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	  
  
  
  public static String[] getPicsAndIDs (int userid) throws Exception {
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
  }
  
  
  //needs full testing, not yet tested
  public static int loginAuthentication (String username, String password, String ipAddress, int identifier){
	  //identify 0 = client, 1 = phone
	  Connection conn = connect();
	  String query;
	  int result = 0;
	  if (conn == null) {
		  System.out.println("NULL");
	  }
	  else {
		 
		  query = "SELECT password, phoneip, clientip FROM users WHERE username = " + username;
		 
		  try {
			  statement = conn.createStatement();
			  rs = statement.executeQuery(query);
			  rs.next();
			  if (rs.getString("password").equals(password)){
				 result = 1;
				 
				 if (identifier == 1) {
					 if (!ipAddress.equals(rs.getString("phoneip"))){
						 query = "UPDATE users SET phoneip = " + ipAddress + " WHERE username = " + username;
						 rs = statement.executeQuery(query);
					 }
				 }
				 
				 else {
					 if (!ipAddress.equals(rs.getString("clientip"))){
						 query = "UPDATE users SET clientip = " + ipAddress + " WHERE username = " + username;
						 rs = statement.executeQuery(query);
					 }
				 }
			  }
		  } catch (SQLException e ) {} 
		  
		  
	  }//end else
	  return result;
  }
  
  //test method
  public static void main(String[] args) throws Exception{
	  getPicsAndIDs(12345);
  }
  
}
