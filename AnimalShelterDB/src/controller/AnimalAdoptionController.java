package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import application.AnimalAdoptionView;
import application.Main;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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

public class AnimalAdoptionController implements EventHandler<ActionEvent>{
	AnimalAdoptionView animalAdoptionView;
	ShelterFile file = new ShelterFile();
	AnimalList animalList = new AnimalList();
	
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
			try {
				animalList = file.getListFromFile("Found");
				int index = animalList.getIndexBySearch(Integer.parseInt(animalID));

				if(LocalDate.now().minusMonths(1).equals(animalList.getAnimalList().get(index).getAnimalCategory().getDate()) ||
						LocalDate.now().minusMonths(1).isAfter(animalList.getAnimalList().get(index).getAnimalCategory().getDate())) {
						
					RadioButton chipped = (RadioButton)animalChipped;
					RadioButton neutered = (RadioButton)animalNeutered;
					RadioButton vaccinated = (RadioButton)animalVaccinated;

					file.fromFoundToAdoption(Integer.parseInt(animalID), animalName, chipped.getText(), neutered.getText(), 
							vaccinated.getText(), animalStatus, "Not Reserved");
							
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
			} catch(FileNotFoundException e) {
				System.out.println("File couldn't be found");
			} catch(IOException e) {
				System.out.println("I/O Problem");
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
			try {
				animalList = file.getListFromFile("Adoption");
				int index = animalList.getIndexBySearch(Integer.parseInt(animalID));
				
				if(index != -1) {
					animalAdoptionView.getAnimalIdName().setVisible(true);
					animalAdoptionView.getAnimalType().setVisible(true);
					animalAdoptionView.getAnimalBreedAge().setVisible(true);
					animalAdoptionView.getAnimalGenderColour().setVisible(true);
					animalAdoptionView.getAnimalDescriptionLabel().setVisible(true);
					animalAdoptionView.getAnimalDescriptionArea().setVisible(true);
					animalAdoptionView.getNeuteredChipped().setVisible(true);
					animalAdoptionView.getVaccinatedStatus().setVisible(true);
					animalAdoptionView.getActionButtons().setVisible(true);
					
					String animalGender = animalList.getAnimalList().get(index).getAnimalGender();
					boolean isNeutered = animalList.getAnimalList().get(index).getAnimalCategory().isNeutered();
					boolean isChipped = animalList.getAnimalList().get(index).getAnimalCategory().isChipped();
					boolean isVaccinated = animalList.getAnimalList().get(index).getAnimalCategory().isVaccinated();
								
					animalAdoptionView.getAnimalIdField().setText(String.valueOf(animalList.getAnimalList().get(index).getAnimalId()));
					animalAdoptionView.getAnimalNameField().setText(animalList.getAnimalList().get(index).getAnimalName());
					animalAdoptionView.getAnimalTypeField().setValue(animalList.getAnimalList().get(index).getAnimalType());
					animalAdoptionView.getAnimalBreedField().setText(animalList.getAnimalList().get(index).getAnimalBreed());
					animalAdoptionView.getAnimalAgeField().setText(String.valueOf(animalList.getAnimalList().get(index).getAnimalAge()));
					animalAdoptionView.getAnimalColourField().setText(animalList.getAnimalList().get(index).getAnimalColour());
					animalAdoptionView.getAnimalDescriptionArea().setText(animalList.getAnimalList().get(index).getAnimalDescription());
					animalAdoptionView.getStatusField().setValue(animalList.getAnimalList().get(index).getAnimalCategory().getStatus());
					
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
			} catch(FileNotFoundException e) {
				System.out.println("File couldn't be found");
			} catch(IOException e) {
				System.out.println("I/O Problem");
			}
		}
	}

	public ObservableList<Animal> displayAll() {
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
			animalList = file.getListFromFile("Adoption");
			details.addAll(animalList.getAnimalList());
				
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
			
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}

		return details;
	}
	
