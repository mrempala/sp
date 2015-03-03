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
	
	Lunchbox(int id, int grandParent){
		this.id = id;
		this.grandParent = grandParent;
		this.squibList = new ArrayList<Squib>();
	}
	
	public List<Squib> getSquibList() {
		return squibList;
	}

	// If the lunchbox contains a squib at a given channel, return true
	// otherwise return false.
	public Boolean hasSquibAtChannel(int channel){
		for (Squib s : squibList){
			if (s.getChannel() == channel) {
				return true;
			}
		}
		
		return false;
	}
	
	// Getters & Setters
	public int getId() {
		return id;
	}

	public int getGrandParent() {
		return grandParent;
	}
	
	public void addSquib(Squib squib) {
		this.squibList.add(squib);
	}
	
	// TODO: Why is this here?  I probably put it there... -eb
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
