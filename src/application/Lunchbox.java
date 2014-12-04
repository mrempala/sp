package application;

import java.util.ArrayList;
import java.util.List;

public class Lunchbox {
	List<Squib> squibList = new ArrayList<Squib>();
	
	// Constructor
	Lunchbox(List<Squib> squibList) {
		this.squibList = squibList;
	}
	
	public void addSquib(Squib squib) {
		squibList.add(squib);
	}
	
	public Squib getSquib(){
		
		return null;
	}
}
