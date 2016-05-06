package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import controller.AnimalAdoptionController;
import controller.ShelterFile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import model.Animal;
import model.AnimalList;
import model.Person;

public class AnimalAdoptionView extends Scene {
	private Pane actionPane;
	private VBox editDetails;
	private HBox title, animalIdName, date, animalType, animalBreedAge, animalGenderColour, animalSearch,
	             personName, personTelephoneEmail, personAddress, neuteredChipped, vaccinatedStatus, actionButtons;
	private Label sectionTitle, animalIdLabel, animalNameLabel, animalDateLabel, animalSearchLabel, animalDescriptionLabel,
	              animalColourLabel, animalAgeLabel, animalTypeLabel, animalGenderLabel, animalBreedLabel, neuteredLabel, 
	              chippedLabel, vaccinatedLabel, statusLabel, personNameLabel, personEmailLabel, personTelephoneLabel, personAddressLabel,
	              organizeByLabel;	
	private TextField animalIdField, animalNameField, animalLocationField, animalColourField, animalAgeField, animalBreedField,
					  personNameField, personTelephoneField, personEmailField, personAddressField, animalSearchField;
	private TextArea animalDescriptionArea;
	private RadioButton femaleAnimal, maleAnimal, yesNeutered, noNeutered, yesChipped, noChipped, yesVaccinated, noVaccinated;
	private ToggleGroup genderAnimalGroup, neuteredGroup, chippedGroup, vaccinatedGroup;
	private DatePicker animalDateField;	
	private Button removeButton, cancelButton, submitButton, clearButton, searchToAddButton, searchToDisplayButton, searchToDeleteButton,
	               addPersonButton, updateButton, allocateButton, generateReportButton;
	private CheckBox allowEditAnimalCheckBox;
	private ObservableList<String> types = FXCollections.observableArrayList("Cat", "Dog");
	private ObservableList<String> moreTypes = FXCollections.observableArrayList("All", "Cat", "Dog");
	private ObservableList<String> status = FXCollections.observableArrayList("in Training", "Ready");
	private ObservableList<String> organizations = FXCollections.observableArrayList("Age", "Name", "Reservation");
	private ObservableList<String> classifications = FXCollections.observableArrayList("All", "Adults", "Puppies");
	
	private TableView<Animal> animalDetails;
	private TableColumn<Animal, Number> idColumn, ageColumn;
	private TableColumn<Animal, String> nameColumn, typeColumn, colourColumn, genderColumn, descriptionColumn, breedColumn, statusColumn;
	private TableColumn<Animal, Boolean> chippedColumn, neuteredColumn, vaccinatedColumn, reservedColumn;
	
	final ComboBox<String> animalTypeField = new ComboBox<String>(types);
	final ComboBox<String> statusField = new ComboBox<String>(status);
	final ComboBox<String> animalTypeReport = new ComboBox<String>(moreTypes);
	final ComboBox<String> organizationField = new ComboBox<String>(organizations);
	final ComboBox<String> classificationField = new ComboBox<String>(classifications);
	private ComboBox<String> adoptionIds, adoptionNames;
    
	public AnimalAdoptionView(BorderPane root, double width, double height, String action) {
		super(root, width, height);
		
		HBox adaptableBox = new HBox();
		title = new HBox();
	    animalIdName = new HBox();
	    animalIdName.setSpacing(30);
	    animalType = new HBox();
	    animalType.setSpacing(30);
	    animalBreedAge = new HBox();
	    animalBreedAge.setSpacing(30);
	    animalGenderColour = new HBox();
	    animalGenderColour.setSpacing(30);
	    date = new HBox();
	    date.setSpacing(30);
	    editDetails = new VBox();
	    editDetails.setSpacing(15);
	    personName = new HBox();
	    personName.setSpacing(30);
	    personTelephoneEmail = new HBox();
	    personTelephoneEmail.setSpacing(30);
	    personAddress = new HBox();
	    personAddress.setSpacing(30);
	    actionButtons = new HBox();
	    actionButtons.setSpacing(20);
	    animalSearch = new HBox();
		animalSearch.setSpacing(30);
		neuteredChipped = new HBox();
		neuteredChipped.setSpacing(30);
		vaccinatedStatus = new HBox();
		vaccinatedStatus.setSpacing(30);
		
		DisplayMenu displayMenu = new DisplayMenu();
		displayMenu.menu();

		if(action.equalsIgnoreCase("ADD")) {
			actionPane = addAnimalAdoptionAction();
		}
		
		else if(action.equalsIgnoreCase("Remove")) {
			actionPane = removeAnimalAdoptionAction();
		}
		
		else if(action.equalsIgnoreCase("Display")){
			actionPane = displayAnimalAdoptionAction();
		}
		
		else if(action.equalsIgnoreCase("Allocate")) {
			actionPane = allocateAnimalToFamilyAction();
		}
		
		else if(action.equalsIgnoreCase("Report")) {
			actionPane = reportAnimalAdoptionAction();
		}
		
		else {
			actionPane = addPersonAdoptionAction();
		}
		
		root.setTop(displayMenu.getMenuBar());
		adaptableBox.getChildren().add(actionPane);
		root.setCenter(adaptableBox);
	}
	
