package model;

import java.util.ArrayList;

public class AnimalList {
	private ArrayList<Animal> animalList = new ArrayList<Animal>();

	public AnimalList() {
		
	}
	
	public AnimalList(ArrayList<Animal> animalList) {
		this.animalList = animalList;
	}

	public ArrayList<Animal> getAnimalList() {
		return animalList;
	}

	public void setAnimalList(ArrayList<Animal> animalList) {
		this.animalList = animalList;
	}
	
	public boolean searchAnimal(int id) {
		boolean found = false;
		
		if (animalList.isEmpty()) {
			found = false;
		}
		
		else {
			for(int i = 0; i < this.animalList.size(); i++){
		        if(animalList.get(i).getAnimalId() == id){
		            found = true;
		            break;
		        }
			}
		}
		
		return found;
	}
	
	public int getIndexBySearch(int id) {
		int index = -1;
		
		for(int i = 0; i < this.animalList.size(); i++){
	        if(animalList.get(i).getAnimalId() == id){
	            index = i;
	            break;
	        }
		}
		
		return index;
	}
	
	public void addAnimal(Animal animal) {
		if(!searchAnimal(animal.getAnimalId())) {
			animalList.add(animal);
		}
	}
	
	public void removeAnimal(Animal animal) {
		if(searchAnimal(animal.getAnimalId())) {
			animalList.remove(animalList.indexOf(animal));
		}
	}
	
	public void printAnimalListScreen() {
		if(animalList.isEmpty()) {
			System.out.println(">> Empty List <<");
		}
		
		else {
			System.out.println("-------------------------------");
			System.out.println("         ANIMAL LIST");
			System.out.println("-------------------------------");
			
			for(int i = 0; i < this.animalList.size(); i++){
				System.out.println("ID: " + animalList.get(i).getAnimalId());
				System.out.println("Name: " + animalList.get(i).getAnimalName());
				System.out.println("Type: " + animalList.get(i).getAnimalType() + 
						"   Breed: " + animalList.get(i).getAnimalBreed());
				System.out.println("Colour: " + animalList.get(i).getAnimalColour());
				System.out.println("Age: " + animalList.get(i).getAnimalAge() + 
						"   Gender: " + animalList.get(i).getAnimalGender());
				System.out.println("Description: " + animalList.get(i).getAnimalDescription());
				System.out.println();
				System.out.println("-------------------------------");
				System.out.println();
			}
		}	
	}
}

