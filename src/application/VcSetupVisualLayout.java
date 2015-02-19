package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
//import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class VcSetupVisualLayout implements Initializable {

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcVisualSchematicView visualSchematicController;
	
	@FXML Button button_loadSequencePreview;
	@FXML Pane sceneContainer;
	@FXML AnchorPane squibsToPlace;
	
	@FXML ListView<String> listview_squibGroups;
	ObservableList<String> items = FXCollections.observableArrayList ();
	int groupCount = 0;
	
	Sequence sequence;
	Universe universe;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO: Load all squibs to be placed visually in scene

	}
	
	public void setSequence(Sequence sequence){
		this.sequence = sequence;
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	public void makeSquibGroup(){
		// Add the newly created group to the sequence
		// TODO: Test to confirm clone is making a deep copy
		sequence.squibGroups.add((ArrayList<Squib>) visualSchematicController.selectedSquibs.clone());
		items.add("Group " + groupCount);
		listview_squibGroups.setItems(items);
		
		// Clear the selected squibs
		visualSchematicController.selectedSquibs.clear();
		
		// Redraw the universe to erase previous selections
		visualSchematicController.drawUniverseSchematic();
		for (List<Squib> squibList : sequence.squibGroups){
			System.out.println(squibList.toString() + "\n");
		}
		groupCount++;
	}
	
	@FXML 
	protected void loadSequenceEditor(ActionEvent event) throws IOException{
		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequenceEditor.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSequenceEditor seqEditorController = loader.<VcSequenceEditor>getController();
        seqEditorController.visualSchematicController.setUniverse(sequence.universe);
        seqEditorController.visualSchematicController.drawUniverseSchematic();
        
        // Hack to get sequence into the sequence previewer
        seqEditorController.setSequence(sequence);
        
        // Build the time line animation
        //seqEditorController.buildTimelineAnimation();
        
        // Register the sequence preview as an observer of the time line to get play and pause events
        seqEditorController.timeLineController.addObserver(seqEditorController);
        
        Scene scene = new Scene(root, 1000, 450);
        Stage stage = new Stage();
        stage.setTitle("Sequence Selector");
        stage.setScene(scene);
        stage.show();
        
        // Get a handle to the stage, close the current window 
        Stage currentstage = (Stage) button_loadSequencePreview.getScene().getWindow();
        currentstage.close();
        //universe.writeUniverse("test_output.txt");
	}
}