package application;

import gnu.io.NoSuchPortException;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BBSendTimelineToUniverse implements IButtonBehavior, Runnable {
	private String portNum;
	private SerialComm serialComm;
	private boolean stopWork = false;
	private boolean pauseWork = false;

	private enum STATES {
		ON, OFF
	};

	private STATES state;
	private List<TimeStep> timeLine;
	private Thread runningUniverse;
	private boolean connected;
	
	// Observable universeMessageProperty used to pass messages back to the main FXML GUI
	public static StringProperty universeMessageProperty = new SimpleStringProperty();

	public BBSendTimelineToUniverse(String portNum) {
		this.portNum = portNum;
		this.state = STATES.OFF;
		
		try {
			this.serialComm = new SerialComm("COM" + this.portNum);
			connected = true;
		} catch (NoSuchPortException e) {
			connected = false;
		}
	}

	public BBSendTimelineToUniverse(String portNum, List<TimeStep> timeLine) {
		this.portNum = portNum;
		this.state = STATES.OFF;
		this.timeLine = timeLine;
		
		try {
			this.serialComm = new SerialComm("COM" + this.portNum);
			connected = true;
		} catch (NoSuchPortException e) {
			connected = false;
		}
	}

	public void run() {
		try {
			// Setup universe, make sure LB's are addressed
			serialComm.prepUniverse();
			
			// Send each timestep to be fired
			for (int i = 0; i < timeLine.size(); i++) {
				//System.out.println(t);
				// If the window has been closed, break out of the loop
				if (stopWork){
					break;
				}
				// If the system is in a paused state, return i to it's previous
				// value and continue the loop
				if (pauseWork) {
					i--;
					continue;
				}
				threadMessage(serialComm.runTimeStep(timeLine.get(i)));
			}
		} catch (Exception e) {
			System.out.println(e);
			serialComm.close();
		}
		serialComm.close();
		threadMessage("Sequence finished!");
	}
	
	public void stop(){
		stopWork = true;
	}
	
	public void toggleFiring(boolean pauseWork){
		this.pauseWork = pauseWork;
	}

	// Function to get message from run thread and pass along to main FXML gui
	// FXML gui is alerted b/c it is a listener of universeMessageProperty
    static void threadMessage(String message) {
    	VcSequencePreview.universeFeedback += message;
    	universeMessageProperty.setValue(VcSequencePreview.universeFeedback);
    }
    
	@Override
	public void click() {
		// TODO Auto-generated method stub
	}
	
	public boolean isConnected() {
		return connected;
	}

	public void click(List<TimeStep> timeLine) {

		try {
			switch (state) {

			case OFF:
				state = STATES.ON;
				
				threadMessage("Starting sequence\n");
				
				// Create a new thread to run the actual sequence so we can get feedback to the GUI
				runningUniverse = new Thread(this);
				runningUniverse.start();
				break;

			case ON:
				state = STATES.OFF;
				break;
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
