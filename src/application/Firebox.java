package application;

import java.util.ArrayList;
import java.util.List;

public class Firebox {
	public List<Lunchbox> lunchboxList;
	// TODO: Do we want to store the firebox universe ID number here?
	public int id;
	public int timeStepSleepNumber;
	
	// Constructor
	Firebox(int id){
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
}
