package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import application.FoundAnimalView;
import application.Main;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
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
import model.AnimalList;
import model.Category;
import model.FoundAnimal;
import model.Person;

public class FoundAnimalController implements EventHandler<ActionEvent>{
	FoundAnimalView foundAnimalView;
	
	String animalID, animalName, animalBreed, animalAge, animalColour, animalDescription, animalLocation, animalType;
	String ownerName, ownerEmail, ownerTelephone, ownerAddress;
	LocalDate animalDate, betweenDate;
	Toggle animalGender;
	
	public FoundAnimalController(FoundAnimalView foundAnimalView) {
		this.foundAnimalView = foundAnimalView;
	}

	@Override
	public void handle(ActionEvent event) {{
		
		if(event.getSource() == foundAnimalView.getSubmitButton()) {
			animalID = foundAnimalView.getAnimalIdField().getText();
			animalName = foundAnimalView.getAnimalNameField().getText();
			animalType = foundAnimalView.getAnimalTypeField().getValue();
			animalBreed = foundAnimalView.getAnimalBreedField().getText();
			animalGender = foundAnimalView.getGenderAnimalGroup().getSelectedToggle();
			animalAge = foundAnimalView.getAnimalAgeField().getText();
			animalColour = foundAnimalView.getAnimalColourField().getText();
			animalDescription = foundAnimalView.getAnimalDescriptionArea().getText();
			animalDate = foundAnimalView.getAnimalDateField().getValue();
			animalLocation = foundAnimalView.getAnimalLocationField().getText();
			
			submit();
		}
		
		else if(event.getSource() == foundAnimalView.getClearButton()) {
			clearSubmit();
		}
		
		else if(event.getSource() == foundAnimalView.getCancelButton()){
			cancel();
		}
		
		else if(event.getSource() == foundAnimalView.getSearchToDeleteButton()) {
			animalID = foundAnimalView.getAnimalIdField().getText();
			animalName = foundAnimalView.getAnimalNameField().getText();
			animalBreed = foundAnimalView.getAnimalBreedField().getText();
			animalAge = foundAnimalView.getAnimalAgeField().getText();
			animalColour = foundAnimalView.getAnimalColourField().getText();
			animalDescription = foundAnimalView.getAnimalDescriptionArea().getText();
			animalDate = foundAnimalView.getAnimalDateField().getValue();
			animalLocation = foundAnimalView.getAnimalLocationField().getText();
			
			searchToDelete();
		}
		
		else if(event.getSource() == foundAnimalView.getRemoveButton()){
			animalID = foundAnimalView.getAnimalIdField().getText();
			
			ownerName = foundAnimalView.getOwnerNameField().getText();
			ownerTelephone = foundAnimalView.getOwnerTelephoneField().getText();
			ownerEmail = foundAnimalView.getOwnerEmailField().getText();
			ownerAddress = foundAnimalView.getOwnerAddressField().getText();
			
			remove();
		}
		
		else if(event.getSource() == foundAnimalView.getAddFromLostButton()) {
			animalID = foundAnimalView.getAnimalSearchField().getText();
			animalDate = foundAnimalView.getAnimalDateField().getValue();
			animalLocation = foundAnimalView.getAnimalLocationField().getText();
			
			addFromLost();
		}
		
		else if(event.getSource() == foundAnimalView.getGenerateReportButton()) {
			animalType = foundAnimalView.getAnimalTypeReport().getValue();
			animalDate = foundAnimalView.getAnimalDateField().getValue();
			betweenDate = foundAnimalView.getBetweenDateField().getValue();
			animalLocation = foundAnimalView.getAnimalLocationField().getText();
			
			generateReport();
		}
	}
}

	public void submit() {

		if(animalID.isEmpty() || animalType == "" || animalDate == null  || 
				animalLocation.isEmpty() || animalDescription.isEmpty() || 
				(!foundAnimalView.getFemaleAnimal().isSelected() && !foundAnimalView.getMaleAnimal().isSelected())) {
				
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Empty Field!");
			alert.setContentText("One or more of the following fields is empty. Please fill it."
					+ "\n* ID \n* Type \n* Gender \n* Description \n* Date \n* Location");
			alert.showAndWait();	
		}
			
		else {
			if(!Main.getConnection().searchAnimalId(Integer.parseInt(animalID))) {
				RadioButton gender = (RadioButton) animalGender;
					
				Animal animal = new Animal();
				animal.setAnimalId(Integer.parseInt(animalID));
				animal.setAnimalAge(Integer.parseInt(animalAge));
				animal.setAnimalBreed(animalBreed);
				animal.setAnimalColour(animalColour);
				animal.setAnimalDescription(animalDescription);
				animal.setAnimalGender(gender.getText());
				animal.setAnimalName(animalName);
				animal.setAnimalType(animalType);
					
				Category category = new FoundAnimal(animalDate, null, animalLocation);
				animal.setAnimalCategory(category);
	          		
				Main.getConnection().addAnimal(animal, "Found");
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Animal added successfully!");
				alert.showAndWait();
				
				clearSubmit();
			}
		}	
	}
	
