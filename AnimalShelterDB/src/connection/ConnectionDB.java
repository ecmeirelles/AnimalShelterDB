package connection;

import java.sql.*;
import java.util.Random;

import model.Animal;
import model.Person;

public class ConnectionDB {
	static final String URL = "jdbc:mysql://127.0.0.1:3306/animal_shelter";
	static final String USER = "root";
	static final String PASSWORD = "root";
	   
	private Connection connection = null;
	private Random random = new Random();
	   
	public ConnectionDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} 
		
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public void closeConnection() {
		try {
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addPerson(Person person) {
		try {
			PreparedStatement insertPerson = connection.prepareStatement("INSERT INTO animal_shelter.person(idPerson, personName, personAddress, personPhone, personEmail)" 
					+ "VALUES (?, ?, ?, ?, ?);");
			
			int value = random.nextInt(10000);
			
			insertPerson.setInt(1, value);
			insertPerson.setString(2, person.getPersonName());
			insertPerson.setString(3, person.getPersonAddress());
			insertPerson.setString(4, person.getPersonPhone());
			insertPerson.setString(5, person.getPersonEmail());
			
			insertPerson.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public int searchIdPersonLinkToAnimal(Animal animal) {
		int idPerson = 0;
	
		try {
			PreparedStatement selectPerson = connection.prepareStatement("SELECT idPerson FROM animal_shelter.person WHERE personPhone = ? AND personName = ?;");
			selectPerson.setString(1, animal.getAnimalCategory().getEmergencyContact().getPersonPhone());
			selectPerson.setString(2, animal.getAnimalCategory().getEmergencyContact().getPersonName());
			
			ResultSet resultPerson = selectPerson.executeQuery();
			
			if(resultPerson.next()) {
				idPerson = resultPerson.getInt("idPerson");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idPerson;
	}
	
	public void addCategory(Animal animal) {
		
		try {
			PreparedStatement insertCategory = connection.prepareStatement("INSERT INTO animal_shelter.category " + "VALUES (?, ?, ?);");
			
			int valueCategory = random.nextInt(10000);
			insertCategory.setInt(1, valueCategory);
			insertCategory.setDate(2, Date.valueOf(animal.getAnimalCategory().getDate()));
			insertCategory.setInt(3, searchIdPersonLinkToAnimal(animal));
			insertCategory.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	public int searchAnimalCategory(Animal animal) {
		int idCategory = 0;
		
		try {
			PreparedStatement selectCategory = connection.prepareStatement("SELECT idCategory FROM animal_shelter.category WHERE idPerson = ?;");
			selectCategory.setInt(1, searchIdPersonLinkToAnimal(animal));
			ResultSet resultCategory = selectCategory.executeQuery();			
			
			if(resultCategory.next()) {
				idCategory = resultCategory.getInt("idCategory");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idCategory;
	}
	
	public void addAnimal(Animal animal, String category) {
		try {
			int idPerson = searchIdPersonLinkToAnimal(animal);
			
			if(idPerson != 0) {
				addCategory(animal);
				int idCategory = searchAnimalCategory(animal);
								
				if(idCategory != 0) {
					PreparedStatement insertSituation = null;
					int valueSituation = random.nextInt(10000);
					
					if(category.equalsIgnoreCase("Lost")) {
						insertSituation = connection.prepareStatement("INSERT INTO animal_shelter.lost " + "VALUES (?, ?, ?);");			
						insertSituation.setInt(1, valueSituation);
						insertSituation.setString(2, animal.getAnimalCategory().getLocation());
						insertSituation.setInt(3, idCategory);			
						insertSituation.executeUpdate();
					}
					
					else if(category.equalsIgnoreCase("Found")) {
						insertSituation = connection.prepareStatement("INSERT INTO animal_shelter.found " + "VALUES (?, ?, ?);");			
						insertSituation.setInt(1, valueSituation);
						insertSituation.setString(2, animal.getAnimalCategory().getLocation());
						insertSituation.setInt(3, idCategory);			
						insertSituation.executeUpdate();
					}
					
					else {
						insertSituation = connection.prepareStatement("INSERT INTO animal_shelter.adoption " + "VALUES (?, ?, ?, ?, ?, ?, ?);");			
						insertSituation.setInt(1, valueSituation);
						insertSituation.setBoolean(2, animal.getAnimalCategory().isNeutered());
						insertSituation.setBoolean(3, animal.getAnimalCategory().isVaccinated());
						insertSituation.setBoolean(4, animal.getAnimalCategory().isChipped());
						insertSituation.setString(5, animal.getAnimalCategory().getStatus());
						insertSituation.setBoolean(6, animal.getAnimalCategory().isReserved());
						insertSituation.setInt(7, idCategory);			
						insertSituation.executeUpdate();
					}
					
					PreparedStatement insertAnimal = connection.prepareStatement("INSERT INTO animal_shelter.animal " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
					insertAnimal.setInt(1, animal.getAnimalId());
					insertAnimal.setInt(2, animal.getAnimalAge());
					insertAnimal.setString(3, animal.getAnimalType());
					insertAnimal.setString(4, animal.getAnimalColour());
					insertAnimal.setString(5, animal.getAnimalGender());
					insertAnimal.setString(6, animal.getAnimalDescription());
					insertAnimal.setString(7, animal.getAnimalName());
					insertAnimal.setString(8, "");
					insertAnimal.setString(9, animal.getAnimalBreed());
					insertAnimal.setInt(10, idCategory);
					insertAnimal.executeUpdate();
				}
				
				else {
					System.out.println("ERROR: Wrong idCategory");
				}
			}
			
			else {
				System.out.println("ERROR: Wrong idPerson");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