	public Pane addAnimalAdoptionAction() {
		GridPane addAnimalAdoptionPane = new GridPane();
		addAnimalAdoptionPane.setAlignment(Pos.CENTER);
		addAnimalAdoptionPane.setHgap(30);
		addAnimalAdoptionPane.setVgap(12);
		addAnimalAdoptionPane.setPadding(new Insets(100));

		sectionTitle = new Label("ANIMAL ADOPTION");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
		
		animalSearchLabel = new Label("ID:");
	    animalSearchField = new TextField();
	    animalSearchField.setMinWidth(400);
	    searchToAddButton = new Button("Search");
	    searchToAddButton.setOnAction(new AnimalAdoptionController(this));
	    animalSearch.getChildren().addAll(animalSearchLabel, animalSearchField, searchToAddButton);
	    animalSearch.setAlignment(Pos.CENTER);
	    
		animalIdLabel = new Label("ID:");
		animalIdField = new TextField();
	    animalIdField.setMinWidth(50);
	    animalIdField.setEditable(false);
	    animalNameLabel = new Label("Name:");
	    animalNameField = new TextField();
	    animalNameField.setMinWidth(435);
	    animalIdName.setVisible(false);
	    animalIdName.getChildren().addAll(animalIdLabel, animalIdField, animalNameLabel, animalNameField);
	    
	    animalTypeLabel = new Label("Type:");
	    animalType.setVisible(false);
	    animalType.getChildren().addAll(animalTypeLabel, animalTypeField);
	    
	    animalBreedLabel = new Label("Breed:");
	    animalBreedField = new TextField();
	    animalBreedField.setEditable(false);
	    animalBreedField.setMinWidth(315);
	    animalAgeLabel = new Label("Age: ");
	    animalAgeField = new TextField();
	    animalAgeField.setEditable(false);
	    animalAgeField.setMinWidth(315);
	    animalBreedAge.setVisible(false);
	    animalBreedAge.getChildren().addAll(animalBreedLabel, animalBreedField, animalAgeLabel, animalAgeField);
	    
	    genderAnimalGroup = new ToggleGroup();
	    
	    animalGenderLabel = new Label("Gender:");
	    femaleAnimal = new RadioButton("Female");
	    femaleAnimal.setToggleGroup(genderAnimalGroup);
	    maleAnimal = new RadioButton("Male");
	    maleAnimal.setToggleGroup(genderAnimalGroup);
	    animalColourLabel = new Label("Colour: ");
	    animalColourField = new TextField();
	    animalColourField.setEditable(false);
	    animalColourField.setMinWidth(295);
	    animalGenderColour.setVisible(false);
	    animalGenderColour.getChildren().addAll(animalGenderLabel, femaleAnimal, maleAnimal, animalColourLabel, animalColourField);
	    
	    animalDescriptionLabel = new Label("Description:");
	    animalDescriptionLabel.setVisible(false);
	    animalDescriptionArea = new TextArea();
	    animalDescriptionArea.setMinSize(800, 60);
	    animalDescriptionArea.setVisible(false);
	    animalDescriptionArea.setEditable(false);
	    
	    animalDateLabel = new Label("Date:");
	    animalDateField = new DatePicker();
	    animalDateField.setMinWidth(10);
	    animalDateField.setPromptText("DD-MM-YYYY");
	    animalDateField.setConverter(new StringConverter<LocalDate>() {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	        @Override 
	        public String toString(LocalDate date) {
	            if (date != null) {
	                return dateFormatter.format(date);
	            } 
	            else {
	                return "";
	            }
	        }

	        @Override 
	        public LocalDate fromString(String string) {
	            if (string != null && !string.isEmpty()) {
	                return LocalDate.parse(string, dateFormatter);
	            } 
	            else {
	                return null;
	            }
	        }
	    });
	    animalDateField.setEditable(false);
	    date.setVisible(false);
	    date.getChildren().addAll(animalDateLabel, animalDateField);

	    neuteredGroup = new ToggleGroup();
	    chippedGroup = new ToggleGroup();
	    
	    neuteredLabel = new Label("Neutered*:");
	    yesNeutered = new RadioButton("Yes");
	    yesNeutered.setToggleGroup(neuteredGroup);
	    noNeutered = new RadioButton("No");
	    noNeutered.setToggleGroup(neuteredGroup);
	    chippedLabel = new Label("Chipped*:");
	    yesChipped = new RadioButton("Yes");
	    yesChipped.setToggleGroup(chippedGroup);
	    noChipped = new RadioButton("No");
	    noChipped.setToggleGroup(chippedGroup);
	    neuteredChipped.setVisible(false);
	    neuteredChipped.getChildren().addAll(neuteredLabel, yesNeutered, noNeutered, chippedLabel, yesChipped, noChipped);
	    
	    vaccinatedGroup = new ToggleGroup();
	    
	    vaccinatedLabel = new Label("Vaccinated*:");
	    yesVaccinated = new RadioButton("Yes");
	    yesVaccinated.setToggleGroup(vaccinatedGroup);
	    noVaccinated = new RadioButton("No");
	    noVaccinated.setToggleGroup(vaccinatedGroup);
	    statusLabel = new Label("Status");
	    vaccinatedStatus.setVisible(false);
	    vaccinatedStatus.getChildren().addAll(vaccinatedLabel, yesVaccinated, noVaccinated, statusLabel, statusField);
	    
	    submitButton = new Button("Submit");
	    submitButton.setStyle("-fx-background-color: darkGreen; -fx-text-fill: white");
	    submitButton.setOnAction(new AnimalAdoptionController(this));
	    cancelButton = new Button("Cancel");
	    cancelButton.setOnAction(new AnimalAdoptionController(this));
	    actionButtons.setVisible(false);
	    actionButtons.getChildren().addAll(submitButton, cancelButton);
	    actionButtons.setAlignment(Pos.CENTER);

	    addAnimalAdoptionPane.add(title, 0, 0);
	    addAnimalAdoptionPane.add(animalSearch, 0, 3, 2, 1);
	    addAnimalAdoptionPane.add(animalIdName, 0, 5, 2, 1);
	    addAnimalAdoptionPane.add(animalType, 0, 6, 2, 1);
	    addAnimalAdoptionPane.add(animalBreedAge, 0, 7, 2, 1);
	    addAnimalAdoptionPane.add(animalGenderColour, 0, 8, 2, 1);
	    addAnimalAdoptionPane.add(animalDescriptionLabel, 0, 9);
	    addAnimalAdoptionPane.add(animalDescriptionArea, 0, 10);
	    addAnimalAdoptionPane.add(neuteredChipped, 0, 11, 2, 1);
	    addAnimalAdoptionPane.add(vaccinatedStatus, 0, 12, 2, 1);
	    addAnimalAdoptionPane.add(actionButtons, 0, 15);
		
        return addAnimalAdoptionPane;
	}
	