	public void clearSubmit() {
		foundAnimalView.getAnimalIdField().setText("");
		foundAnimalView.getAnimalNameField().setText("");
		foundAnimalView.getAnimalTypeField().setValue("");;
		foundAnimalView.getAnimalBreedField().setText("");
		foundAnimalView.getAnimalAgeField().setText("");
		foundAnimalView.getFemaleAnimal().setSelected(false);
		foundAnimalView.getMaleAnimal().setSelected(false);
		foundAnimalView.getAnimalColourField().setText("");
		foundAnimalView.getAnimalDateField().setValue(null);
		foundAnimalView.getAnimalLocationField().setText("");
		foundAnimalView.getAnimalDescriptionArea().setText("");
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
		if(!foundAnimalView.getAnimalSearchField().getText().isEmpty()) {
			foundAnimalView.getAnimalIdName().setVisible(true);
			foundAnimalView.getAnimalType().setVisible(true);
			foundAnimalView.getAnimalBreedAge().setVisible(true);
			foundAnimalView.getAnimalGenderColour().setVisible(true);
			foundAnimalView.getAnimalDescriptionLabel().setVisible(true);
			foundAnimalView.getAnimalDescriptionArea().setVisible(true);
			foundAnimalView.getAnimalDateLocation().setVisible(true);
			foundAnimalView.getPersonDetails().setVisible(true);
			foundAnimalView.getOwnerName().setVisible(true);
			foundAnimalView.getOwnerTelephoneEmail().setVisible(true);
			foundAnimalView.getOwnerAddress().setVisible(true);
			foundAnimalView.getActionButtons().setVisible(true);
			
			Animal animal = Main.getConnection().searchAnimalById("Found", Integer.parseInt(foundAnimalView.getAnimalSearchField().getText()));
			
			if(animal != null) {
				String animalType = animal.getAnimalType();
				String animalGender = animal.getAnimalGender();
				
				foundAnimalView.getAnimalIdField().setText(String.valueOf(animal.getAnimalId()));
				foundAnimalView.getAnimalNameField().setText(animal.getAnimalName());
				foundAnimalView.getAnimalBreedField().setText(animal.getAnimalBreed());
				foundAnimalView.getAnimalAgeField().setText(String.valueOf(animal.getAnimalAge()));
				foundAnimalView.getAnimalColourField().setText(animal.getAnimalColour());
				foundAnimalView.getAnimalDescriptionArea().setText(animal.getAnimalDescription());
				foundAnimalView.getAnimalDateField().setValue(animal.getAnimalCategory().getDate());
				foundAnimalView.getAnimalLocationField().setText(animal.getAnimalCategory().getLocation());
				
				if(animalGender.equalsIgnoreCase("Female")) {
					foundAnimalView.getFemaleAnimal().setSelected(true);
					foundAnimalView.getMaleAnimal().setSelected(false);
				}
				
				else if(animalGender.equalsIgnoreCase("Male")) {
					foundAnimalView.getFemaleAnimal().setSelected(false);
					foundAnimalView.getMaleAnimal().setSelected(true);
				}
				
				if(animalType.equalsIgnoreCase("Cat")) {
					foundAnimalView.getAnimalTypeField().setValue("Cat");
				}
				
				else {
					foundAnimalView.getAnimalTypeField().setValue("Dog");
				}
				
				foundAnimalView.getAnimalSearchField().setText("");
			}
			
			else {
				clearRemove();
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Animal couldn't be found!");
				alert.showAndWait();
			}
		}
		
		else {
			clearRemove();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("ID must not be empty!");
			alert.showAndWait();
		}
	}
	
