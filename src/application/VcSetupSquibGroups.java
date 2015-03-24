package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class VcSetupSquibGroups extends VcMainController{

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML VcPtVisualSchematicViewClickable visualSchematicController;
	
	@FXML Button button_loadSequencePreview;
	@FXML Button button_setGroup;
	@FXML Pane sceneContainer;
	@FXML AnchorPane squibsToPlace;
	@FXML Label label_message;
	
	@FXML ListView<String> listview_squibGroups;
	ObservableList<String> items = FXCollections.observableArrayList ();
	int groupCount = 1;
	int groupToEdit = -1; // Edit variable tracks if an item other than "New Group" has been selected
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		listview_squibGroups.setItems(items);
		listview_squibGroups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        System.out.println("Selected item: " + newValue);
                String elements[] = newValue.split(" ");
                groupToEdit = Integer.parseInt(elements[1]);
                selectSquibGroup(groupToEdit);
		    }
		});
	}
	
	@FXML void newSquibGroup(){		
		// Create the new squibgroup in the Sequence object
		SquibGroup squibGroup1 = new SquibGroup();
		squibGroup1.setGroupName("Group " + groupCount);
		sequence.squibGroups.add(squibGroup1);
		
		// Create a new list item for group and select it
		items.add("Group " + groupCount);
		listview_squibGroups.setItems(items);
		listview_squibGroups.getSelectionModel().selectLast();
		
		// TODO: Should we check to see if this is the first group or just reset the variable each time?
		// Enable clickable
		visualSchematicController.clickable = true;
		
		// Clear the selected squibs
		visualSchematicController.selectedSquibs.clear();
		
		// Redraw the universe to erase previous selections
		visualSchematicController.drawUniverseSchematic();
		groupToEdit = groupCount;
		groupCount++;
		
		label_message.setText("  Created Group " + groupToEdit + ".  ");
		button_setGroup.setDisable(false);
	}
	
	@FXML
	public void setSquibGroup(){
		// Add the newly created group to the sequence
		Universe squibGroup = new Universe();
		int fb = 0;
		int lb = 0;
		
		// For each selected squib add to the squib group universe
		for (Squib squib : visualSchematicController.selectedSquibs){
			while (squibGroup.getFireboxList().size() <= squib.getFirebox()) {
				Firebox firebox = new Firebox(fb);
				squibGroup.getFireboxList().add(firebox);
				fb++;
			}
			while (squibGroup.getFireboxList().get(squib.getFirebox()).getLunchboxList().size() <= squib.getLunchbox()){
				Lunchbox lunchbox = new Lunchbox(lb, fb);
				squibGroup.getFireboxList().get(squib.getFirebox()).getLunchboxList().add(lunchbox);
				lb++;
			}
			squibGroup.getFireboxList().get(squib.getFirebox()).getLunchboxList().get(squib.getLunchbox()).getSquibList().add(squib);
		}
		
		squibGroup.traverseUniverse();
		
		// Set the new universe squib group to the appropriate squib group object
		sequence.squibGroups.get(groupToEdit).setUniverse(squibGroup);
		
		// Check all the universes in the list
		for (SquibGroup squibList : sequence.squibGroups){
			squibList.squibs.traverseUniverse();
		}
		
		label_message.setText("  Group " + groupToEdit + " updated.  ");
	}
	
	public void selectSquibGroup(int index){
		Universe u = sequence.squibGroups.get(index).getSquibs();
		
		// Clear any previously selected squibs
		visualSchematicController.selectedSquibs.clear();
		
		// And select the squibs from the given group
		for (Firebox fb : u.getFireboxList()){
			for (Lunchbox lb : fb.getLunchboxList()){
				for (Squib s : lb.getSquibList()){
					visualSchematicController.selectedSquibs.add(s);
				}
			}
		}
		
		// Redraw the universe for the up to date selection list
		visualSchematicController.drawUniverseSchematic();
	}
}