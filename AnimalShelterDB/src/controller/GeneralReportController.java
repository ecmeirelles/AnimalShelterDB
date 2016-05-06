package controller;

import java.io.IOException;
import java.util.ArrayList;

import application.GeneralReportView;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import model.Animal;
import model.AnimalList;
import model.Person;

public class GeneralReportController {
	GeneralReportView generalReportView;
	ShelterFile file = new ShelterFile();
	ArrayList<Person> personList = new ArrayList<>();
	
	public GeneralReportController(GeneralReportView generalReportView) {
		this.generalReportView = generalReportView;
	}
	
	public ObservableList<Person> displaySponsorships() {
		ObservableList<Person> details = FXCollections.observableArrayList();
		
		try {
			personList = file.getSponsorshipsFromFile();
			details.addAll(personList);
				
			generalReportView.getNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPersonName()));
			generalReportView.getTelephoneColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPersonPhone()));
			generalReportView.getEmailColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPersonEmail()));
			generalReportView.getAddressColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPersonAddress()));
		    
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}

		return details;
	} 
	
	public ObservableList<Animal> displayAnimals() {
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
			AnimalList animalList = file.getListFromFile("All");
			details.addAll(animalList.getAnimalList());
				
			generalReportView.getAgeColumn().setSortType(TableColumn.SortType.ASCENDING);
			
			generalReportView.getIdColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalId()));
			generalReportView.getAnimalNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalName()));
			generalReportView.getTypeColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalType()));
			generalReportView.getAgeColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalAge()));
			generalReportView.getBreedColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalBreed()));
			generalReportView.getColourColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalColour()));
			generalReportView.getGenderColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalGender()));
			generalReportView.getDescriptionColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalDescription()));
		    
			generalReportView.getAnimalDetails().setItems(details);
		    generalReportView.getAnimalDetails().getSortOrder().add(generalReportView.getAgeColumn());
		    generalReportView.getAnimalDetails().setVisible(true);
		    
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}

		return details;
	} 
}
