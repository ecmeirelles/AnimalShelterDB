package model;

import java.time.LocalDate;

public class LostAnimal extends Category {
	private String lostLocation;

	public LostAnimal(LocalDate date, Person emergencyContact) {
		super(date, emergencyContact);
	}
	
	public LostAnimal(LocalDate date, Person emergencyContact, String lostLocation) {
		super(date, emergencyContact);
		this.lostLocation = lostLocation;
	}
	
	 @Override
	public String getLocation() {
		return lostLocation;
	}

	public void setLostLocation(String lostLocation) {
		this.lostLocation = lostLocation;
	}
}
