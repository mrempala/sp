package application;

import java.util.ArrayList;

public class RecentProjects {
	private ArrayList<String> recentProj; 	// List to store the recent project name
	private ArrayList<String> recentProjPath; // List to store the absoulte path to the recent project
	
	public RecentProjects() {
		recentProj = new ArrayList<String>();
		recentProjPath = new ArrayList<String>();
	}
	
	public void addRecentProject(String projectName, String projectPath) {
		// Add the new project and path to the lists
		recentProj.add(0, projectName);
		recentProjPath.add(0, projectPath);
		
		// If the list is now longer than 5, drop the last element in the lists
		if (recentProj.size() > 5) {
			recentProj.remove(recentProj.size() - 1);
			recentProjPath.remove(recentProjPath.size() - 1);
		}
	}
	public void setRecentProjectNames(ArrayList<String> recentProj) {
		this.recentProj = recentProj;
	}
	public ArrayList<String> getRecentProjectNames(){
		return recentProj;
	}
	public void setRecentProjectPaths(ArrayList<String> recentProjPath) {
		this.recentProjPath = recentProjPath;
	}
	public ArrayList<String> getRecentProjectPaths(){
		return recentProjPath;
	}
}
