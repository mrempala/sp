package application;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
 
public class VcProgramStart extends VcMainController {
    @FXML private Text actiontarget;
    @FXML private Button button_newproject;
    @FXML private Button button_browse;
    
    // Canned universe links
    @FXML public Hyperlink link1;
    @FXML public Hyperlink link2;
    @FXML public Hyperlink link3;
    @FXML public Hyperlink link4;
    @FXML public Hyperlink link5;
    
	@FXML 
	void loadExistingUniverse(ActionEvent event) throws IOException{
		
		
		Hyperlink clickedLink = (Hyperlink) event.getSource();
		String selectedSelection = clickedLink.getText();
		String fileToOpen = "uninitalized";
		Universe universe = new Universe();
		
		RecentUpdater recentProjList = new RecentUpdater();
		recentProjList.load();
		
		//Always have a number at the end of displayed text or else this will fail
		int textLength = selectedSelection.length();
		int index = Character.getNumericValue(selectedSelection.charAt(textLength - 1)) - 1;
	    
		fileToOpen = recentProjList.get(index);

		BBLoad behavior = new BBLoad(fileToOpen, currentStage);
        behavior.click();
    	universe = behavior.getUniverse();
		System.out.println(universe.toString());
		
		SquibGroup sg = new SquibGroup();
        sg.setUniverse(universe);

        sequence = new Sequence(universe);
        sequence.getSquibGroups().add(sg);
        
        recentProjList.update(fileToOpen);;
        recentProjList.save();   
        
        openSequenceEditor(event);
	}
    
    @FXML  
    protected void browseExistingProject(ActionEvent event) throws IOException {
        
        BBLoad behavior = new BBLoad(currentStage);
        behavior.click();
    	Universe universe = behavior.getUniverse();
		//System.out.println(universe.toString());
		
		SquibGroup sg = new SquibGroup();
        sg.setUniverse(universe);

        sequence = new Sequence(universe);
        sequence.getSquibGroups().add(sg);

		openSequenceEditor(event);

    }
}

