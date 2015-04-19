package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
 
public class VcProgramStart extends VcMainController {
    @FXML private Text actiontarget;
    @FXML private Button button_newproject;
    @FXML private Button button_browse;
    @FXML private Pane paneMain;
    
    // Canned universe links
    @FXML public Hyperlink link1;
    @FXML public Hyperlink link2;
    @FXML public Hyperlink link3;
    @FXML public Hyperlink link4;
    @FXML public Hyperlink link5;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sequence = new Sequence();
		int yOffset = 78;
		// Generate the recent project hyper link list
		RecentUpdater recentUpdater = new RecentUpdater();
		recentUpdater.load();
		RecentProjects recentProjects = recentUpdater.getRecentProjects();
		// Temporary hack to escape init if no projects found
		if (recentProjects == null) {
			return;
		}
		int i = 0;
		for (String projectName : recentProjects.getRecentProjectNames()){
			Hyperlink link = new Hyperlink();
			link.setText(projectName);
			link.setUserData(recentProjects.getRecentProjectPaths().get(i));
			link.setOnAction((event)->{
				try {
					loadExistingUniverse(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}});
			link.setLayoutX(58);
			link.setLayoutY(yOffset);
			paneMain.getChildren().add(link);
			
			i++;
			yOffset += 23;
		}
	}
    
	@FXML 
	void loadExistingUniverse(ActionEvent event) throws IOException{
		Hyperlink clickedLink = (Hyperlink) event.getSource();
		String fileToOpen = (String) clickedLink.getUserData();

		BBLoad behavior = new BBLoad(fileToOpen, currentStage);
        behavior.click();
    	sequence = behavior.getSequence();
        
        openSequenceEditor(event);
	}
    
    @FXML  
    protected void browseExistingProject(ActionEvent event) throws IOException {
        
        BBLoad behavior = new BBLoad(currentStage);
        behavior.click();
    	//Universe universe = behavior.getUniverse();
		//System.out.println(universe.toString());
		
		//SquibGroup sg = new SquibGroup();
        //sg.setUniverse(universe);

        sequence = behavior.getSequence();
        //sequence.getSquibGroups().add(sg);

		openSequenceEditor(event);

    }
}

