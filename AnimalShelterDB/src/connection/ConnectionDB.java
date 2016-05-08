package connection;

import java.sql.*;

public class ConnectionDB {
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/animal_shelter";
	static final String USER = "root";
	static final String PASSWORD = "root";
	   
	private Connection connection = null;
	private PreparedStatement statement = null;
	   
	public ConnectionDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL,USER,PASSWORD);
		} 
		
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
