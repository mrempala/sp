package application;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

public class Universe implements java.io.Serializable {
	public List<Firebox> fireboxList;
	
	public Universe (){
		this.fireboxList = new ArrayList<Firebox>();
	}
	
	public Universe (List<Firebox> fireboxList) {
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

	public List<Firebox> getFireboxList() {
		return fireboxList;
	}

	public void setFireboxList(List<Firebox> fireboxList) {
		this.fireboxList = fireboxList;
	}

	@Override
	public String toString() {
		return "Universe [fireboxList=" + fireboxList + "]";
	}
	
}
