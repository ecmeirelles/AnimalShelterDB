package model;

import java.time.LocalDate;

public class AnimalAdoption extends Category {
	private boolean isNeutered;
	private boolean isChipped;
	private boolean isVaccinated;
	private String animalStatus;
	private boolean isReserved;
	
	public AnimalAdoption(LocalDate date, Person emergencyContact, boolean isNeutered, boolean isChipped, boolean isVaccinated, String animalStatus,
			boolean isReserved) {
		super(date, emergencyContact);
		
		this.isNeutered = isNeutered;
		this.isChipped = isChipped;
		this.isVaccinated = isVaccinated;
		this.animalStatus = animalStatus;
		this.isReserved = isReserved;
	}

	public boolean isNeutered() {
		return isNeutered;
	}

	public void setNeutered(boolean isNeutered) {
		this.isNeutered = isNeutered;
	}

	public boolean isChipped() {
		return isChipped;
	}

	public void setChipped(boolean isChipped) {
		this.isChipped = isChipped;
	}

	public boolean isVaccinated() {
		return isVaccinated;
	}

	public void setVaccinated(boolean isVaccinated) {
		this.isVaccinated = isVaccinated;
	}

	public String getStatus() {
		return animalStatus;
	}

	public void setStatus(String animalStatus) {
		this.animalStatus = animalStatus;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}
}
