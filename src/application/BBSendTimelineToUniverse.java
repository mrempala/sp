package application;

import gnu.io.NoSuchPortException;

import java.util.List;

public class BBSendTimelineToUniverse implements IButtonBehavior {
	private String portNum;
	private SerialComm serialComm;

	private enum STATES {
		ON, OFF
	};

	private STATES state;
	private List<TimeStep> timeLine;
	//private Thread loopy;
	private boolean connected;

	public BBSendTimelineToUniverse(String portNum) {
		this.portNum = portNum;
		this.state = STATES.OFF;
		//this.loopy = new Thread(this);
		
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
		//this.loopy = new Thread(this);
		this.timeLine = timeLine;
		
		try {
			this.serialComm = new SerialComm("COM" + this.portNum);
			connected = true;
		} catch (NoSuchPortException e) {
			connected = false;
		}
	}

	public void run() {
		/*
		 * int i = 0; while (i != 1000000000) { i++; System.out.println("1"); if
		 * (Thread.interrupted()) { System.out.println("Done"); return; } }
		 */
		try {
			serialComm.prepUniverse();
			
			for (TimeStep t : timeLine) {
				/*if (Thread.interrupted()) {
					System.out.println("Interrupted");
					return;
				}*/
				System.out.println(t);
				serialComm.runTimeStep(t);
				//System.out.println("loop");
				
			}
			System.out.println("Done");
			//return;
		} catch (Exception e) {

		}
		serialComm.close();

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
				//this.timeLine = timeLine;
				//serialComm = new SerialComm("COM" + portNum);
				//serialComm.initialize();
				//loopy.start();
				run();
                //serialComm.close();
				System.out.println("done");
				break;

			case ON:
				state = STATES.OFF;
				//serialComm.close();
				//loopy.interrupt();
				//loopy.join();
				//this.timeLine = timeLine;
					
				// throw new InterruptedException();
				break;
			}
		}
		/*
		 * catch (InterruptedException ie) { System.out.println("OFF"); return;
		 * }
		 */
		catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

}
