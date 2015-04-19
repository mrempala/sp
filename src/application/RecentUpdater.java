package application;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class RecentUpdater {
	private RecentProjects recentProjects;
	
	public RecentUpdater() {
		recentProjects = new RecentProjects();
	}
	
	public void load() {
    	try {
        	 
        	FileInputStream fis = new FileInputStream("RecentProj.xml");
        	
        	BufferedInputStream bis = new BufferedInputStream(fis);
        	XMLDecoder xmlDecoder = new XMLDecoder(bis);
        		  
        	recentProjects = (RecentProjects) xmlDecoder.readObject();
        	xmlDecoder.close();
                 
        	System.out.println("RecentRead Done");
        } 
        catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
	
	public void save() {
    	try {
        	
        	FileOutputStream fos = new FileOutputStream("RecentProj.xml");
        	BufferedOutputStream bos = new BufferedOutputStream(fos);
        	XMLEncoder xmlEncoder = new XMLEncoder(bos);
            
        	xmlEncoder.writeObject(recentProjects);
        	xmlEncoder.close();
            
        	System.out.println(recentProjects.toString());
        	System.out.println("RecentWrite Done");
        	
        } 
        catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
 
	public void update(String projectName, String projectPath) {
		recentProjects.addRecentProject(projectName, projectPath);
	}
	
	public RecentProjects getRecentProjects(){
		return recentProjects;
	}
}

	

