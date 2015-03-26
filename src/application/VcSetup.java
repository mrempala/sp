package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
 
public class VcSetup extends VcMainController {
    @FXML private Button button_universeconfig;
    
    // New project setup text fields
    @FXML public TextField tfProjectName;
    @FXML public TextArea taProjectDetails;
    //@FXML public TextField tfVenue;
    //@FXML public TextField tfDj;
    
    String projectName;
    String projectDetails;
    
    @FXML 
    protected void openUniverseConfig(ActionEvent event) throws IOException {    	
    	// Get text field values and confirm
    	projectName = tfProjectName.getText();
    	projectDetails = taProjectDetails.getText();
    	
    	//TODO: Add more rigorous check, for now just make sure the user input something for fields
    	//TODO: Reenable setting project details, stopped check temporarily to expedite debugging
    	/*if (show.equals("") || projectName.equals("") || venue.equals("") || dj.equals("")) {
    		System.out.println("Bad Input, try again!");
    		return;
    	}
    	
    	System.out.println(projectName + " " + venue + " " + show + " " + dj);
    	*/
    	sequence.setProjectName(projectName);
    	sequence.setProjectDetails(projectDetails);
    	
    	// Call to load the manual configuration window
    	openSetupManualLoad(event);
    }
}

