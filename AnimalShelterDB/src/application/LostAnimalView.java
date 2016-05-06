package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import controller.LostAnimalController;
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
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import model.Animal;

public class LostAnimalView extends Scene {
	private Pane actionPane;
	
	private HBox title, animalIdName, animalDateLocation, animalType, animalBreedAge, animalGenderColour, animalSearch, 
	             ownerName, ownerTelephoneEmail, ownerAddress, ownerDetails, allowOwnerDetails, actionButtons;
	private Label sectionTitle, animalIdLabel, animalNameLabel, animalDateLabel, animalLocationLabel, 
	              animalDescriptionLabel, animalColourLabel, animalAgeLabel, animalTypeLabel, animalGenderLabel, animalBreedLabel, 
	              personDetails, ownerNameLabel, ownerTelephoneLabel, ownerEmailLabel, ownerAddressLabel;
	private TextField animalIdField, animalNameField, animalLocationField, animalColourField, animalAgeField, animalBreedField, 
	                  ownerNameField, ownerTelephoneField, ownerEmailField, ownerAddressField;
	private RadioButton femaleAnimal, maleAnimal;
	private ToggleGroup genderAnimalGroup;
	private DatePicker animalDateField;
	private TextArea animalDescriptionArea;
	private ObservableList<String> types = FXCollections.observableArrayList("Cat", "Dog");
	private ObservableList<String> moreTypes = FXCollections.observableArrayList("All", "Cat", "Dog");
	private CheckBox allowOwnerDetailsCheckBox;
	private Button cancelButton, submitButton, clearButton, searchToDisplayButton, generateReportButton; 
	 
	private TableView<Animal> animalDetails;
	private TableColumn<Animal, Number> idColumn, ageColumn;
	private TableColumn<Animal, String> nameColumn, typeColumn, colourColumn, genderColumn, descriptionColumn, dateColumn, locationColumn, breedColumn;
	
	final ComboBox<String> animalTypeField = new ComboBox<>(types);
	final ComboBox<String> animalTypeReport = new ComboBox<>(moreTypes);

	public LostAnimalView(BorderPane root, double width, double height, String action) {
		super(root, width, height);
		
		DisplayMenu displayMenu = new DisplayMenu();
		displayMenu.menu();
		
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
		allowOwnerDetails = new HBox();
		allowOwnerDetails.setSpacing(20);
		ownerDetails = new HBox();
		ownerDetails.setSpacing(30);	

		if(action.equalsIgnoreCase("ADD")) {
			actionPane = addLostAnimalAction();
		}
		
		else if(action.equalsIgnoreCase("Report")) {
			actionPane = reportLostAnimalAction();
		}
		
		else {
			actionPane = displayLostAnimalAction();
		}
		
		root.setTop(displayMenu.getMenuBar());
		adaptableBox.getChildren().add(actionPane);
		root.setCenter(adaptableBox);
	}

