package application;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Universe {
	public List<Firebox> fireboxList;
	
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
	
	public void readUniverse(String inFile) {
		try {
			Scanner fileReader = new Scanner(new File(inFile));
			
			int numFB = fileReader.nextInt();
			this.fireboxList = new ArrayList<Firebox>();
			
			for (int i = 0; i < numFB; i++) {
				int FBID = fileReader.nextInt();
				Firebox fb = new Firebox(FBID);
				
				int numLB = fileReader.nextInt();
				
				for (int j = 0; j < numLB; j++) {
					int LBID = fileReader.nextInt();
					Lunchbox lb = new Lunchbox(LBID, FBID);
					
					int numSquib = fileReader.nextInt();
					for (int k = 0; k < numSquib; k++) {
						int squibNum = fileReader.nextInt();
						//TODO: Get/Save Channel number of squibs
						int squibChannel = 1;
						lb.addSquib(new Squib(FBID, LBID, squibNum, squibChannel));
					}
					
					fb.addLunchbox(lb);
				}
				this.fireboxList.add(fb);
			}
			fileReader.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void writeUniverse(String outFile) {
		try {
			PrintWriter writer = new PrintWriter(outFile, "ASCII");
			int numFB = this.fireboxList.size();
			writer.println(numFB);
			
			for (int i = 0; i < numFB; i++) {
				 Firebox fb = fireboxList.get(i);
				 int FBID = fb.getId(); 
				 
				 writer.println(FBID);
				
				 int numLB = fb.getLunchboxList().size();
				 writer.println(numLB);
				 
				for (int j = 0; j < numLB; j++) {
					Lunchbox lb = fb.getLunchboxList().get(j);
					int LBID = lb.getId();
					
					writer.println(LBID);
					
					int numSquib = lb.getSquibList().size();
					writer.println(numSquib);
					
					List<Squib> list = lb.getSquibList();
					for (int k = 0; k < numSquib; k++) {
						writer.println(list.get(k).getSquib());
					}
					
				}
			}
			writer.close();
		} catch (Exception e) {
			this.fireboxList.size();
		}

	}
}
