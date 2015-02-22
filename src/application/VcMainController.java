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

public class VcMainController implements Initializable{

    Stage currentStage;
	Sequence sequence;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sequence = new Sequence();
	}
	
	public Sequence getSequence(){
		return sequence;
	}
	
	public void setSequence(Sequence sequence){
		this.sequence = sequence;
	}
	
	public Universe getUniverse(){
		return sequence.universe;
	}
	
	public void setUniverse(Universe universe){
		sequence.universe = universe;
	}
	
	public void setCurrentStage(Stage currentStage){
		this.currentStage = currentStage;
	}
	
	/*
	 * Function calls to load various windows of the program
	 * 
	 * 
	 */
	
    @FXML protected void openNewProjectDetails(ActionEvent event){
        Parent root;
        try {
        	// Load the next window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-Setup.fxml"));
            root = (Parent)loader.load();            
            VcNewProjectSetup newProjectController = loader.<VcNewProjectSetup>getController();
    	    newProjectController.setSequence(sequence);
    	    
            Scene scene = new Scene(root, 500, 300);
            Stage stage = new Stage();
            stage.setTitle("New Project - Details");
            stage.setScene(scene);
            stage.show();
            newProjectController.setCurrentStage(stage);
            
            // Close the current window
            // get a handle to the stage
            //Stage currentstage = (Stage) button_newproject.getScene().getWindow();
            // and close it
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Load Universe manual setup window
    @FXML 
    protected void openUniverseManualConfig(ActionEvent event) throws IOException {    	
    	// Open new window
        Parent root;

    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-Setup-ManualLoad.fxml"));
        root = (Parent)loader.load();

	    // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
	    VcSetupManualLoad setupManualController = loader.<VcSetupManualLoad>getController();
	    setupManualController.setSequence(sequence);
        
        Scene scene = new Scene(root, 300, 320);
        Stage stage = new Stage();
        stage.setTitle("Universe Configuration");
        stage.setScene(scene);
        stage.show();
        setupManualController.setCurrentStage(stage);
        
        // Close the current window
        // get a handle to the stage
        //Stage currentstage = (Stage) button_universeconfig.getScene().getWindow();
        currentStage.close();
    }
	
	// Load the visual organizer
	@FXML 
	protected void openVisualOrganizer(ActionEvent event) throws IOException{
		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-Setup-VisualLayout.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSetupVisualLayout seqVisualLayoutController = loader.<VcSetupVisualLayout>getController();
        seqVisualLayoutController.setSequence(sequence);
        seqVisualLayoutController.visualSchematicController.setUniverse(sequence.universe);
        seqVisualLayoutController.visualSchematicController.drawUniverseSchematic();

        Scene scene = new Scene(root, 1000, 450);
        Stage stage = new Stage();
        stage.setTitle("Visual Organization");
        stage.setScene(scene);
        stage.show();
        seqVisualLayoutController.setCurrentStage(stage);
        
        // Get a handle to the stage, close the current window 
        //Stage currentstage = (Stage) button_openVisualOrganizer.getScene().getWindow();
        currentStage.close();
	}
	
	// Load the Sequence Editor window
	@FXML 
	protected void loadSequenceEditor(ActionEvent event) throws IOException{
		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequenceEditor.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSequenceEditor seqEditorController = loader.<VcSequenceEditor>getController();
        seqEditorController.setSequence(sequence);
        seqEditorController.visualSchematicController.setUniverse(sequence.universe);
        seqEditorController.visualSchematicController.drawUniverseSchematic();
        
        seqEditorController.loadGroups();
        
        // Register the sequence preview as an observer of the time line to get play and pause events
        seqEditorController.timeLineController.addObserver(seqEditorController);
        
        Scene scene = new Scene(root, 1000, 450);
        Stage stage = new Stage();
        stage.setTitle("Sequence Selector");
        stage.setScene(scene);
        stage.show();
        seqEditorController.setCurrentStage(stage);
        
        // Get a handle to the stage, close the current window 
        //Stage currentstage = (Stage) button_loadSequencePreview.getScene().getWindow();
        currentStage.close();
	}
	
	// Load the Sequence Preview window
	@FXML 
	protected void loadSequencePreview(ActionEvent event) throws IOException {
		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequencePreview.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSequencePreview seqPreviewController = loader.<VcSequencePreview>getController();
        // And hackily push the necessary variables into it
        seqPreviewController.setSequence(sequence);
        seqPreviewController.loadProjectInfo();
        seqPreviewController.visualSchematicController.setUniverse(sequence.universe);
        seqPreviewController.visualSchematicController.drawUniverseSchematic();
        
        // Hack to get sequence into the sequence previewer
        seqPreviewController.buildTimelineAnimation();
        
        // Register the sequence preview as an observer of the time line to get play and pause events
        seqPreviewController.timeLineController.addObserver(seqPreviewController);
        
        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setTitle("Sequence Preview");
        stage.setScene(scene);
        stage.show();
        seqPreviewController.setCurrentStage(stage);
        
        // Close the current window
        currentStage.close();
	}
}