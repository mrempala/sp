package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class VcSetupManualLoad extends VcMainController{

    @FXML private TreeView<String> mainTreeView;
    @FXML private TreeItem<String> rootTreeNode;
    @FXML private TextField text_inputValue;
    @FXML private Label label_setTreeNode;
    @FXML private Label label_errorMessage;
    @FXML private Button button_openVisualOrganizer;
    
    private TreeItem<String> currentTreeNode;
    private String elementToSet;	//This defines which universe element to set, num set by elementNum

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set currentTreeNode to the root node defined in fxml file
		currentTreeNode = rootTreeNode;
		elementToSet = "Firebox";
		
		// Make the treeview in the fxml file observable
        mainTreeView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldTreeNode, TreeItem<String> newTreeNode) {
            	// Handle the tree selection change event
            	currentTreeNode = newTreeNode;
                String element = currentTreeNode.getValue();
                String elements[] = element.split(" ");
                
                //System.out.println("Selected Text : " + elements[0]);
                
                // Update the element to be set as well as the label based on element value of currentTreeNode
                if (elements[0].equals("Firebox")){
                	elementToSet = "Lunchbox";
                	label_setTreeNode.setText("Number of Lunchboxes:");
                }
                else if (elements[0].equals("Lunchbox")){
                	elementToSet = "Squib";
                	label_setTreeNode.setText("Number of Squibs:");
                }
                else if (elements[0].equals("Universe")){
                	elementToSet = "Firebox";
                	label_setTreeNode.setText("Number of Fireboxes:");
                }
                else if (elements[0].equals("Squib")){
                	// Add/modify squib channel number 
                	elementToSet = "Channel";
                	label_setTreeNode.setText("Channel of Squib:");
                }
                else if(elements[0].equals("Channel")){
                	currentTreeNode = newTreeNode.getParent();
                	// Add/modify squib channel number 
                	elementToSet = "Channel";
                	label_setTreeNode.setText("Channel of Squib:");
                }
                else {
                	System.out.println("ERROR!");
                }
            }
          });
	}
	
	@FXML 
	protected void setValue(ActionEvent event) throws IOException {
		int value = Integer.parseInt(text_inputValue.getText());
		
		// Clear any previous error messages
		label_errorMessage.textProperty().set("");
		
		//System.out.println("\n\n" + currentTreeNode.getValue() + "\n\n" + elementToSet);
		
		// When clearing the same element twice in a row, tree item selection changed event is fired
		// and selects the parent item.  For this reason we need to back up the currently selected
		// node and node to set to ensure values don't get corrupted.
		if (!(currentTreeNode.getChildren().isEmpty())){
			TreeItem<String> oldTreeNode = currentTreeNode;
			String oldElementToSet = elementToSet;
			currentTreeNode.getChildren().clear();
			currentTreeNode = oldTreeNode;
			elementToSet = oldElementToSet;
		}
		
		// added check to prevent creating more than allowed according to hardware limitations (vp)
		if(elementToSet == "Squib" && value > 8) {
			label_errorMessage.textProperty().set(" ERROR: Squib value must be between 0 and 8! ");
			return;
		}
		else if(elementToSet == "Firebox" && value > 16){
			label_errorMessage.textProperty().set(" ERROR: Firebox value must be between 0 and 16! ");
			return;
		}
		else if(elementToSet == "Lunchbox" && value > 12){
			label_errorMessage.textProperty().set(" ERROR: Lunchbox value must be between 0 and 12! ");
			return;
		}
		
		if (elementToSet.equals("Channel")) {
			//System.out.println(currentTreeNode.getParent().getValue());
			for (TreeItem<String> s : currentTreeNode.getParent().getChildren()) {
				for (TreeItem<String> s1 : s.getChildren()){
					String currentValue[] = s1.getValue().split(" ");
					int val = Integer.parseInt(currentValue[1]);
					//System.out.println(s1.getValue());
					if (val == value){
						label_errorMessage.textProperty().set(" ERROR: Cannot have duplicate squib channels! ");
						return;
					}
				}
			}
			TreeItem<String> item = new TreeItem<String> (elementToSet + " " +  String.valueOf(value));
			item.setExpanded(true);
			currentTreeNode.getChildren().add(item);
		}
		else {
			for (int i = 0; i < value; i++) {
				TreeItem<String> item = new TreeItem<String> (elementToSet + " " +  String.valueOf(i+1));
				item.setExpanded(true);
				if (elementToSet.equals("Squib")) {
					TreeItem<String> childitem = new TreeItem<String> ("Channel " +  String.valueOf(i+1));
					item.getChildren().add(childitem);
				}
				currentTreeNode.getChildren().add(item);
			}
		}
		
		// Populate the universe based on users setting
		// TODO: Move this call to openVisualOrganizer once testing done.
		populateUniverse(rootTreeNode, 0);
	}
	
	public void traverseTree (TreeItem<String> t){
		//System.out.println(t.getValue());
		for(TreeItem<String> s : t.getChildren()){
			traverseTree(s);
		}
	}
	
	private void populateUniverse(TreeItem<String> tree, int elementNum) {	
		sequence.setUniverse(new Universe());
		for(TreeItem<String> s : tree.getChildren()){
			Firebox firebox = new Firebox(elementNum);
			//String universeItem = s.getValue();
			
			//System.out.println("Adding element: " + universeItem + "  " + elementNum);
			sequence.getUniverse().addFirebox(firebox);
			populateFirebox(s, 0, elementNum);
			elementNum++;
		}
	}
	
	private void populateFirebox(TreeItem<String> tree, int elementNum, int parentNum) {
		for(TreeItem<String> s : tree.getChildren()){
			//String universeItem = s.getValue();
			Lunchbox lunchbox = new Lunchbox(elementNum, parentNum);
			
			//System.out.println("Adding element: " + universeItem + "  " + elementNum + "  " + parentNum);
			sequence.getUniverse().getFireboxList().get(parentNum).addLunchbox(lunchbox);
			populateLunchbox(s, 0, elementNum, parentNum);
			elementNum++;
		}
	}
	
	private void populateLunchbox(TreeItem<String> tree, int elementNum, int parentNum, int grandparentNum) {
		for(TreeItem<String> s : tree.getChildren()){
			//String universeItem = s.getValue();

			// Get the squib channel number out of the tree
			String element = s.getChildren().get(0).getValue();
            String elements[] = element.split(" ");
            int squibChannel = Integer.parseInt(elements[1]);
			
			Squib squib = new Squib(grandparentNum, parentNum, elementNum, squibChannel);
			
			//System.out.println("Adding element: " + universeItem + "  " + elementNum + "  " + parentNum + "  " + grandparentNum);
			sequence.getUniverse().getFireboxList().get(grandparentNum).getLunchboxList().get(parentNum).addSquib(squib);
			elementNum++;
			
			button_openVisualOrganizer.setDisable(false);
		}
	}
}