	public Pane removeAnimalAdoptionAction() {
		GridPane removeAnimalAdoptionPane = new GridPane();
		removeAnimalAdoptionPane.setAlignment(Pos.CENTER);
		removeAnimalAdoptionPane.setHgap(30);
		removeAnimalAdoptionPane.setVgap(12);
		removeAnimalAdoptionPane.setPadding(new Insets(100));
		

		sectionTitle = new Label("ANIMAL ADOPTION");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
		
		animalSearchLabel = new Label("ID:");
	    animalSearchField = new TextField();
	    animalSearchField.setMinWidth(400);
	    searchToDeleteButton = new Button("Search");
	    searchToDeleteButton.setOnAction(new AnimalAdoptionController(this));
	    animalSearch.getChildren().addAll(animalSearchLabel, animalSearchField, searchToDeleteButton);
	    animalSearch.setAlignment(Pos.CENTER);
	    
		animalIdLabel = new Label("ID:");
		animalIdField = new TextField();
	    animalIdField.setMinWidth(50);
	    animalIdField.setEditable(false);
	    animalNameLabel = new Label("Full name:");
	    animalNameField = new TextField();
	    animalNameField.setMinWidth(435);
	    animalNameField.setEditable(false);
	    animalIdName.setVisible(false);
	    animalIdName.getChildren().addAll(animalIdLabel, animalIdField, animalNameLabel, animalNameField);
	    
	    animalTypeLabel = new Label("Type:");
	    animalType.setVisible(false);
	    animalType.getChildren().addAll(animalTypeLabel, animalTypeField);
	    
	    animalBreedLabel = new Label("Breed:");
	    animalBreedField = new TextField();
	    animalBreedField.setEditable(false);
	    animalBreedField.setMinWidth(315);
	    animalAgeLabel = new Label("Age: ");
	    animalAgeField = new TextField();
	    animalAgeField.setEditable(false);
	    animalAgeField.setMinWidth(315);
	    animalBreedAge.setVisible(false);
	    animalBreedAge.getChildren().addAll(animalBreedLabel, animalBreedField, animalAgeLabel, animalAgeField);
	    
	    genderAnimalGroup = new ToggleGroup();
	    
	    animalGenderLabel = new Label("Gender:");
	    femaleAnimal = new RadioButton("Female");
	    femaleAnimal.setToggleGroup(genderAnimalGroup);
	    maleAnimal = new RadioButton("Male");
	    maleAnimal.setToggleGroup(genderAnimalGroup);
	    animalColourLabel = new Label("Colour: ");
	    animalColourField = new TextField();
	    animalColourField.setEditable(false);
	    animalColourField.setMinWidth(295);
	    animalGenderColour.setVisible(false);
	    animalGenderColour.getChildren().addAll(animalGenderLabel, femaleAnimal, maleAnimal, animalColourLabel, animalColourField);
	    
	    animalDescriptionLabel = new Label("Description:");
	    animalDescriptionLabel.setVisible(false);
	    animalDescriptionArea = new TextArea();
	    animalDescriptionArea.setMinSize(800, 60);
	    animalDescriptionArea.setVisible(false);
	    animalDescriptionArea.setEditable(false);
	    
	    neuteredGroup = new ToggleGroup();
	    chippedGroup = new ToggleGroup();
	    
	    neuteredLabel = new Label("Neutered*:");
	    yesNeutered = new RadioButton("Yes");
	    yesNeutered.setToggleGroup(neuteredGroup);
	    noNeutered = new RadioButton("No");
	    noNeutered.setToggleGroup(neuteredGroup);
	    chippedLabel = new Label("Chipped*:");
	    yesChipped = new RadioButton("Yes");
	    yesChipped.setToggleGroup(chippedGroup);
	    noChipped = new RadioButton("No");
	    noChipped.setToggleGroup(chippedGroup);
	    neuteredChipped.setVisible(false);
	    neuteredChipped.getChildren().addAll(neuteredLabel, yesNeutered, noNeutered, chippedLabel, yesChipped, noChipped);
	    
	    vaccinatedGroup = new ToggleGroup();
	    
	    vaccinatedLabel = new Label("Vaccinated*:");
	    yesVaccinated = new RadioButton("Yes");
	    yesVaccinated.setToggleGroup(vaccinatedGroup);
	    noVaccinated = new RadioButton("No");
	    noVaccinated.setToggleGroup(vaccinatedGroup);
	    statusLabel = new Label("Status");
	    vaccinatedStatus.setVisible(false);
	    vaccinatedStatus.getChildren().addAll(vaccinatedLabel, yesVaccinated, noVaccinated, statusLabel, statusField);
	    
	    removeButton = new Button("Remove");
	    removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white");
	    removeButton.setOnAction(new AnimalAdoptionController(this));
	    cancelButton = new Button("Cancel");
	    cancelButton.setOnAction(new AnimalAdoptionController(this));
	    actionButtons.setVisible(false);
	    actionButtons.getChildren().addAll(removeButton, cancelButton);
	    actionButtons.setAlignment(Pos.CENTER);

	    removeAnimalAdoptionPane.add(title, 0, 0);
	    removeAnimalAdoptionPane.add(animalSearch, 0, 3, 2, 1);
	    removeAnimalAdoptionPane.add(animalIdName, 0, 5, 2, 1);
	    removeAnimalAdoptionPane.add(animalType, 0, 6, 2, 1);
	    removeAnimalAdoptionPane.add(animalBreedAge, 0, 7, 2, 1);
	    removeAnimalAdoptionPane.add(animalGenderColour, 0, 8, 2, 1);
	    removeAnimalAdoptionPane.add(animalDescriptionLabel, 0, 9);
	    removeAnimalAdoptionPane.add(animalDescriptionArea, 0, 10);
	    removeAnimalAdoptionPane.add(neuteredChipped, 0, 11, 2, 1);
	    removeAnimalAdoptionPane.add(vaccinatedStatus, 0, 12, 2, 1);
	    removeAnimalAdoptionPane.add(actionButtons, 0, 16);
		
        return removeAnimalAdoptionPane;
	}
	
