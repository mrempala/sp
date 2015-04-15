package application;

public class BBSendTimelineToUniverse implements IButtonBehavior {
	private String portNum;
	
	public BBSendTimelineToUniverse(String portNum) {
		this.portNum = portNum;
	}
	
	@Override
	public void click() {
		// TODO Auto-generated method stub
		SerialComm serialComm = new SerialComm("COM" + portNum);
		
		try {
			serialComm.runTimeStep(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
