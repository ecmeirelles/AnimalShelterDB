package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import controller.FoundAnimalController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import model.Animal;

public class FoundAnimalView extends Scene {
	
	private Pane actionPane;
	private HBox title, animalIdName, animalDateLocation, animalType, animalBreedAge, animalGenderColour, animalSearch, 
	             ownerName, ownerTelephoneEmail, ownerAddress, actionButtons;	
	private Label sectionTitle, animalIdLabel, animalNameLabel, animalDateLabel, animalLocationLabel, animalSearchLabel, 
	              animalDescriptionLabel, animalColourLabel, animalAgeLabel, animalTypeLabel, animalGenderLabel, animalBreedLabel,
	              personDetails, ownerNameLabel, ownerTelephoneLabel, ownerEmailLabel, ownerAddressLabel, andLabel;
	
	private TextField animalIdField, animalNameField, animalLocationField, animalColourField, animalAgeField, animalBreedField,
					  ownerNameField, ownerTelephoneField, ownerEmailField, ownerAddressField, animalSearchField;
	private TextArea animalDescriptionArea;
	private RadioButton femaleAnimal, maleAnimal, searchToAdd, addManually;
	private ToggleGroup genderAnimalGroup, addGroup;
	private DatePicker animalDateField, betweenDateField;
	private Button removeButton, cancelButton, submitButton, clearButton, searchToDeleteButton, searchToDisplayButton, 
	               addFromLostButton, generateReportButton;
	private ObservableList<String> types = FXCollections.observableArrayList("Cat", "Dog");
	private ObservableList<String> moreTypes = FXCollections.observableArrayList("All", "Cat", "Dog");
	
	private TableView<Animal> animalDetails;
	private TableColumn<Animal, Number> idColumn, ageColumn;
	private TableColumn<Animal, String> nameColumn, typeColumn, colourColumn, genderColumn, descriptionColumn, dateColumn, locationColumn, breedColumn;	
	
	final ComboBox<String> animalTypeField = new ComboBox<String>(types);
	final ComboBox<String> animalTypeReport = new ComboBox<>(moreTypes);

	public FoundAnimalView(BorderPane root, double width, double height, String action) {
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
	    animalDateLocation = new HBox();
	    animalDateLocation.setSpacing(30);
	    ownerName = new HBox();
	    ownerName.setSpacing(30);
	    ownerTelephoneEmail = new HBox();
	    ownerTelephoneEmail.setSpacing(30);
	    ownerAddress = new HBox();
	    ownerAddress.setSpacing(30);
	    actionButtons = new HBox();
	    actionButtons.setSpacing(20);
	    animalSearch = new HBox();
		animalSearch.setSpacing(30);
		
		DisplayMenu displayMenu = new DisplayMenu();
		displayMenu.menu();

		if(action.equalsIgnoreCase("ADD")) {
			actionPane = addFoundAnimalAction();
		}
		
		else if(action.equalsIgnoreCase("Remove")) {
			actionPane = removeFoundAnimalAction();
		}
		
		else if(action.equalsIgnoreCase("Report")) {
			actionPane = reportFoundAnimalAction();
		}
		
		else {
			actionPane = displayFoundAnimalAction();
		}
		
		root.setTop(displayMenu.getMenuBar());
		adaptableBox.getChildren().add(actionPane);
		root.setCenter(adaptableBox);
	}

