package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
//import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class VcNewProjectSetup {
    @FXML private Text actiontarget;
    @FXML private Button button_newproject;
    @FXML private Button button_universeconfig;
    @FXML private ToggleGroup universeConfiguration;
    
    // New project setup text fields
    @FXML public TextField tfShow;
    @FXML public TextField tfProjectName;
    @FXML public TextField tfVenue;
    @FXML public TextField tfDj;
    
    String show;
    String projectName;
    String venue;
    String dj;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }
    
    @FXML protected void openNewProjectDetails(ActionEvent event){
        Parent root;
        try {
        	// Load the next window
            root = FXMLLoader.load(getClass().getResource("Views/UI-Setup.fxml"));
            Scene scene = new Scene(root, 500, 300);
            Stage stage = new Stage();
            stage.setTitle("New Project - Details");
            stage.setScene(scene);
            stage.show();
            
            // Close the current window
            // get a handle to the stage
            Stage currentstage = (Stage) button_newproject.getScene().getWindow();
            // and close it
            currentstage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML protected void openUniverseConfig(ActionEvent event) {
    	// TODO: Pass sequence into manual load to get the user defined project name, etc.
    	Sequence sequence = new Sequence();
    	String windowToLoad;
    	int width, height;
    	
    	// Get text field values and confirm
    	show = tfShow.getText();
    	projectName = tfProjectName.getText();
    	venue = tfVenue.getText();
    	dj = tfDj.getText();
    	
    	//TODO: Add more rigorous check, for now just make sure the user input something for fields
    	if (show.equals("") || projectName.equals("") || venue.equals("") || dj.equals("")) {
    		System.out.println("Bad Input, try again!");
    		return;
    	}
    	
    	System.out.println(projectName + " " + venue + " " + show + " " + dj);
    	
    	sequence.setProjectName(projectName);
    	sequence.setDj(dj);
    	sequence.setShow(show);
    	sequence.setVenue(venue);
    	
    	// If auto universe detection is selected by radio button
    	if (universeConfiguration.getSelectedToggle().getUserData().toString().equals("auto")){
    		windowToLoad = "UI-Setup-AutoLoad";
    		width = 500;
    		height = 150;
    		System.out.println("auto");
    	}
    	else {
    		windowToLoad = "UI-Setup-ManualLoad";
    		width = 300;
    		height = 320;
    		System.out.println("manual");
    	}
    	
    	// Open new window
        Parent root;
        try {
        	// Load the next window
            root = FXMLLoader.load(getClass().getResource("Views/" + windowToLoad + ".fxml"));
            //TreeItem<String> newNode = new TreeItem<String>("Test Node");
            //FB1.getChildren().add(newNode);
            Scene scene = new Scene(root, width, height);
            Stage stage = new Stage();
            stage.setTitle("Project Name - Configuration");
            stage.setScene(scene);
            stage.show();
            
            // Close the current window
            // get a handle to the stage
            Stage currentstage = (Stage) button_universeconfig.getScene().getWindow();
            // and close it
            currentstage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

