package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class VcSequencePreview implements Initializable {

	//@FXML AnchorPane visualContainer;
	//@FXML AnchorPane schematicContainer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        
	}
	
	@FXML 
	protected void openSequenceEditor(ActionEvent event) throws IOException{
		Parent root;
    	// Load the next window
        root = FXMLLoader.load(getClass().getResource("Views/UI-SequenceEditor.fxml"));
        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setTitle("New Project - Details");
        stage.setScene(scene);
        stage.show();
        
        // Close the current window
        // get a handle to the stage
        //Stage currentstage = (Stage) button_openVisualOrganizer.getScene().getWindow();
        // and close it
        //currentstage.close();
	}
}