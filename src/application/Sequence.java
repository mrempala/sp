package application;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
	public Universe universe;
	public List<TimeStep> timeLine = new ArrayList<TimeStep>();
	
	// TODO: As pointed out by Vance, these fields only make sense at a
	// concert, we may need to generalize more or have a new layer
	// of setup where the user can choose the type of show, such as
	// concert, play, orchestra, etc... But for hacking something together
	// by Wednesday, lets just stick with these values
	String projectName = "Temp Name", venue = "Temp Venue", show = "Temp Show", dj = "Temp dj"; 

	Sequence () {
		
	}
	
	Sequence(Universe universe) {
		this.universe = universe;
	}
	
	// Start at FB1-LB1-SQ1, fire each squib in LB1,
	// step to LB2, until end of LB chain, then start
	// over at FB2.
	public void loadUniverseSweep(){
		// Clear the timeline
		timeLine.clear();
		
		// Populate timeline with new sequence
		for(Firebox f : universe.fireboxList) {
			for(Lunchbox l : f.lunchboxList) {
				for(Squib s : l.squibList) {
					TimeStep t = new TimeStep();
					t.squibList.add(s);
					Object[] result = validate(t);
					Integer newResult = (Integer)result[0];
					String error = (String)result[2];
					if (newResult.intValue() == 0){
						timeLine.add(t);
						System.out.println("Inserted timestep");
					}
					else {
						System.out.println("Failed to insert" + error);
					}
				}
			}
		}
		System.out.println(timeLine);
	}
	
	// zig zags through the current setup starting at the first firebox,
		// going through all its squibs, going to the last squib of the next
		// firebox, go back through those squibs, etc.
		public void loadUniverseZigZag()
		{
			timeLine.clear();
			
			boolean leftToRight = true;
			
			// Populate timeline with new sequence
			for(Firebox f : universe.fireboxList)
			{
				if(leftToRight == true)
				{
					for(Lunchbox l : f.lunchboxList)
					{
						for(Squib s : l.squibList)
						{
							TimeStep t = new TimeStep();
							t.squibList.add(s);
							Object[] result = validate(t);
							Integer newResult = (Integer)result[0];
							String error = (String)result[2];
							if (newResult.intValue() == 0)
							{
								timeLine.add(t);
								System.out.println("Inserted timestep");
							}
							else
							{
								System.out.println("Failed to insert" + error);
							}
						}
					}
					
					leftToRight = false;
				}
				else
				{
					for(int i = f.lunchboxList.size() - 1; i >= 0; i--)//for(Lunchbox l : f.lunchboxList)
					{
						Lunchbox l = f.lunchboxList.get(i);
						
						for(int j = l.squibList.size() - 1; j >= 0; j--)//for(Squib s : l.squibList)
						{
							Squib s = l.squibList.get(j);
							TimeStep t = new TimeStep();
							t.squibList.add(s);
							Object[] result = validate(t);
							Integer newResult = (Integer)result[0];
							String error = (String)result[2];
							if (newResult.intValue() == 0)
							{
								timeLine.add(t);
								System.out.println("Inserted timestep");
							}
							else
							{
								System.out.println("Failed to insert" + error);
							}
						}
					}
					
					leftToRight = true;
				}
				
			}
			System.out.println(timeLine);
		}
		
	// simultaneously sweep through squibs 1-8 in each firebox
	public void loadUniverseSimultaneousSweep()
	{
		timeLine.clear();
		
		// loop for each possible squib position
		for(int i = 0; i < 8; i++)
		{
			TimeStep t = new TimeStep();
			
			for(Firebox f : universe.fireboxList)
			{
				for(Lunchbox l : f.lunchboxList)
				{
					if(l.squibList.size() > i)
					{
						Squib s = l.squibList.get(i);
						t.squibList.add(s);
						Object[] result = validate(t);
						Integer newResult = (Integer)result[0];
						String error = (String)result[2];
						if (newResult.intValue() == 0){
							timeLine.add(t);
							System.out.println("Inserted timestep");
						}
						else {
							System.out.println("Failed to insert" + error);
						}
					}
				}
			}
		}
	}
	
	// sequence alternates between even and odd lunchboxes firing squibs
	public void loadUniverseAlternate()
	{
		timeLine.clear();
		
		TimeStep t = new TimeStep();
		
		for(Firebox f : universe.fireboxList)
		{
			for(int i = 0; i < f.lunchboxList.size(); i++)
			{
				if(i % 2 == 0)
				{
					Lunchbox l = f.lunchboxList.get(i);
					if(l.squibList.size() > 0)
					{
						Squib s = l.squibList.get(0);
						t.squibList.add(s);
						Object[] result = validate(t);
						Integer newResult = (Integer)result[0];
						String error = (String)result[2];
						if (newResult.intValue() == 0)
						{
							timeLine.add(t);
							System.out.println("Inserted timestep");
						}
						else 
						{
							System.out.println("Failed to insert" + error);
						}
					}
				}
			}
		}
		
		t = new TimeStep();
		
		for(Firebox f : universe.fireboxList)
		{
			for(int i = 0; i < f.lunchboxList.size(); i++)
			{
				if(i % 2 != 0)
				{
					Lunchbox l = f.lunchboxList.get(i);
					if(l.squibList.size() > 0)
					{
						Squib s = l.squibList.get(0);
						t.squibList.add(s);
						Object[] result = validate(t);
						Integer newResult = (Integer)result[0];
						String error = (String)result[2];
						if (newResult.intValue() == 0)
						{
							timeLine.add(t);
							System.out.println("Inserted timestep");
						}
						else 
						{
							System.out.println("Failed to insert" + error);
						}
					}
				}
			}
		}
	}
	
	// 
	/*
	// Fire even lunchboxes, then odd lunchboxes
	// I'm (eric) is not thinking straight at the moment and it's not working
	public void loadUniverseEvenOddStep(){
		boolean isEven = false;
		int success;
		for (int j = 0; j < 2; j++){
			TimeStep t = new TimeStep();
			do {
				for (Firebox f : universe.fireboxList){
					int i = 0;
					for (Lunchbox l : f.lunchboxList){
						if ((i%2) == 0 && isEven){
							t.squibList.add(l.squibList.get(0));
							System.out.println(l.squibList.get(0).getLunchbox() + i);
						}
						else if (!isEven) {
							t.squibList.add(l.squibList.get(0));
							System.out.println(l.squibList.get(0).getLunchbox() + i);
						}
						i++;
					}
				}
				Object[] result = validate(t);
				Integer newResult = (Integer)result[0];
				success = newResult.intValue();
				String error = (String)result[2];
				if (success == 0){
					timeLine.add(t);
					System.out.println("Inserted timestep");
				}
				else {
					t = new TimeStep();
					validate(t);
					timeLine.add(t);
					System.out.println("Failed to insert, inserting blank. " + error);
				}
				if (isEven){
					isEven = false;
				}
				else {
					isEven = true;
				}
			} while(success != 0);
		}
		System.out.println(timeLine);
	}
	 */
	
	public Object[] validate(TimeStep timeStep){
		Object[] error  = new Object[3];
		
		// Decrement the sleep number in all FBs
		for(Firebox f : universe.fireboxList){
			if (f.timeStepSleepNumber > 0) {
				f.timeStepSleepNumber--;
			}
		}
		
		// Scan current time step
		int j = 0;
		for (Squib s : timeStep.squibList){
			// Check current squib against each previous squib in timestep to prevent duplicate fire
			for (int i = 0; i < j; i++){
				Squib t = timeStep.squibList.get(i);
				if (s.getFirebox() == t.getFirebox() && s.getLunchbox() == t.getLunchbox()) {
					error[0] = 1;
					error[1] = 1;
					error[2] = "Attempt to fire 2 or more squibs on single lunchbox " + s.getLunchbox();
					return error;
				}
			}
			j++;
			
			// Check to make sure firebox chain isn't firing too fast
			if (universe.fireboxList.get(s.getFirebox()).timeStepSleepNumber > 0){
				error[0] = 1;
				error[1] = 2;
				error[2] = "Attempt to fire squibs too fast on Firebox " + s.getFirebox();
				return error;
			}
			
			universe.fireboxList.get(s.getFirebox()).timeStepSleepNumber++;
		}
		
		error[0] = 0;
		error[1] = 0;
		error[2] = "Success";
		
		return error;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public String getDj() {
		return dj;
	}

	public void setDj(String dj) {
		this.dj = dj;
	}
}
