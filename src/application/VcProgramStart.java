package application;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
 
public class VcProgramStart extends VcMainController {
    @FXML private Text actiontarget;
    @FXML private Button button_newproject;
    @FXML private Button button_browse;
    
    // Canned universe links
    @FXML public Hyperlink link1;
    @FXML public Hyperlink link2;
    @FXML public Hyperlink link3;
    @FXML public Hyperlink link4;
    @FXML public Hyperlink link5;
    
	@FXML 
	void loadExistingUniverse(ActionEvent event) throws IOException{
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
    	openSequenceEditor(event);
	}
    
    @FXML  
    protected void browseExistingProject(ActionEvent event) throws IOException {
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
        	openSequenceEditor(event);
        }
        else{
        	System.out.println("Error, file could not be opened");
        }
    }
}

