package connection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Animal;
import model.AnimalAdoption;
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
	
	public ArrayList<Integer> getAdoptionIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		
		try {
			PreparedStatement selectAdoption = connection.prepareStatement("SELECT * FROM animal_shelter.adoption;");
			ResultSet resultAdoption = selectAdoption.executeQuery();
			
			while(resultAdoption.next()) {
				ids.add(resultAdoption.getInt("idCategory"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		
		return ids;	
	}
	
	public ArrayList<Integer> getAdoptionAnimalIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		
		try {
			PreparedStatement selectAdoption = connection.prepareStatement("SELECT * FROM animal_shelter.adoption;");
			ResultSet resultAdoption = selectAdoption.executeQuery();
			
			int idCategory = 0;
			while(resultAdoption.next()) {
				idCategory = resultAdoption.getInt("idCategory");
				
				PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idCategory = ?;");
				selectAnimal.setInt(1, idCategory);
				ResultSet resultAnimal = selectAnimal.executeQuery();
				
				if(resultAnimal.next()) {
					ids.add(resultAnimal.getInt("idAnimal"));
				}
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
			
			else {
				categoryIds = getAdoptionIds();
			}
			
			for(int i =0; i < categoryIds.size(); i++) {
				PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idCategory = ?;");
				selectAnimal.setInt(1, categoryIds.get(i));
				ResultSet resultAnimal = selectAnimal.executeQuery();			
				
				if(resultAnimal.next()) {
					Animal animal = new Animal();
					String location = "", status = "";
					boolean isChipped = false, isVaccinated = false, isNeutered = false, isReserved = false;
					
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
					
					else {
						PreparedStatement selectAdoption = connection.prepareStatement("SELECT * FROM animal_shelter.adoption WHERE idCategory = ?;");
						selectAdoption.setInt(1, categoryIds.get(i));
						ResultSet resultAdoption = selectAdoption.executeQuery();
						
						if(resultAdoption.next()) {						
							isNeutered = resultAdoption.getBoolean("adoptionNeutered");
							isVaccinated = resultAdoption.getBoolean("adoptionVaccinated");
							isChipped = resultAdoption.getBoolean("adoptionChipped");
							status = resultAdoption.getString("adoptionStatus");
							isReserved = resultAdoption.getBoolean("adoptionReserved");
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
						
						else {
							Category animalCategory = new AnimalAdoption(null, null, isNeutered, isChipped, isVaccinated, status, isReserved);
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
	
	public ObservableList<Animal> getFoundAnimalsToReport(String type, String location, LocalDate date, LocalDate betweenDate) {
		String categoryLocation = "";
		AnimalList animalList = new AnimalList();
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
			ArrayList<Integer> categoryIds = getFoundIds();		
			
			for(int i =0; i < categoryIds.size(); i++) {
				PreparedStatement selectAnimal = null;
				ResultSet resultAnimal = null;
				
				if(type.equals("All")) {
					selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idCategory = ?;");
					selectAnimal.setInt(1, categoryIds.get(i));
					resultAnimal = selectAnimal.executeQuery();
				}
				
				else {
					selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idCategory = ? "
							+ "AND animalType = ?;");
					selectAnimal.setInt(1, categoryIds.get(i));
					selectAnimal.setString(2, type);
					resultAnimal = selectAnimal.executeQuery();
				}
									
				if(resultAnimal.next()) {
					PreparedStatement selectFound = null;
					ResultSet resultFound = null;
					
					if(!location.equals("")) {
						selectFound = connection.prepareStatement("SELECT * FROM animal_shelter.found WHERE idCategory = ? "
								+ "AND FoundLocation = ?;");
						selectFound.setInt(1, categoryIds.get(i));
						selectFound.setString(2, location);
						resultFound = selectFound.executeQuery();
						
					}
					
					else {
						selectFound = connection.prepareStatement("SELECT * FROM animal_shelter.found WHERE idCategory = ?;");
						selectFound.setInt(1, categoryIds.get(i));
						resultFound = selectFound.executeQuery();
					}
					
					Animal animal = new Animal();
					
					if(resultFound.next()) {														
						animal.setAnimalId(resultAnimal.getInt("idAnimal"));
						animal.setAnimalAge(resultAnimal.getInt("animalAge"));
						animal.setAnimalType(resultAnimal.getString("animalType"));
						animal.setAnimalColour(resultAnimal.getString("animalColour"));
						animal.setAnimalGender(resultAnimal.getString("animalGender"));
						animal.setAnimalDescription(resultAnimal.getString("animalDescription"));
						animal.setAnimalName(resultAnimal.getString("animalName"));
						animal.setAnimalBreed(resultAnimal.getString("animalBreed"));
						
						if(!location.equals("")) {
							categoryLocation = location;
						}
						
						else {
							categoryLocation = resultFound.getString("foundLocation");
						}
						
						if(date == null && betweenDate == null) {
							PreparedStatement selectCategory = connection.prepareStatement("SELECT * FROM animal_shelter.category WHERE idCategory = ?;");
							selectCategory.setInt(1, categoryIds.get(i));
							ResultSet resultCategory = selectCategory.executeQuery();
								
							if(resultCategory.next()) {
								Category animalCategory = new FoundAnimal(LocalDate.parse(resultCategory.getDate("categoryDate").toString()), 
										searchPersonLinkedToAnimal(categoryIds.get(i)), categoryLocation);
								animal.setAnimalCategory(animalCategory);
								
								animalList.addAnimal(animal);
							}
						}
						
						else {
							PreparedStatement selectCategory = connection.prepareStatement("SELECT * FROM animal_shelter.category WHERE idCategory = ? "
									+ "AND (categoryDate >= ? AND categoryDate <= ?);");
							selectCategory.setInt(1, categoryIds.get(i));
							selectCategory.setDate(2, Date.valueOf(date));
							selectCategory.setDate(3, Date.valueOf(betweenDate));
							ResultSet resultCategory = selectCategory.executeQuery();
								
							if(resultCategory.next()) {
								Category animalCategory = new FoundAnimal(date, searchPersonLinkedToAnimal(categoryIds.get(i)), categoryLocation);
								animal.setAnimalCategory(animalCategory);
								
								animalList.addAnimal(animal);
							}
						}
					}		
				}
			}
			
			details.addAll(animalList.getAnimalList());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return details;
	}
	
	public Animal searchAnimalById(String category, int id) {
		Animal animal = new Animal();
		
		try {
			PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idAnimal = ?;");
			selectAnimal.setInt(1, id);
			ResultSet resultAnimal = selectAnimal.executeQuery();			
				
			if(resultAnimal.next()) {
				String location = "", status = "";
				boolean isChipped = false, isVaccinated = false, isNeutered = false, isReserved = false;
				int idCategory = 0;
					
				animal.setAnimalId(resultAnimal.getInt("idAnimal"));
				animal.setAnimalAge(resultAnimal.getInt("animalAge"));
				animal.setAnimalType(resultAnimal.getString("animalType"));
				animal.setAnimalColour(resultAnimal.getString("animalColour"));
				animal.setAnimalGender(resultAnimal.getString("animalGender"));
				animal.setAnimalDescription(resultAnimal.getString("animalDescription"));
				animal.setAnimalName(resultAnimal.getString("animalName"));
				animal.setAnimalBreed(resultAnimal.getString("animalBreed"));
				
				idCategory = resultAnimal.getInt("idCategory");
					
				if(category.equalsIgnoreCase("Lost")) {
					PreparedStatement selectLost = connection.prepareStatement("SELECT * FROM animal_shelter.lost WHERE idCategory = ?;");
					selectLost.setInt(1, idCategory);
					ResultSet resultLost = selectLost.executeQuery();
					
					if(resultLost.next()) {						
						location = resultLost.getString("lostLocation");
					}
					
					else {
						animal = null;
					}
				}
					
				else if(category.equalsIgnoreCase("Found")) {
					PreparedStatement selectFound = connection.prepareStatement("SELECT * FROM animal_shelter.found WHERE idCategory = ?;");
					selectFound.setInt(1, idCategory);
					ResultSet resultFound = selectFound.executeQuery();
					
					if(resultFound.next()) {						
						location = resultFound.getString("foundLocation");
					}
					
					else {
						animal = null;
					}
				}
				
				else {
					PreparedStatement selectAdoption = connection.prepareStatement("SELECT * FROM animal_shelter.adoption WHERE idCategory = ?;");
					selectAdoption.setInt(1, idCategory);
					ResultSet resultAdoption = selectAdoption.executeQuery();
					
					if(resultAdoption.next()) {						
						isNeutered = resultAdoption.getBoolean("adoptionNeutered");
						isVaccinated = resultAdoption.getBoolean("adoptionVaccinated");
						isChipped = resultAdoption.getBoolean("adoptionChipped");
						status = resultAdoption.getString("adoptionStatus");
						isReserved = resultAdoption.getBoolean("adoptionReserved");
					}
					
					else {
						animal = null;
					}
				}
						
				PreparedStatement selectCategory = connection.prepareStatement("SELECT * FROM animal_shelter.category WHERE idCategory = ?;");
				selectCategory.setInt(1, idCategory);
				ResultSet resultCategory = selectCategory.executeQuery();
						
				if(resultCategory.next()) {
					if(category.equalsIgnoreCase("Lost")) {
						if(animal != null) {
							Category animalCategory = new LostAnimal(LocalDate.parse(resultCategory.getDate("categoryDate").toString()), 
								searchPersonLinkedToAnimal(idCategory), location);
							animal.setAnimalCategory(animalCategory);
						}
					}
						
					else if(category.equalsIgnoreCase("Found")) {
						if(animal != null) {
							Category animalCategory = new FoundAnimal(LocalDate.parse(resultCategory.getDate("categoryDate").toString()), 
								searchPersonLinkedToAnimal(idCategory), location);
							animal.setAnimalCategory(animalCategory);
						}
					}
					
					else {
						if(animal != null) {
							Category animalCategory = new AnimalAdoption(null, null, isNeutered, isChipped, isVaccinated, status, isReserved);
							animal.setAnimalCategory(animalCategory);
						}
					}
				}		
			}
			
			else {
				animal = null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return animal;
	}
	
	public int getIdCategoryByIdAnimal(int id) {
		int idCategory = 0;
		try {
			PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idAnimal = ?;");
			selectAnimal.setInt(1, id);
			ResultSet resultAnimal = selectAnimal.executeQuery();			
				
			if(resultAnimal.next()) {			
				idCategory = resultAnimal.getInt("idCategory");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idCategory;
	}
	
	public void deleteAnimal(String category, int id) {
		try {
			int idCategory = getIdCategoryByIdAnimal(id);
			
			PreparedStatement selectAnimal = connection.prepareStatement("DELETE FROM animal_shelter.animal WHERE idAnimal = ?;");
			selectAnimal.setInt(1, id);
			selectAnimal.execute();	
			
			if(category.equalsIgnoreCase("Lost")) {
				PreparedStatement selectLost = connection.prepareStatement("DELETE FROM animal_shelter.lost WHERE idCategory = ?;");
				selectLost.setInt(1, idCategory);
				selectLost.execute();	
			}
			
			else if(category.equalsIgnoreCase("Found")) {
				PreparedStatement selectFound = connection.prepareStatement("DELETE FROM animal_shelter.found WHERE idCategory = ?;");
				selectFound.setInt(1, idCategory);
				selectFound.execute();
			}
			
			else {
				PreparedStatement selectAdoption = connection.prepareStatement("DELETE FROM animal_shelter.adoption WHERE idCategory = ?;");
				selectAdoption.setInt(1, idCategory);
				selectAdoption.execute();
			}
			
			PreparedStatement selectCategory = connection.prepareStatement("UPDATE animal_shelter.category SET categoryDate = null WHERE idCategory = ?;");
			selectCategory.setInt(1, idCategory);
			selectCategory.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void fromLostToFound(int id, String location, LocalDate date) {
		try {
			PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idAnimal = ?;");
			selectAnimal.setInt(1, id);
			ResultSet resultAnimal = selectAnimal.executeQuery();			
				
			if(resultAnimal.next()) {
				int idCategory = getIdCategoryByIdAnimal(id);
				
				PreparedStatement selectLost = connection.prepareStatement("DELETE FROM animal_shelter.lost WHERE idCategory = ?;");
				selectLost.setInt(1, idCategory);
				selectLost.execute();
				
				PreparedStatement selectCategory = connection.prepareStatement("UPDATE animal_shelter.category SET categoryDate = ? WHERE idCategory = ?;");
				selectCategory.setDate(1, Date.valueOf(date));
				selectCategory.setInt(2, idCategory);
				selectCategory.executeUpdate();
				
				PreparedStatement insertFound = connection.prepareStatement("INSERT INTO animal_shelter.found " + "VALUES (?, ?, ?);");
				
				int value = random.nextInt(10000);				
				insertFound.setInt(1, value);
				insertFound.setString(2, location);
				insertFound.setInt(3, idCategory);
				
				insertFound.executeUpdate();
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void fromFoundToAdoption(int id, boolean neutered, boolean chipped, boolean vaccinated, String status, boolean reserved) {
		try {
			PreparedStatement selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idAnimal = ?;");
			selectAnimal.setInt(1, id);
			ResultSet resultAnimal = selectAnimal.executeQuery();			
				
			if(resultAnimal.next()) {
				int idCategory = getIdCategoryByIdAnimal(id);
				
				PreparedStatement selectFound = connection.prepareStatement("DELETE FROM animal_shelter.found WHERE idCategory = ?;");
				selectFound.setInt(1, idCategory);
				selectFound.execute();
				
				PreparedStatement selectCategory = connection.prepareStatement("UPDATE animal_shelter.category SET categoryDate = ? WHERE idCategory = ?;");
				selectCategory.setDate(1, null);
				selectCategory.setInt(2, idCategory);
				selectCategory.executeUpdate();
				
				PreparedStatement insertAdoption = connection.prepareStatement("INSERT INTO animal_shelter.adoption " + "VALUES (?, ?, ?, ?, ?, ?, ?);");
				
				int value = random.nextInt(10000);				
				insertAdoption.setInt(1, value);
				insertAdoption.setBoolean(2, neutered);
				insertAdoption.setBoolean(3, vaccinated);
				insertAdoption.setBoolean(4, chipped);
				insertAdoption.setString(5, status);
				insertAdoption.setBoolean(6, reserved);
				insertAdoption.setInt(7, idCategory);
				
				insertAdoption.executeUpdate();
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ObservableList<Animal> getLostAnimalsToReport(String type, String location, LocalDate date) {
		String categoryLocation = "";
		AnimalList animalList = new AnimalList();
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
			ArrayList<Integer> categoryIds = getLostIds();		
			
			for(int i =0; i < categoryIds.size(); i++) {
				PreparedStatement selectAnimal = null;
				ResultSet resultAnimal = null;
				
				if(type.equals("All")) {
					selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idCategory = ?;");
					selectAnimal.setInt(1, categoryIds.get(i));
					resultAnimal = selectAnimal.executeQuery();
				}
				
				else {
					selectAnimal = connection.prepareStatement("SELECT * FROM animal_shelter.animal WHERE idCategory = ? "
							+ "AND animalType = ?;");
					selectAnimal.setInt(1, categoryIds.get(i));
					selectAnimal.setString(2, type);
					resultAnimal = selectAnimal.executeQuery();
				}
									
				if(resultAnimal.next()) {
					PreparedStatement selectLost = null;
					ResultSet resultLost = null;
					
					if(!location.equals("")) {
						selectLost = connection.prepareStatement("SELECT * FROM animal_shelter.lost WHERE idCategory = ? "
								+ "AND lostLocation = ?;");
						selectLost.setInt(1, categoryIds.get(i));
						selectLost.setString(2, location);
						resultLost = selectLost.executeQuery();
						
					}
					
					else {
						selectLost = connection.prepareStatement("SELECT * FROM animal_shelter.lost WHERE idCategory = ?;");
						selectLost.setInt(1, categoryIds.get(i));
						resultLost = selectLost.executeQuery();
					}
					
					Animal animal = new Animal();
					
					if(resultLost.next()) {														
						animal.setAnimalId(resultAnimal.getInt("idAnimal"));
						animal.setAnimalAge(resultAnimal.getInt("animalAge"));
						animal.setAnimalType(resultAnimal.getString("animalType"));
						animal.setAnimalColour(resultAnimal.getString("animalColour"));
						animal.setAnimalGender(resultAnimal.getString("animalGender"));
						animal.setAnimalDescription(resultAnimal.getString("animalDescription"));
						animal.setAnimalName(resultAnimal.getString("animalName"));
						animal.setAnimalBreed(resultAnimal.getString("animalBreed"));
						
						if(!location.equals("")) {
							categoryLocation = location;
						}
						
						else {
							categoryLocation = resultLost.getString("lostLocation");
						}
						
						if(date == null) {
							PreparedStatement selectCategory = connection.prepareStatement("SELECT * FROM animal_shelter.category WHERE idCategory = ?;");
							selectCategory.setInt(1, categoryIds.get(i));
							ResultSet resultCategory = selectCategory.executeQuery();
								
							if(resultCategory.next()) {
								Category animalCategory = new LostAnimal(LocalDate.parse(resultCategory.getDate("categoryDate").toString()), 
										searchPersonLinkedToAnimal(categoryIds.get(i)), categoryLocation);
								animal.setAnimalCategory(animalCategory);
								
								animalList.addAnimal(animal);
							}
						}
						
						else {
							PreparedStatement selectCategory = connection.prepareStatement("SELECT * FROM animal_shelter.category WHERE idCategory = ? "
									+ "AND categoryDate = ?;");
							selectCategory.setInt(1, categoryIds.get(i));
							selectCategory.setDate(2, Date.valueOf(date));
							ResultSet resultCategory = selectCategory.executeQuery();
								
							if(resultCategory.next()) {
								Category animalCategory = new LostAnimal(date, searchPersonLinkedToAnimal(categoryIds.get(i)), categoryLocation);
								animal.setAnimalCategory(animalCategory);
								
								animalList.addAnimal(animal);
							}
						}
					}		
				}
			}
			
			details.addAll(animalList.getAnimalList());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return details;
	}
	
	public ArrayList<Person> getInterestAdoptionPeople() {
		ArrayList<Person> peopleList = new ArrayList<>();
		ArrayList<Integer> categoryIds = new ArrayList<>();
		
		try {
			categoryIds = getAdoptionIds();
			
			for(int i =0; i < categoryIds.size(); i++) {
				PreparedStatement selectPerson = connection.prepareStatement("SELECT * FROM animal_shelter.person WHERE idCategory = ?;");
				selectPerson.setInt(1, categoryIds.get(i));
				ResultSet resultPerson = selectPerson.executeQuery();			
				
				while(resultPerson.next()) {
					Person person = new Person();
					person.setPersonName(resultPerson.getString("personName"));
					person.setPersonAddress(resultPerson.getString("personAddress"));
					person.setPersonEmail(resultPerson.getString("personEmail"));
					person.setPersonPhone(resultPerson.getString("personPhone"));
					
					peopleList.add(person);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return peopleList;
	}
	
	public LocalDate getDatefromAnimal(int id) {
		LocalDate date = null;
		
		try {
			int idCategory = getIdCategoryByIdAnimal(id);
			
			PreparedStatement selectCategory = connection.prepareStatement("SELECT * FROM animal_shelter.category WHERE idCategory = ?;");
			selectCategory.setInt(1, idCategory);
			ResultSet resultCategory = selectCategory.executeQuery();
				
			if(resultCategory.next()) {
				date = LocalDate.parse(resultCategory.getDate("categoryDate").toString());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return date;
	}
}
