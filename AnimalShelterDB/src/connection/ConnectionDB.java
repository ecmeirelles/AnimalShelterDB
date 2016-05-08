package connection;

import java.sql.*;
import java.util.Random;

import model.Animal;
import model.Person;

public class ConnectionDB {
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/animal_shelter";
	static final String USER = "root";
	static final String PASSWORD = "root";
	   
	private Connection connection = null;
	private Statement statement = null;
	   
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
	
	public void addPerson(Person person) {
		try {
			System.out.println("Insert person...");
			PreparedStatement insertPerson = connection.prepareStatement("INSERT INTO animal_shelter.person(idPerson, personName, personAddress, personPhone, personEmail)" 
					+ "VALUES (?, ?, ?, ?, ?);");
			
			Random random = new Random();
			int value = random.nextInt(10000);
			System.out.println(value);
			insertPerson.setInt(1, value);
			insertPerson.setString(2, person.getPersonName());
			insertPerson.setString(3, person.getPersonAddress());
			insertPerson.setString(4, person.getPersonPhone());
			insertPerson.setString(5, person.getPersonEmail());
			
			insertPerson.executeUpdate();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
