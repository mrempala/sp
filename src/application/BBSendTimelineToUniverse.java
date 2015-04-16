package application;

import gnu.io.NoSuchPortException;

import java.util.List;

public class BBSendTimelineToUniverse extends Thread implements IButtonBehavior {
	private String portNum;
	private SerialComm serialComm;

	private enum STATES {
		ON, OFF
	};

	private STATES state;
	private List<TimeStep> timeLine;
	private Thread loopy;
	private boolean connected;

	public BBSendTimelineToUniverse(String portNum) {
		this.portNum = portNum;
		this.state = STATES.OFF;
		this.loopy = new Thread(this);
		
		try {
			this.serialComm = new SerialComm("COM" + portNum);
			connected = true;
		} catch (NoSuchPortException e) {
			connected = false;
		}
	}

	public BBSendTimelineToUniverse(String portNum, List<TimeStep> timeLine) {
		this.portNum = portNum;
		this.state = STATES.OFF;
		this.loopy = new Thread(this);
		this.timeLine = timeLine;
		
		try {
			this.serialComm = new SerialComm("COM" + portNum);
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
			for (TimeStep t : timeLine) {
				if (Thread.interrupted()) {
					System.out.println("Interrupted");
					return;
				}
				System.out.println(t);
				serialComm.runTimeStep(t);
			}
		} catch (Exception e) {

		}

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
				loopy.start();

				break;

			case ON:
				state = STATES.OFF;
				loopy.interrupt();
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
