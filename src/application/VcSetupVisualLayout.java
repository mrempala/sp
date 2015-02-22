package application;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class VcSetupVisualLayout extends VcMainController implements Initializable  {

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcVisualSchematicView visualSchematicController;
	
	@FXML Button button_loadSequencePreview;
	@FXML Pane sceneContainer;
	@FXML AnchorPane squibsToPlace;
	
	@FXML ListView<String> listview_squibGroups;
	ObservableList<String> items = FXCollections.observableArrayList ();
	int groupCount = 0;
	

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
}