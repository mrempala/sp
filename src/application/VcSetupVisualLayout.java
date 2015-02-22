package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class VcSetupVisualLayout extends VcMainController{

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcVisualSchematicView visualSchematicController;
	
	@FXML Button button_loadSequencePreview;
	@FXML Pane sceneContainer;
	@FXML AnchorPane squibsToPlace;
	
	@FXML ListView<String> listview_squibGroups;
	ObservableList<String> items = FXCollections.observableArrayList ();
	int groupCount = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		listview_squibGroups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        // TODO: When the user selects the group, reselect corresponding squibs in schematic view
		    	//       and color appropriately, allowing user to edit the given group.
		        System.out.println("Selected item: " + newValue);
		    }
		});
	}
	
	@FXML
	public void makeSquibGroup(){
		// Add the newly created group to the sequence
		Universe squibGroup = new Universe();
		int fb = 0;
		int lb = 0;
		
		// For each selected squib add to the squib group universe
		for (Squib squib : visualSchematicController.selectedSquibs){
			while (squibGroup.fireboxList.size() <= squib.getFirebox()) {
				Firebox firebox = new Firebox(fb);
				squibGroup.fireboxList.add(firebox);
				fb++;
			}
			while (squibGroup.fireboxList.get(squib.getFirebox()).lunchboxList.size() <= squib.getLunchbox()){
				Lunchbox lunchbox = new Lunchbox(lb, fb);
				squibGroup.fireboxList.get(squib.getFirebox()).lunchboxList.add(lunchbox);
				lb++;
			}
			squibGroup.fireboxList.get(squib.getFirebox()).lunchboxList.get(squib.getLunchbox()).squibList.add(squib);
		}
		
		squibGroup.traverseUniverse();
		
		// TODO: Test to confirm clone is making a deep copy
		sequence.squibGroups.add(squibGroup);
		items.add("Group " + groupCount);
		listview_squibGroups.setItems(items);
		
		// Clear the selected squibs
		visualSchematicController.selectedSquibs.clear();
		
		// Redraw the universe to erase previous selections
		visualSchematicController.drawUniverseSchematic();
		
		// Check all the universes in the list
		for (Universe squibList : sequence.squibGroups){
			squibList.traverseUniverse();
		}
		groupCount++;
	}
}