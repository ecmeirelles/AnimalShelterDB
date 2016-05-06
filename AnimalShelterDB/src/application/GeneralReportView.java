package application;

import controller.GeneralReportController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import model.Animal;
import model.Person;

public class GeneralReportView extends Scene {
	private Pane actionPane;
	private Label sectionTitle;
	private HBox title;
	private TableView<Person> sponsorshipsDetails;
	private TableView<Animal> animalDetails;
	private TableColumn<Animal, Number> idColumn, ageColumn;
	private TableColumn<Person, String> nameColumn, telephoneColumn, emailColumn, addressColumn;
	private TableColumn<Animal, String> animalNameColumn, typeColumn, colourColumn, genderColumn, descriptionColumn, breedColumn;

	public GeneralReportView(BorderPane root, double width, double height, String action) {
		super(root, width, height);
		
		HBox adaptableBox = new HBox();
		title = new HBox();
		title.setSpacing(30);
		
		DisplayMenu displayMenu = new DisplayMenu();
		displayMenu.menu();
		
		if(action.equalsIgnoreCase("Sponsorships")) {
			actionPane = displayAllSponsorshipsAction();
		}
		
		else {
			actionPane = displayAllAnimalsAction();
		}
		
		root.setTop(displayMenu.getMenuBar());
		adaptableBox.getChildren().add(actionPane);
		root.setCenter(adaptableBox);
	}
	
	@SuppressWarnings("unchecked")
	public Pane displayAllSponsorshipsAction() {
		GridPane displaySponsorshipsPane = new GridPane();
		displaySponsorshipsPane.setAlignment(Pos.CENTER);
		displaySponsorshipsPane.setHgap(30);
		displaySponsorshipsPane.setVgap(12);
		displaySponsorshipsPane.setPadding(new Insets(100));

		sectionTitle = new Label("SPONSORSHIPS");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    sponsorshipsDetails = new TableView<>();
	    sponsorshipsDetails.setMinWidth(600);
	    
	    nameColumn = new TableColumn<>("Name");
        telephoneColumn = new TableColumn<>("Telephone");
        emailColumn = new TableColumn<>("Email");
        addressColumn = new TableColumn<>("Address");

        sponsorshipsDetails.getColumns().addAll(nameColumn, telephoneColumn, emailColumn, addressColumn);
        sponsorshipsDetails.setItems(new GeneralReportController(this).displaySponsorships());
        
        displaySponsorshipsPane.add(title, 0, 0);
        displaySponsorshipsPane.add(sponsorshipsDetails, 0, 5);
	    
	    return displaySponsorshipsPane;
	}
	
	@SuppressWarnings("unchecked")
	public Pane displayAllAnimalsAction() {
		GridPane displayAnimalPane = new GridPane();
		displayAnimalPane.setAlignment(Pos.CENTER);
		displayAnimalPane.setHgap(30);
		displayAnimalPane.setVgap(12);
		displayAnimalPane.setPadding(new Insets(100));

		sectionTitle = new Label("SPONSORSHIPS");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);

	    animalDetails = new TableView<>();
	    animalDetails.setMinWidth(600);
	    
        idColumn = new TableColumn<>("ID");
        animalNameColumn = new TableColumn<>("Name");
        typeColumn = new TableColumn<>("Type");
        ageColumn = new TableColumn<>("Age");
        breedColumn = new TableColumn<>("Breed");
        colourColumn = new TableColumn<>("Colour");
        genderColumn = new TableColumn<>("Gender");
        descriptionColumn = new TableColumn<>("Description");
        

        animalDetails.getColumns().addAll(idColumn, animalNameColumn, typeColumn, ageColumn, breedColumn, colourColumn, genderColumn, descriptionColumn);
        animalDetails.setItems(new GeneralReportController(this).displayAnimals());
        
        displayAnimalPane.add(title, 0, 0);
        displayAnimalPane.add(animalDetails, 0, 5);
	    
	    return displayAnimalPane;
	}

	public TableView<Person> getSponsorshipsDetails() {
		return sponsorshipsDetails;
	}

	public TableColumn<Person, String> getNameColumn() {
		return nameColumn;
	}

	public TableColumn<Person, String> getTelephoneColumn() {
		return telephoneColumn;
	}

	public TableColumn<Person, String> getEmailColumn() {
		return emailColumn;
	}

	public TableColumn<Person, String> getAddressColumn() {
		return addressColumn;
	}

	public TableView<Animal> getAnimalDetails() {
		return animalDetails;
	}

	public TableColumn<Animal, Number> getIdColumn() {
		return idColumn;
	}

	public TableColumn<Animal, Number> getAgeColumn() {
		return ageColumn;
	}

	public TableColumn<Animal, String> getAnimalNameColumn() {
		return animalNameColumn;
	}

	public TableColumn<Animal, String> getTypeColumn() {
		return typeColumn;
	}

	public TableColumn<Animal, String> getColourColumn() {
		return colourColumn;
	}

	public TableColumn<Animal, String> getGenderColumn() {
		return genderColumn;
	}

	public TableColumn<Animal, String> getDescriptionColumn() {
		return descriptionColumn;
	}

	public TableColumn<Animal, String> getBreedColumn() {
		return breedColumn;
	}
}
