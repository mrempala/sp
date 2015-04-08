package application;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BBSaveAS implements IButtonBehavior {

	private Sequence sequence;
	private Stage currentStage;

	public BBSaveAS(Sequence sequence, Stage s){
		this.sequence = sequence;
		currentStage = s;
	}
	@Override
	public void click() {
		// TODO Auto-generated method stub

    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter( "XML files (*.xml)", "*.xml"));
    	
    	File file = fileChooser.showSaveDialog(currentStage);
    	RecentUpdater recentList = new RecentUpdater();
    	recentList.load();
    	
    	try {
        	if (file != null) {
        		FileOutputStream fos = new FileOutputStream(file);
        		BufferedOutputStream bos = new BufferedOutputStream(fos);
        		XMLEncoder xmlEncoder = new XMLEncoder(bos);

        		xmlEncoder.writeObject(sequence);
        		xmlEncoder.close();
        		
        		recentList.update(file.getName(), file.getAbsolutePath());
        		
                //System.out.println(universe.toString());
        		System.out.println("Button Behaviour: Write Done");
        		
        		recentList.save();
           	
        	}
        	else{
        		System.out.println("Error, file could not be opened");
        	}
        } 
        catch (Exception e) {
        	System.out.println("Error1");
        	System.out.println(e.getMessage());
        }
	}

}


