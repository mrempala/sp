package application;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
 
public class VcNewProjectSetup extends VcMainController {
    @FXML private Text actiontarget;
    @FXML private Button button_newproject;
    @FXML private Button button_universeconfig;
    // @FXML private ToggleGroup universeConfiguration; // Used in the event DSSP wants to add auto universe detection in the future
    @FXML private Button button_browse;
    
    // New project setup text fields
    @FXML public TextField tfShow;
    @FXML public TextField tfProjectName;
    @FXML public TextField tfVenue;
    @FXML public TextField tfDj;
    
    // Canned universe links
    @FXML public Hyperlink link1;
    @FXML public Hyperlink link2;
    @FXML public Hyperlink link3;
    @FXML public Hyperlink link4;
    @FXML public Hyperlink link5;
    
    String show;
    String projectName;
    String venue;
    String dj;
    
	@FXML void loadExistingUniverse(ActionEvent event) throws IOException{
		Hyperlink clickedLink = (Hyperlink) event.getSource();
		String selectedSelection = clickedLink.getText();
		String fileToOpen = "uninitalized";
		Universe universe = new Universe();
		
		if(selectedSelection.equals("Scene 1")){
			fileToOpen = "universe_4x6x8.txt";
		}
		else if(selectedSelection.equals("Scene 2")){
			fileToOpen = "universe_4x4x8.txt";
		}
		else if(selectedSelection.equals("Scene 3")){
			fileToOpen = "universe_1x4x8.txt";
		}
		else if(selectedSelection.equals("Scene 4")){
			fileToOpen = "universe_pyramid.txt";
		}
		else if(selectedSelection.equals("Scene 5")){
			fileToOpen = "universe_unusual.txt";
		}

    	universe.readUniverse(fileToOpen);
    	
    	sequence = new Sequence(universe);
    	loadSequenceEditor(event);
	}
    
    @FXML  protected void browseExistingProject(ActionEvent event) throws IOException {
    	System.out.println("browse button hit");
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	File file = fileChooser.showOpenDialog(currentStage);
        if (file != null) {
        	System.out.println("File does exist");
        	System.out.println(file);
        	
        	Universe universe = new Universe();
        	universe.readUniverse(file.getAbsolutePath());
        	
        	sequence = new Sequence(universe);
        	loadSequenceEditor(event);
        }
        else{
        	System.out.println("Error, file could not be opened");
        }
        
    }
    
    @FXML protected void openUniverseConfig(ActionEvent event) throws IOException {    	
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
    	openUniverseManualConfig(event);
    }
}

