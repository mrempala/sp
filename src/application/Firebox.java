package application;

import java.util.ArrayList;
import java.util.List;

public class Firebox {
	List<Lunchbox> lunchboxList;
	// TODO: Do we want to store the firebox universe ID number here?
	private int id;
	
	// Constructor
	Firebox(int id){
		this.id = id;
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
}
