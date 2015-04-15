package application;

public class BBSendTimelineToUniverse  extends Thread implements IButtonBehavior {
	private String portNum;
	private SerialComm serialComm;
	private enum STATES {ON, OFF};
	private STATES state;
	Thread loopy;
	
	public BBSendTimelineToUniverse(String portNum) {
		this.portNum = portNum;
		this.serialComm = new SerialComm("COM" + portNum);
		state = STATES.OFF;
		loopy = new Thread(this);
	}
	
	public void run() {
		int i = 0;
		while (i != 1000000000) {
			i++;
			System.out.println("1");
			if (Thread.interrupted()) {
				return;
			}
		}
		System.out.println("Done");
	}
	@Override
	public void click() {
		// TODO Auto-generated method stub
		 
		
		try {
			switch (state) {
				case OFF:
					state = STATES.ON; 
					loopy.start();
					//serialComm.runTimeStep(null);
					break;
				case ON:
					state = STATES.OFF;
					loopy.interrupt();
					throw new InterruptedException();
					
			}
		} 
		catch (InterruptedException ie) {
				System.out.println("OFF");
				return;
	    }
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