	public Pane addFoundAnimalAction() {
		GridPane addFoundAnimalPane = new GridPane();
		addFoundAnimalPane.setAlignment(Pos.CENTER);
		addFoundAnimalPane.setHgap(30);
		addFoundAnimalPane.setVgap(12);
		addFoundAnimalPane.setPadding(new Insets(100));
		
	    sectionTitle = new Label("FOUND ANIMAL");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    addGroup = new ToggleGroup();
	    searchToAdd = new RadioButton("Add from a lost animal");
	    searchToAdd.setToggleGroup(addGroup);
	    addManually = new RadioButton("Add manually");
	    addManually.setToggleGroup(addGroup);
	    
	    animalSearchLabel = new Label("ID:");
	    animalSearchField = new TextField();
	    animalSearchField.setMinWidth(750);
	    animalSearch.getChildren().addAll(animalSearchLabel, animalSearchField);
	    animalSearch.setVisible(false);
	    
	    addFromLostButton = new Button("Submit");
	    addFromLostButton.setStyle("-fx-background-color: darkGreen; -fx-text-fill: white");
	    addFromLostButton.setOnAction(new FoundAnimalController(this));
	    addFromLostButton.setAlignment(Pos.CENTER);
	    addFromLostButton.setVisible(false);
	    
	    animalIdLabel = new Label("ID*:");
	    animalIdField = new TextField();
	    animalIdField.setMinWidth(50);
	    animalNameLabel = new Label("Name:");
	    animalNameField = new TextField();
	    animalNameField.setMinWidth(435);
	    animalIdName.getChildren().addAll(animalIdLabel, animalIdField, animalNameLabel, animalNameField);
	    animalIdName.setVisible(false);
	    
	    animalTypeLabel = new Label("Type*:");
	    animalType.getChildren().addAll(animalTypeLabel, animalTypeField);
	    animalType.setVisible(false);
	    
	    animalBreedLabel = new Label("Breed:");
	    animalBreedField = new TextField();
	    animalBreedField.setMinWidth(315);
	    animalAgeLabel = new Label("Age: ");
	    animalAgeField = new TextField();
	    animalAgeField.setMinWidth(315);
	    animalBreedAge.getChildren().addAll(animalBreedLabel, animalBreedField, animalAgeLabel, animalAgeField);
	    animalBreedAge.setVisible(false);
	    
	    genderAnimalGroup = new ToggleGroup();
	    
	    animalGenderLabel = new Label("Gender*:");
	    femaleAnimal = new RadioButton("Female");
	    femaleAnimal.setToggleGroup(genderAnimalGroup);
	    maleAnimal = new RadioButton("Male");
	    maleAnimal.setToggleGroup(genderAnimalGroup);
	    animalColourLabel = new Label("Colour: ");
	    animalColourField = new TextField();
	    animalColourField.setMinWidth(440);
	    animalGenderColour.getChildren().addAll(animalGenderLabel, femaleAnimal, maleAnimal, animalColourLabel, animalColourField);
	    animalGenderColour.setVisible(false);
	    
	    animalDescriptionLabel = new Label("Description*:");
	    animalDescriptionLabel.setVisible(false);
	    animalDescriptionArea = new TextArea();
	    animalDescriptionArea.setMinSize(800, 60);
	    animalDescriptionArea.setVisible(false);
	    
	    animalDateLabel = new Label("Date*:");
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
	    animalLocationLabel = new Label("Location*:");
	    animalLocationField = new TextField();
	    animalLocationField.setMinWidth(395);
	    animalDateLocation.getChildren().addAll(animalDateLabel, animalDateField, animalLocationLabel, animalLocationField);
	    animalDateLocation.setVisible(false);

	    submitButton = new Button("Submit");
	    submitButton.setStyle("-fx-background-color: darkGreen; -fx-text-fill: white");
	    submitButton.setOnAction(new FoundAnimalController(this));
	    clearButton = new Button("Clear");
	    clearButton.setOnAction(new FoundAnimalController(this));
	    cancelButton = new Button("Cancel");
	    cancelButton.setOnAction(new FoundAnimalController(this));
	    actionButtons.getChildren().addAll(submitButton, clearButton, cancelButton);
	    actionButtons.setAlignment(Pos.CENTER);
	    actionButtons.setVisible(false);

	    addFoundAnimalPane.add(title, 0, 0);
	    addFoundAnimalPane.add(searchToAdd, 0, 5);
	    addFoundAnimalPane.add(addManually, 0, 6);
	    addFoundAnimalPane.add(animalSearch, 0, 5, 2, 1);
	    addFoundAnimalPane.add(animalIdName, 0, 3, 2, 1);
	    addFoundAnimalPane.add(animalType, 0, 4, 2, 1);
	    addFoundAnimalPane.add(animalBreedAge, 0, 5, 2, 1);
	    addFoundAnimalPane.add(animalGenderColour, 0, 6, 2, 1);
	    addFoundAnimalPane.add(animalDescriptionLabel, 0, 7);
	    addFoundAnimalPane.add(animalDescriptionArea, 0, 8);
	    addFoundAnimalPane.add(animalDateLocation, 0, 9, 2, 1);
	    addFoundAnimalPane.add(actionButtons, 0, 15);
	    addFoundAnimalPane.add(addFromLostButton, 0, 15);
	    
	    searchToAdd.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(searchToAdd.isSelected()) {
					searchToAdd.setVisible(false);
					addManually.setVisible(false);
					animalSearch.setVisible(true);
					animalDateLocation.setVisible(true);
					addFromLostButton.setVisible(true);
					
				}
			}
		});
	    
	    addManually.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(addManually.isSelected()) {
					searchToAdd.setVisible(false);
					addManually.setVisible(false);
					animalIdName.setVisible(true);
					animalType.setVisible(true);
					animalBreedAge.setVisible(true);
					animalGenderColour.setVisible(true);
					animalDescriptionLabel.setVisible(true);
					animalDescriptionArea.setVisible(true);
					animalDateLocation.setVisible(true);
					actionButtons.setVisible(true);
				}
			}
		});
		
        return addFoundAnimalPane;
	}
	
	public Pane removeFoundAnimalAction() {
		GridPane removeFoundAnimalPane = new GridPane();
		removeFoundAnimalPane.setAlignment(Pos.CENTER);
		removeFoundAnimalPane.setHgap(30);
		removeFoundAnimalPane.setVgap(12);
		removeFoundAnimalPane.setPadding(new Insets(100));
		
		sectionTitle = new Label("FOUND ANIMAL");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
		
		animalSearchLabel = new Label("ID:");
	    animalSearchField = new TextField();
	    animalSearchField.setMinWidth(400);
	    searchToDeleteButton = new Button("Search");
	    searchToDeleteButton.setOnAction(new FoundAnimalController(this));
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
	    animalLocationLabel = new Label("Location:");
	    animalLocationField = new TextField();
	    animalLocationField.setMinWidth(395);
	    animalLocationField.setEditable(false);
	    animalDateLocation.setVisible(false);
	    animalDateLocation.getChildren().addAll(animalDateLabel, animalDateField, animalLocationLabel, animalLocationField);

	    personDetails = new Label("OWNER DETAILS (FOR SPONSORSHIP):");
	    personDetails.setFont(Font.font("Berlin Sans FB", 15));
	    personDetails.setVisible(false);
	    
	    ownerNameLabel = new Label("Full Name*:");
	    ownerNameField = new TextField();
	    ownerNameField.setMinWidth(690);
	    ownerName.setVisible(false);
	    ownerName.getChildren().addAll(ownerNameLabel, ownerNameField);
	    
	    ownerTelephoneLabel = new Label("Telephone*:");
	    ownerTelephoneField = new TextField();
	    ownerTelephoneField.setMinWidth(70);
	    ownerEmailLabel = new Label("Email:");
	    ownerEmailField = new TextField();
	    ownerEmailField.setMinWidth(405);
	    ownerTelephoneEmail.setVisible(false);
	    ownerTelephoneEmail.getChildren().addAll(ownerTelephoneLabel, ownerTelephoneField, ownerEmailLabel, ownerEmailField);
	     
	    ownerAddressLabel = new Label("Address:");
	    ownerAddressField = new TextField();
	    ownerAddressField.setMinWidth(710);
	    ownerAddress.setVisible(false);
	    ownerAddress.getChildren().addAll(ownerAddressLabel, ownerAddressField);
	    
	    removeButton = new Button("Remove");
	    removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white");
	    removeButton.setOnAction(new FoundAnimalController(this));
	    cancelButton = new Button("Cancel");
	    cancelButton.setOnAction(new FoundAnimalController(this));
	    actionButtons.setVisible(false);
	    actionButtons.getChildren().addAll(removeButton, cancelButton);
	    actionButtons.setAlignment(Pos.CENTER);

	    removeFoundAnimalPane.add(title, 0, 0);
	    removeFoundAnimalPane.add(animalSearch, 0, 3, 2, 1);
	    removeFoundAnimalPane.add(animalIdName, 0, 5, 2, 1);
	    removeFoundAnimalPane.add(animalType, 0, 6, 2, 1);
	    removeFoundAnimalPane.add(animalBreedAge, 0, 7, 2, 1);
	    removeFoundAnimalPane.add(animalGenderColour, 0, 8, 2, 1);
	    removeFoundAnimalPane.add(animalDescriptionLabel, 0, 9);
	    removeFoundAnimalPane.add(animalDescriptionArea, 0, 10);
	    removeFoundAnimalPane.add(animalDateLocation, 0, 11, 2, 1);
	    removeFoundAnimalPane.add(personDetails, 0, 14);
	    removeFoundAnimalPane.add(ownerName, 0, 15);
	    removeFoundAnimalPane.add(ownerTelephoneEmail, 0, 16);
	    removeFoundAnimalPane.add(ownerAddress, 0, 17);
	    removeFoundAnimalPane.add(actionButtons, 0, 18);
		
        return removeFoundAnimalPane;
	}
	
	@SuppressWarnings("unchecked")
	public Pane displayFoundAnimalAction() {
		GridPane displayFoundAnimalPane = new GridPane();
		displayFoundAnimalPane.setAlignment(Pos.CENTER);
		displayFoundAnimalPane.setHgap(30);
		displayFoundAnimalPane.setVgap(12);
		displayFoundAnimalPane.setPadding(new Insets(100));
		
	    sectionTitle = new Label("FOUND ANIMAL");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);

	    animalDetails = new TableView<>();
	    
        idColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Name");
        typeColumn = new TableColumn<>("Type");
        ageColumn = new TableColumn<>("Age");
        breedColumn = new TableColumn<>("Breed");
        colourColumn = new TableColumn<>("Colour");
        genderColumn = new TableColumn<>("Gender");
        descriptionColumn = new TableColumn<>("Description");
        dateColumn = new TableColumn<>("Date");
        locationColumn = new TableColumn<>("Location");
        

        animalDetails.getColumns().addAll(idColumn, nameColumn, typeColumn, ageColumn, breedColumn, colourColumn, genderColumn, descriptionColumn, 
        		                          dateColumn, locationColumn);
        animalDetails.setItems(new FoundAnimalController(this).displayAll());
	    
	    displayFoundAnimalPane.add(title, 0, 0);
	    displayFoundAnimalPane.add(animalDetails, 0, 5);
		
        return displayFoundAnimalPane;
	}
	
	@SuppressWarnings("unchecked")
	public Pane reportFoundAnimalAction() {
		GridPane reportFoundAnimalPane = new GridPane();
		reportFoundAnimalPane.setAlignment(Pos.CENTER);
		reportFoundAnimalPane.setHgap(30);
		reportFoundAnimalPane.setVgap(12);
		reportFoundAnimalPane.setPadding(new Insets(100));

	    sectionTitle = new Label("FOUND ANIMAL");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    animalTypeLabel = new Label("Type:");
	    animalTypeReport.setValue(moreTypes.get(0));
	    
	    animalLocationLabel = new Label("Location:");
	    animalLocationField = new TextField();
	    animalLocationField.setMinWidth(395);
	    animalType.getChildren().addAll(animalTypeLabel, animalTypeReport, animalLocationLabel, animalLocationField);
	    
	    animalDateLabel = new Label("Between:");
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
	    
	    andLabel = new Label("and:");
	    betweenDateField = new DatePicker();
	    betweenDateField.setMinWidth(10);
	    betweenDateField.setPromptText("DD-MM-YYYY");
	    betweenDateField.setConverter(new StringConverter<LocalDate>() {
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
	    
	    animalDateLocation.getChildren().addAll(animalDateLabel, animalDateField, andLabel, betweenDateField);

	    generateReportButton = new Button("Generate Report");
	    generateReportButton.setOnAction(new FoundAnimalController(this));
	    actionButtons.getChildren().add(generateReportButton);
	    actionButtons.setAlignment(Pos.CENTER);
	    
	    animalDetails = new TableView<>();
	    animalDetails.setMinWidth(850);
	    
        idColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Name");
        typeColumn = new TableColumn<>("Type");
        ageColumn = new TableColumn<>("Age");
        breedColumn = new TableColumn<>("Breed");
        colourColumn = new TableColumn<>("Colour");
        genderColumn = new TableColumn<>("Gender");
        descriptionColumn = new TableColumn<>("Description");
        dateColumn = new TableColumn<>("Date");
        locationColumn = new TableColumn<>("Location");
        

        animalDetails.getColumns().addAll(idColumn, nameColumn, typeColumn, ageColumn, breedColumn, colourColumn, genderColumn, descriptionColumn, 
        		                          dateColumn, locationColumn);
        animalDetails.setVisible(false);

	    reportFoundAnimalPane.add(title, 0, 0);
	    reportFoundAnimalPane.add(animalType, 0, 5);
	    reportFoundAnimalPane.add(animalDateLocation, 0, 6, 2, 1);
	    reportFoundAnimalPane.add(actionButtons, 0, 9);
	    reportFoundAnimalPane.add(animalDetails, 0, 12);
		
        return reportFoundAnimalPane;
	}
	
	/* Getters  */
	public TextField getAnimalIdField() {
		return animalIdField;
	}

	public TextField getAnimalNameField() {
		return animalNameField;
	}

	public TextField getAnimalLocationField() {
		return animalLocationField;
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

	public DatePicker getAnimalDateField() {
		return animalDateField;
	}
	
	public DatePicker getBetweenDateField() {
		return betweenDateField;
	}

	public TextField getOwnerNameField() {
		return ownerNameField;
	}

	public TextField getOwnerTelephoneField() {
		return ownerTelephoneField;
	}

	public TextField getOwnerEmailField() {
		return ownerEmailField;
	}

	public TextField getOwnerAddressField() {
		return ownerAddressField;
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
	
	public Button getAddFromLostButton() {
		return addFromLostButton;
	}
	
	public Button getGenerateReportButton() {
		return generateReportButton;
	}
	
	public Label getAnimalDescriptionLabel() {
		return animalDescriptionLabel;
	}
	
	public ToggleGroup getGenderAnimalGroup() {
		return genderAnimalGroup;
	}
	
	public Label getPersonDetails() {
		return personDetails;
	}

	public HBox getAnimalIdName() {
		return animalIdName;
	}

	public HBox getAnimalDateLocation() {
		return animalDateLocation;
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

	public HBox getOwnerName() {
		return ownerName;
	}

	public HBox getOwnerTelephoneEmail() {
		return ownerTelephoneEmail;
	}

	public HBox getOwnerAddress() {
		return ownerAddress;
	}

	public HBox getActionButtons() {
		return actionButtons;
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

	public TableColumn<Animal, String> getDateColumn() {
		return dateColumn;
	}

	public TableColumn<Animal, String> getLocationColumn() {
		return locationColumn;
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
