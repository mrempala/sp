package application;

import java.util.ArrayList;
import java.util.List;

public class SquibGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public Universe squibs;
	public List<Integer> squibPlayGroups; // If value in list is negative, it is just a placeholder of timesteps
	public String groupName;
	public List<TimeStep> timeLine = new ArrayList<TimeStep>();
	
	public SquibGroup(){
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

	public List<TimeStep> getTimeLine() {
		return timeLine;
	}

	public void setTimeLine(List<TimeStep> timeLine) {
		this.timeLine = timeLine;
	}

	public void setSquibs(Universe squibs) {
		this.squibs = squibs;
	}

	public void setSquibPlayGroups(List<Integer> squibPlayGroups) {
		this.squibPlayGroups = squibPlayGroups;
	}
}
