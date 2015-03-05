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
	int groupCount = 0;
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
		// Create a Universe object for the new squib group and add it to the list of groups
		Universe squibGroup = new Universe();
		sequence.squibGroups.add(squibGroup);
		
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
		
		sequence.squibGroups.remove(groupToEdit);
		sequence.squibGroups.add(groupToEdit, squibGroup);
		
		// Check all the universes in the list
		for (Universe squibList : sequence.squibGroups){
			squibList.traverseUniverse();
		}
		
		label_message.setText("  Group " + groupToEdit + " updated.  ");
	}
	
	public void selectSquibGroup(int index){
		Universe u = sequence.squibGroups.get(index);
		
		// Clear any previously selected squibs
		visualSchematicController.selectedSquibs.clear();
		
		// And select the squibs from the given group
		for (Firebox fb : u.fireboxList){
			for (Lunchbox lb : fb.lunchboxList){
				for (Squib s : lb.squibList){
					visualSchematicController.selectedSquibs.add(s);
				}
			}
		}
		
		// Redraw the universe for the up to date selection list
		visualSchematicController.drawUniverseSchematic();
	}
}