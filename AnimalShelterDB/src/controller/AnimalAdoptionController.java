package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import application.AnimalAdoptionView;
import application.Main;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Toggle;
import javafx.scene.control.Alert.AlertType;
import model.Animal;
import model.Person;

public class AnimalAdoptionController implements EventHandler<ActionEvent>{
	AnimalAdoptionView animalAdoptionView;
	
	String animalID, animalName, animalBreed, animalAge, animalColour, animalDescription, animalLocation, animalType, animalStatus,
		   personName, personEmail, personTelephone, personAddress;
	LocalDate animalDate;
	Toggle animalChipped, animalNeutered, animalVaccinated;
	
	public AnimalAdoptionController(AnimalAdoptionView animalAdoptionView) {
		this.animalAdoptionView = animalAdoptionView;
	}

	@Override
	public void handle(ActionEvent event) {{

		if(event.getSource() == animalAdoptionView.getSubmitButton()) {
			animalID = animalAdoptionView.getAnimalIdField().getText();
			animalName = animalAdoptionView.getAnimalNameField().getText();
			animalChipped = animalAdoptionView.getChippedGroup().getSelectedToggle();
			animalNeutered = animalAdoptionView.getNeuteredGroup().getSelectedToggle();
			animalVaccinated = animalAdoptionView.getVaccinatedGroup().getSelectedToggle();
			animalStatus = animalAdoptionView.getStatusField().getValue();
			
			submit();
		}
		
		else if(event.getSource() == animalAdoptionView.getClearButton()) {
			clear();
		}
		
		else if(event.getSource() == animalAdoptionView.getCancelButton()){
			cancel();
		}
		
		else if(event.getSource() == animalAdoptionView.getSearchToDeleteButton()) {
			animalID = animalAdoptionView.getAnimalSearchField().getText();
			
			searchToDelete();
		}

		else if(event.getSource() == animalAdoptionView.getRemoveButton()){
			animalID = animalAdoptionView.getAnimalIdField().getText();
			
			remove();
		}
		
		else if(event.getSource() == animalAdoptionView.getSearchToAddButton()) {
			animalID = animalAdoptionView.getAnimalSearchField().getText();
			
			searchToAdd();
		}
		
		else if(event.getSource() == animalAdoptionView.getAddPersonButton()) {
			personName = animalAdoptionView.getPersonNameField().getText();
			personEmail = animalAdoptionView.getPersonEmailField().getText();
			personTelephone = animalAdoptionView.getPersonTelephoneField().getText();
			personAddress = animalAdoptionView.getPersonAddressField().getText();
			
			addInterestedPerson();
		}
		
		else if(event.getSource() == animalAdoptionView.getUpdateButton()) {
			Animal animal = animalAdoptionView.getAnimalDetails().getSelectionModel().getSelectedItem();
			animalID = String.valueOf(animal.getAnimalId());
			animalChipped = animalAdoptionView.getChippedGroup().getSelectedToggle();
			animalNeutered = animalAdoptionView.getNeuteredGroup().getSelectedToggle();
			animalVaccinated = animalAdoptionView.getVaccinatedGroup().getSelectedToggle();
			animalStatus = animalAdoptionView.getStatusField().getValue();
			
			updateInformation();
		}
		
		else if(event.getSource() == animalAdoptionView.getAllocateButton()) {
			animalID = animalAdoptionView.getAdoptionIds().getValue();
			personName = animalAdoptionView.getAdoptionNames().getValue();
			
			allocateAnimal();
		}
		
		else if(event.getSource() == animalAdoptionView.getGenerateReportButton()) {
			animalType = animalAdoptionView.getAnimalTypeReport().getValue();
			animalStatus = animalAdoptionView.getStatusField().getValue();
			
			generateReport();
		}
	}
}

