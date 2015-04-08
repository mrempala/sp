package application;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BBLoad implements IButtonBehavior {

	private Universe universe;
	private Sequence sequence;
	private Stage currentStage;
	private String fileName;
	
	public BBLoad(Stage s) {
		currentStage = s;
		fileName = "";
	}
	
	public BBLoad(String f, Stage s) {
		currentStage = s;
		fileName = f;
	}
	@Override
	public void click() {
		// TODO Auto-generated method stub

    	try {
        	
        	FileInputStream fis;
        	RecentUpdater recentList = new RecentUpdater();
        	recentList.load();
        	
        	if (fileName.isEmpty()) {
        		FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Open Resource File");
            	File file = fileChooser.showOpenDialog(currentStage);
                    
        		fis = new FileInputStream(file);
        		recentList.update(file.getName(), file.getAbsolutePath());
        	} else { 
        		fis = new FileInputStream(fileName);
        		recentList.update(fileName, "Error");
        	}
        	
        	BufferedInputStream bis = new BufferedInputStream(fis);
        	XMLDecoder xmlDecoder = new XMLDecoder(bis);
        	System.out.println(fileName);
        	sequence = (Sequence) xmlDecoder.readObject();
        	xmlDecoder.close();
        		
        	System.out.println(universe.toString());
                 
        	System.out.println("Button Behaviour: Read Done");
        	
        	recentList.save();
        	
        } 
        catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
	
	public Universe getUniverse() {
		return universe;
	}
	public Stage getCurrentStage() {
		return currentStage;
	}
	public Sequence getSequence() {
		return sequence;
	}
    
	
}