	public ObservableList<Animal> displayAll() {
		ObservableList<Animal> details = Main.getConnection().getAnimals("Found");
				
		foundAnimalView.getIdColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalId()));
		foundAnimalView.getNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalName()));
		foundAnimalView.getTypeColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalType()));
		foundAnimalView.getAgeColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalAge()));
		foundAnimalView.getBreedColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalBreed()));
		foundAnimalView.getColourColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalColour()));
		foundAnimalView.getGenderColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalGender()));
		foundAnimalView.getDescriptionColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalDescription()));
		foundAnimalView.getDateColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getDate().toString()));
		foundAnimalView.getLocationColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getLocation()));

		return details;
	}
	
	public void remove() {
		if(ownerName.isEmpty() || ownerTelephone.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Owner's name and/or telephone must not be empty!");
			alert.showAndWait();
		}
		
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setContentText("Do you really want to delete?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				
				Alert confirmation = new Alert(AlertType.INFORMATION);
				confirmation.setHeaderText(null);
				confirmation.setContentText("Animal removed successfully!");
				confirmation.showAndWait();
					
				Person person = new Person();
				person.setPersonName(ownerName);
				person.setPersonAddress(ownerAddress);
				person.setPersonPhone(ownerTelephone);
				person.setPersonEmail(ownerEmail);
					
				Main.getConnection().addPerson(person, Main.getConnection().getIdCategoryByIdAnimal(Integer.parseInt(animalID)));
				Main.getConnection().deleteAnimal("Found", Integer.parseInt(animalID));
				
				clearRemove();
			} 
		}
	}
	
	public void clearRemove() {
		foundAnimalView.getAnimalIdName().setVisible(false);
		foundAnimalView.getAnimalType().setVisible(false);
		foundAnimalView.getAnimalBreedAge().setVisible(false);
		foundAnimalView.getAnimalGenderColour().setVisible(false);
		foundAnimalView.getAnimalDescriptionLabel().setVisible(false);
		foundAnimalView.getAnimalDescriptionArea().setVisible(false);
		foundAnimalView.getAnimalDateLocation().setVisible(false);
		foundAnimalView.getPersonDetails().setVisible(false);
		foundAnimalView.getOwnerName().setVisible(false);
		foundAnimalView.getOwnerTelephoneEmail().setVisible(false);
		foundAnimalView.getOwnerAddress().setVisible(false);
		foundAnimalView.getActionButtons().setVisible(false);
	}
	
	public void addFromLost() {
		if(animalID.isEmpty() || animalDate == null || animalLocation.isEmpty()) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Empty Field!");
			alert.setContentText("One or more of the following fields is empty. Please fill it."
					+ "\n* ID \n* Date \n* Location");
			alert.showAndWait();		
		}
		
		else {
			try {
				animalList = file.getListFromFile("All");
			} catch (FileNotFoundException e1) {
				System.out.println("File couldn't be found!");
			} catch (IOException e1) {
				System.out.println("I/O Problem!");
			}
			
			if(animalList.searchAnimal(Integer.parseInt(animalID))) {
				try {							
					file.fromLostToFound(Integer.parseInt(animalID), String.valueOf(animalDate), animalLocation);
					
							
					Alert information = new Alert(AlertType.INFORMATION);
					information.setHeaderText(null);
					information.setContentText("Animal added as found successfully!");
					information.showAndWait();
							
					foundAnimalView.getAnimalSearchField().setText("");
					foundAnimalView.getAnimalDateField().setValue(null);
					foundAnimalView.getAnimalLocationField().setText("");
				}
				
				catch (NumberFormatException e) {
					System.out.println("Animal ID is not an integer");
				} catch (IOException e) {
					System.out.println("I/O Problem!");
				} 
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
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
			if(animalDate == null && animalLocation.isEmpty() && animalType == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Fill at least two fields");
				alert.showAndWait();
			}
			
			else {
				animalList = file.getListToReport("Found", animalType, animalDate, betweenDate, animalLocation);
				
				if(animalList.getAnimalList().size() == 0) {
					foundAnimalView.getAnimalDetails().setVisible(false);
					
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("No animal found!");
					alert.showAndWait();
				}
				
				else {
					details.addAll(animalList.getAnimalList());
					foundAnimalView.getGenderColumn().setSortType(TableColumn.SortType.ASCENDING);
					
					foundAnimalView.getIdColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalId()));
				    foundAnimalView.getNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalName()));
				    foundAnimalView.getTypeColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalType()));
				    foundAnimalView.getAgeColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalAge()));
				    foundAnimalView.getBreedColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalBreed()));
				    foundAnimalView.getColourColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalColour()));
				    foundAnimalView.getGenderColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalGender()));
				    foundAnimalView.getDescriptionColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalDescription()));
				    foundAnimalView.getDateColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getDate().toString()));
				    foundAnimalView.getLocationColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getLocation()));
				    
				    foundAnimalView.getAnimalDetails().setItems(details);
				    if(animalLocation.isEmpty()) {
				    	foundAnimalView.getAnimalDetails().getSortOrder().add(foundAnimalView.getGenderColumn());
				    }
				    foundAnimalView.getAnimalDetails().setVisible(true);
				}
			}
		    
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}
	}
}
