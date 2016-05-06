package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import controller.ShelterFile;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AnimalList;

public class Main extends Application {
	private static Stage stage;
	private static Scene scene;
	private ShelterFile file = new ShelterFile();

	private BorderPane root;
	
	private Label shelterIcon;
	private Label shelterDescription;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setResizable(false);
			primaryStage.setTitle("Animal Shelter");
			
			root = new BorderPane();
			scene = new Scene(root, 1000, 800);
			
			file.copyAFile("Animal.dat", "Animal.txt");
			file.copyAFile("OwnerContact.dat", "OwnerContact.txt");
			file.copyAFile("InterestAdopting.dat", "InterestAdopting.txt");
			
			DisplayMenu displayMenu = new DisplayMenu();
			displayMenu.menu();
			root.setTop(displayMenu.getMenuBar());

			shelterIcon = new Label("");
			shelterIcon.setGraphic(new ImageView(new Image("file:src\\images\\shelter-icon.png")));
			root.setCenter(shelterIcon);
			
			shelterDescription = new Label("What are you looking for? Choose between lost, found and adopted animals.");
			shelterDescription.setPrefHeight(150);
			shelterDescription.setFont(Font.font("Berlin Sans FB", 20));
			BorderPane.setAlignment(shelterDescription, Pos.TOP_CENTER);
			root.setBottom(shelterDescription);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			AnimalList animalList = file.getListFromFile("Found");	
			for(int i =0; i < animalList.getAnimalList().size(); i++) {
				if(LocalDate.now().minusMonths(1).equals(animalList.getAnimalList().get(i).getAnimalCategory().getDate())) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("Transference Available");
					alert.setContentText("The animal " + animalList.getAnimalList().get(i).getAnimalId() + " is available for transference to Adoption");

					ButtonType goToTransference = new ButtonType("Transfer");
					ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
					alert.getButtonTypes().setAll(goToTransference, cancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == goToTransference){
						AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "ADD");
						primaryStage.setScene(animalAdoption);
					} 
				}
			}
			
			AnimalList adoptionList = file.getListFromFile("Adoption");
			for(int i =0; i < adoptionList.getAnimalList().size(); i++) {
				if(adoptionList.getAnimalList().get(i).getAnimalCategory().isChipped() && 
						adoptionList.getAnimalList().get(i).getAnimalCategory().isNeutered() &&
						adoptionList.getAnimalList().get(i).getAnimalCategory().isVaccinated() &&
						adoptionList.getAnimalList().get(i).getAnimalCategory().getStatus().equalsIgnoreCase("Ready") &&
						adoptionList.getAnimalList().get(i).getAnimalCategory().isReserved()) {
					
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("Animal Ready");
					alert.setContentText("The animal " + adoptionList.getAnimalList().get(i).getAnimalId() + " is ready to go home");

					ButtonType pickAnimalUp = new ButtonType("Pick Animal Up");
					ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
					alert.getButtonTypes().setAll(pickAnimalUp, cancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == pickAnimalUp){
						AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "Remove");
						primaryStage.setScene(animalAdoption);
					} 
				}
			}
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	  
		        	Alert alert = new Alert(AlertType.CONFIRMATION);
		  			alert.setHeaderText(null);
		  			alert.setContentText("Information will not be kept. \nDo you want to save?");
		  			
		  			ButtonType yes = new ButtonType("Yes", ButtonData.YES);
		  			ButtonType no = new ButtonType("No", ButtonData.NO);

		  			alert.getButtonTypes().setAll(yes, no);


		  			Optional<ButtonType> result = alert.showAndWait();
		  			try {
		  				Path animalTemporary = Paths.get("Animal.txt");
						Path ownerTemporary = Paths.get("OwnerContact.txt");
						
						Path animalSource = Paths.get("Animal.dat");
						Path ownerSource = Paths.get("OwnerContact.dat");
						
						Path personSource = Paths.get("InterestAdopting.dat");
						Path personTemporary = Paths.get("InterestAdopting.txt");
						
		  				if (result.get() == yes){
			  		        Files.delete(animalSource);
			  		        Files.move(animalTemporary, animalTemporary.resolveSibling(animalSource));
			  		        
			  		        Files.delete(ownerSource);
			  		        Files.move(ownerTemporary, ownerTemporary.resolveSibling(ownerSource));
			  		        
			  		        Files.delete(personSource);
			  		        Files.move(personTemporary, personTemporary.resolveSibling(personSource));
			  			}
			  			
			  			else if(result.get() == no){
							Files.delete(animalTemporary);
							Files.delete(ownerTemporary);
							Files.delete(personTemporary);
			  			}
		  			}
		  			
		  			catch (IOException e) {
						e.printStackTrace();
					}
		          }
		      });        
			
			stage = primaryStage;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 
	
	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		Main.stage = stage;
	}

	public static Scene getScene() {
		return scene;
	}

	public static void setScene(Scene scene) {
		Main.scene = scene;
	}
}