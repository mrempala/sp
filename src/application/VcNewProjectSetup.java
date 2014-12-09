package application;

import java.io.File;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
public class VcNewProjectSetup {
    @FXML private Text actiontarget;
    @FXML private Button button_newproject;
    @FXML private Button button_universeconfig;
    @FXML private ToggleGroup universeConfiguration;
    @FXML private Button button_browse;
    
    // New project setup text fields
    @FXML public TextField tfShow;
    @FXML public TextField tfProjectName;
    @FXML public TextField tfVenue;
    @FXML public TextField tfDj;
    
    public Stage stage;
    
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
    
    @FXML  protected void browseExistingProject(ActionEvent event) throws IOException {
    	System.out.println("browse button hit");
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
        	System.out.println("File does exist");
        	System.out.println(file);
        	
        	Universe universe = new Universe();
        	universe.readUniverse(file.getAbsolutePath());
        	
        	Sequence sequence = new Sequence(universe);
    		Parent root;
    		
        	// Load the next window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequenceEditor.fxml"));
            root = (Parent)loader.load();
            
            // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
            VcSequenceEditor seqEditorController = loader.<VcSequenceEditor>getController();
            seqEditorController.visualSchematicController.setUniverse(universe);
            seqEditorController.visualSchematicController.drawUniverseSchematic();
            
            // Hack to get sequence into the sequence previewer
            seqEditorController.setSequence(sequence);
            
            // Register the sequence preview as an observer of the time line to get play and pause events
            seqEditorController.timeLineController.addObserver(seqEditorController);
            
            Scene scene = new Scene(root, 1000, 500);
            Stage stage = new Stage();
            stage.setTitle("Sequence Preview");
            stage.setScene(scene);
            stage.show();
            
            // Close the current window
            // get a handle to the stage
            Stage currentstage = (Stage) button_browse.getScene().getWindow();
            // and close it
            currentstage.close();
            //universe.writeUniverse("test_output.txt");
        }
        else{
        	System.out.println("Error, file could not be opened");
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

