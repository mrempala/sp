package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javafx.stage.FileChooser;

public class RecentUpdater {
	private ArrayList<String> recentProj;
	private static final int CAP = 5;
	
	public RecentUpdater() {
		recentProj = new ArrayList<String>();
	}
	
	public void load() {
		// TODO Auto-generated method stub

    	try {
        	 
        	FileInputStream fis = new FileInputStream("RecentProj.xml");
        	
        	BufferedInputStream bis = new BufferedInputStream(fis);
        	XMLDecoder xmlDecoder = new XMLDecoder(bis);
        		  
        	recentProj = (ArrayList<String>) xmlDecoder.readObject();
        	xmlDecoder.close();
        		
        	System.out.println(recentProj.toString());
                 
        	System.out.println("RecentRead Done");
        	
        } 
        catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
	
	public void save() {
		// TODO Auto-generated method stub

    
    	try {
        	
        	FileOutputStream fos = new FileOutputStream("RecentProj.xml");
        	BufferedOutputStream bos = new BufferedOutputStream(fos);
        	XMLEncoder xmlEncoder = new XMLEncoder(bos);
            
        	xmlEncoder.writeObject(recentProj);
        	xmlEncoder.close();
            
        	System.out.println(recentProj.toString());
        	System.out.println("RecentWrite Done");
        	
        } 
        catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
 
	public void update(String latest) {
		recentProj.remove(latest);
		recentProj.add(0, latest);
		
		if (recentProj.size() > CAP) {
			recentProj.remove(CAP);
		}
			
	}
	
	public String get(int idx) {
		return recentProj.get(idx);
		
	}
	
/*
	//Test Case to write your own save file
	public static void main(String args[]) {
		RecentUpdater test1 = new RecentUpdater();
		test1.load();
		test1.update("universe_4x4x8.txt");
        test1.update("universe_1x4x8.txt");
        test1.update("universe_pyramid.txt");
        test1.update("universe_unusual.txt");
		test1.update("universe_4x4x8.txt");
		test1.update("yayo.txt");
		test1.update("XMLTest.xml");
		test1.save();
		test1.load();
	}
*/
}

	

