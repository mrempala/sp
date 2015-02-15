package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;


public class VcSetupManualLoad implements Initializable {

    @FXML private TreeView<String> mainTreeView;
    @FXML private TreeItem<String> rootTreeNode;
    @FXML private Button button_openVisualOrganizer;
    @FXML private TextField text_inputValue;
    @FXML private Label label_setTreeNode;
    private TreeItem<String> currentTreeNode;
    private String elementToSet;	//This defines which universe element to set, num set by elementNum
    //private String elementNum;		//This defines the element number, ex. Firebox 1 or Squib 1-2-3
    private Universe universe;
    
    Sequence sequence = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TreeItem<String> newNode = new TreeItem<String>("Dynamic Node");
        //FB1.getChildren().add(newNode);
		
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
                
                // Set the selected element number
                // TODO: Wrap in try/catch block so don't need Universe 0
                //elementNum = elements[1];
                
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
                	// If channel is selected, use the previously selected element
                	currentTreeNode = oldTreeNode;
                }
                else {
                	System.out.println("ERROR!");
                }
                //System.out.println(elementToSet);
            }

          });
	}
	
	@FXML 
	protected void setValue(ActionEvent event) throws IOException {
		int value = Integer.parseInt(text_inputValue.getText());
		
		System.out.println("\n\n" + currentTreeNode.getValue() + "\n\n" + elementToSet);
		
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
		currentTreeNode.getChildren();
		
		// added check to prevent creating more than allowed according to hardware limitations (vp)
		// TODO: Add error message feedback if bad input received
		if(elementToSet == "Squib" && value > 8)
			value = 8;
		if(elementToSet == "Firebox" && value > 16)
			value = 16;
		if(elementToSet == "Lunchbox" && value > 12)
			value = 12;

		if (elementToSet.equals("Channel")) {
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
		
		// Check created tree
		//traverseTree(rootTreeNode);
		
		// Populate the universe based on users setting
		// TODO: Move this call to openVisualOrganizer once testing done.
		populateUniverse(rootTreeNode, 0);
	}
	
	@FXML
	protected void testAction(ActionEvent event){
		System.out.println("test");
	}
	
	public void traverseTree (TreeItem<String> t){
		System.out.println(t.getValue());
		for(TreeItem<String> s : t.getChildren()){
			traverseTree(s);
		}
	}
	
	private void populateUniverse(TreeItem<String> tree, int elementNum) {	
		universe = new Universe();
		for(TreeItem<String> s : tree.getChildren()){
			Firebox firebox = new Firebox(elementNum);
			String universeItem = s.getValue();
			
			System.out.println("Adding element: " + universeItem + "  " + elementNum);
			universe.addFirebox(firebox);
			populateFirebox(s, 0, elementNum);
			elementNum++;
		}
	}
	
	private void populateFirebox(TreeItem<String> tree, int elementNum, int parentNum) {
		for(TreeItem<String> s : tree.getChildren()){
			String universeItem = s.getValue();
			Lunchbox lunchbox = new Lunchbox(elementNum, parentNum);
			
			System.out.println("Adding element: " + universeItem + "  " + elementNum + "  " + parentNum);
			universe.fireboxList.get(parentNum).addLunchbox(lunchbox);
			populateLunchbox(s, 0, elementNum, parentNum);
			elementNum++;
		}
	}
	
	private void populateLunchbox(TreeItem<String> tree, int elementNum, int parentNum, int grandparentNum) {
		for(TreeItem<String> s : tree.getChildren()){
			String universeItem = s.getValue();
			int squibChannel = 1;
			//System.out.println(s.getChildren().get(0).getValue());
			//TODO: Actually grab channel number
			Squib squib = new Squib(grandparentNum, parentNum, elementNum, squibChannel);
			
			System.out.println("Adding element: " + universeItem + "  " + elementNum + "  " + parentNum + "  " + grandparentNum);
			universe.fireboxList.get(grandparentNum).lunchboxList.get(parentNum).addSquib(squib);
			elementNum++;
		}
	}
	
	@FXML 
	protected void openVisualOrganizer(ActionEvent event) throws IOException{		
		if (sequence == null){
			sequence = new Sequence(universe);
		}
		else {
			sequence.universe = universe;
		}
	
		Parent root;
    	// Load the next window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/UI-SequenceEditor.fxml"));
        root = (Parent)loader.load();
        
        // Get a reference to the VisualSchematic controller so we can pass a reference of the universe to it.
        VcSequenceEditor seqEditorController = loader.<VcSequenceEditor>getController();
        seqEditorController.visualSchematicController.setUniverse(universe);
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
        
        // Close the current window
        // get a handle to the stage
        Stage currentstage = (Stage) button_openVisualOrganizer.getScene().getWindow();
        // and close it
        currentstage.close();
        //universe.writeUniverse("test_output.txt");
	}
}