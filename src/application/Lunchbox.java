package application;

import java.util.ArrayList;
import java.util.List;

public class Lunchbox {
	public List<Squib> squibList;
	private int id;
	private int grandParent;
	
	// Constructor
	Lunchbox(List<Squib> squibList) {
		this.squibList = new ArrayList<Squib>();
		this.squibList = squibList;
	}
	
	public List<Squib> getSquibList() {
		return squibList;
	}

	public int getId() {
		return id;
	}

	public int getGrandParent() {
		return grandParent;
	}

	Lunchbox(int id, int grandParent){
		this.id = id;
		this.grandParent = grandParent;
		this.squibList = new ArrayList<Squib>();
	}
	
	public void addSquib(Squib squib) {
		this.squibList.add(squib);
	}
	
	public Squib getSquib(){
		
		return null;
	}

	public void traverseUniverse() {
		System.out.println("\t\tLunchbox " + id);
		for (Squib s : squibList){
			s.traverseUniverse();
		}
	}
}
