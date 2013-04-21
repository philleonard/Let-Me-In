import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class sqlConnections{
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
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
		  connect.close();
		  System.out.println("Disconnected from database");
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
			  statement = conn.createStatement();
			  ResultSet rs = statement.executeQuery(query);
			  while (rs.next()) {
				  String location = rs.getString("location");
				  myList.add(location);
				  System.out.println(location);
			  }
			  addList = (String[]) myList.toArray();
		  } catch (SQLException e ) {} 
		  finally {
			  if (statement != null) { statement.close(); }
		  }
	  }
	  disconnect(conn);
	  return addList;
  }
  public static void main(String[] args) throws Exception{
	  getPicsAndIDs(12345);
  }
  
}
