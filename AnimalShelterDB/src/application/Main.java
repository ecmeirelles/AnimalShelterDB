package application;

import java.time.LocalDate;
import java.util.Optional;

import connection.ConnectionDB;
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
	private static ConnectionDB connection;
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
			
			connection = new ConnectionDB();
			
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
			
			//TODO: Transferencia
			
			//TODO: Revisar essa parte
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		        	connection.closeConnection();
		        	  
		        	Alert alert = new Alert(AlertType.CONFIRMATION);
		  			alert.setHeaderText(null);
		  			alert.setContentText("Information will not be kept. \nDo you want to save?");
		  			
		  			ButtonType yes = new ButtonType("Yes", ButtonData.YES);
		  			ButtonType no = new ButtonType("No", ButtonData.NO);

		  			alert.getButtonTypes().setAll(yes, no);


		  			Optional<ButtonType> result = alert.showAndWait();
			
					//TODO: Metodo para nao salvar
					if(result.get() == no){
						
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
	
	public static ConnectionDB getConnection() {
		return connection;
	}
}