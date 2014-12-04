package application;

import java.util.ArrayList;
import java.util.Iterator;

public class TimeStep implements Iterable<Squib> {
    ArrayList<Squib> list;
    
	@Override
	public Iterator<Squib> iterator() {
		// TODO Auto-generated method stub
		return list.iterator();
	}

	
}
