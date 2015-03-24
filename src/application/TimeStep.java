package application;

import java.util.ArrayList;
import java.util.Iterator;

public class TimeStep implements Iterable<Squib> {
    private ArrayList<Squib> squibList = new ArrayList<Squib>();
    
	@Override
	public String toString() {
		return "TimeStep [squibList=" + squibList + "]";
	}
	
	public ArrayList<Squib> getSquibList(){
		return this.squibList;
	}
	
	public void setSquibList(ArrayList<Squib> squibList){
		this.squibList = squibList;
	}

	@Override
	public Iterator<Squib> iterator() {
		// TODO Auto-generated method stub
		return squibList.iterator();
	}	
}
