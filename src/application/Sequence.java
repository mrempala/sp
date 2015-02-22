package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sequence {
	public Universe universe;
	
	// The master timeline to be sent to the universe
	public List<TimeStep> timeLine = new ArrayList<TimeStep>();
	
	// A list to contain the user defined groups of squibs
	// Stored as universe objects so we can apply various animations
	// to sub groups without changing the way we create the animations
	public List<Universe> squibGroups = new ArrayList<Universe>();
	
	// TODO: As pointed out by Vance, these fields only make sense at a
	// concert, we may need to generalize more or have a new layer
	// of setup where the user can choose the type of show, such as
	// concert, play, orchestra, etc... But for hacking something together
	// by Wednesday, lets just stick with these values
	public String projectName = "Temp Name";
	public String venue = "Temp Venue";
	public String show = "Temp Show";
	public String dj = "Temp dj"; 

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
		
		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		timeLine.add(t);
	}
	
	//Default for loadRandomOneAtATimeSequence
	public void loadRandomOneAtATimeSequence(){
		loadRandomOneAtATimeSequence(100);
	}
	
	//A sequence that fires one random squib at a time from
	//anywhere in the universe per time step.
	public void loadRandomOneAtATimeSequence(int numTimeSteps){
		// Clear the timeline
		timeLine.clear();
		
		// Populate timeline with new sequence
		List<Squib>tempSquibList = new ArrayList<Squib>();
		for(Firebox f : universe.fireboxList) {
			for(Lunchbox l : f.lunchboxList) {
				for(Squib s : l.squibList) {
					tempSquibList.add(s);
				}
			}
		}
		
		//Select a random squib and add it to the time step list for numTimeSteps
		int numSquibs = tempSquibList.size();
		if(numSquibs > 0){
			Random randomGenerator = new Random();
			for(int i=0; i<numTimeSteps; i++){
				int randSquib = randomGenerator.nextInt(numSquibs);
				TimeStep t = new TimeStep();
				t.squibList.add(tempSquibList.get(randSquib));
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
		
		//TODO: Else statement Error, number of squibs found in universe is 0
		
		System.out.println(timeLine);
		
		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		timeLine.add(t);
	}
	
	//A sequence that fires one random squib from each firebox
	//per timestep so that multiple squibs are fired per time step
	//if there are multiple fireboxes with squibs.
	public void loadRandomOnePerFireboxSequence(int numTimeSteps){
		// Clear the timeline
		timeLine.clear();
		// Populate timeline with new sequence
		for(int i=0; i<numTimeSteps; i++){
			TimeStep t = new TimeStep();
			for(Firebox f : universe.fireboxList) {
				//TODO: Initialize and refactor code so a list 
				//will not be recreated multiple times
				List<Squib>tempSquibList = new ArrayList<Squib>();
				for(Lunchbox l : f.lunchboxList) {
					for(Squib s : l.squibList) {
						tempSquibList.add(s);
					}
				}
				
				int numSquibs = tempSquibList.size();
				if(numSquibs > 0){
					Random randomGenerator = new Random();
					int randSquib = randomGenerator.nextInt(numSquibs);
					t.squibList.add(tempSquibList.get(randSquib));
				}
			}
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

		System.out.println(timeLine);
		
		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		timeLine.add(t);
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
		
		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		timeLine.add(t);
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
						
						while (newResult.intValue() == 2){
							TimeStep tBlank = new TimeStep();
							timeLine.add(tBlank);							
						}

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
		System.out.println(timeLine);
		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		timeLine.add(t);
	}
	
	// sequence alternates between even and odd lunchboxes firing squibs
	public void loadUniverseAlternate()
	{
		timeLine.clear();
		TimeStep t;
		
		for(int i = 0; i < 40; i++)
		{
			t = new TimeStep();
			// if i is even
			if(i % 2 == 0)
			{
				System.out.println("even");
				
				for(Firebox f : universe.fireboxList)
				{
					for(int j = 0; j < f.lunchboxList.size(); j++)
					{
						// if it is an even lunchbox
						if(j % 2 == 0)
						{
							Lunchbox l = f.lunchboxList.get(j);
							if(l.squibList.size() > 0)
							{
								Squib s = l.squibList.get(0);
								t.squibList.add(s);
								Object[] result = validate(t);
								Integer newResult = (Integer)result[0];
								String error = (String)result[2];
								
								while (newResult.intValue() == 2){
									TimeStep tBlank = new TimeStep();
									timeLine.add(tBlank);							
								}
								
								if (newResult.intValue() == 0)
								{
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
			else // if i is odd
			{
				System.out.println("odd");
				
				for(Firebox f : universe.fireboxList)
				{
					for(int j = 0; j < f.lunchboxList.size(); j++)
					{
						// if it is an even lunchbox
						if(j % 2 != 0)
						{
							Lunchbox l = f.lunchboxList.get(j);
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
								else {
									System.out.println("Failed to insert" + error);
								}
							}
						}
					}
				}	
			}
		}
		
		// Insert a trailing blank time step to clear universe
		t = new TimeStep();
		timeLine.add(t);
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
		error[0] = 0;
		error[1] = 0;
		error[2] = "Success";
		
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
		}
		
		// Only increment sleep count after successfully looking at all squibs to be fired
		for (Squib s : timeStep.squibList){
			universe.fireboxList.get(s.getFirebox()).timeStepSleepNumber++;
		}
		
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
