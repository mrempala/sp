package application;

import java.io.IOException;

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
		
		if(selectedSelection.equals("Scene 1")){
			fileToOpen = "XMLTest.xml";
		}
		else if(selectedSelection.equals("Scene 2")){
			fileToOpen = "universe_4x4x8.txt";
		}
		else if(selectedSelection.equals("Scene 3")){
			fileToOpen = "universe_1x4x8.txt";
		}
		else if(selectedSelection.equals("Scene 4")){
			fileToOpen = "universe_pyramid.txt";
		}
		else if(selectedSelection.equals("Scene 5")){
			fileToOpen = "universe_unusual.txt";
		}

		BBLoad behavior = new BBLoad(fileToOpen, currentStage);
        behavior.click();
    	universe = behavior.getUniverse();
		System.out.println(universe.toString());
		
		SquibGroup sg = new SquibGroup();
        sg.setUniverse(universe);

        sequence = new Sequence(universe);
        sequence.getSquibGroups().add(sg);

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
    	/*System.out.println("browse button hit");
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	File file = fileChooser.showOpenDialog(currentStage);
        
    	if (file != null) {
        	//System.out.println("File does exist");
        	System.out.println(file);
        	
        	Squib sb = new Squib();
            Lunchbox lb = new Lunchbox();
   		    Firebox fb = new Firebox();
   		    
            lb.addSquib(sb);
            fb.addLunchbox(lb);
            
            Universe universe = new Universe();
        	universe.addFirebox(fb);
        	
            SquibGroup sg = new SquibGroup();
            sg.setUniverse(universe);

            sequence = new Sequence(universe);
            sequence.getSquibGroups().add(sg);
            
   		    FileOutputStream fos = new FileOutputStream("XMLTest.xml");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            XMLEncoder xmlEncoder = new XMLEncoder(bos);

            xmlEncoder.writeObject(universe);
            xmlEncoder.close();
            
            System.out.println("Write Done");
           
            FileInputStream fis = new FileInputStream("XMLTest.xml");
    		BufferedInputStream bis = new BufferedInputStream(fis);
    	 	XMLDecoder xmlDecoder = new XMLDecoder(bis);
    		  
    		universe = (Universe) xmlDecoder.readObject();
    		xmlDecoder.close();
    		
    		System.out.println("Read Done");
    		System.out.println("Default Test:" + fb.toString());
        	
        	openSequenceEditor(event);
        }
        else{
        	System.out.println("Error, file could not be opened");
        }*/
    }
}

