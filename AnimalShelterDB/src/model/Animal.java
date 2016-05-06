package model;

import javafx.scene.image.Image;

public class Animal {
	
	private int animalId;
	private int animalAge;
	private String animalType;
	private String animalColour;
	private String animalGender;
	private String animalDescription;
	private String animalName;
	private Image animalPicture;
	private String animalBreed;
	private Category animalCategory;
	
	public Animal() {
		
	}
	
	public Animal(int animalId, int animalAge, String animalType, String animalColour, String animalGender,
			String animalDescription, String animalName, Image animalPicture, String animalBreed, Category animalCategory) {
		
		this.animalId = animalId;
		this.animalAge = animalAge;
		this.animalType = animalType;
		this.animalColour = animalColour;
		this.animalGender = animalGender;
		this.animalDescription = animalDescription;
		this.animalName = animalName;
		this.animalPicture = animalPicture;
		this.animalBreed = animalBreed;
		this.animalCategory = animalCategory;
	}

	public Animal(int animalId, String animalType, String animalColour, String animalGender, String animalDescription,
			String animalName, Image animalPicture, String animalBreed) {
		
		this.animalId = animalId;
		this.animalType = animalType;
		this.animalColour = animalColour;
		this.animalGender = animalGender;
		this.animalDescription = animalDescription;
		this.animalName = animalName;
		this.animalPicture = animalPicture;
		this.animalBreed = animalBreed;
	}

	public Animal(int animalId, int animalAge, String animalType, String animalColour, String animalGender,
			String animalDescription, String animalName, String animalBreed) {
		
		this.animalId = animalId;
		this.animalAge = animalAge;
		this.animalType = animalType;
		this.animalColour = animalColour;
		this.animalGender = animalGender;
		this.animalDescription = animalDescription;
		this.animalName = animalName;
		this.animalBreed = animalBreed;
	}

	public int getAnimalId() {
		return animalId;
	}

	public void setAnimalId(int animalId) {
		this.animalId = animalId;
	}

	public int getAnimalAge() {
		return animalAge;
	}

	public void setAnimalAge(int animalAge) {
		this.animalAge = animalAge;
	}

	public String getAnimalType() {
		return animalType;
	}

	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}

	public String getAnimalColour() {
		return animalColour;
	}

	public void setAnimalColour(String animalColour) {
		this.animalColour = animalColour;
	}

	public String getAnimalGender() {
		return animalGender;
	}

	public void setAnimalGender(String animalGender) {
		this.animalGender = animalGender;
	}

	public String getAnimalDescription() {
		return animalDescription;
	}

	public void setAnimalDescription(String animalDescription) {
		this.animalDescription = animalDescription;
	}

	public String getAnimalName() {
		return animalName;
	}

	public void setAnimalName(String animalName) {
		this.animalName = animalName;
	}

	public Image getAnimalPicture() {
		return animalPicture;
	}

	public void setAnimalPicture(Image animalPicture) {
		this.animalPicture = animalPicture;
	}

	public String getAnimalBreed() {
		return animalBreed;
	}

	public void setAnimalBreed(String animalBreed) {
		this.animalBreed = animalBreed;
	}

	public Category getAnimalCategory() {
		return animalCategory;
	}

	public void setAnimalCategory(Category animalCategory) {
		this.animalCategory = animalCategory;
	}
}