	public Pane addLostAnimalAction() {
		GridPane addLostAnimalPane = new GridPane();
		addLostAnimalPane.setAlignment(Pos.CENTER);
		addLostAnimalPane.setHgap(30);
		addLostAnimalPane.setVgap(12);
		addLostAnimalPane.setPadding(new Insets(100));
		
	    sectionTitle = new Label("LOST ANIMAL");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
		animalIdLabel = new Label("ID*:");
	    animalIdField = new TextField();
	    animalIdField.setMinWidth(50);
	    animalNameLabel = new Label("Name*:");
	    animalNameField = new TextField();
	    animalNameField.setMinWidth(435);
	    animalIdName.getChildren().addAll(animalIdLabel, animalIdField, animalNameLabel, animalNameField);
	    
	    animalTypeLabel = new Label("Type*:");
	    animalType.getChildren().addAll(animalTypeLabel, animalTypeField);
	    
	    animalBreedLabel = new Label("Breed:");
	    animalBreedField = new TextField();
	    animalBreedField.setMinWidth(315);
	    animalAgeLabel = new Label("Age: ");
	    animalAgeField = new TextField();
	    animalAgeField.setMinWidth(315);
	    animalBreedAge.getChildren().addAll(animalBreedLabel, animalBreedField, animalAgeLabel, animalAgeField);
	    
	    genderAnimalGroup = new ToggleGroup();
	    
	    animalGenderLabel = new Label("Gender*:");
	    femaleAnimal = new RadioButton("Female");
	    femaleAnimal.setToggleGroup(genderAnimalGroup);
	    maleAnimal = new RadioButton("Male");
	    maleAnimal.setToggleGroup(genderAnimalGroup);
	    animalColourLabel = new Label("Colour:");
	    animalColourField = new TextField();
	    animalColourField.setMinWidth(440);
	    animalGenderColour.getChildren().addAll(animalGenderLabel, femaleAnimal, maleAnimal, animalColourLabel, animalColourField);
	    
	    animalDescriptionLabel = new Label("Description*:");
	    animalDescriptionArea = new TextArea();
	    animalDescriptionArea.setMinSize(800, 60);
	    
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
	    
	    personDetails = new Label("OWNER DETAILS:");
	    personDetails.setFont(Font.font("Berlin Sans FB", 15));
	    
	    ownerNameLabel = new Label("Full Name*:");
	    ownerNameField = new TextField();
	    ownerNameField.setMinWidth(690);
	    ownerName.getChildren().addAll(ownerNameLabel, ownerNameField);
	    
	    ownerTelephoneLabel = new Label("Telephone*:");
	    ownerTelephoneField = new TextField();
	    ownerTelephoneField.setMinWidth(70);
	    ownerEmailLabel = new Label("Email:");
	    ownerEmailField = new TextField();
	    ownerEmailField.setMinWidth(405);
	    ownerTelephoneEmail.getChildren().addAll(ownerTelephoneLabel, ownerTelephoneField, ownerEmailLabel, ownerEmailField);
	     
	    ownerAddressLabel = new Label("Address:");
	    ownerAddressField = new TextField();
	    ownerAddressField.setMinWidth(710);
	    ownerAddress.getChildren().addAll(ownerAddressLabel, ownerAddressField);

	    submitButton = new Button("Submit");
	    submitButton.setStyle("-fx-background-color: darkGreen; -fx-text-fill: white");
	    submitButton.setOnAction(new LostAnimalController(this));
	    clearButton = new Button("Clear");
	    clearButton.setOnAction(new LostAnimalController(this));
	    cancelButton = new Button("Cancel");
	    cancelButton.setOnAction(new LostAnimalController(this));
	    actionButtons.getChildren().addAll(submitButton, clearButton, cancelButton);
	    actionButtons.setAlignment(Pos.CENTER);

	    addLostAnimalPane.add(title, 0, 0);
	    addLostAnimalPane.add(animalIdName, 0, 3, 2, 1);
	    addLostAnimalPane.add(animalType, 0, 4, 2, 1);
	    addLostAnimalPane.add(animalBreedAge, 0, 6, 2, 1);
	    addLostAnimalPane.add(animalGenderColour, 0, 7, 2, 1);
	    addLostAnimalPane.add(animalDescriptionLabel, 0, 8);
	    addLostAnimalPane.add(animalDescriptionArea, 0, 9);
	    addLostAnimalPane.add(animalDateLocation, 0, 10, 2, 1);
	    addLostAnimalPane.add(personDetails, 0, 13);
	    addLostAnimalPane.add(ownerName, 0, 15);
	    addLostAnimalPane.add(ownerTelephoneEmail, 0, 16);
	    addLostAnimalPane.add(ownerAddress, 0, 17);
	    addLostAnimalPane.add(actionButtons, 0, 20);
		
        return addLostAnimalPane;
	}
	
