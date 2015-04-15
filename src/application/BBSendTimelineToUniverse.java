package application;

import java.util.List;

public class BBSendTimelineToUniverse implements IButtonBehavior {
	private String portNum;
	private List<TimeStep> timeLine;
	
	public BBSendTimelineToUniverse(String portNum, List<TimeStep> timeLine) {
		this.portNum = portNum;
		this.timeLine = timeLine;
	}
	
	@Override
	public void click() {
		SerialComm serialComm = new SerialComm("COM" + portNum);
		
		try {
			for (TimeStep t : timeLine) {
				System.out.println(t);
				serialComm.runTimeStep(t);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
