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
	@FXML VcVisualSchematicViewClickable visualSchematicController;
	
	@FXML Button button_loadSequencePreview;
	@FXML Pane sceneContainer;
	@FXML AnchorPane squibsToPlace;
	
	@FXML ListView<String> listview_squibGroups;
	ObservableList<String> items = FXCollections.observableArrayList ();
	int groupCount = 0;
	int groupToEdit = -1; // Edit variable tracks if an item other than "New Group" has been selected
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		items.add("New Group");
		listview_squibGroups.setItems(items);
		listview_squibGroups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        // TODO: When the user selects the group, reselect corresponding squibs in schematic view
		    	//       and color appropriately, allowing user to edit the given group.
		        System.out.println("Selected item: " + newValue);
		        if (newValue.equals("New Group")) {
		        	groupToEdit = -1;
		        	visualSchematicController.selectedSquibs.clear();
		        	visualSchematicController.drawUniverseSchematic();
		        	return;
		        }
                String elements[] = newValue.split(" ");
                groupToEdit = Integer.parseInt(elements[1]);
                selectSquibGroup(groupToEdit);
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
		
		if (groupToEdit== -1){
			sequence.squibGroups.add(squibGroup);
			items.add("Group " + groupCount);
			listview_squibGroups.setItems(items);
			groupCount++;
		}
		else {
			sequence.squibGroups.remove(groupToEdit);
			sequence.squibGroups.add(groupToEdit, squibGroup);
		}
		
		// Clear the selected squibs
		visualSchematicController.selectedSquibs.clear();
		
		// Redraw the universe to erase previous selections
		visualSchematicController.drawUniverseSchematic();
		
		// Check all the universes in the list
		for (Universe squibList : sequence.squibGroups){
			squibList.traverseUniverse();
		}
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