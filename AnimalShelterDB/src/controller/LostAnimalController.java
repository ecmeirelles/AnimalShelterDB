package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import application.LostAnimalView;
import application.Main;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import model.Animal;
import model.AnimalList;

public class LostAnimalController extends ActionEvent implements EventHandler<ActionEvent> {
	private static final long serialVersionUID = 1L;
	ShelterFile file = new ShelterFile();
	AnimalList animalList = new AnimalList();
	LostAnimalView lostAnimalView;
	
	String animalID, animalName, animalBreed, animalAge, animalColour, animalDescription, animalLocation, animalType;
	String ownerName, ownerEmail, ownerTelephone, ownerAddress;
	LocalDate animalDate;
	Toggle animalGender;
	
	public LostAnimalController(LostAnimalView lostAnimalView) {
		this.lostAnimalView = lostAnimalView;
	}

	@Override
	public void handle(ActionEvent event) {{
		if(event.getSource() == lostAnimalView.getSubmitButton()) {
			animalID = lostAnimalView.getAnimalIdField().getText();
			animalName = lostAnimalView.getAnimalNameField().getText();
			animalType = lostAnimalView.getAnimalTypeField().getValue();
			animalBreed = lostAnimalView.getAnimalBreedField().getText();
			animalGender = lostAnimalView.getGenderAnimalGroup().getSelectedToggle();
			animalAge = lostAnimalView.getAnimalAgeField().getText();
			animalColour = lostAnimalView.getAnimalColourField().getText();
			animalDescription = lostAnimalView.getAnimalDescriptionArea().getText();
			animalDate = lostAnimalView.getAnimalDateField().getValue();
			animalLocation = lostAnimalView.getAnimalLocationField().getText();
			
			ownerName = lostAnimalView.getOwnerNameField().getText();
			ownerTelephone = lostAnimalView.getOwnerTelephoneField().getText();
			ownerEmail = lostAnimalView.getOwnerEmailField().getText();
			ownerAddress = lostAnimalView.getOwnerAddressField().getText();
			
			submit();
		}
		
		else if(event.getSource() == lostAnimalView.getClearButton()) {
			clear();
		}
		
		else if(event.getSource() == lostAnimalView.getCancelButton()){
			cancel();
		}
		
		else if(event.getSource() == lostAnimalView.getGenerateReportButton()) {
			animalType = lostAnimalView.getAnimalTypeReport().getValue();
			animalDate = lostAnimalView.getAnimalDateField().getValue();
			animalLocation = lostAnimalView.getAnimalLocationField().getText();
			
			generateReport();
		}
	}
}

	public void submit() {

		try {
			if(animalID.isEmpty() || animalType == "" || animalDate == null  || 
					animalLocation.isEmpty() || animalDescription.isEmpty() || 
					(!lostAnimalView.getFemaleAnimal().isSelected() && !lostAnimalView.getMaleAnimal().isSelected())) {
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Empty Field!");
				alert.setContentText("One or more of the following fields is empty. Please fill it."
						+ "\n* ID \n* Type \n* Gender \n* Description \n* Date \n* Location");

				alert.showAndWait();	
			}
			
			else if(ownerName.isEmpty() || ownerTelephone.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Empty Field!");
				alert.setContentText("Owner name and/or telephone must not be empty.");

				alert.showAndWait();
			}
			
			else {
				animalList = file.getListFromFile("All");
					
				if(!animalList.searchAnimal(Integer.parseInt(animalID)) && !file.searchAnimalIdByPerson(Integer.parseInt(animalID))) {
					RadioButton gender = (RadioButton) animalGender;
					
					file.writeInFile("OwnerContact.txt", "Lost" + "\t" + animalID + "\t" + ownerName + "\t" + ownerTelephone + "\t" 
							+ ownerEmail + "\t" + ownerAddress);
					
					file.writeInFile("Animal.txt", "Lost" + "\t" + animalID + "\t" + animalName + "\t" + animalType + "\t" 
						+ animalBreed + "\t" + animalAge + "\t" + gender.getText() + "\t" + animalColour + "\t" 
						+ animalDescription + "\t" + animalDate + "\t" + animalLocation);
						
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText(null);
					alert.setContentText("Animal added successfully!");
					alert.showAndWait();
						
					clear();
				}
					
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("ID detected! Animal already exists.");
					alert.showAndWait();
				}
			}
			
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}
	}
	
	public void clear() {
		lostAnimalView.getAnimalIdField().setText("");
		lostAnimalView.getAnimalNameField().setText("");
		lostAnimalView.getAnimalTypeField().setValue("");;
		lostAnimalView.getAnimalBreedField().setText("");
		lostAnimalView.getAnimalAgeField().setText("");
		lostAnimalView.getFemaleAnimal().setSelected(false);
		lostAnimalView.getMaleAnimal().setSelected(false);
		lostAnimalView.getAnimalColourField().setText("");
		lostAnimalView.getAnimalDateField().setValue(null);
		lostAnimalView.getAnimalLocationField().setText("");
		lostAnimalView.getAnimalDescriptionArea().setText("");
		lostAnimalView.getOwnerNameField().setText("");
		lostAnimalView.getOwnerTelephoneField().setText("");
		lostAnimalView.getOwnerEmailField().setText("");
		lostAnimalView.getOwnerAddressField().setText("");
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

	public ObservableList<Animal> displayAll() {
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
			animalList = file.getListFromFile("Lost");
			
			if(animalList.getAnimalList().size() != 0) {
				details.addAll(animalList.getAnimalList());
				
				lostAnimalView.getIdColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalId()));
			    lostAnimalView.getNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalName()));
			    lostAnimalView.getTypeColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalType()));
			    lostAnimalView.getAgeColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalAge()));
			    lostAnimalView.getBreedColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalBreed()));
			    lostAnimalView.getColourColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalColour()));
			    lostAnimalView.getGenderColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalGender()));
			    lostAnimalView.getDescriptionColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalDescription()));
			    lostAnimalView.getDateColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getDate().toString()));
			    lostAnimalView.getLocationColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getLocation()));
			}
			
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}

		return details;
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
				animalList = file.getListToReport("Lost", animalType, animalDate, null, animalLocation);
				
				if(animalList.getAnimalList().size() == 0) {
					lostAnimalView.getAnimalDetails().setVisible(false);
					
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("No animal found!");
					alert.showAndWait();
				}
				
				else {
					details.addAll(animalList.getAnimalList());
					
					lostAnimalView.getIdColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalId()));
				    lostAnimalView.getNameColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalName()));
				    lostAnimalView.getTypeColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalType()));
				    lostAnimalView.getAgeColumn().setCellValueFactory(data -> new ReadOnlyIntegerWrapper(data.getValue().getAnimalAge()));
				    lostAnimalView.getBreedColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalBreed()));
				    lostAnimalView.getColourColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalColour()));
				    lostAnimalView.getGenderColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalGender()));
				    lostAnimalView.getDescriptionColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalDescription()));
				    lostAnimalView.getDateColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getDate().toString()));
				    lostAnimalView.getLocationColumn().setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAnimalCategory().getLocation()));
				    
				    lostAnimalView.getAnimalDetails().setItems(details);
				    lostAnimalView.getAnimalDetails().setVisible(true);
				}
			}
		    
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}
	}
}
