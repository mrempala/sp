package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
 
public class VcSetup extends VcMainController {
    @FXML private Button button_universeconfig;
    
    // New project setup text fields
    @FXML public TextField tfShow;
    @FXML public TextField tfProjectName;
    @FXML public TextField tfVenue;
    @FXML public TextField tfDj;
    
    String show;
    String projectName;
    String venue;
    String dj;
    
    @FXML 
    protected void openUniverseConfig(ActionEvent event) throws IOException {    	
    	// Get text field values and confirm
    	show = tfShow.getText();
    	projectName = tfProjectName.getText();
    	venue = tfVenue.getText();
    	dj = tfDj.getText();
    	
    	//TODO: Add more rigorous check, for now just make sure the user input something for fields
    	//TODO: Reenable setting project details, stopped check temporarily to expedite debugging
    	/*if (show.equals("") || projectName.equals("") || venue.equals("") || dj.equals("")) {
    		System.out.println("Bad Input, try again!");
    		return;
    	}
    	
    	System.out.println(projectName + " " + venue + " " + show + " " + dj);
    	*/
    	// TODO: For some reason these values aren't carrying through the project
    	sequence.setProjectName(projectName);
    	sequence.setDj(dj);
    	sequence.setShow(show);
    	sequence.setVenue(venue);
    	
    	// Call to load the manual configuration window
    	openSetupManualLoad(event);
    }
}