	public void submit() {

		if(animalStatus == null || (!animalAdoptionView.getYesChipped().isSelected() && 
				!animalAdoptionView.getNoChipped().isSelected())  || (!animalAdoptionView.getYesNeutered().isSelected() && 
				!animalAdoptionView.getNoNeutered().isSelected()) || (!animalAdoptionView.getYesVaccinated().isSelected() && 
				!animalAdoptionView.getNoVaccinated().isSelected())) {
				
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Empty Field!");
			alert.setContentText("One or more of the following fields is empty. Please fill it."
					+ "\n* Neutered \n* Chipped \n* Vaccinated \n* Status");
			alert.showAndWait();	
		}
			
		else {
			LocalDate foundDate = Main.getConnection().getDatefromAnimal(Integer.parseInt(animalID));

			if(LocalDate.now().minusMonths(1).equals(foundDate) || LocalDate.now().minusMonths(1).isAfter(foundDate)) {
					
				RadioButton chipped = (RadioButton)animalChipped;
				RadioButton neutered = (RadioButton)animalNeutered;
				RadioButton vaccinated = (RadioButton)animalVaccinated;
				
				boolean isChipped, isVaccinated, isNeutered;

				if(chipped.getText().equalsIgnoreCase("Yes")) { isChipped = true; } else { isChipped = false; }
				if(neutered.getText().equalsIgnoreCase("Yes")) { isNeutered = true; } else { isNeutered = false; }
				if(vaccinated.getText().equalsIgnoreCase("Yes")) { isVaccinated = true; } else { isVaccinated = false; }
				
				Main.getConnection().fromFoundToAdoption(Integer.parseInt(animalID), isNeutered, isChipped, isVaccinated, animalStatus, false);
							
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Animal added to adoption successfully!");
				alert.showAndWait();
							
				clear();
			}
					
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Operation Failed! 30 days is the minimum time for a valid transference");
				alert.showAndWait();
			}
		}	
	}	
	
	public void cancel() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText("Attention: Information will not be kept!\nDo you want to proceed?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    Main.getStage().setScene(Main.getScene());
		} 
	}
	
	public void searchToDelete() {
		if(animalID.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("ID must not be empty!");
			alert.showAndWait();
		}
		
		else {
			Animal animal = Main.getConnection().searchAnimalById("Adoption", Integer.parseInt(animalAdoptionView.getAnimalSearchField().getText()));
			
			if(animal != null) {
				animalAdoptionView.getAnimalIdName().setVisible(true);
				animalAdoptionView.getAnimalType().setVisible(true);
				animalAdoptionView.getAnimalBreedAge().setVisible(true);
				animalAdoptionView.getAnimalGenderColour().setVisible(true);
				animalAdoptionView.getAnimalDescriptionLabel().setVisible(true);
				animalAdoptionView.getAnimalDescriptionArea().setVisible(true);
				animalAdoptionView.getNeuteredChipped().setVisible(true);
				animalAdoptionView.getVaccinatedStatus().setVisible(true);
				animalAdoptionView.getActionButtons().setVisible(true);
				
				String animalGender = animal.getAnimalGender();
				boolean isNeutered = animal.getAnimalCategory().isNeutered();
				boolean isChipped = animal.getAnimalCategory().isChipped();
				boolean isVaccinated = animal.getAnimalCategory().isVaccinated();
							
				animalAdoptionView.getAnimalIdField().setText(String.valueOf(animal.getAnimalId()));
				animalAdoptionView.getAnimalNameField().setText(animal.getAnimalName());
				animalAdoptionView.getAnimalTypeField().setValue(animal.getAnimalType());
				animalAdoptionView.getAnimalBreedField().setText(animal.getAnimalBreed());
				animalAdoptionView.getAnimalAgeField().setText(String.valueOf(animal.getAnimalAge()));
				animalAdoptionView.getAnimalColourField().setText(animal.getAnimalColour());
				animalAdoptionView.getAnimalDescriptionArea().setText(animal.getAnimalDescription());
				animalAdoptionView.getStatusField().setValue(animal.getAnimalCategory().getStatus());
				
				if(animalGender.equalsIgnoreCase("Female")) {
					animalAdoptionView.getFemaleAnimal().setSelected(true);
					animalAdoptionView.getMaleAnimal().setSelected(false);
				}
				
				else {
					animalAdoptionView.getFemaleAnimal().setSelected(false);
					animalAdoptionView.getMaleAnimal().setSelected(true);
				}
				
				if(isChipped) {
					animalAdoptionView.getYesChipped().setSelected(true);
					animalAdoptionView.getNoChipped().setSelected(false);
				}
				
				else {
					animalAdoptionView.getNoChipped().setSelected(true);
					animalAdoptionView.getYesChipped().setSelected(false);
				}
				
				if(isNeutered) {
					animalAdoptionView.getYesNeutered().setSelected(true);
					animalAdoptionView.getNoNeutered().setSelected(false);
				}
				
				else {
					animalAdoptionView.getNoNeutered().setSelected(true);
					animalAdoptionView.getYesNeutered().setSelected(false);
				}
				
				if(isVaccinated) {
					animalAdoptionView.getYesVaccinated().setSelected(true);
					animalAdoptionView.getNoVaccinated().setSelected(false);
				}
				
				else {
					animalAdoptionView.getNoVaccinated().setSelected(true);
					animalAdoptionView.getYesVaccinated().setSelected(false);
				}
				
				animalAdoptionView.getAnimalSearchField().setText("");
			}
			
			else {
				clear();
					
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Animal couldn't be found!");
				alert.showAndWait();
			}
		}
	}

	public ObservableList<Animal> displayAll() {
		ObservableList<Animal> details = Main.getConnection().getAnimals("Adoption");
			
		animalAdoptionView.getReservedColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isReserved()));
		animalAdoptionView.getIdColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalId()));
		animalAdoptionView.getNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalName()));
		animalAdoptionView.getTypeColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalType()));
		animalAdoptionView.getAgeColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalAge()));
		animalAdoptionView.getBreedColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalBreed()));
		animalAdoptionView.getColourColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalColour()));
		animalAdoptionView.getGenderColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalGender()));
		animalAdoptionView.getDescriptionColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalDescription()));
		animalAdoptionView.getChippedColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isChipped()));
		animalAdoptionView.getNeuteredColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isNeutered()));
		animalAdoptionView.getVaccinatedColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isVaccinated()));
		animalAdoptionView.getStatusColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getStatus()));

		return details;
	}
	
	public void remove() {
		Animal animal = Main.getConnection().searchAnimalById("Adoption", Integer.parseInt(animalID));
		
		if(animal != null) {
			if(animal.getAnimalCategory().isChipped() && animal.getAnimalCategory().isNeutered() &&
					animal.getAnimalCategory().isVaccinated() && animal.getAnimalCategory().getStatus().equalsIgnoreCase("Ready") && 
					animal.getAnimalCategory().isReserved()) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText(null);
				alert.setContentText("Do you really want to delete?");
	
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					Main.getConnection().deleteAnimal("Adoption", Integer.parseInt(animalID));
					
					Alert confirmation = new Alert(AlertType.INFORMATION);
					confirmation.setHeaderText(null);
					confirmation.setContentText("Animal removed successfully!");
					confirmation.showAndWait();
					
					clear();
				}
			}
			
			else {
				Alert confirmation = new Alert(AlertType.ERROR);
				confirmation.setHeaderText(null);
				confirmation.setContentText("Animal is not ready to be removed!");
				confirmation.showAndWait();
				
				clear();
			}
		}
	}
	
	public void clear() {
		animalAdoptionView.getAnimalIdName().setVisible(false);
		animalAdoptionView.getAnimalType().setVisible(false);
		animalAdoptionView.getAnimalBreedAge().setVisible(false);
		animalAdoptionView.getAnimalGenderColour().setVisible(false);
		animalAdoptionView.getAnimalDescriptionLabel().setVisible(false);
		animalAdoptionView.getAnimalDescriptionArea().setVisible(false);
		animalAdoptionView.getNeuteredChipped().setVisible(false);
		animalAdoptionView.getVaccinatedStatus().setVisible(false);
		animalAdoptionView.getActionButtons().setVisible(false);
	}
	
	public void searchToAdd() {
		if(animalID.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("ID must not be empty!");
			alert.showAndWait();
		}
		
		else {
			Animal animal = Main.getConnection().searchAnimalById("Found", Integer.parseInt(animalAdoptionView.getAnimalSearchField().getText()));
				
			if(animal != null) {
				animalAdoptionView.getAnimalIdName().setVisible(true);
				animalAdoptionView.getAnimalType().setVisible(true);

				animalAdoptionView.getAnimalBreedAge().setVisible(true);
				animalAdoptionView.getAnimalGenderColour().setVisible(true);
				animalAdoptionView.getAnimalDescriptionLabel().setVisible(true);
				animalAdoptionView.getAnimalDescriptionArea().setVisible(true);
				animalAdoptionView.getNeuteredChipped().setVisible(true);
				animalAdoptionView.getVaccinatedStatus().setVisible(true);
				animalAdoptionView.getActionButtons().setVisible(true);
				
				String animalType = animal.getAnimalType();
				String animalGender = animal.getAnimalGender();
				
				animalAdoptionView.getAnimalIdField().setText(String.valueOf(animal.getAnimalId()));
				animalAdoptionView.getAnimalNameField().setText(animal.getAnimalName());
				animalAdoptionView.getAnimalBreedField().setText(animal.getAnimalBreed());
				animalAdoptionView.getAnimalAgeField().setText(String.valueOf(animal.getAnimalAge()));
				animalAdoptionView.getAnimalColourField().setText(animal.getAnimalColour());
				animalAdoptionView.getAnimalDescriptionArea().setText(animal.getAnimalDescription());
				
				if(animalGender.equalsIgnoreCase("Female")) {
					animalAdoptionView.getFemaleAnimal().setSelected(true);
					animalAdoptionView.getMaleAnimal().setSelected(false);
				}
				
				else if(animalGender.equalsIgnoreCase("Male")) {
					animalAdoptionView.getFemaleAnimal().setSelected(false);
					animalAdoptionView.getMaleAnimal().setSelected(true);
				}
				
				if(animalType.equalsIgnoreCase("Cat")) {
					animalAdoptionView.getAnimalTypeField().setValue("Cat");
				}
				
				else {
					animalAdoptionView.getAnimalTypeField().setValue("Dog");
				}
				
				animalAdoptionView.getAnimalSearchField().setText("");
			}
			
			else {
				clear();
			
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Animal couldn't be found!");
				alert.showAndWait();
			}
		}
	}
	
	public void addInterestedPerson() {
		if(personName.isEmpty() || personTelephone.isEmpty()) {
				
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Empty Field!");
			alert.setContentText("One or more of the following fields is empty. Please fill it \n* Full name \n* Telephone");
			alert.showAndWait();	
		}
			
		else {
			Person person = new Person();
			person.setPersonName(personName);
			person.setPersonAddress(personAddress);
			person.setPersonEmail(personEmail);
			person.setPersonPhone(personTelephone);
			
			ArrayList<Integer> id = Main.getConnection().getAdoptionIds();
			Main.getConnection().addPerson(person, id.get(0));
							
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Person added successfully!");
			alert.showAndWait();
				
			animalAdoptionView.getPersonNameField().setText("");
			animalAdoptionView.getPersonEmailField().setText("");
			animalAdoptionView.getPersonTelephoneField().setText("");
			animalAdoptionView.getPersonAddressField().setText("");
		}	
	}
	
	public void updateInformation() {
		RadioButton chipped = (RadioButton)animalChipped;
		RadioButton neutered = (RadioButton)animalNeutered;
		RadioButton vaccinated = (RadioButton)animalVaccinated;
		
		boolean isChipped, isVaccinated, isNeutered;

		if(chipped.getText().equalsIgnoreCase("Yes")) { isChipped = true; } else { isChipped = false; }
		if(neutered.getText().equalsIgnoreCase("Yes")) { isNeutered = true; } else { isNeutered = false; }
		if(vaccinated.getText().equalsIgnoreCase("Yes")) { isVaccinated = true; } else { isVaccinated = false; }
		
		Main.getConnection().updateAdoptionAnimal(Integer.parseInt(animalID), isNeutered, isChipped, isVaccinated, false, animalStatus);
			
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText("Animal updated successfully!");
		alert.showAndWait();
		
		animalAdoptionView.getAnimalDetails().setItems(displayAll());
		animalAdoptionView.getAllowEditAnimalCheckBox().setSelected(false);
	}
	
	public void allocateAnimal() {
		if(animalID == null || personName == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("No field must be empty!");
			alert.showAndWait();
		}
		
		else {
			Animal animal = Main.getConnection().searchAnimalById("Adoption", Integer.parseInt(animalID));
			
			if(animal != null) {
				if(animal.getAnimalCategory().isReserved()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("This animal is already reserved!");
					alert.showAndWait();
				}
				
				else {
					Main.getConnection().allocateToFamily(Integer.parseInt(animalID), personName);
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText(null);
					alert.setContentText("Animal allocated successfully!");
					alert.showAndWait();
				}
				
				animalAdoptionView.getAdoptionIds().setValue("");
				animalAdoptionView.getAdoptionNames().setValue("");
			}
			
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Animal not found!");
				alert.showAndWait();
			}
		}
	}
	
	public void generateReport() {		
		if(animalStatus == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Status must not be empty");
			alert.showAndWait();
		}
		
		else if(animalAdoptionView.getOrganizationField().getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Choose an organization mode");
			alert.showAndWait();
		}
		
		else {
			ObservableList<Animal> details = Main.getConnection().getAdoptionAnimalsToReport(animalType, animalStatus, animalAdoptionView.getClassificationField().getValue());
			
			if(details.isEmpty()) {
				animalAdoptionView.getAnimalDetails().setVisible(false);
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("No animal found!");
				alert.showAndWait();
			}
			
			else {
				animalAdoptionView.getAgeColumn().setSortType(TableColumn.SortType.ASCENDING);
				animalAdoptionView.getNameColumn().setSortType(TableColumn.SortType.ASCENDING);
				animalAdoptionView.getReservedColumn().setSortType(TableColumn.SortType.DESCENDING);
				
				animalAdoptionView.getReservedColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isReserved()));
				animalAdoptionView.getIdColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalId()));
				animalAdoptionView.getNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalName()));
				animalAdoptionView.getTypeColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalType()));
				animalAdoptionView.getAgeColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalAge()));
				animalAdoptionView.getBreedColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalBreed()));
				animalAdoptionView.getColourColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalColour()));
				animalAdoptionView.getGenderColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalGender()));
				animalAdoptionView.getDescriptionColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalDescription()));
				animalAdoptionView.getChippedColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isChipped()));
				animalAdoptionView.getNeuteredColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isNeutered()));
				animalAdoptionView.getVaccinatedColumn().setCellValueFactory(data -> new ReadOnlyBooleanWrapper(data.getValue().getAnimalCategory().isVaccinated()));
				animalAdoptionView.getStatusColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getStatus()));
				
				animalAdoptionView.getAnimalDetails().setItems(details);
				
				if(animalAdoptionView.getOrganizationField().getValue().equalsIgnoreCase("Age")) {
					animalAdoptionView.getAnimalDetails().getSortOrder().add(animalAdoptionView.getAgeColumn());
			    }
				
				else if(animalAdoptionView.getOrganizationField().getValue().equalsIgnoreCase("Name")) {
					animalAdoptionView.getAnimalDetails().getSortOrder().add(animalAdoptionView.getNameColumn());
			    }
				
				else {
					animalAdoptionView.getAnimalDetails().getSortOrder().add(animalAdoptionView.getReservedColumn());
			    }
				
				animalAdoptionView.getAnimalDetails().setVisible(true);
			}
		}
	}
}
