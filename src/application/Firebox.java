package application;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Firebox implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Lunchbox> lunchboxList;
	// TODO: Do we want to store the firebox universe ID number here?
	private int id;
	private int timeStepSleepNumber;
	
	//Java Bean requires no arg constructor, gave invalid values to avoid crash
	public Firebox() {
		this.id = -1;
		this.timeStepSleepNumber = -1;
		this.lunchboxList = new ArrayList<Lunchbox>();
	}
	
	// Constructor
	public Firebox(int id){
		this.id = id;
		this.timeStepSleepNumber = 0;
		this.lunchboxList = new ArrayList<Lunchbox>();
	}
	
	public void addLunchbox(Lunchbox lunchbox) {
		this.lunchboxList.add(lunchbox);
	}

	public void traverseUniverse() {
		System.out.println("\tFirebox " + id);
		for (Lunchbox l : lunchboxList){
			l.traverseUniverse();
		}
	}

	public List<Lunchbox> getLunchboxList() {
		return lunchboxList;
	}

	public int getId() {
		return id;
	}

	public int getTimeStepSleepNumber() {
		return timeStepSleepNumber;
	}

	public void setTimeStepSleepNumber(int timeStepSleepNumber) {
		this.timeStepSleepNumber = timeStepSleepNumber;
	}

	public void setLunchboxList(List<Lunchbox> lunchboxList) {
		this.lunchboxList = lunchboxList;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Firebox [lunchboxList=" + lunchboxList + ", id=" + id
				+ ", timeStepSleepNumber=" + timeStepSleepNumber + "]";
	}
	

	
	
}
