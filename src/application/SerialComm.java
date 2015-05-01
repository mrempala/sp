package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;

public class SerialComm implements SerialPortEventListener {
	private static final int PACKET_SIZE = 9;
	private static final int NUM_FB = 16;
	private static final int NUM_LB = 12;
	
	private String comPort;
	private SerialPort serialPort;
	private String data;
	private String[] buffer;
	private int numEvents = 0;
	private boolean armed = false;
	private static boolean[] armedFB = new boolean[NUM_FB];
	private BufferedReader input;
	private boolean errorDetected = false;
	private boolean universeResponse = false; // Flag to see if we've received a pong packet from the universe

	/** The output stream to the port */
	private OutputStream output;

	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;

	/** Default bits per second for COM port. */

	private static final int DATA_RATE = 250000;

	public SerialComm(String comPort) throws NoSuchPortException {
		data = new String();
		buffer = new String[PACKET_SIZE];
		this.comPort = comPort;

		initialize();
		resetArmedFB();
	}

	public void initialize() throws NoSuchPortException {
		System.setProperty("gnu.io.rxtx.SerialPorts", comPort);

		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();

			if (currPortId.getName().equals(comPort)) {
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			throw new NoSuchPortException();

		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (Exception e) {
			System.err.println(e.toString());

		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		universeResponse = true;
		data = new String();
		
		// Reset buffer
		for (int i = 0; i < 9; i++) {
			buffer[i] = "";
		}
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = null;
				int i = 0;

				if (input.ready()) {
					inputLine = input.readLine();
					data += inputLine + "|";
					buffer[i++] = inputLine;
					numEvents++;
				}

				while (numEvents < 9) {
					inputLine = input.readLine();
					data += inputLine + "|";
					buffer[i++] = inputLine;

					numEvents++;
				}

				// Try to detect if we're getting a bad packet.
				// The first byte should be AA (header)
				if ( !(buffer[0].equals("AA")) ){
					//errorDetected = true;
					System.out.println("Packet Order Error Detected");
					int packetStart = 0;
					// String[] tempBuffer = new String[9];
					for (int j = 0; j < 9; j++) {
						if (buffer[j].equals("AA")){
							packetStart = j;
							break;
						}
						//tempBuffer[j] = buffer[j];
					}
					System.out.println(packetStart);
					rotateArray(buffer, packetStart);
					//System.out.println("Fixed packet: " + buffer);
					
					for (int j = 0; j < 9; j++) {
						System.out.println(buffer[j]);
					}
					
					
				}
				
				// Switch the firebox in the armedFB array back to false
				// signifying that it is charged and ready
				if (buffer[6].equals("2")) {
					armedFB[Integer.parseInt(buffer[3])] = false;
					//System.out.println("Received a firebox ready");
				}

				System.out.println(data);

				numEvents = 0;
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
	
	private void rotateArray(String[] array, int index){
		String[] result;
		
		result = new String[9];
		
		System.arraycopy(array, index, result, 0, array.length - index);
		System.arraycopy(array, 0, result, array.length - index, index);
		System.arraycopy(result, 0, array, 0, array.length);
		
	}

	private void sendData(byte[] data) {
		try {
			output = serialPort.getOutputStream();
			output.write(data);
		} catch (Exception e) {
			System.err.println(e.toString());
			System.exit(0);
		}
	}

	// sum the elements of an array from 0 upto and inclusing pos. IE: check checksum at pos.
	private byte byteSum(byte victim[], int pos) 
	{
		byte sum = 0;
		for (int i = 0; i <= pos; i++) {
			sum += victim[i];
		}
		return sum;
	}

	// set numBoxes number of boxes to be fired off given firebox
	private byte[] setBoxes(byte firebox, byte numBoxes, byte[] squibs)
	{
		// clearMemory(outBuffer,
		int i = 0;
		byte[] outBuffer = new byte[9 + numBoxes];
		outBuffer[i++] = (byte) 0xAA;// header //0
		outBuffer[i++] = (byte) firebox;// address//1
		i++;// 2
		outBuffer[i++] = (byte) 0x80;// r //3
		outBuffer[i++] = (byte) 0x80;// r //4
		outBuffer[i++] = (byte) 0xCF;// SAC command //5
		outBuffer[i++] = 0x00;// prmtr //6

		for (int k = 0; k < numBoxes; k++) {
			outBuffer[i++] = squibs[k];
		}

		outBuffer[2] = (byte) i;// pos of chksum
		outBuffer[i++] = (byte) ((~byteSum(outBuffer, i - 1)) + 1); // the sum from 0 to the last payload byte
		outBuffer[i++] = 0x0A;
		return outBuffer;
	}

	private void sendCommand(byte address, byte command, byte parameter) {
		byte[] twoBytebuffer = { (byte) 0xAA, (byte) 0x00, (byte) 0x07,
				(byte) 0x80, (byte) 0x80, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x0A };
		twoBytebuffer[1] = address;
		twoBytebuffer[5] = command;
		twoBytebuffer[6] = parameter;
		twoBytebuffer[7] = (byte) ((~byteSum(twoBytebuffer, 6)) + 1);
		sendData(twoBytebuffer);
	}

	private void fire() {
		sendCommand((byte) 0xFF, (byte) 0xFA, (byte) 0x00); // every address, fire all
	}

	public void prepUniverse() throws Exception {
		// send null command, no responds expected
		sendCommand((byte) 0x00, (byte) 0x00, (byte) 0x00);

		// Clear/Set all lunchbox address, expect pong packet
		sendCommand((byte) 0x00, (byte) 0x77, (byte) 0x00);
		Thread.sleep(12);
	}

	private void armFB(byte address) {
		sendCommand(address, (byte) 0xA2, (byte) 0x01);
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void resetArmedFB() {
		for (int i = 0; i < 12; i++) {
			armedFB[i] = false;
		}
	}

	public String runTimeStep(TimeStep step) throws Exception {
		String error = "";
		// Temporary 2-d array to hold which lb's to fire on each fb
		byte[][] lbsToFire = new byte[NUM_FB][NUM_LB];

		// Hack for keeping track of the number of LBs
		int[] numLbs = new int[NUM_LB];

		if (step.getSquibList().size() == 0) {
			// If there's no squibs to be fired, get out of here and don't arm
			// anything
			Thread.sleep(35);
			return "";
		}

		// Clear all marked FB
		resetArmedFB();

		// Mark which to arm
		for (Squib s : step) {
			armedFB[s.getFirebox()] = true;
		}

		// Arm FB
		for (int i = 0; i < NUM_FB; i++) {
			if (armedFB[i]) {
				armFB((byte) i);
			}
		}
        
		int ping = 0;
		int sleep = 0;
		armed = false;
		// Reset universeResponse so we send at least 1 ping next time runTimeStep is called
		universeResponse = true;
		while (!armed) {
			armed = true;
			
			// If we haven't received a response from the universe yet, don't do anything.
			// If we've slept for over 100 milliseconds, try to resent the packet
			if (!universeResponse && sleep < 20) {
				//System.out.println("Awaiting universe response...");
				armed = false;
				Thread.sleep(5);
				sleep++;
				error += "WARNING: No response from universe.  Trying to ping again\n";
				continue;
			}
			universeResponse = false;
			sleep = 0;
			
			if (errorDetected) {
				errorDetected = false;
				return error + "\nERROR: There was a problem with the received packet.  " +
						"Expected header to start with AA but received packet " +
						data + "\n   Try resetting the consol!!\n";
			}
			
			// Ping Firebox to see if charged & ready to fire
			for (int i = 0; i < NUM_FB; i++) {
				// If firebox needs to be armed but hasn't been set to 
				// false by serial event handler, ping the box again
				if (armedFB[i]) {
					sendCommand((byte) i, (byte) 0x22, (byte) 0x00);
					armed = false;
					//Thread.sleep(5);
				}
			}
			ping++;
			
			// If we haven't received a response, assume FB-LB is broken or not there so exit
			if (ping > 15) {
				return "ERROR: Firebox not armed after 15 pings. \n" +
						"   This is likely due to non-existent or disabled Firebox, but could be due a to malformed packet\n" +
						"   Last packet received: " + data + "\n";
			}
            
			//Thread.sleep(10);
		}
		
		// Reset our LB count in prep for sending arm commands
		for (int i = 0; i < NUM_LB; i++) {
			numLbs[i] = 0;
		}

		// Figure out which FB's are to be fired
		// and figure out which which squib to fire on each LB
		for (Squib s : step) {
			lbsToFire[s.getFirebox()][s.getLunchbox()] = (byte) (s.getSquib() + 1); 
			armedFB[s.getFirebox()] = true; // Remark which fireboxes are to be fired
			numLbs[s.getFirebox()]++;
		}

		// Send commands to mark LB squibs for each FB to be fired 
		for (int i = 0; i < NUM_FB; i++) {
			if (armedFB[i]) {
				sendData(setBoxes((byte) i, // FB address
						(byte) numLbs[i], // Number of lunchboxes
						lbsToFire[i])); // pass the ith FB array
				/*System.out.println("Setting boxes: " + i + " " + numLbs[i]
						+ " " + lbsToFire[i][0]);*/
				Thread.sleep(5);
			}
		}
		//Thread.sleep(50);
		
		// Bombs away
		fire();
		Thread.sleep(5);

		// If everything fired as expected, just return an empty string
		return "";
	}
}