package application;

import java.util.Iterator;

public class Squib implements Iterable {
	private int firebox;
	private int lunchbox;
	private int squib;
	private int firecount;
	//private bool health;
	//test
	
	
	// Getters & Setters
	// TODO: Set up constraints on sets, make sure value isn't out of bounds
	public int getFirebox() {
		return firebox;
	}
	
	public void setFirebox(int firebox){
		this.firebox = firebox;
	}
	
	public int getLunchbox() {
		return lunchbox;
	}
	
	public void setLunch(int lunchbox){
		this.lunchbox = lunchbox;
	}
	
	public int getSquib() {
		return squib;
	}
	
	public void setSquib(int squib){
		this.squib = squib;
	}
	
	public int getFirecount() {
		return firecount;
	}
	
	public void setFirecount(int firecount){
		this.firecount = firecount;
	}

	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
