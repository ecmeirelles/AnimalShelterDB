package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Animal;
import model.AnimalAdoption;
import model.AnimalList;
import model.Category;
import model.FoundAnimal;
import model.LostAnimal;
import model.Person;

public class ShelterFile {
	
	public void copyAFile(String original, String copy) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(original));
        String line = null;

        File file = new File(copy);
        file.createNewFile();
        
        while((line = input.readLine()) != null) {
        	writeInFile(copy, line);
        }
        
        input.close();
	}
	
	public void writeInFile(String fileName, String text) throws FileNotFoundException {    
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
	        bw.write(text);
	        bw.newLine();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public AnimalList getListFromFile(String wantedList) throws FileNotFoundException, IOException {
		AnimalList animalList = new AnimalList();
		
       	BufferedReader input =  new BufferedReader(new FileReader("Animal.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");
        	
        	Animal animal = new Animal();
        	animal.setAnimalId(Integer.parseInt(values[1]));
        	animal.setAnimalName(values[2]);
        	animal.setAnimalType(values[3]);
        	animal.setAnimalBreed(values[4]);
        	
        	if(values[5].equals("")) {
        		animal.setAnimalAge(0);
        	}
        	
        	else {
        		animal.setAnimalAge(Integer.parseInt(values[5]));
        	}
        	
        	animal.setAnimalGender(values[6]);
        	animal.setAnimalColour(values[7]);
        	animal.setAnimalDescription(values[8]);
        	
        	Person owner = getOwnerFromFile(animal.getAnimalId(), wantedList);
        	if(wantedList.equalsIgnoreCase("Lost")) {
        		if(values[0].equalsIgnoreCase("Lost")) {
            		Category category = new LostAnimal(LocalDate.parse(values[9]), owner, values[10]);
            		animal.setAnimalCategory(category);
            		
            		animalList.addAnimal(animal);
            	}
        	}
        	
        	else if(wantedList.equalsIgnoreCase("Found")) {
        		if(values[0].equalsIgnoreCase("Found")) {
            		Category category = new FoundAnimal(LocalDate.parse(values[9]), owner, values[10]);
            		animal.setAnimalCategory(category);
            		
            		animalList.addAnimal(animal);
            	}
        	}
        	
        	else if(wantedList.equalsIgnoreCase("Adoption")) {
        		if(values[0].equalsIgnoreCase("Adoption")) {
        			boolean isChipped, isNeutered, isVaccinated, isReserved;
        			
        			if(values[9].equalsIgnoreCase("Chipped")) {isChipped = true;} else {isChipped = false;}
        			if(values[10].equalsIgnoreCase("Neutered")) {isNeutered = true;} else {isNeutered = false;}
        			if(values[11].equalsIgnoreCase("Vaccinated")) {isVaccinated = true;} else {isVaccinated = false;}
        			if(values[13].equalsIgnoreCase("Reserved")) {isReserved = true;} else {isReserved = false;}
        			
            		Category category = new AnimalAdoption(null, owner, isNeutered, isChipped, isVaccinated, values[12], isReserved);
            		animal.setAnimalCategory(category);      
            		
            		animalList.addAnimal(animal);
            	}
        	}
        	
        	else {
        		if(values[0].equalsIgnoreCase("Lost")) {
            		Category category = new LostAnimal(LocalDate.parse(values[9]), null, values[10]);
            		animal.setAnimalCategory(category);
            	}
        		
        		else if(values[0].equalsIgnoreCase("Found")) {
            		Category category = new FoundAnimal(LocalDate.parse(values[9]), null, values[10]);
            		animal.setAnimalCategory(category);
            	}
        		
        		else if(values[0].equalsIgnoreCase("Adoption")) {
        			boolean isChipped, isNeutered, isVaccinated, isReserved;
        			
        			if(values[9].equalsIgnoreCase("Chipped")) {isChipped = true;} else {isChipped = false;}
        			if(values[10].equalsIgnoreCase("Neutered")) {isNeutered = true;} else {isNeutered = false;}
        			if(values[11].equalsIgnoreCase("Vaccinated")) {isVaccinated = true;} else {isVaccinated = false;}
        			if(values[13].equalsIgnoreCase("Reserved")) {isReserved = true;} else {isReserved = false;}
        			
            		Category category = new AnimalAdoption(null, owner, isNeutered, isChipped, isVaccinated, values[12], isReserved);
            		animal.setAnimalCategory(category);
        		}
        		
        		animalList.addAnimal(animal);
        	}
        }
        
        input.close();
        
        return animalList;
    }
	
	public AnimalList getListToReport(String wantedList, String type, LocalDate date, LocalDate betweenDate, String location) throws NumberFormatException, IOException {
		AnimalList animalList = new AnimalList();
		
		BufferedReader input =  new BufferedReader(new FileReader("Animal.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");
        	
        	Animal animal = new Animal();
        	animal.setAnimalId(Integer.parseInt(values[1]));
        	animal.setAnimalName(values[2]);
        	animal.setAnimalType(values[3]);
        	animal.setAnimalBreed(values[4]);
        	
        	if(values[5].equals("")) {
        		animal.setAnimalAge(0);
        	}
        	
        	else {
        		animal.setAnimalAge(Integer.parseInt(values[5]));
        	}
        	
        	animal.setAnimalGender(values[6]);
        	animal.setAnimalColour(values[7]);
        	animal.setAnimalDescription(values[8]);
        	
        	Person owner = getOwnerFromFile(animal.getAnimalId(), wantedList);
        	if(wantedList.equalsIgnoreCase("Lost")) {
        		if(values[0].equalsIgnoreCase("Lost")) {
            		Category category = new LostAnimal(LocalDate.parse(values[9]), owner, values[10]);
            		animal.setAnimalCategory(category);
            		
            		if(date == null) {
            			if(type.equalsIgnoreCase(values[3]) && location.equalsIgnoreCase(values[10])){
            				animalList.addAnimal(animal);
            			}
            			
            			else if(type.equalsIgnoreCase("All") && location.equalsIgnoreCase(values[10])){
                			animalList.addAnimal(animal);
                		}
            		}
            		
            		else if(location.equals("")) {
            			if(type.equalsIgnoreCase(values[3]) && date.equals(LocalDate.parse(values[9]))){
            				animalList.addAnimal(animal);
            			}
            			
            			else if(type.equalsIgnoreCase("All") && date.equals(LocalDate.parse(values[9]))){
                			animalList.addAnimal(animal);
                		}
            		}
            		
            		else {
            			if(type.equalsIgnoreCase("All") && location.equalsIgnoreCase(values[10]) && date.equals(LocalDate.parse(values[9]))) {
                			animalList.addAnimal(animal);
                		}	
            			
            			else if(type.equalsIgnoreCase(values[3]) && location.equalsIgnoreCase(values[10]) && date.equals(LocalDate.parse(values[9]))) {
                			animalList.addAnimal(animal);
                		}
            		}
            	}
        	}
        	
        	else if(wantedList.equalsIgnoreCase("Found")) {
        		if(values[0].equalsIgnoreCase("Found")) {
            		Category category = new FoundAnimal(LocalDate.parse(values[9]), owner, values[10]);
            		animal.setAnimalCategory(category);
            		
            		if(date == null) {
            			if(type.equalsIgnoreCase(values[3]) && location.equalsIgnoreCase(values[10])){
            				animalList.addAnimal(animal);
            			}
            			
            			else if(type.equalsIgnoreCase("All") && location.equalsIgnoreCase(values[10])){
                			animalList.addAnimal(animal);
                		}
            		}
            		
            		else if(location.equals("")) {
            			if(type.equalsIgnoreCase(values[3])) {
            				if((date.isBefore(LocalDate.parse(values[9])) || date.equals(LocalDate.parse(values[9])))
            				   && (betweenDate.isAfter(LocalDate.parse(values[9])) || betweenDate.equals(LocalDate.parse(values[9])))){
            					animalList.addAnimal(animal);	
            				}
            			}
            			
            			else if(type.equalsIgnoreCase("All")){
            				if((date.isBefore(LocalDate.parse(values[9])) || date.equals(LocalDate.parse(values[9])))
                 				   && (betweenDate.isAfter(LocalDate.parse(values[9])) || betweenDate.equals(LocalDate.parse(values[9])))){
                 				animalList.addAnimal(animal);	
                 			}
                		}
            		}
            		
            		else {
            			if(type.equalsIgnoreCase("All") && location.equalsIgnoreCase(values[10])) {
            				if((date.isBefore(LocalDate.parse(values[9])) || date.equals(LocalDate.parse(values[9])))
                 				   && (betweenDate.isAfter(LocalDate.parse(values[9])) || betweenDate.equals(LocalDate.parse(values[9])))){
                 				animalList.addAnimal(animal);	
                 			}
                		}	
            			
            			else if(type.equalsIgnoreCase(values[3]) && location.equalsIgnoreCase(values[10])) {
            				if((date.isBefore(LocalDate.parse(values[9])) || date.equals(LocalDate.parse(values[9])))
                 				   && (betweenDate.isAfter(LocalDate.parse(values[9])) || betweenDate.equals(LocalDate.parse(values[9])))){
                 				animalList.addAnimal(animal);	
                 			}
                		}
            		}
        		}
        	}
        	
        	else {
        		if(values[0].equalsIgnoreCase("Lost")) {
            		Category category = new LostAnimal(LocalDate.parse(values[9]), owner, values[10]);
            		animal.setAnimalCategory(category);
            	}
        		
        		else if(values[0].equalsIgnoreCase("Found")) {
            		Category category = new FoundAnimal(LocalDate.parse(values[9]), owner, values[10]);
            		animal.setAnimalCategory(category);
            	}
        		
        		animalList.addAnimal(animal);
        	}
        }
        
        input.close();
		
		return animalList;
	}
	
	public AnimalList getAdoptionListToReport(String type, String status, String classification) throws IOException {
		AnimalList animalList = new AnimalList();
		
		BufferedReader input =  new BufferedReader(new FileReader("Animal.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");
        	
        	Animal animal = new Animal();
        	if(values[0].equalsIgnoreCase("Adoption")) {
        		animal.setAnimalId(Integer.parseInt(values[1]));
        		animal.setAnimalName(values[2]);
            	animal.setAnimalType(values[3]);
            	animal.setAnimalBreed(values[4]);
            	
            	if(values[5].equals("")) {
            		animal.setAnimalAge(0);
            	}
            	
            	else {
            		animal.setAnimalAge(Integer.parseInt(values[5]));
            	}
            	
            	animal.setAnimalGender(values[6]);
            	animal.setAnimalColour(values[7]);
            	animal.setAnimalDescription(values[8]);
            	
            	Person person = getAllocatedPerson(Integer.parseInt(values[1]));
            	
            	boolean isChipped, isNeutered, isVaccinated, isReserved;
    			
    			if(values[9].equalsIgnoreCase("Chipped")) {isChipped = true;} else {isChipped = false;}
    			if(values[10].equalsIgnoreCase("Neutered")) {isNeutered = true;} else {isNeutered = false;}
    			if(values[11].equalsIgnoreCase("Vaccinated")) {isVaccinated = true;} else {isVaccinated = false;}
    			if(values[13].equalsIgnoreCase("Reserved")) {isReserved = true;} else {isReserved = false;}
            	
            	Category category = new AnimalAdoption(null, person, isNeutered, isChipped, isVaccinated, values[12], isReserved);
            	animal.setAnimalCategory(category);
            	
            	if(classification.equalsIgnoreCase("All")) {
            		if(type.equalsIgnoreCase("All") && status.equalsIgnoreCase(values[12])) {
                		animalList.addAnimal(animal);
                	}
                	
                	else if(type.equalsIgnoreCase(values[3]) && status.equalsIgnoreCase(values[12])) {
                		animalList.addAnimal(animal);
                	}
            	}
            	
            	else if(classification.equalsIgnoreCase("Adults")) {
            		if(type.equalsIgnoreCase("All") && status.equalsIgnoreCase(values[12]) && animal.getAnimalAge() > 2) {
                		animalList.addAnimal(animal);
                	}
                	
                	else if(type.equalsIgnoreCase(values[3]) && status.equalsIgnoreCase(values[12]) && animal.getAnimalAge() > 2) {
                		animalList.addAnimal(animal);
                	}
            	}
            	
            	else if(classification.equalsIgnoreCase("Puppies")) {
            		if(type.equalsIgnoreCase("All") && status.equalsIgnoreCase(values[12]) && animal.getAnimalAge() <= 2) {
                		animalList.addAnimal(animal);
                	}
                	
                	else if(type.equalsIgnoreCase(values[3]) && status.equalsIgnoreCase(values[12]) && animal.getAnimalAge() <= 2) {
                		animalList.addAnimal(animal);
                	}
            	}
            	
        	}
        }
        
        input.close();
		
		return animalList;
	}
	
	public void removeAnimalFromFile(int id) throws FileNotFoundException, IOException {
		AnimalList animalList = getListFromFile("All");
		int index = animalList.getIndexBySearch(id);
		
		BufferedReader input = new BufferedReader(new FileReader("Animal.txt"));
		File temporary = new File("Animal.temp");
		temporary.createNewFile();
		
        String line = null;
        int i = 0;
        
        while((line = input.readLine()) != null) {
        	if(i != index) {
        		writeInFile(temporary.getAbsolutePath(), line);
        	}
        	
        	i++;
        }
        input.close();
        
        Path source = Paths.get("Animal.txt");
        Files.delete(source);
        Path temporaryPath = Paths.get(temporary.getPath());
        Files.move(temporaryPath, temporaryPath.resolveSibling("Animal.txt"));
	}
	
	public void updateAdoptionFile(int animalId, String isChipped, String isNeutered, String isVaccinated, String status) throws FileNotFoundException, IOException {
		AnimalList animalList = getListFromFile("All");
		int index = animalList.getIndexBySearch(animalId);
		
		BufferedReader input = new BufferedReader(new FileReader("Animal.txt"));
		File temporary = new File("Animal.temp");
		temporary.createNewFile();
		
		String chipped, neutered, vaccinated;
		
		if(isChipped.equalsIgnoreCase("Yes")) {chipped = "Chipped";} else {chipped = "Not Chipped";}
		if(isNeutered.equalsIgnoreCase("Yes")) {neutered = "Neutered";} else {neutered = "Not Neutered";}
		if(isVaccinated.equalsIgnoreCase("Yes")) {vaccinated = "Vaccinated";} else {vaccinated = "Not Vaccinated";}
			
        String line = null;
        int i = 0;
        
        while((line = input.readLine()) != null) {
        	if(i == index) {
        		int id = animalList.getAnimalList().get(index).getAnimalId();
        		String name = animalList.getAnimalList().get(index).getAnimalName();
        		String type = animalList.getAnimalList().get(index).getAnimalType();
        		String breed = animalList.getAnimalList().get(index).getAnimalBreed();
        		int age = animalList.getAnimalList().get(index).getAnimalAge();
        		String gender = animalList.getAnimalList().get(index).getAnimalGender();
        		String colour = animalList.getAnimalList().get(index).getAnimalColour();
        		String description = animalList.getAnimalList().get(index).getAnimalDescription();
        		
        		writeInFile(temporary.getAbsolutePath(), "Adoption" + "\t" + id + "\t" + name + "\t" + type + "\t" 
								+ breed + "\t" + age + "\t" + gender + "\t" + colour + "\t" + description + "\t" 
        				        + chipped + "\t" + neutered + "\t" + vaccinated + "\t" + status + "\t" + "Reserved");
        	}
        	
        	else {
        		writeInFile(temporary.getAbsolutePath(), line);
        	}
        	
        	i++;
        }
        input.close();
        
        Path source = Paths.get("Animal.txt");
        Files.delete(source);
        Path temporaryPath = Paths.get(temporary.getPath());
        Files.move(temporaryPath, temporaryPath.resolveSibling("Animal.txt"));
	}
	
	public void fromLostToFound(int animalId, String animalDate, String animalLocation) throws FileNotFoundException, IOException {
		AnimalList animalList = getListFromFile("All");
		int index = animalList.getIndexBySearch(animalId);
		
		BufferedReader input = new BufferedReader(new FileReader("Animal.txt"));
		File temporary = new File("Animal.temp");
		temporary.createNewFile();
		
        String line = null;
        int i = 0;
        
        while((line = input.readLine()) != null) {
        	if(i == index) {
        		int id = animalList.getAnimalList().get(index).getAnimalId();
        		String name = animalList.getAnimalList().get(index).getAnimalName();
        		String type = animalList.getAnimalList().get(index).getAnimalType();
        		String breed = animalList.getAnimalList().get(index).getAnimalBreed();
        		int age = animalList.getAnimalList().get(index).getAnimalAge();
        		String gender = animalList.getAnimalList().get(index).getAnimalGender();
        		String colour = animalList.getAnimalList().get(index).getAnimalColour();
        		String description = animalList.getAnimalList().get(index).getAnimalDescription();
        		
        		writeInFile(temporary.getAbsolutePath(), "Found" + "\t" + id + "\t" + name + "\t" + type + "\t" 
								+ breed + "\t" + age + "\t" + gender + "\t" + colour + "\t" 
								+ description + "\t" + animalDate + "\t" + animalLocation);
        	}
        	
        	else {
        		writeInFile(temporary.getAbsolutePath(), line);
        	}
        	
        	i++;
        }
        input.close();
        
        Path source = Paths.get("Animal.txt");
        Files.delete(source);
        Path temporaryPath = Paths.get(temporary.getPath());
        Files.move(temporaryPath, temporaryPath.resolveSibling("Animal.txt"));
	}
	
	public void fromFoundToAdoption(int animalId, String name, String isChipped, String isNeutered, String isVaccinated, String status, String isReserved) throws IOException {
		AnimalList animalList = getListFromFile("All");
		int index = animalList.getIndexBySearch(animalId);
		
		BufferedReader input = new BufferedReader(new FileReader("Animal.txt"));
		File temporary = new File("Animal.temp");
		temporary.createNewFile();
		
		String chipped, neutered, vaccinated;
		
		if(isChipped.equalsIgnoreCase("Yes")) {chipped = "Chipped";} else {chipped = "Not Chipped";}
		if(isNeutered.equalsIgnoreCase("Yes")) {neutered = "Neutered";} else {neutered = "Not Neutered";}
		if(isVaccinated.equalsIgnoreCase("Yes")) {vaccinated = "Vaccinated";} else {vaccinated = "Not Vaccinated";}
			
        String line = null;
        int i = 0;
        
        while((line = input.readLine()) != null) {
        	if(i == index) {
        		int id = animalList.getAnimalList().get(index).getAnimalId();
        		String type = animalList.getAnimalList().get(index).getAnimalType();
        		String breed = animalList.getAnimalList().get(index).getAnimalBreed();
        		int age = animalList.getAnimalList().get(index).getAnimalAge();
        		String gender = animalList.getAnimalList().get(index).getAnimalGender();
        		String colour = animalList.getAnimalList().get(index).getAnimalColour();
        		String description = animalList.getAnimalList().get(index).getAnimalDescription();
        		
        		writeInFile(temporary.getAbsolutePath(), "Adoption" + "\t" + id + "\t" + name + "\t" + type + "\t" 
								+ breed + "\t" + age + "\t" + gender + "\t" + colour + "\t" + description + "\t" 
        				        + chipped + "\t" + neutered + "\t" + vaccinated + "\t" + status + "\t" + isReserved);
        	}
        	
        	else {
        		writeInFile(temporary.getAbsolutePath(), line);
        	}
        	
        	i++;
        }
        input.close();
        
        Path source = Paths.get("Animal.txt");
        Files.delete(source);
        Path temporaryPath = Paths.get(temporary.getPath());
        Files.move(temporaryPath, temporaryPath.resolveSibling("Animal.txt"));
	}
	
	public boolean searchAnimalIdByPerson(int id) throws IOException {
		boolean found = false;
		
		BufferedReader input =  new BufferedReader(new FileReader("OwnerContact.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");
        	
        	if(id == Integer.parseInt(values[1])) {
        		found = true;
        	}
        }
        
        input.close();
		
		return found;
	}
	
	public boolean searchReservationByPerson(int id) throws IOException {
		boolean found = false;
		
		BufferedReader input =  new BufferedReader(new FileReader("InterestAdopting.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");

        	if(values.length > 4 && id == Integer.parseInt(values[4])) {
        		found = true;
        	}
        }
        
        input.close();
		
		return found;
	}
	
	public Person getOwnerFromFile(int animalId, String wantedList) throws NumberFormatException, IOException {
		Person person = new Person();

       	BufferedReader input =  new BufferedReader(new FileReader("OwnerContact.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");

        	if(wantedList.equalsIgnoreCase("Lost")) {
        		if(values[0].equalsIgnoreCase("Lost") && animalId == Integer.parseInt(values[1])) {
        			person.setPersonName(values[2]);
                	person.setPersonPhone(values[3]);
                	person.setPersonEmail(values[4]);
                	person.setPersonAddress(values[5]);
            	}
        	}
        	
        	else if(wantedList.equalsIgnoreCase("Found")) {
        		if(values[0].equalsIgnoreCase("Found") && animalId == Integer.parseInt(values[1])) {
        			person.setPersonName(values[2]);
                	person.setPersonPhone(values[3]);
                	person.setPersonEmail(values[4]);
                	person.setPersonAddress(values[5]);
            	}
        	}
        }
        
        input.close();
		
		return person;
	}
	
	public ArrayList<Person> getInterestedPeopleFromFile() throws IOException {
		
		ArrayList<Person> personList = new ArrayList<>();
		
		BufferedReader input =  new BufferedReader(new FileReader("InterestAdopting.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");

        	Person person = new Person();
        	person.setPersonName(values[0]);
            person.setPersonPhone(values[1]);
            person.setPersonEmail(values[2]);
            person.setPersonAddress(values[3]);
            
            personList.add(person);
        }
        
        input.close();
		
		return personList;
	}
	
	public Person getAllocatedPerson(int animalId) throws IOException {
		Person person = new Person();
		
		BufferedReader input =  new BufferedReader(new FileReader("InterestAdopting.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");

        	if(values.length > 4 && values[4].equals(String.valueOf(animalId))) {
        		person.setPersonName(values[0]);
                person.setPersonPhone(values[1]);
                person.setPersonEmail(values[2]);
                person.setPersonAddress(values[3]);
                
                break;
        	}

        }
        
        input.close();
		
		return person;
	}
	
	public Person searchInterestedPerson(String name) throws IOException {
		Person person = new Person();

       	BufferedReader input =  new BufferedReader(new FileReader("InterestAdopting.txt"));
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");

        	if(values[0].equalsIgnoreCase(name)) {
        		person.setPersonName(values[0]);
               	person.setPersonPhone(values[1]);
               	person.setPersonEmail(values[2]);
               	person.setPersonAddress(values[3]);
            }
        }
        
        input.close();
		
		return person;
	}
	
	public void updateInterestAdopting(int animalId, String name) throws IOException {
       	BufferedReader input =  new BufferedReader(new FileReader("InterestAdopting.txt"));
       	File temporary = new File("InterestAdopting.temp");
		temporary.createNewFile();
		
        String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");

        	if(values[0].equalsIgnoreCase(name)) {
        		writeInFile(temporary.getAbsolutePath(), values[0] + "\t" + values[1] + "\t" + values[2] + "\t" + values[3] + "\t" + animalId);
        		break;
            }
        }
        
        input.close();
        
        Path source = Paths.get("InterestAdopting.txt");
        Files.delete(source);
        Path temporaryPath = Paths.get(temporary.getPath());
        Files.move(temporaryPath, temporaryPath.resolveSibling(source));
	}
	
	public void removeOwnerFromFile(int animalId) throws IOException {
		AnimalList animalList = getListFromFile("All");
		int index = animalList.getIndexBySearch(animalId);
		
		BufferedReader input = new BufferedReader(new FileReader("OwnerContact.txt"));
		File temporary = new File("OwnerContact.temp");
		temporary.createNewFile();
		
		String line = null;
        int i = 0;
        
        while((line = input.readLine()) != null) {
        	if(i != index) {
        		writeInFile(temporary.getAbsolutePath(), line);
        	}
        	
        	i++;
        }
        input.close();
        
        Path source = Paths.get("OwnerContact.txt");
        Files.delete(source);
        Path temporaryPath = Paths.get(temporary.getPath());
        Files.move(temporaryPath, temporaryPath.resolveSibling(source));
	}
	
	public void allocateAnimalToFamily(int animalId, String personName) throws FileNotFoundException, IOException {
		AnimalList animalList = getListFromFile("Adoption");
		int index = animalList.getIndexBySearch(animalId);
		
		BufferedReader input = new BufferedReader(new FileReader("Animal.txt"));
		File temporary = new File("Animal.temp");
		temporary.createNewFile();
		
		String line = null;
        
        while((line = input.readLine()) != null) {
        	String values[] = line.split("\t");
        	
        	if(values[1].equals(String.valueOf(animalId))) {
        		updateInterestAdopting(animalId, personName);
        		
        		AnimalAdoption animalAdoption = (AnimalAdoption) animalList.getAnimalList().get(index).getAnimalCategory();
        		animalAdoption.setEmergencyContact(searchInterestedPerson(personName));
        		animalAdoption.setReserved(true);       		
        		
        		int id = animalList.getAnimalList().get(index).getAnimalId();
        		String name = animalList.getAnimalList().get(index).getAnimalName();
        		String type = animalList.getAnimalList().get(index).getAnimalType();
        		String breed = animalList.getAnimalList().get(index).getAnimalBreed();
        		int age = animalList.getAnimalList().get(index).getAnimalAge();
        		String gender = animalList.getAnimalList().get(index).getAnimalGender();
        		String colour = animalList.getAnimalList().get(index).getAnimalColour();
        		String description = animalList.getAnimalList().get(index).getAnimalDescription();
        		boolean isChipped = animalList.getAnimalList().get(index).getAnimalCategory().isChipped();
        		boolean isNeutered = animalList.getAnimalList().get(index).getAnimalCategory().isNeutered();
        		boolean isVaccinated = animalList.getAnimalList().get(index).getAnimalCategory().isVaccinated();
        		boolean isReserved = animalList.getAnimalList().get(index).getAnimalCategory().isReserved();
        		String status = animalList.getAnimalList().get(index).getAnimalCategory().getStatus();
        		
        		String chipped, neutered, vaccinated, reserved;
        		
        		if(isChipped) {chipped = "Chipped";} else {chipped = "Not Chipped";}
        		if(isNeutered) {neutered = "Neutered";} else {neutered = "Not Neutered";}
        		if(isVaccinated) {vaccinated = "Vaccinated";} else {vaccinated = "Not Vaccinated";}
        		if(isReserved) {reserved = "Reserved";} else {reserved = "Not Reserved";}
        		
        		
        		writeInFile(temporary.getAbsolutePath(), "Adoption" + "\t" + id + "\t" + name + "\t" + type + "\t" 
								+ breed + "\t" + age + "\t" + gender + "\t" + colour + "\t" + description + "\t" 
        				        + chipped + "\t" + neutered + "\t" + vaccinated + "\t" + status + "\t" + reserved);
        	}
        	
        	else {
        		writeInFile(temporary.getAbsolutePath(), line);
        	}
        }
        
        input.close();
        
        Path source = Paths.get("Animal.txt");
        Files.delete(source);
        Path temporaryPath = Paths.get(temporary.getPath());
        Files.move(temporaryPath, temporaryPath.resolveSibling(source));       
	}
	
	public ArrayList<Person> getSponsorshipsFromFile() throws IOException {
		ArrayList<Person> personList = new ArrayList<>();
		
		BufferedReader input1 =  new BufferedReader(new FileReader("InterestAdopting.txt"));
		BufferedReader input2 =  new BufferedReader(new FileReader("OwnerContact.txt"));
        String line = null;
        
        while((line = input1.readLine()) != null) {
        	String values[] = line.split("\t");

        	Person person = new Person();
        	person.setPersonName(values[0]);
            person.setPersonPhone(values[1]);
            person.setPersonEmail(values[2]);
            person.setPersonAddress(values[3]);
            
            personList.add(person);
        }
        
        while((line = input2.readLine()) != null) {
        	String values[] = line.split("\t");

        	Person person = new Person();
        	person.setPersonName(values[2]);
            person.setPersonPhone(values[3]);
            person.setPersonEmail(values[4]);
            person.setPersonAddress(values[5]);
            
            personList.add(person);
        }
        
        input1.close();
        input2.close();
		
		return personList;
	}
}