package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class DisplayMenu {
	private MenuBar menuBar;
	private Menu homepage, lostAnimal, foundAnimal, animalAdoption, newAdoption, generalReports;
	private MenuItem goToHomepage, addLostAnimal, reportLostAnimal, displayLostAnimal, addFoundAnimal, removeFoundAnimal, displayFoundAnimal, 
	                 reportFoundAnimal, addAnimalAdoption, addPersonAdoption, removeAnimalAdoption, displayAnimalAdoption, reportAnimalAdoption,
	                 displayAllOwners, displayAllAnimals, allocateAnimal;
	
	public void menu() {
		menuBar = new MenuBar();
		
		homepage = new Menu("");
		homepage.setGraphic(new ImageView(new Image("file:src\\images\\home-icon.png")));
		
		goToHomepage = new MenuItem("Go to Homepage");
		goToHomepage.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Main.getStage().setScene(Main.getScene());
			}
		});
		
		lostAnimal = new Menu("Lost Animal");
		foundAnimal = new Menu("Found Animal");
		animalAdoption = new Menu("Animal Adoption");
		newAdoption = new Menu("New...");
		generalReports = new Menu("General Reports");
		
		addLostAnimal = new MenuItem("New");
		addLostAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				LostAnimalView lostAnimalScene = new LostAnimalView(new BorderPane(), 1000, 800, "ADD");
				Main.getStage().setScene(lostAnimalScene);
				
			}
		});
	
		displayLostAnimal = new MenuItem("Display All");
		displayLostAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				LostAnimalView lostAnimalScene = new LostAnimalView(new BorderPane(), 1000, 800, "Display");
				Main.getStage().setScene(lostAnimalScene);
				
			}
		});
		
		reportLostAnimal = new MenuItem("Report");
		reportLostAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				LostAnimalView lostAnimalScene = new LostAnimalView(new BorderPane(), 1000, 800, "Report");
				Main.getStage().setScene(lostAnimalScene);			
			}
		});
		
		addFoundAnimal = new MenuItem("New");
		addFoundAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				FoundAnimalView foundAnimalScene = new FoundAnimalView(new BorderPane(), 1000, 800, "ADD");
				Main.getStage().setScene(foundAnimalScene);
				
			}
		});
		
		removeFoundAnimal = new MenuItem("Remove");
		removeFoundAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				FoundAnimalView foundAnimalScene = new FoundAnimalView(new BorderPane(), 1000, 800, "Remove");
				Main.getStage().setScene(foundAnimalScene);
				
			}
		});
		
		displayFoundAnimal = new MenuItem("Display All");
		displayFoundAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				FoundAnimalView foundAnimalScene = new FoundAnimalView(new BorderPane(), 1000, 800, "Display");
				Main.getStage().setScene(foundAnimalScene);
				
			}
		});
		
		reportFoundAnimal = new MenuItem("Report");
		reportFoundAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				FoundAnimalView foundAnimalScene = new FoundAnimalView(new BorderPane(), 1000, 800, "Report");
				Main.getStage().setScene(foundAnimalScene);
				
			}
		});
		
		addAnimalAdoption = new MenuItem("Animal");
		addAnimalAdoption.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "ADD");
				Main.getStage().setScene(animalAdoption);
				
			}
		});
		
		addPersonAdoption = new MenuItem("Interested Person");
		addPersonAdoption.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "Person");
				Main.getStage().setScene(animalAdoption);
				
			}
		});
		
		allocateAnimal = new MenuItem("Allocate to a Family");
		allocateAnimal.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "Allocate");
				Main.getStage().setScene(animalAdoption);
				
			}
		});

		removeAnimalAdoption = new MenuItem("Remove");
		removeAnimalAdoption.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "Remove");
				Main.getStage().setScene(animalAdoption);
				
			}
		});
		
		displayAnimalAdoption = new MenuItem("Display All");
		displayAnimalAdoption.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "Display");
				Main.getStage().setScene(animalAdoption);
				
			}
		});
		
		reportAnimalAdoption = new MenuItem("Report");
		reportAnimalAdoption.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				AnimalAdoptionView animalAdoption = new AnimalAdoptionView(new BorderPane(), 1000, 800, "Report");
				Main.getStage().setScene(animalAdoption);
				
			}
		});
		
		displayAllOwners = new MenuItem("Display Sponsorships");
		displayAllOwners.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				GeneralReportView animalAdoption = new GeneralReportView(new BorderPane(), 1000, 800, "Sponsorships");
				Main.getStage().setScene(animalAdoption);
				
			}
		});
		
		displayAllAnimals = new MenuItem("Display Animals");
		displayAllAnimals.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				GeneralReportView animalAdoption = new GeneralReportView(new BorderPane(), 1000, 800, "Animals");
				Main.getStage().setScene(animalAdoption);
				
			}
		});
					
		menuBar.getMenus().addAll(homepage, lostAnimal, foundAnimal, animalAdoption, generalReports);
		homepage.getItems().add(goToHomepage);
		lostAnimal.getItems().addAll(addLostAnimal, displayLostAnimal, reportLostAnimal);
		foundAnimal.getItems().addAll(addFoundAnimal, removeFoundAnimal, displayFoundAnimal, reportFoundAnimal);
		newAdoption.getItems().addAll(addAnimalAdoption, addPersonAdoption);
		animalAdoption.getItems().addAll(newAdoption, allocateAnimal, removeAnimalAdoption, displayAnimalAdoption, reportAnimalAdoption);
		generalReports.getItems().addAll(displayAllOwners, displayAllAnimals);
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}
}