	@SuppressWarnings("unchecked")
	public Pane displayAnimalAdoptionAction() {
		GridPane displayAnimalAdoptionPane = new GridPane();
		displayAnimalAdoptionPane.setAlignment(Pos.CENTER);
		displayAnimalAdoptionPane.setHgap(30);
		displayAnimalAdoptionPane.setVgap(12);
		displayAnimalAdoptionPane.setPadding(new Insets(100));
		
	    sectionTitle = new Label("ANIMAL ADOPTION");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    animalDetails = new TableView<>();
	    
	    reservedColumn = new TableColumn<>("Reserved?");
        idColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Name");
        typeColumn = new TableColumn<>("Type");
        ageColumn = new TableColumn<>("Age");
        breedColumn = new TableColumn<>("Breed");
        colourColumn = new TableColumn<>("Colour");
        genderColumn = new TableColumn<>("Gender");
        descriptionColumn = new TableColumn<>("Description");
        chippedColumn = new TableColumn<>("Chipped?");
        neuteredColumn = new TableColumn<>("Neutered?");
        vaccinatedColumn = new TableColumn<>("Vaccinated?");
        statusColumn = new TableColumn<>("Status");

        animalDetails.getColumns().addAll(reservedColumn, idColumn, nameColumn, typeColumn, ageColumn, breedColumn, colourColumn, genderColumn, descriptionColumn, 
        		                          chippedColumn, neuteredColumn, vaccinatedColumn, statusColumn);
        animalDetails.setItems(new AnimalAdoptionController(this).displayAll());
        
        allowEditAnimalCheckBox = new CheckBox("Edit Animal");
	    
	    neuteredGroup = new ToggleGroup();
	    chippedGroup = new ToggleGroup();
	    
	    neuteredLabel = new Label("Neutered:");
	    yesNeutered = new RadioButton("Yes");
	    yesNeutered.setToggleGroup(neuteredGroup);
	    noNeutered = new RadioButton("No");
	    noNeutered.setToggleGroup(neuteredGroup);
	    chippedLabel = new Label("Chipped:");
	    yesChipped = new RadioButton("Yes");
	    yesChipped.setToggleGroup(chippedGroup);
	    noChipped = new RadioButton("No");
	    noChipped.setToggleGroup(chippedGroup);
	    neuteredChipped.getChildren().addAll(neuteredLabel, yesNeutered, noNeutered, chippedLabel, yesChipped, noChipped);
	    
	    vaccinatedGroup = new ToggleGroup();
	    
	    vaccinatedLabel = new Label("Vaccinated:");
	    yesVaccinated = new RadioButton("Yes");
	    yesVaccinated.setToggleGroup(vaccinatedGroup);
	    noVaccinated = new RadioButton("No");
	    noVaccinated.setToggleGroup(vaccinatedGroup);
	    statusLabel = new Label("Status");
	    vaccinatedStatus.getChildren().addAll(vaccinatedLabel, yesVaccinated, noVaccinated, statusLabel, statusField);
	    
	    updateButton = new Button("Update");
	    updateButton.setStyle("-fx-background-color: darkOrange; -fx-text-fill: white");
	    updateButton.setOnAction(new AnimalAdoptionController(this));
	    actionButtons.getChildren().add(updateButton);
	    
	    editDetails.getChildren().addAll(neuteredChipped, vaccinatedStatus, actionButtons);
	    editDetails.setVisible(false);
	    
	    displayAnimalAdoptionPane.add(title, 0, 0);
	    displayAnimalAdoptionPane.add(animalDetails, 0, 5);
	    displayAnimalAdoptionPane.add(allowEditAnimalCheckBox, 0, 13);
	    displayAnimalAdoptionPane.add(editDetails, 0, 15);
	    
	    allowEditAnimalCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(allowEditAnimalCheckBox.isSelected()) {
					Animal animal = animalDetails.getSelectionModel().getSelectedItem();
					if(animal != null) {
						if(animal.getAnimalCategory().isChipped()) { yesChipped.setSelected(true); }
						else { noChipped.setSelected(true); }
						
						if(animal.getAnimalCategory().isNeutered()) { yesNeutered.setSelected(true); }
						else { noNeutered.setSelected(true); }
						
						if(animal.getAnimalCategory().isVaccinated()) { yesVaccinated.setSelected(true); }
						else { noVaccinated.setSelected(true); }
						
						statusField.setValue(animal.getAnimalCategory().getStatus());
					}
					editDetails.setVisible(true);
				}
				
				else {
					editDetails.setVisible(false);
				}
			}
		});
		
        return displayAnimalAdoptionPane;
	}
	
	public Pane addPersonAdoptionAction() {
		GridPane addPersonAdoptionPane = new GridPane();
		addPersonAdoptionPane.setAlignment(Pos.CENTER);
		addPersonAdoptionPane.setHgap(30);
		addPersonAdoptionPane.setVgap(12);
		addPersonAdoptionPane.setPadding(new Insets(100));
		
	    sectionTitle = new Label("ANIMAL ADOPTION");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    personNameLabel = new Label("Full Name*:");
	    personNameField = new TextField();
	    personNameField.setMinWidth(690);
	    personName.getChildren().addAll(personNameLabel, personNameField);
	    
	    personTelephoneLabel = new Label("Telephone*:");
	    personTelephoneField = new TextField();
	    personTelephoneField.setMinWidth(70);
	    personEmailLabel = new Label("Email:");
	    personEmailField = new TextField();
	    personEmailField.setMinWidth(405);
	    personTelephoneEmail.getChildren().addAll(personTelephoneLabel, personTelephoneField, personEmailLabel, personEmailField);
	     
	    personAddressLabel = new Label("Address:");
	    personAddressField = new TextField();
	    personAddressField.setMinWidth(710);
	    personAddress.getChildren().addAll(personAddressLabel, personAddressField);

	    addPersonButton = new Button("Submit");
	    addPersonButton.setStyle("-fx-background-color: darkGreen; -fx-text-fill: white");
	    addPersonButton.setOnAction(new AnimalAdoptionController(this));
	    clearButton = new Button("Clear");
	    clearButton.setOnAction(new AnimalAdoptionController(this));
	    cancelButton = new Button("Cancel");
	    cancelButton.setOnAction(new AnimalAdoptionController(this));
	    actionButtons.getChildren().addAll(addPersonButton, clearButton, cancelButton);
	    actionButtons.setAlignment(Pos.CENTER);

	    addPersonAdoptionPane.add(title, 0, 0);
	    addPersonAdoptionPane.add(personName, 0, 5);
	    addPersonAdoptionPane.add(personTelephoneEmail, 0, 6);
	    addPersonAdoptionPane.add(personAddress, 0, 7);
	    addPersonAdoptionPane.add(actionButtons, 0, 15);
		
        return addPersonAdoptionPane;
	}
	
	public Pane allocateAnimalToFamilyAction() {
		GridPane allocateAnimalPane = new GridPane();
		allocateAnimalPane.setAlignment(Pos.CENTER);
		allocateAnimalPane.setHgap(30);
		allocateAnimalPane.setVgap(12);
		allocateAnimalPane.setPadding(new Insets(100));
		
	    sectionTitle = new Label("ANIMAL ADOPTION");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    animalIdLabel = new Label("Animal ID:");
	    
	    try {
			AnimalList animalList = new ShelterFile().getListFromFile("Adoption");
			ObservableList<String> ids = FXCollections.observableArrayList();

			for(int i = 0; i < animalList.getAnimalList().size(); i++) {
				ids.add(String.valueOf(animalList.getAnimalList().get(i).getAnimalId()));
			}
			
			adoptionIds = new ComboBox<>(ids);
			
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}
	    
	    Label allocateTo = new Label("allocate to");	    
	    
	    personNameLabel = new Label("Person's Name:");
	    
	    try {
			ArrayList<Person> personList = new ShelterFile().getInterestedPeopleFromFile();
			ObservableList<String> names = FXCollections.observableArrayList();

			for(int i = 0; i < personList.size(); i++) {
				names.add(personList.get(i).getPersonName());
			}
			
			adoptionNames = new ComboBox<>(names);
			adoptionNames.setMinWidth(300);
			
		} catch (IOException e) {
			System.out.println("I/O Problem");
		}
	    animalIdName.getChildren().addAll(animalIdLabel, adoptionIds, allocateTo, personNameLabel, adoptionNames);

	    allocateButton = new Button("Allocate");
	    allocateButton.setStyle("-fx-background-color: darkGreen; -fx-text-fill: white");
	    allocateButton.setOnAction(new AnimalAdoptionController(this));
	    cancelButton = new Button("Cancel");
	    cancelButton.setOnAction(new AnimalAdoptionController(this));
	    actionButtons.getChildren().addAll(allocateButton, cancelButton);
	    actionButtons.setAlignment(Pos.CENTER);

	    allocateAnimalPane.add(title, 0, 0);
	    allocateAnimalPane.add(animalIdName, 0, 5);
	    allocateAnimalPane.add(actionButtons, 0, 10);
		
        return allocateAnimalPane;
	}
	
	@SuppressWarnings("unchecked")
	public Pane reportAnimalAdoptionAction() {
		GridPane reportAnimalAdoptionPane = new GridPane();
		reportAnimalAdoptionPane.setAlignment(Pos.CENTER);
		reportAnimalAdoptionPane.setHgap(30);
		reportAnimalAdoptionPane.setVgap(12);
		reportAnimalAdoptionPane.setPadding(new Insets(100));

	    sectionTitle = new Label("ANIMAL ADOPTION");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    animalTypeLabel = new Label("Type:");
	    animalTypeReport.setValue(moreTypes.get(0));
	    
	    statusLabel = new Label("Status");
	    statusField.setMinWidth(200);
	    
	    animalAgeLabel = new Label("Classification");
	    classificationField.setValue(classifications.get(0));
	    animalType.getChildren().addAll(animalTypeLabel, animalTypeReport, statusLabel, statusField, animalAgeLabel, classificationField);
	    
	    organizeByLabel = new Label("Organize by:");

	    generateReportButton = new Button("Generate Report");
	    generateReportButton.setOnAction(new AnimalAdoptionController(this));
	    actionButtons.getChildren().add(generateReportButton);
	    actionButtons.setAlignment(Pos.CENTER);
	    
	    animalDetails = new TableView<>();
	    animalDetails.setMinWidth(850);
	    
	    reservedColumn = new TableColumn<>("Reserved?");
        idColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Name");
        typeColumn = new TableColumn<>("Type");
        ageColumn = new TableColumn<>("Age");
        breedColumn = new TableColumn<>("Breed");
        colourColumn = new TableColumn<>("Colour");
        genderColumn = new TableColumn<>("Gender");
        descriptionColumn = new TableColumn<>("Description");
        chippedColumn = new TableColumn<>("Chipped?");
        neuteredColumn = new TableColumn<>("Neutered?");
        vaccinatedColumn = new TableColumn<>("Vaccinated?");
        statusColumn = new TableColumn<>("Status");

        animalDetails.getColumns().addAll(reservedColumn, idColumn, nameColumn, typeColumn, ageColumn, breedColumn, colourColumn, genderColumn, descriptionColumn, 
        		                          chippedColumn, neuteredColumn, vaccinatedColumn, statusColumn);
        animalDetails.setVisible(false);

	    reportAnimalAdoptionPane.add(title, 0, 0);
	    reportAnimalAdoptionPane.add(animalType, 0, 5);
	    reportAnimalAdoptionPane.add(organizeByLabel, 0, 7);
	    reportAnimalAdoptionPane.add(organizationField, 0, 8);
	    reportAnimalAdoptionPane.add(actionButtons, 0, 12);
	    reportAnimalAdoptionPane.add(animalDetails, 0, 15);
		
        return reportAnimalAdoptionPane;
	}
	
	public TextField getAnimalIdField() {
		return animalIdField;
	}

	public TextField getAnimalNameField() {
		return animalNameField;
	}

	public TextField getAnimalLocationField() {
		return animalLocationField;
	}

	public void setAnimalLocationField(TextField animalLocationField) {
		this.animalLocationField = animalLocationField;
	}
	
	public TextField getAnimalBreedField() {
		return animalBreedField;
	}

	public TextField getAnimalAgeField() {
		return animalAgeField;
	}

	public RadioButton getFemaleAnimal() {
		return femaleAnimal;
	}

	public RadioButton getMaleAnimal() {
		return maleAnimal;
	}
	
	public TextField getAnimalColourField() {
		return animalColourField;
	}

	public ComboBox<String> getAnimalTypeField() {
		return animalTypeField;
	}
	
	public ComboBox<String> getAnimalTypeReport() {
		return animalTypeReport;
	}
	
	public ComboBox<String> getOrganizationField() {
		return organizationField;
	}
	
	public ComboBox<String> getClassificationField() {
		return classificationField;
	}

	public DatePicker getAnimalDateField() {
		return animalDateField;
	}

	public TextField getPersonNameField() {
		return personNameField;
	}

	public TextField getPersonTelephoneField() {
		return personTelephoneField;
	}

	public TextField getPersonEmailField() {
		return personEmailField;
	}

	public TextField getPersonAddressField() {
		return personAddressField;
	}

	public TextField getAnimalSearchField() {
		return animalSearchField;
	}

	public TextArea getAnimalDescriptionArea() {
		return animalDescriptionArea;
	}

	public Button getRemoveButton() {
		return removeButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public Button getSubmitButton() {
		return submitButton;
	}
	
	public Button getClearButton() {
		return clearButton;
	}

	public Button getSearchToDeleteButton() {
		return searchToDeleteButton;
	}
	
	public Button getSearchToDisplayButton() {
		return searchToDisplayButton;
	}
	
	public Button getSearchToAddButton() {
		return searchToAddButton;
	}
	
	public Button getAddPersonButton() {
		return addPersonButton;
	}
	
	public Button getUpdateButton() {
		return updateButton;
	}
	
	public Button getAllocateButton() {
		return allocateButton;
	}
	
	public Button getGenerateReportButton() {
		return generateReportButton;
	}

	public HBox getAnimalIdName() {
		return animalIdName;
	}

	public HBox getDate() {
		return date;
	}

	public HBox getAnimalType() {
		return animalType;
	}

	public HBox getAnimalBreedAge() {
		return animalBreedAge;
	}

	public HBox getAnimalGenderColour() {
		return animalGenderColour;
	}

	public HBox getAnimalSearch() {
		return animalSearch;
	}

	public HBox getPersonName() {
		return personName;
	}

	public HBox getPersonTelephoneEmail() {
		return personTelephoneEmail;
	}

	public HBox getPersonAddress() {
		return personAddress;
	}

	public HBox getNeuteredChipped() {
		return neuteredChipped;
	}

	public HBox getVaccinatedStatus() {
		return vaccinatedStatus;
	}

	public HBox getActionButtons() {
		return actionButtons;
	}

	public Label getAnimalDescriptionLabel() {
		return animalDescriptionLabel;
	}

	public RadioButton getYesNeutered() {
		return yesNeutered;
	}

	public RadioButton getNoNeutered() {
		return noNeutered;
	}

	public RadioButton getYesChipped() {
		return yesChipped;
	}

	public RadioButton getNoChipped() {
		return noChipped;
	}

	public RadioButton getYesVaccinated() {
		return yesVaccinated;
	}

	public RadioButton getNoVaccinated() {
		return noVaccinated;
	}
	
	public CheckBox getAllowEditAnimalCheckBox() {
		return allowEditAnimalCheckBox;
	}
	
	public ComboBox<String> getAdoptionIds() {
		return adoptionIds;
	}
	
	public ComboBox<String> getAdoptionNames() {
		return adoptionNames;
	}

	public ComboBox<String> getStatusField() {
		return statusField;
	}

	public ToggleGroup getGenderAnimalGroup() {
		return genderAnimalGroup;
	}

	public ToggleGroup getNeuteredGroup() {
		return neuteredGroup;
	}

	public ToggleGroup getChippedGroup() {
		return chippedGroup;
	}

	public ToggleGroup getVaccinatedGroup() {
		return vaccinatedGroup;
	}
	
	public TableColumn<Animal, Number> getIdColumn() {
		return idColumn;
	}

	public TableColumn<Animal, String> getNameColumn() {
		return nameColumn;
	}

	public TableColumn<Animal, String> getTypeColumn() {
		return typeColumn;
	}

	public TableColumn<Animal, String> getGenderColumn() {
		return genderColumn;
	}
	
	public TableColumn<Animal, String> getDescriptionColumn() {
		return descriptionColumn;
	}

	public TableColumn<Animal, Boolean> getVaccinatedColumn() {
		return vaccinatedColumn;
	}

	public TableColumn<Animal, Boolean> getChippedColumn() {
		return chippedColumn;
	}
	
	public TableColumn<Animal, Boolean> getNeuteredColumn() {
		return neuteredColumn;
	}
	
	public TableColumn<Animal, Boolean> getReservedColumn() {
		return reservedColumn;
	}
	
	public TableColumn<Animal, String> getStatusColumn() {
		return statusColumn;
	}
	
	public TableColumn<Animal, Number> getAgeColumn() {
		return ageColumn;
	}
	
	public TableColumn<Animal, String> getBreedColumn() {
		return breedColumn;
	}
	
	public TableColumn<Animal, String> getColourColumn() {
		return colourColumn;
	}
	
	public TableView<Animal> getAnimalDetails() {
		return animalDetails;
	}
}
