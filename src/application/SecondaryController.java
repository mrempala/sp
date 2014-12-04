package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
//import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class SecondaryController {
    @FXML private Text actiontarget;
    @FXML private Button button_newproject;
    @FXML private Button button_universeconfig;
    @FXML private ToggleGroup universeConfiguration;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }
    
    @FXML protected void openNewProjectDetails(ActionEvent event){
        Parent root;
        try {
        	// Load the next window
            root = FXMLLoader.load(getClass().getResource("Views/UI-New-Project-Details.fxml"));
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
        Parent root;
        try {
        	// Load the next window
            root = FXMLLoader.load(getClass().getResource("Views/UI-New-Project-Universe-Manual.fxml"));
            //TreeItem<String> newNode = new TreeItem<String>("Test Node");
            //FB1.getChildren().add(newNode);
            Scene scene = new Scene(root, 300, 330);
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

