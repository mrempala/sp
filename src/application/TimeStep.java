package application;

import java.util.ArrayList;
import java.util.Iterator;

public class TimeStep implements Iterable<Squib> {
    ArrayList<Squib> squibList = new ArrayList<Squib>();
    
	@Override
	public String toString() {
		return "TimeStep [squibList=" + squibList + "]";
	}

	@Override
	public Iterator<Squib> iterator() {
		// TODO Auto-generated method stub
		return squibList.iterator();
	}	
}
