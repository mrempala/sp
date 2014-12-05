package application;

import java.util.ArrayList;
import java.util.List;

public class Universe {
	List<Firebox> fireboxList;
	
	Universe (){
		this.fireboxList = new ArrayList<Firebox>();
	}
	
	Universe (List<Firebox> fireboxList) {
		this.fireboxList = fireboxList;
	}
	
	public void setFireboxList(int numFireboxes) {
		// Reset firebox list to be empty
		this.fireboxList = new ArrayList<Firebox>();
		
		// Create the number of fireboxes as defined by the input integer
		for (int i = 0; i < numFireboxes; i++) {
			Firebox firebox = new Firebox(i+1);
			this.fireboxList.add(firebox);
		}
	}
	
	public void addFirebox(Firebox firebox) {
		this.fireboxList.add(firebox);
	}
	
	public void traverseUniverse(){
		System.out.println("Universe");
		for(Firebox f : fireboxList){
			f.traverseUniverse();
		}
	}
	
}
