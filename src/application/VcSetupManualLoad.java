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
                	// We don't want to add children to squibs!
                	currentTreeNode = oldTreeNode;
                }
                //System.out.println(elementToSet);
            }

          });
	}
	
	@FXML 
	protected void setValue(ActionEvent event) throws IOException {
		int value = Integer.parseInt(text_inputValue.getText());
		currentTreeNode.getChildren().clear();
		
		// added check to prevent creating more than allowed according to hardware limitations (vp)
		if(elementToSet == "Squib" && value > 8)
			value = 8;
		if(elementToSet == "Firebox" && value > 16)
			value = 16;
		if(elementToSet == "Lunchbox" && value > 12)
			value = 12;

		for (int i = 0; i < value; i++) {
			TreeItem<String> item = new TreeItem<String> (elementToSet + " " +  String.valueOf(i+1));
			item.setExpanded(true);
			currentTreeNode.getChildren().add(item);
		}
		
		// Check created tree
		//traverseTree(rootTreeNode);
		
		// Populate the universe based on users setting
		// TODO: Move this call to openVisualOrganizer once testing done.
		populateUniverse(rootTreeNode, 0);
	}
	
	@FXML 
	protected void openVisualOrganizer(ActionEvent event) throws IOException{
		Sequence sequence = new Sequence(universe);

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
        
        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setTitle("Sequence Preview");
        stage.setScene(scene);
        stage.show();
        
        // Close the current window
        // get a handle to the stage
        Stage currentstage = (Stage) button_openVisualOrganizer.getScene().getWindow();
        // and close it
        currentstage.close();
  
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
			Squib squib = new Squib(grandparentNum, parentNum, elementNum);
			
			System.out.println("Adding element: " + universeItem + "  " + elementNum + "  " + parentNum + "  " + grandparentNum);
			universe.fireboxList.get(grandparentNum).lunchboxList.get(parentNum).addSquib(squib);
			elementNum++;
		}
	}
}