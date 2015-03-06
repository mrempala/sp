package application;

import java.util.ArrayList;
import java.util.List;

public class SquibGroup {
	public Universe squibs;
	public List<Integer> squibPlayGroups; // If value in list is negative, it is just a placeholder of timesteps
	public String groupName;
	
	SquibGroup(){
		squibs = new Universe();
		squibPlayGroups = new ArrayList<Integer>();
	}
	
	public void setUniverse (Universe squibs){
		this.squibs = squibs;
	}
	public Universe getSquibs(){
		return squibs;
	}
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	public String getGroupName(){
		return groupName;
	}
	public List<Integer> getSquibPlayGroups(){
		return squibPlayGroups;
	}
}
