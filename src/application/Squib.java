package application;

import java.io.Serializable;

public class Squib implements Serializable {
	private static final long serialVersionUID = 1L;
	private int firebox;
	private int lunchbox;
	private int squib;
	private int channel;  // Can be a value of 1-8
	private int firecount;
	private int audiolevel; // Set to 1 for soft, 2 for loud
	private double xPos;
	private double yPos;
	private int selected;
	
	/*private bool health;
	*/
	public Squib () {
		this.firebox = -1;
		this.lunchbox = -1;
		this.squib = -1;
		
		this.channel = -1;
		this.audiolevel = -1;
		this.firecount = -1;
		
		this.xPos = 0;
		this.yPos = 0;
		this.selected = 0;
	}
	
	Squib(int firebox, int lunchbox, int squib, int channel) {
		this.firebox = firebox;
		this.lunchbox = lunchbox;
		this.squib = squib;
		this.channel = channel;
		
		this.xPos = 100 + (lunchbox * 120) + (15 * squib);
		this.yPos = 50 + (firebox * 50);
		
		audiolevel = 1;
		firecount = 0;
	}
	
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
	
	public int getChannel() {
		return channel;
	}
	
	public void setChannel(int channel){
		this.channel = channel;
	}
	
	public int getFirecount() {
		return firecount;
	}
	
	public void setFirecount(int firecount){
		this.firecount = firecount;
	}
	
	public int getAudiolevel() {
		return audiolevel;
	}
	
	public void setAudiolevel(int audiolevel){
		this.audiolevel = audiolevel;
	}

	public void traverseUniverse() {
		System.out.println("\t\t\tSquib " + squib);
	}
	
	@Override
	public String toString() {
		return firebox + "-" + lunchbox
				+ "-" + squib;
	}

	public void setLunchbox(int lunchbox) {
		this.lunchbox = lunchbox;
	}
	
	public void setXPos(double x)
	{
		this.xPos = x;
	}
	
	public void setYPos(double y)
	{
		this.yPos = y;
	}
	
	public void incXPos(int x)
	{
		this.xPos += x;
	}
	
	public void incYPos(int y)
	{
		this.yPos += y;
	}
	
	public double getXPos()
	{
		return xPos;
	}
	
	public double getYPos()
	{
		return yPos;
	}
	
	public void setSelected(int value)
	{
		this.selected = value;
	}
	
	public int getSelected()
	{
		return selected;
	}
}
