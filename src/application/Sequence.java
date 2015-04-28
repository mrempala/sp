package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sequence implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private final int MAX_FIRE_COUNT = 200;

	private Universe universe;

	// The master timeline to be sent to the universe
	private List<TimeStep> timeLine = new ArrayList<TimeStep>();

	// A list to contain the user defined groups of squibs
	// Stored as universe objects so we can apply various animations
	// to sub groups without changing the way we create the animations
	// Note that the first element at squibGroups.get(0) should always
	// be the group for the main universe
	private List<SquibGroup> squibGroups = new ArrayList<SquibGroup>();
	
	private List<Squib> squibsOverMaxFireCount = new ArrayList<Squib>();

	// TODO: As pointed out by Vance, these fields only make sense at a
	// concert, we may need to generalize more or have a new layer
	// of setup where the user can choose the type of show, such as
	// concert, play, orchestra, etc... But for hacking something together
	// by Wednesday, lets just stick with these values
	private String projectName = "Temp Name";
	private String projectDetails = "Temp Details";

	public Sequence() {

	}

	public Sequence(Universe universe) {
		this.universe = universe;
	}

	private void mergeTimelines() {
		// Clear the old timeline
		timeLine.clear();
		squibsOverMaxFireCount.clear();

		for (int i = 0; i < squibGroups.get(0).getTimeLine().size(); i++) {
			// Create a new timestep to hold all firing squibs from the various
			// groups
			TimeStep t = new TimeStep();
			for (SquibGroup squibGroup : squibGroups) {
				// Add all the squibs in the given group timestep to the master
				// timestep
				for (Squib s : squibGroup.getTimeLine().get(i)) {
					t.getSquibList().add(s);
					
					// Check on the firecount, alert user if over max count
					if (s.getFirecount() > MAX_FIRE_COUNT && !squibsOverMaxFireCount.contains(s)) {
						squibsOverMaxFireCount.add(s);
						System.out.println("WARNING: Exceeding max fire count on Squib " + s.getFirebox() 
											+ "-" + s.getLunchbox() + "-" + s.getSquib());
					}
				}
			}
			// TODO: Add some validation here to check for merge conflicts
			timeLine.add(t);
		}
	}

	// A temporary function to pad all squib groups not currently being animated
	// with blanks
	// This way every squib group maintains a timeline of the same length.
	private void insertBlanks(int squibGroupNum, int numTimesteps) {
		int i = 0;
		for (SquibGroup squibGroup : squibGroups) {
			if (i != squibGroupNum) {
				for (int j = 0; j < numTimesteps; j++) {
					squibGroup.getTimeLine().add(new TimeStep());
				}
			}
			i++;
		}
	}

	private int insertRatePadding(int rate, int squibGroup) {
		int insertedTimesteps = 0;
		TimeStep blankTimestep = new TimeStep();
		// Insert blank timesteps to adjust the rate of the animation
		// Use a decrementing loop becuase rate is value 1-10 and 10 is fastest
		// (so we want 10-10=0 to be full speed
		for (int i = 0; i > ((rate - 10) * 3.1); i--) {
			// Validate the timestep to properly decrement firbox's time to
			// sleep
			// but no need to check what validate returns as it is simply a
			// blank timestep
			validate(blankTimestep);
			squibGroups.get(squibGroup).getTimeLine().add(blankTimestep);
			insertedTimesteps++;
		}
		return insertedTimesteps;
	}

	// Start at FB1-LB1-SQ1, fire each squib in LB1,
	// step to LB2, until end of LB chain, then start
	// over at FB2.
	public int loadUniverseSweep(Universe universe, int rate, int squibGroup) {
		int numTimesteps = 0;

		// Populate timeline with new sequence
		for (Firebox f : universe.getFireboxList()) {
			for (Lunchbox l : f.getLunchboxList()) {
				for (Squib s : l.getSquibList()) {
					TimeStep t = new TimeStep();
					// Add Squib to the timestep
					t.getSquibList().add(s);
					// Update the squibs firecount
					s.setFirecount(s.getFirecount() + 1);
					
					// Check to see if the new timestep is valid
					Object[] result = validate(t);
					Integer newResult = (Integer) result[0];
					String error = (String) result[2];
					if (newResult.intValue() == 0) {
						squibGroups.get(squibGroup).getTimeLine().add(t);
						numTimesteps++;

						// Add blank timesteps to adjust rate
						numTimesteps += insertRatePadding(rate, squibGroup);

						//System.out.println("Inserted timestep");
					} else {
						System.out.println("Failed to insert" + error);
					}
				}
			}
		}
		System.out.println(timeLine);

		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		squibGroups.get(squibGroup).getTimeLine().add(t);
		numTimesteps++;

		// Add blanks to other squib groups
		insertBlanks(squibGroup, numTimesteps);

		// Merge all squibgroup timelines into the main timeline
		mergeTimelines();

		return numTimesteps;
	}

	// Default for loadRandomOneAtATimeSequence
	public int loadRandomOneAtATimeSequence(Universe universe, int rate,
			int squibGroup) {
		return loadRandomOneAtATimeSequence1(universe, 100, rate, squibGroup);
	}

	// A sequence that fires one random squib at a time from
	// anywhere in the universe per time step.
	public int loadRandomOneAtATimeSequence1(Universe universe,
			int numTimeSteps, int rate, int squibGroup) {
		int numTimesteps = 0;

		// Populate timeline with new sequence
		List<Squib> tempSquibList = new ArrayList<Squib>();
		for (Firebox f : universe.getFireboxList()) {
			for (Lunchbox l : f.getLunchboxList()) {
				for (Squib s : l.getSquibList()) {
					tempSquibList.add(s);
				}
			}
		}

		// Select a random squib and add it to the time step list for
		// numTimeSteps
		int numSquibs = tempSquibList.size();
		if (numSquibs > 0) {
			Random randomGenerator = new Random();
			for (int i = 0; i < numTimeSteps; i++) {
				int randSquib = randomGenerator.nextInt(numSquibs);
				TimeStep t = new TimeStep();
				t.getSquibList().add(tempSquibList.get(randSquib));
				tempSquibList.get(randSquib).setFirecount(tempSquibList.get(randSquib).getFirecount() + 1);
				Object[] result = validate(t);
				Integer newResult = (Integer) result[0];
				String error = (String) result[2];
				if (newResult.intValue() == 0) {
					squibGroups.get(squibGroup).getTimeLine().add(t);
					numTimesteps++;

					// Add blank timesteps to adjust rate
					numTimesteps += insertRatePadding(rate, -1);
					//System.out.println("Inserted timestep");
				} else {
					System.out.println("Failed to insert" + error);
				}
			}
		}

		// TODO: Else statement Error, number of squibs found in universe is 0

		System.out.println(timeLine);

		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		squibGroups.get(squibGroup).getTimeLine().add(t);
		numTimesteps++;

		// Add blanks to other squib groups
		insertBlanks(squibGroup, numTimesteps);

		// Merge all squibgroup timelines into the main timeline
		mergeTimelines();

		return numTimesteps;
	}

	// A sequence that fires one random squib from each firebox
	// per timestep so that multiple squibs are fired per time step
	// if there are multiple fireboxes with squibs.
	public int loadRandomOnePerFireboxSequence(Universe universe,
			int numTimeSteps, int rate, int squibGroup) {
		int numTimesteps = 0;

		// Populate timeline with new sequence
		for (int i = 0; i < numTimeSteps; i++) {
			TimeStep t = new TimeStep();
			for (Firebox f : universe.getFireboxList()) {
				// TODO: Initialize and refactor code so a list
				// will not be recreated multiple times
				List<Squib> tempSquibList = new ArrayList<Squib>();
				for (Lunchbox l : f.getLunchboxList()) {
					for (Squib s : l.getSquibList()) {
						tempSquibList.add(s);
					}
				}

				int numSquibs = tempSquibList.size();
				if (numSquibs > 0) {
					Random randomGenerator = new Random();
					int randSquib = randomGenerator.nextInt(numSquibs);
					t.getSquibList().add(tempSquibList.get(randSquib));
					tempSquibList.get(randSquib).setFirecount(tempSquibList.get(randSquib).getFirecount() + 1);
				}
			}
			Object[] result = validate(t);
			Integer newResult = (Integer) result[0];
			String error = (String) result[2];
			if (newResult.intValue() == 0) {
				squibGroups.get(squibGroup).getTimeLine().add(t);
				numTimesteps++;

				numTimesteps += insertRatePadding(rate, -1);
				//System.out.println("Inserted timestep");
			} else {
				System.out.println("Failed to insert" + error);
			}
		}

		System.out.println(timeLine);

		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		squibGroups.get(squibGroup).getTimeLine().add(t);
		numTimesteps++;

		// Add blanks to other squib groups
		insertBlanks(squibGroup, numTimesteps);

		// Merge all squibgroup timelines into the main timeline
		mergeTimelines();
		return numTimesteps;
	}

	public int loadUniverseMusic(Universe universe, int rate, int squibGroup) {
		int numTimesteps = 0;
		MidiParser mp = new MidiParser();
		ArrayList<Integer> seq;

		try {
			seq = mp.run();
			int max = Collections.max(seq);
			int min = Collections.min(seq);

			System.out.println("Min: " + min + " Max: " + max);

			for (Integer key : seq) {
				int dioctive = (key - 33) / 9;
				System.out.println(dioctive);
				Squib s = universe.getFireboxList().get(0).getLunchboxList()
						.get(0).getSquibList().get(dioctive);

				TimeStep t = new TimeStep();
				t.getSquibList().add(s);
				s.setFirecount(s.getFirecount() + 1);
				
				Object[] result = validate(t);
				Integer newResult = (Integer) result[0];
				String error = (String) result[2];
				if (newResult.intValue() == 0) {
					squibGroups.get(squibGroup).getTimeLine().add(t);
					numTimesteps++;

					// Add blank timesteps to adjust rate
					numTimesteps += insertRatePadding(rate, squibGroup);

					//System.out.println("Inserted timestep");
				} else {
					System.out.println("Failed to insert" + error);
				}
			}
			System.out.println(timeLine);

			// Insert a trailing blank time step to clear universe
			TimeStep t = new TimeStep();
			squibGroups.get(squibGroup).getTimeLine().add(t);
			numTimesteps++;

			// Add blanks to other squib groups
			insertBlanks(squibGroup, numTimesteps);

			// Merge all squibgroup timelines into the main timeline
			mergeTimelines();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return numTimesteps;
	}

	// zig zags through the current setup starting at the first firebox,
	// going through all its squibs, going to the last squib of the next
	// firebox, go back through those squibs, etc.
	public int loadUniverseZigZag(Universe universe, int rate, int squibGroup) {
		int numTimesteps = 0;

		boolean leftToRight = true;

		// Populate timeline with new sequence
		for (Firebox f : universe.getFireboxList()) {
			if (leftToRight == true) {
				for (Lunchbox l : f.getLunchboxList()) {
					for (Squib s : l.getSquibList()) {
						TimeStep t = new TimeStep();
						t.getSquibList().add(s);
						s.setFirecount(s.getFirecount() + 1);
						
						Object[] result = validate(t);
						Integer newResult = (Integer) result[0];
						String error = (String) result[2];
						if (newResult.intValue() == 0) {
							squibGroups.get(squibGroup).getTimeLine().add(t);
							numTimesteps++;

							numTimesteps += insertRatePadding(rate, -1);
							//System.out.println("Inserted timestep");
						} else {
							System.out.println("Failed to insert" + error);
						}
					}
				}

				leftToRight = false;
			} else {
				for (int i = f.getLunchboxList().size() - 1; i >= 0; i--)// for(Lunchbox
																			// l
																			// :
																			// f.getLunchboxList())
				{
					Lunchbox l = f.getLunchboxList().get(i);

					for (int j = l.getSquibList().size() - 1; j >= 0; j--)// for(Squib
																			// s
																			// :
																			// l.squibList)
					{
						Squib s = l.getSquibList().get(j);
						TimeStep t = new TimeStep();
						t.getSquibList().add(s);
						s.setFirecount(s.getFirecount() + 1);
						Object[] result = validate(t);
						Integer newResult = (Integer) result[0];
						String error = (String) result[2];
						if (newResult.intValue() == 0) {
							squibGroups.get(squibGroup).getTimeLine().add(t);
							numTimesteps++;
							numTimesteps += insertRatePadding(rate, -1);
							//System.out.println("Inserted timestep");
						} else {
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
		squibGroups.get(squibGroup).getTimeLine().add(t);
		numTimesteps++;

		// Add blanks to other squib groups
		insertBlanks(squibGroup, numTimesteps);

		// Merge all squibgroup timelines into the main timeline
		mergeTimelines();

		return numTimesteps;
	}

	// simultaneously sweep through squibs 1-8 in each firebox
	public int loadUniverseSimultaneousSweep(Universe universe, int rate,
			int squibGroup) {
		int numTimesteps = 0;

		// loop for each possible squib position
		for (int i = 0; i < 8; i++) {
			TimeStep t = new TimeStep();

			for (Firebox f : universe.getFireboxList()) {
				for (Lunchbox l : f.getLunchboxList()) {
					if (l.getSquibList().size() > i) {
						Squib s = l.getSquibList().get(i);
						t.getSquibList().add(s);
						s.setFirecount(s.getFirecount() + 1);
						Object[] result = validate(t);
						Integer newResult = (Integer) result[0];
						String error = (String) result[2];

						while (newResult.intValue() == 2) {
							TimeStep tBlank = new TimeStep();
							timeLine.add(tBlank);
							numTimesteps++;
						}

						if (newResult.intValue() == 0) {
							squibGroups.get(squibGroup).getTimeLine().add(t);
							numTimesteps++;

							numTimesteps += insertRatePadding(rate, -1);
							//System.out.println("Inserted timestep");
						} else {
							System.out.println("Failed to insert" + error);
						}
					}
				}
			}

		}
		System.out.println(timeLine);
		// Insert a trailing blank time step to clear universe
		TimeStep t = new TimeStep();
		squibGroups.get(squibGroup).getTimeLine().add(t);
		numTimesteps++;

		// Add blanks to other squib groups
		insertBlanks(squibGroup, numTimesteps);

		// Merge all squibgroup timelines into the main timeline
		mergeTimelines();

		return numTimesteps;
	}

	// sequence alternates between even and odd lunchboxes firing squibs
	public int loadUniverseAlternate(Universe universe) {
		int numTimesteps = 0;
		TimeStep t;

		for (int i = 0; i < 40; i++) {
			t = new TimeStep();
			// if i is even
			if (i % 2 == 0) {
				System.out.println("even");

				for (Firebox f : universe.getFireboxList()) {
					for (int j = 0; j < f.getLunchboxList().size(); j++) {
						// if it is an even lunchbox
						if (j % 2 == 0) {
							Lunchbox l = f.getLunchboxList().get(j);
							if (l.getSquibList().size() > 0) {
								Squib s = l.getSquibList().get(0);
								t.getSquibList().add(s);
								s.setFirecount(s.getFirecount() + 1);
								Object[] result = validate(t);
								Integer newResult = (Integer) result[0];
								String error = (String) result[2];

								while (newResult.intValue() == 2) {
									TimeStep tBlank = new TimeStep();
									timeLine.add(tBlank);
									numTimesteps++;
								}

								if (newResult.intValue() == 0) {
									timeLine.add(t);
									numTimesteps++;
									//System.out.println("Inserted timestep");
								} else {
									System.out.println("Failed to insert"
											+ error);
								}
							}
						}
					}
				}
			} else // if i is odd
			{
				System.out.println("odd");

				for (Firebox f : universe.getFireboxList()) {
					for (int j = 0; j < f.getLunchboxList().size(); j++) {
						// if it is an even lunchbox
						if (j % 2 != 0) {
							Lunchbox l = f.getLunchboxList().get(j);
							if (l.getSquibList().size() > 0) {
								Squib s = l.getSquibList().get(0);
								t.getSquibList().add(s);
								s.setFirecount(s.getFirecount() + 1);
								Object[] result = validate(t);
								Integer newResult = (Integer) result[0];
								String error = (String) result[2];
								if (newResult.intValue() == 0) {
									timeLine.add(t);
									numTimesteps++;
									//System.out.println("Inserted timestep");
								} else {
									System.out.println("Failed to insert"
											+ error);
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
		numTimesteps++;

		return numTimesteps;
	}

	// Insert a period with no firing squibs into the sequence, based on the
	// rate selector bar
	public int loadUniversePause(int rate) {
		// Some crazy casting, need to cast rate to float to not truncate, then
		// multiply decimal value by 100
		// and cast back to int to set the number of blank timesteps.
		int numTimesteps = (int) ((1 / (float) rate) * 100);
		
		// Call insertBlanks with invalid group number -1 to insert blank
		// timesteps into all squib groups
		insertBlanks(-1, numTimesteps);
		mergeTimelines();

		return numTimesteps;
	}

	public Object[] validate(TimeStep timeStep) {
		Object[] error = new Object[3];
		error[0] = 0;
		error[1] = 0;
		error[2] = "Success";

		// Decrement the sleep number in all FBs
		for (Firebox f : universe.getFireboxList()) {
			if (f.getTimeStepSleepNumber() > 0) {
				f.setTimeStepSleepNumber(f.getTimeStepSleepNumber() - 1);
			}
		}

		// Scan current time step
		int j = 0;
		for (Squib s : timeStep.getSquibList()) {
			// Check current squib against each previous squib in timestep to
			// prevent duplicate fire
			for (int i = 0; i < j; i++) {
				Squib t = timeStep.getSquibList().get(i);
				if (s.getFirebox() == t.getFirebox()
						&& s.getLunchbox() == t.getLunchbox()) {
					error[0] = 1;
					error[1] = 1;
					error[2] = "Attempt to fire 2 or more squibs on single lunchbox "
							+ s.getLunchbox();
					return error;
				}
			}
			j++;

			// Check to make sure firebox chain isn't firing too fast
			if (universe.getFireboxList().get(s.getFirebox())
					.getTimeStepSleepNumber() > 0) {
				error[0] = 1;
				error[1] = 2;
				error[2] = "Attempt to fire squibs too fast on Firebox "
						+ s.getFirebox();
				return error;
			}
		}

		// Only increment sleep count after successfully looking at all squibs
		// to be fired
		for (Squib s : timeStep.getSquibList()) {
			int sleepNum = universe.getFireboxList().get(s.getFirebox())
					.getTimeStepSleepNumber();
			sleepNum++;
			universe.getFireboxList().get(s.getFirebox())
					.setTimeStepSleepNumber(sleepNum);
		}

		return error;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDetails() {
		return projectDetails;
	}

	public void setProjectDetails(String projectDetails) {
		this.projectDetails = projectDetails;
	}

	public Universe getUniverse() {
		return universe;
	}

	public void setUniverse(Universe universe) {
		this.universe = universe;
	}

	public List<TimeStep> getTimeLine() {
		return timeLine;
	}

	public void setTimeLine(List<TimeStep> timeLine) {
		this.timeLine = timeLine;
	}

	public List<SquibGroup> getSquibGroups() {
		return squibGroups;
	}

	public void setSquibGroups(List<SquibGroup> squibGroups) {
		this.squibGroups = squibGroups;
	}
	
	public List<Squib> getSquibsOverMaxFireCount() {
		return squibsOverMaxFireCount;
	}
}