	@SuppressWarnings({"unchecked"})
	public Pane displayLostAnimalAction() {
		GridPane displayLostAnimalPane = new GridPane();
		displayLostAnimalPane.setAlignment(Pos.CENTER);
		displayLostAnimalPane.setHgap(30);
		displayLostAnimalPane.setVgap(12);
		displayLostAnimalPane.setPadding(new Insets(100));

	    sectionTitle = new Label("LOST ANIMAL");
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
        animalDetails.setItems(new LostAnimalController(this).displayAll());
        
	    allowOwnerDetailsCheckBox = new CheckBox("Allow Owner Details");
	    allowOwnerDetails.getChildren().addAll(allowOwnerDetailsCheckBox);
	    
	    ownerNameLabel = new Label("Full Name:");
	    ownerNameField = new TextField();
	    ownerNameField.setMinWidth(295);
	    ownerNameField.setEditable(false);
	    ownerTelephoneLabel = new Label("Phone:");
	    ownerTelephoneField = new TextField();
	    ownerTelephoneField.setMinWidth(295);
	    ownerTelephoneField.setEditable(false);
	    ownerDetails.getChildren().addAll(ownerNameLabel, ownerNameField, ownerTelephoneLabel, ownerTelephoneField);
	    ownerDetails.setVisible(false);

	    displayLostAnimalPane.add(title, 0, 0);
	    displayLostAnimalPane.add(animalDetails, 0, 5);
	    displayLostAnimalPane.add(allowOwnerDetails, 0, 13);
	    displayLostAnimalPane.add(ownerDetails, 0, 15);
	    
	    allowOwnerDetailsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(allowOwnerDetailsCheckBox.isSelected()) {
					Animal animal = animalDetails.getSelectionModel().getSelectedItem();
					if(animal != null) {
						ownerNameField.setText(animal.getAnimalCategory().getEmergencyContact().getPersonName());
						ownerTelephoneField.setText(animal.getAnimalCategory().getEmergencyContact().getPersonPhone());
					}
					ownerDetails.setVisible(true);
				}
				
				else {
					ownerDetails.setVisible(false);
				}
			}
		});
		
        return displayLostAnimalPane;
	}

	@SuppressWarnings("unchecked")
	public Pane reportLostAnimalAction() {
		GridPane reportLostAnimalPane = new GridPane();
		reportLostAnimalPane.setAlignment(Pos.CENTER);
		reportLostAnimalPane.setHgap(30);
		reportLostAnimalPane.setVgap(12);
		reportLostAnimalPane.setPadding(new Insets(100));

	    sectionTitle = new Label("LOST ANIMAL");
	    sectionTitle.setFont(Font.font("Berlin Sans FB", 20));
	    title.getChildren().add(sectionTitle);
	    title.setAlignment(Pos.CENTER);
	    
	    animalTypeLabel = new Label("Type:");
	    animalTypeReport.setValue(moreTypes.get(0));
	    animalType.getChildren().addAll(animalTypeLabel, animalTypeReport);
	    
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
	    
	    animalLocationLabel = new Label("Location:");
	    animalLocationField = new TextField();
	    animalLocationField.setMinWidth(395);
	    animalDateLocation.getChildren().addAll(animalDateLabel, animalDateField, animalLocationLabel, animalLocationField);

	    generateReportButton = new Button("Generate Report");
	    generateReportButton.setOnAction(new LostAnimalController(this));
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

	    reportLostAnimalPane.add(title, 0, 0);
	    reportLostAnimalPane.add(animalType, 0, 5);
	    reportLostAnimalPane.add(animalDateLocation, 0, 6, 2, 1);
	    reportLostAnimalPane.add(actionButtons, 0, 9);
	    reportLostAnimalPane.add(animalDetails, 0, 12);
		
        return reportLostAnimalPane;
	}
	/* Getters */
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
	
	public ToggleGroup getGenderAnimalGroup() {
		return genderAnimalGroup;
	}

	public DatePicker getAnimalDateField() {
		return animalDateField;
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

	public TextArea getAnimalDescriptionArea() {
		return animalDescriptionArea;
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
	
	public Button getSearchToDisplayButton() {
		return searchToDisplayButton;
	}
	
	public Button getGenerateReportButton() {
		return generateReportButton;
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
