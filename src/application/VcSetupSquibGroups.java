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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class VcSetupSquibGroups extends VcMainController{

	// Included VisualSchematic View reference
	@FXML TabPane visualSchematic;
	@FXML SplitPane splitpane_sPane;
	@FXML VcPtVisualSchematicViewClickable visualSchematicController;
	
	@FXML Button button_loadSequencePreview;
	@FXML Button button_setGroup;
	@FXML Button button_newGroup;
	@FXML Button button_clearGroup;
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
		        //System.out.println("Selected item: " + newValue);
                String elements[] = newValue.split(" ");
                groupToEdit = Integer.parseInt(elements[1]);
                selectSquibGroup(groupToEdit);
                
                // Disable buttons to ensure user saves group before continuing
        		button_newGroup.setDisable(true);
        		button_loadSequencePreview.setDisable(true);
		    }
		});
	}
	
	@FXML void newSquibGroup(){		
		// Create the new squibgroup in the Sequence object
		SquibGroup squibGroup1 = new SquibGroup();
		squibGroup1.setGroupName("Group " + groupCount);
		sequence.getSquibGroups().add(squibGroup1);
		
		// Create a new list item for group and select it
		items.add("Group " + groupCount);
		listview_squibGroups.setItems(items);
		listview_squibGroups.getSelectionModel().selectLast();
		
		// TODO: Should we check to see if this is the first group or just reset the variable each time?
		// Enable clickable
		visualSchematicController.clickable = true;
		
		// Clear the selected squibs
		visualSchematicController.deselect();
		
		// Redraw the universe to erase previous selections
		visualSchematicController.drawUniverseSchematic();
		visualSchematicController.drawUniverseVisual();
		groupToEdit = groupCount;
		groupCount++;
		
		label_message.setText("  Created Group " + groupToEdit + ".  ");
		button_setGroup.setDisable(false);
		
		// Disable buttons to ensure user saves group before continuing
		button_newGroup.setDisable(true);
		button_loadSequencePreview.setDisable(true);
		button_clearGroup.setDisable(false);
		
		label_message.setText("Set group before continuing!");
	}
	
	@FXML
	public void setSquibGroup(){
		// Add the newly created group to the sequence
		Universe squibGroup = new Universe();
		int fbNum = 0;
		int lbNum = 0;
		
		// For each selected squib add to the squib group universe
		for (Firebox fb : visualSchematicController.universe.getFireboxList())
		{
			for (Lunchbox lb : fb.getLunchboxList())
			{
				for (Squib squib : lb.getSquibList())
				{
					if(squib.getSelected() == 1)
					{
						while (squibGroup.getFireboxList().size() <= squib.getFirebox())
						{
							Firebox firebox = new Firebox(fbNum);
							squibGroup.getFireboxList().add(firebox);
							fbNum++;
						}
						while (squibGroup.getFireboxList().get(squib.getFirebox()).getLunchboxList().size() <= squib.getLunchbox())
						{
							Lunchbox lunchbox = new Lunchbox(lbNum, fbNum);
							squibGroup.getFireboxList().get(squib.getFirebox()).getLunchboxList().add(lunchbox);
							lbNum++;
						}
						squibGroup.getFireboxList().get(squib.getFirebox()).getLunchboxList().get(squib.getLunchbox()).getSquibList().add(squib);
					}
				}
			}
		}
		
		squibGroup.traverseUniverse();
		
		// Set the new universe squib group to the appropriate squib group object
		sequence.getSquibGroups().get(groupToEdit).setUniverse(squibGroup);
		
		// Check all the universes in the list
		for (SquibGroup squibList : sequence.getSquibGroups()){
			squibList.getSquibs().traverseUniverse();
		}
		
		label_message.setText("  Group " + groupToEdit + " updated.  ");
		
		// Reenable buttons to let the user continue with setup
		button_newGroup.setDisable(false);
		button_loadSequencePreview.setDisable(false);
	}

	public void selectSquibGroup(int index){
		Universe u = sequence.getSquibGroups().get(index).getSquibs();
		
		// Clear any previously selected squibs
		visualSchematicController.deselect();
		
		// And select the squibs from the given group
		for (Firebox fb : u.getFireboxList())
		{
			for (Lunchbox lb : fb.getLunchboxList())
			{
				for (Squib s : lb.getSquibList())
				{
					for (Firebox uFb : visualSchematicController.universe.getFireboxList())
					{
						for(Lunchbox uLb : uFb.getLunchboxList())
						{
							for(Squib uS : uLb.getSquibList())
							{
								if(uS.getFirebox() == s.getFirebox() && uS.getLunchbox() == s.getLunchbox() && uS.getSquib() == s.getSquib())
								{
									uS.setSelected(1);
								}
							}
						}
					}
				}
			}
		}
		
		// Redraw the universe for the up to date selection list
		visualSchematicController.drawUniverseSchematic();
		visualSchematicController.drawUniverseVisual();
	}
	
	@FXML
	public void clearSquibGroup(){
		// Add the newly created group to the sequence
		Universe squibGroup = new Universe();
		
		// Clear the list of selected squibs in the visualSchematicController
		visualSchematicController.deselect();
		
		// Reset the selected squib group's universe
		sequence.getSquibGroups().get(groupToEdit).setUniverse(squibGroup);
		
		// Redraw the universe with cleared groups
		visualSchematicController.drawUniverseSchematic();
		visualSchematicController.drawUniverseVisual();
		
		label_message.setText("  Group " + groupToEdit + " cleared.  ");
		
		// Reenable buttons to let the user continue with setup
		//button_newGroup.setDisable(false);
		//button_loadSequencePreview.setDisable(false);
	}
	
	// returns the position of the split pane which is needed for the visual view
	public double getSplitPanePosition()
	{
		return (splitpane_sPane.getDividerPositions())[0];
	}
}