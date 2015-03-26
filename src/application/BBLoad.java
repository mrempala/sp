package application;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BBLoad implements IButtonBehavior {

	private Universe universe;
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
        		
        	if (fileName.isEmpty()) {
        		FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Open Resource File");
            	File file = fileChooser.showOpenDialog(currentStage);
                
        		fis = new FileInputStream(file);
        	} else { 
        		fis = new FileInputStream(fileName);
        	}
        	
        	BufferedInputStream bis = new BufferedInputStream(fis);
        	XMLDecoder xmlDecoder = new XMLDecoder(bis);
        		  
        	universe = (Universe) xmlDecoder.readObject();
        	xmlDecoder.close();
        		
        	System.out.println(universe.toString());
                 
        	System.out.println("Read Done");
        	/*
        	// Resave to reverse order of saving
    		BBSaveAS behavior = new BBSaveAS(universe, currentStage);
            behavior.click();
            
            // Reload to get the universe in proper order
        	//try {
            	
            	//FileInputStream fis;
            		
            	if (fileName.isEmpty()) {
            		FileChooser fileChooser = new FileChooser();
                	fileChooser.setTitle("Open Resource File");
                	File file = fileChooser.showOpenDialog(currentStage);
                    
            		fis = new FileInputStream(file);
            	} else { 
            		fis = new FileInputStream(fileName);
            	}
            	
            	bis = new BufferedInputStream(fis);
            	xmlDecoder = new XMLDecoder(bis);
            		  
            	universe = (Universe) xmlDecoder.readObject();
            	xmlDecoder.close();
            		
            	System.out.println(universe.toString());
                     
            	System.out.println("Read Done"); */
           	
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
    
	
}