	public void remove() {
		try {
			animalList = file.getListFromFile("Adoption");
			int i = animalList.getIndexBySearch(Integer.parseInt(animalAdoptionView.getAnimalIdField().getText()));
			
			if(animalList.getAnimalList().get(i).getAnimalCategory().isChipped() && 
					animalList.getAnimalList().get(i).getAnimalCategory().isNeutered() &&
					animalList.getAnimalList().get(i).getAnimalCategory().isVaccinated() &&
					animalList.getAnimalList().get(i).getAnimalCategory().getStatus().equalsIgnoreCase("Ready") &&
					animalList.getAnimalList().get(i).getAnimalCategory().isReserved()) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText(null);
				alert.setContentText("Do you really want to delete?");
	
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					file.removeAnimalFromFile(Integer.parseInt(animalID));
					
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
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
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
			try {
				animalList = file.getListFromFile("Found");
				int index = animalList.getIndexBySearch(Integer.parseInt(animalID));
				
				if(index != -1) {
					animalAdoptionView.getAnimalIdName().setVisible(true);
					animalAdoptionView.getAnimalType().setVisible(true);
					animalAdoptionView.getAnimalBreedAge().setVisible(true);
					animalAdoptionView.getAnimalGenderColour().setVisible(true);
					animalAdoptionView.getAnimalDescriptionLabel().setVisible(true);
					animalAdoptionView.getAnimalDescriptionArea().setVisible(true);
					animalAdoptionView.getNeuteredChipped().setVisible(true);
					animalAdoptionView.getVaccinatedStatus().setVisible(true);
					animalAdoptionView.getActionButtons().setVisible(true);
					
					String animalType = animalList.getAnimalList().get(index).getAnimalType();
					String animalGender = animalList.getAnimalList().get(index).getAnimalGender();
					
					animalAdoptionView.getAnimalIdField().setText(String.valueOf(animalList.getAnimalList().get(index).getAnimalId()));
					animalAdoptionView.getAnimalNameField().setText(animalList.getAnimalList().get(index).getAnimalName());
					animalAdoptionView.getAnimalBreedField().setText(animalList.getAnimalList().get(index).getAnimalBreed());
					animalAdoptionView.getAnimalAgeField().setText(String.valueOf(animalList.getAnimalList().get(index).getAnimalAge()));
					animalAdoptionView.getAnimalColourField().setText(animalList.getAnimalList().get(index).getAnimalColour());
					animalAdoptionView.getAnimalDescriptionArea().setText(animalList.getAnimalList().get(index).getAnimalDescription());
					
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
				
			} catch (FileNotFoundException e) {
				System.out.println("File couldn't be found");
			} catch (IOException e) {
				System.out.println("I/O Problem");
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
			try {
				file.writeInFile("InterestAdopting.txt", personName + "\t" + personTelephone + "\t" + personEmail + "\t" + personAddress);
							
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Person added successfully!");
				alert.showAndWait();
				
				animalAdoptionView.getPersonNameField().setText("");
				animalAdoptionView.getPersonEmailField().setText("");
				animalAdoptionView.getPersonTelephoneField().setText("");
				animalAdoptionView.getPersonAddressField().setText("");

			} catch(FileNotFoundException e) {
				System.out.println("File couldn't be found");
			}
		}	
	}
	
	public void updateInformation() {
		try {
			RadioButton chipped = (RadioButton)animalChipped;
			RadioButton neutered = (RadioButton)animalNeutered;
			RadioButton vaccinated = (RadioButton)animalVaccinated;
			
			file.updateAdoptionFile(Integer.parseInt(animalID), chipped.getText(), neutered.getText(), vaccinated.getText(), animalStatus);
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Animal updated successfully!");
			alert.showAndWait();
			
			animalAdoptionView.getAnimalDetails().setItems(displayAll());
			animalAdoptionView.getAllowEditAnimalCheckBox().setSelected(false);
			
		} catch (FileNotFoundException e) {
			System.out.println("File couldn't be found");
		} catch(IOException e) {
			System.out.println("I/O Problem");
		}
	}
	
	public void allocateAnimal() {
		try {
			if(animalID == null || personName == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("No field must be empty!");
				alert.showAndWait();
			}
			
			else {
				animalList = file.getListFromFile("Adoption");
				int index = animalList.getIndexBySearch(Integer.parseInt(animalID));
				
				if(index != -1) {
					if(animalList.getAnimalList().get(index).getAnimalCategory().isReserved()) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setContentText("This animal is already reserved!");
						alert.showAndWait();
					}
					
					else if(file.searchReservationByPerson(Integer.parseInt(animalID))) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setContentText("This person is allocated to another animal!");
						alert.showAndWait();
					}
					
					else {
						file.allocateAnimalToFamily(Integer.parseInt(animalID), personName);
						
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
			
		} catch (NumberFormatException e) {
			System.out.println("Wrong number format!");
		} catch (FileNotFoundException e) {
			System.out.println("File couldn't be found!");
		} catch (IOException e) {
			System.out.println("I/O Problem!");
		}
	}
	
	public void generateReport() {
		ObservableList<Animal> details = FXCollections.observableArrayList();
		
		try {
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
				animalList = file.getAdoptionListToReport(animalType, animalStatus, animalAdoptionView.getClassificationField().getValue());
				
				if(animalList.getAnimalList().size() == 0) {
					animalAdoptionView.getAnimalDetails().setVisible(false);
					
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("No animal found!");
					alert.showAndWait();
				}
				
				else {
					details.addAll(animalList.getAnimalList());
					
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
		    
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}
	}
}
