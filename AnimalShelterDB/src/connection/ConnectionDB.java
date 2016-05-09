package connection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Animal;
import model.AnimalList;
import model.Category;
import model.FoundAnimal;
import model.LostAnimal;
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
	
	public void addPerson(Person person, int idCategory) {
		try {
			PreparedStatement insertPerson = connection.prepareStatement("INSERT INTO animal_shelter.person " + "VALUES (?, ?, ?, ?, ?, ?);");
			
			int value = random.nextInt(10000);
			
			insertPerson.setInt(1, value);
			insertPerson.setString(2, person.getPersonName());
			insertPerson.setString(3, person.getPersonAddress());
			insertPerson.setString(4, person.getPersonPhone());
			insertPerson.setString(5, person.getPersonEmail());
			insertPerson.setInt(6, idCategory);
			
			insertPerson.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public Person searchPersonLinkedToAnimal(int idCategory) {
		Person person = new Person();
	
		try {
			PreparedStatement selectPerson = connection.prepareStatement("SELECT * FROM animal_shelter.person WHERE idCategory = ?;");
			selectPerson.setInt(1, idCategory);
			
			ResultSet resultPerson = selectPerson.executeQuery();
			
			if(resultPerson.next()) {
				person.setPersonName(resultPerson.getString("personName"));
				person.setPersonAddress(resultPerson.getString("personAddress"));
				person.setPersonPhone(resultPerson.getString("personPhone"));
				person.setPersonEmail(resultPerson.getString("personEmail"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return person;
	}
	
	public int addCategoryAndGetId(Animal animal) {
		int idCategory = 0;
		
		try {
			PreparedStatement insertCategory = connection.prepareStatement("INSERT INTO animal_shelter.category " + "VALUES (?, ?);");
			
			idCategory = random.nextInt(10000);
			insertCategory.setInt(1, idCategory);
			insertCategory.setDate(2, Date.valueOf(animal.getAnimalCategory().getDate()));			
			insertCategory.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return idCategory;
	}
	
	
	public void addAnimal(Animal animal, String category) {
		try {			
			int idCategory = addCategoryAndGetId(animal);
			
			if(idCategory != 0) {
				PreparedStatement insertSituation = null;
				int valueSituation = random.nextInt(10000);
					
				if(category.equalsIgnoreCase("Lost")) {
					addPerson(animal.getAnimalCategory().getEmergencyContact(), idCategory);
					
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean searchAnimalId(int newId) {
		boolean found = false;
		
		try {
			PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idAnimal = ?;");
			selectAnimal.setInt(1, newId);
			ResultSet resultAnimal = selectAnimal.executeQuery();			
			
			if(resultAnimal.next()) {
				found = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return found;
	}
	
	public ArrayList<Integer> getLostIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		
		try {
			PreparedStatement selectLost = connection.prepareStatement("SELECT * FROM animal_shelter.lost;");
			ResultSet resultLost = selectLost.executeQuery();
			
			while(resultLost.next()) {
				ids.add(resultLost.getInt("idCategory"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		
		return ids;	
	}
	
	public ArrayList<Integer> getFoundIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		
		try {
			PreparedStatement selectFound = connection.prepareStatement("SELECT * FROM animal_shelter.found;");
			ResultSet resultFound = selectFound.executeQuery();
			
			while(resultFound.next()) {
				ids.add(resultFound.getInt("idCategory"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		
		return ids;	
	}
	
	public ObservableList<Animal> getAnimals(String category) {
		ArrayList<Integer> categoryIds = new ArrayList<>();
		AnimalList animalList = new AnimalList();
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
			if(category.equalsIgnoreCase("Lost")) {
				categoryIds = getLostIds();
			}
			
			else if(category.equalsIgnoreCase("Found")) {
				categoryIds = getFoundIds();
			}
			
			for(int i =0; i < categoryIds.size(); i++) {
				PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idCategory = ?;");
				selectAnimal.setInt(1, categoryIds.get(i));
				ResultSet resultAnimal = selectAnimal.executeQuery();			
				
				if(resultAnimal.next()) {
					Animal animal = new Animal();
					String location = "";
					
					animal.setAnimalId(resultAnimal.getInt("idAnimal"));
					animal.setAnimalAge(resultAnimal.getInt("animalAge"));
					animal.setAnimalType(resultAnimal.getString("animalType"));
					animal.setAnimalColour(resultAnimal.getString("animalColour"));
					animal.setAnimalGender(resultAnimal.getString("animalGender"));
					animal.setAnimalDescription(resultAnimal.getString("animalDescription"));
					animal.setAnimalName(resultAnimal.getString("animalName"));
					animal.setAnimalBreed(resultAnimal.getString("animalBreed"));
					
					if(category.equalsIgnoreCase("Lost")) {
						PreparedStatement selectLost = connection.prepareStatement("SELECT * FROM animal_shelter.lost WHERE idCategory = ?;");
						selectLost.setInt(1, categoryIds.get(i));
						ResultSet resultLost = selectLost.executeQuery();
						
						if(resultLost.next()) {						
							location = resultLost.getString("lostLocation");
						}
					}
					
					else if(category.equalsIgnoreCase("Found")) {
						PreparedStatement selectFound = connection.prepareStatement("SELECT * FROM animal_shelter.found WHERE idCategory = ?;");
						selectFound.setInt(1, categoryIds.get(i));
						ResultSet resultFound = selectFound.executeQuery();
						
						if(resultFound.next()) {						
							location = resultFound.getString("foundLocation");
						}
					}
						
					PreparedStatement selectCategory = connection.prepareStatement("SELECT * FROM animal_shelter.category WHERE idCategory = ?;");
					selectCategory.setInt(1, categoryIds.get(i));
					ResultSet resultCategory = selectCategory.executeQuery();
						
					if(resultCategory.next()) {
						if(category.equalsIgnoreCase("Lost")) {
							Category animalCategory = new LostAnimal(LocalDate.parse(resultCategory.getDate("categoryDate").toString()), 
									searchPersonLinkedToAnimal(categoryIds.get(i)), location);
							animal.setAnimalCategory(animalCategory);
						}
						
						else if(category.equalsIgnoreCase("Found")) {
							Category animalCategory = new FoundAnimal(LocalDate.parse(resultCategory.getDate("categoryDate").toString()), 
									searchPersonLinkedToAnimal(categoryIds.get(i)), location);
							animal.setAnimalCategory(animalCategory);
						}
					}		
					
					animalList.addAnimal(animal);
				}
			}
			
			details.addAll(animalList.getAnimalList());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return details;
	}
}
