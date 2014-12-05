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
    private String propertyToSet;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TreeItem<String> newNode = new TreeItem<String>("Dynamic Node");
        //FB1.getChildren().add(newNode);
		currentTreeNode = rootTreeNode;
		propertyToSet = "Firebox";
        mainTreeView.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<TreeItem<String>>() {

            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {

                currentTreeNode = newValue;
                System.out.println("Selected Text : " + currentTreeNode.getValue());
                String test = currentTreeNode.getValue();
   
                if (test.indexOf("Firebox") != -1){
                	propertyToSet = "Lunchbox";
                	label_setTreeNode.setText("Number of Lunchboxes:");
                }
                else if (test.indexOf("Lunchbox") != -1){
                	propertyToSet = "Squib";
                	label_setTreeNode.setText("Number of Squibs:");
                }
                else if (test.indexOf("Universe") != -1){
                	propertyToSet = "Firebox";
                	label_setTreeNode.setText("Number of Fireboxes:");
                }
                //propertyToSet = currentTreeNode.getValue();
                //selectedItem.setValue("New Value");
            }

          });
	}
	
	@FXML 
	protected void setValue(ActionEvent event) throws IOException {
		int value = Integer.parseInt(text_inputValue.getText());
		currentTreeNode.getChildren().clear();
		
		for (int i = 0; i < value; i++) {
			TreeItem<String> item = new TreeItem<String> (propertyToSet + " " + String.valueOf(i+1));
			item.setExpanded(true);
			currentTreeNode.getChildren().add(item);
		}
	}
	
	@FXML 
	protected void openVisualOrganizer(ActionEvent event) throws IOException{
		// Traverse user created tree for now, later will need to set these values
		traverseTree(rootTreeNode);
		
		Parent root;
    	// Load the next window
        root = FXMLLoader.load(getClass().getResource("Views/UI-Setup-VisualLayout.fxml"));
        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setTitle("New Project - Visual Organization");
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
	
	private void traverseTree (TreeItem<String> t){
		System.out.println(t.getValue());
		for(TreeItem<String> s : t.getChildren()){
			traverseTree(s);
		}
	}
}