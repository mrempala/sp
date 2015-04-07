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
		return sequence.getUniverse();
	}
	
	public void setUniverse(Universe universe){
		sequence.setUniverse(universe);
	}
	
	public void setCurrentStage(Stage currentStage){
		this.currentStage = currentStage;
	}
	
	/*
	 * Function calls to load various windows of the program
	 * 
	 * I tried to list the calls the order the program opens windows
	 */
	
    @FXML protected void openSetup(ActionEvent event){
        Parent root;
        try {
        	// Load the next window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-Setup.fxml"));
            root = (Parent)loader.load();            
            VcSetup newProjectController = loader.<VcSetup>getController();
    	    newProjectController.setSequence(sequence);
    	    
            Scene scene = new Scene(root, 390, 230);
            Stage stage = new Stage();
            stage.setTitle("Setup: Project Details");
            stage.setScene(scene);
            stage.show();
            newProjectController.setCurrentStage(stage);
            
            // Close the current window
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Load Universe manual setup window
    @FXML 
    protected void openSetupManualLoad(ActionEvent event) throws IOException {    	
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
        stage.setTitle("Setup: Manual Universe Configuration");
        stage.setScene(scene);
        stage.show();
        setupManualController.setCurrentStage(stage);
        
        // Close the current window
        currentStage.close();
    }
    
    @FXML
    protected void openSetupVisualLayout(ActionEvent event) throws IOException {
    	// TODO: Move this initial squibGroup setup elsewhere
		// Add the newly created universe to the sequence's SquibGroup list
		/* 
		 * NOTE: If using this call to openSetupVisualLayout, need to uncomment below
		 * and comment these calls out in openSetupSquibGroups
		SquibGroup squibGroup = new SquibGroup();
		squibGroup.setUniverse(sequence.getUniverse());
		squibGroup.setGroupName("Universe");
		sequence.getSquibGroups().add(squibGroup);
		*/
    	// Open new window
        Parent root;

    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-Setup-VisualLayout.fxml"));
        root = (Parent)loader.load();

	    // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
	    VcSetupVisualLayout setupVisualLayout = loader.<VcSetupVisualLayout>getController();
	    setupVisualLayout.setSequence(sequence);
        
        Scene scene = new Scene(root, 1280, 720);
        Stage stage = new Stage();
        stage.setTitle("Setup: Universe Visual Layout");
        stage.setScene(scene);
        stage.show();
        setupVisualLayout.setCurrentStage(stage);
        
        // Close the current window
        currentStage.close();
    }
	
	// Load the squib groups organizer
	@FXML 
	protected void openSetupSquibGroups(ActionEvent event) throws IOException{
    	// TODO: Move this initial squibGroup setup elsewhere
		// Add the newly created universe to the sequence's SquibGroup list
		SquibGroup squibGroup = new SquibGroup();
		squibGroup.setUniverse(sequence.getUniverse());
		squibGroup.setGroupName("Universe");
		sequence.getSquibGroups().add(squibGroup);
		
		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-Setup-SquibGroups.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSetupSquibGroups seqVisualLayoutController = loader.<VcSetupSquibGroups>getController();
        seqVisualLayoutController.setSequence(sequence);
        seqVisualLayoutController.visualSchematicController.setUniverse(sequence.getUniverse());
        seqVisualLayoutController.visualSchematicController.drawUniverseSchematic();
        seqVisualLayoutController.visualSchematicController.drawUniverseVisual();

        Scene scene = new Scene(root, 1000, 410);
        Stage stage = new Stage();
        stage.setTitle("Setup: Squib Groups");
        stage.setScene(scene);
        stage.show();
        seqVisualLayoutController.setCurrentStage(stage);
        
        currentStage.close();
	}
	
	// Load the Sequence Editor window
	@FXML 
	protected void openSequenceEditor(ActionEvent event) throws IOException{
		Parent root;
		//Parent root1;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequenceEditor.fxml"));
        root = (Parent)loader.load();
       /* FXMLLoader loader1 = new FXMLLoader(getClass().getResource("Views/UI-Part-Menu.fxml"));
        root1 = (Parent)loader1.load();
        */
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSequenceEditor seqEditorController = loader.<VcSequenceEditor>getController();
       // VcPtMenu ptController = loader.<VcPtMenu>getController();
        seqEditorController.setSequence(sequence);
        seqEditorController.PTMenuController.setSequence(sequence);
        
         
        seqEditorController.visualSchematicController.setUniverse(sequence.getUniverse());
        seqEditorController.PTMenuController.setUniverse(sequence.getUniverse());
        
        seqEditorController.visualSchematicController.drawUniverseSchematic();
        
        seqEditorController.loadGroups();
        // Register the sequence preview as an observer of the time line to get play and pause events
        seqEditorController.timeLineController.addObserver(seqEditorController);
        
        Scene scene = new Scene(root, 1081, 550);
        Stage stage = new Stage();
        stage.setTitle("Sequence Selector");
        stage.setScene(scene);
        stage.show();
        seqEditorController.setCurrentStage(stage);
        seqEditorController.PTMenuController.setCurrentStage(stage);
       
        currentStage.close();
	}
	
	// Load the Sequence Preview window
	@FXML 
	protected void openSequencePreview(ActionEvent event) throws IOException {
		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequencePreview.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSequencePreview seqPreviewController = loader.<VcSequencePreview>getController();
        // And hackily push the necessary variables into it
        seqPreviewController.setSequence(sequence);
        seqPreviewController.PTMenuController.setSequence(sequence);
        
        seqPreviewController.loadProjectInfo();
        seqPreviewController.visualSchematicController.setUniverse(sequence.getUniverse());
        seqPreviewController.PTMenuController.setUniverse(sequence.getUniverse());
        
        seqPreviewController.visualSchematicController.drawUniverseSchematic();
        
        // Hack to get sequence into the sequence previewer
        seqPreviewController.buildTimelineAnimation();
        
        // Register the sequence preview as an observer of the time line to get play and pause events
        seqPreviewController.timeLineController.addObserver(seqPreviewController);
        
        Scene scene = new Scene(root, 1081, 500);
        Stage stage = new Stage();
        stage.setTitle("Sequence Preview");
        stage.setScene(scene);
        stage.show();
        seqPreviewController.setCurrentStage(stage);
        
        // Close the current window
        currentStage.close();
	}
	
	
}
