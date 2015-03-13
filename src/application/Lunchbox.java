package application;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Lunchbox implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Squib> squibList;
	private int id;
	private int grandParent;
	
	public Lunchbox() {
		this.squibList = new ArrayList<Squib>();
		
		this.id = -1;
		this.grandParent = -1;
	}
	
	// Constructor
	public Lunchbox(List<Squib> squibList) {
		this.squibList = squibList;
		this.id = -1;
		this.grandParent = -1;
		
	}
	
	public Lunchbox(int id, int grandParent){
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

	public void setSquibList(List<Squib> squibList) {
		this.squibList = squibList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGrandParent(int grandParent) {
		this.grandParent = grandParent;
	}

	@Override
	public String toString() {
		return "Lunchbox [squibList=" + squibList + ", id=" + id
				+ ", grandParent=" + grandParent + "]";
	}
}
