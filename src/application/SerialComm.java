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
	private String comPort;
	private SerialPort serialPort;
	private String data;
	private String[] buffer;
	private int numEvents = 0;
	private boolean armed = false;
    private static boolean[] armedFB = new boolean[12];
    private int FBcount = 0;
	private BufferedReader input;

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

	public void initialize() throws NoSuchPortException{
		System.setProperty("gnu.io.rxtx.SerialPorts", comPort);

		CommPortIdentifier portId = null;
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

				if (buffer[6].equals("2")) {
					armedFB[Integer.getInteger(buffer[4])] = false;
				}

				System.out.println(data);

				numEvents = 0;
				data = new String();

			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
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

	private byte byteSum(byte victim[], int pos) // sum the elements of an array
													// from 0 upto and inclusing
													// pos. IE: check checksum
													// at pos.
	{
		byte sum = 0;
		for (int i = 0; i <= pos; i++) {
			sum += victim[i];
		}
		return sum;
	}

	private byte[] ping(byte box) {
		int i = 0;
		byte[] outBuffer = new byte[9];
		outBuffer[i++] = (byte) 0xAA;
		outBuffer[i++] = box;
		outBuffer[i++] = 0x07;
		outBuffer[i++] = (byte) 0x80;
		outBuffer[i++] = (byte) 0x80;
		outBuffer[i++] = 0x77; // ping command
		outBuffer[i++] = 0x00;
		outBuffer[i++] = (byte) (~byteSum(outBuffer, i - 1) + 1);
		outBuffer[i++] = 0x0A;
		return outBuffer;
	}

	private byte[] setBoxes(byte firebox, byte numBoxes, byte squib)// set n
																	// number of
																	// boxes.
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
			outBuffer[i++] = squib;
		}

		outBuffer[2] = (byte) i;// pos of chksum
		outBuffer[i++] = (byte) ((~byteSum(outBuffer, i - 1)) + 1); // the sum
																	// from 0 to
																	// the last
																	// payload
																	// byte
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
		sendCommand((byte) 0xFF, (byte) 0xFA, (byte) 0x00); // every address,
															// fire all
	}

	public void prepUniverse() throws Exception {
		// send null command, no responds expected
		sendCommand((byte) 0x00, (byte) 0x00, (byte) 0x00);

		// Clear/Set all lunchbox address, expect pong packet
		sendCommand((byte) 0x00, (byte) 0x77, (byte) 0x00);
		Thread.sleep(12);
	}
	
	private void armFB(byte address){
		sendCommand(address, (byte) 0xA2, (byte) 0x01);
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void resetArmedFB() {
		for (int i = 0; i < 12 ; i++) { 
			armedFB[i] = false;
		}
	}
	public void runTimeStep(TimeStep step) throws Exception {
		if (step.getSquibList().size() == 0) {
			// If there's no squibs to be fired, get out of here and don't arm
			// anything
			return;
		}
		// Arms firebox and charge all attached lunchbox
		// We will need to send a charge command for each connected firebox, not
		// just 0x00, maybe we can arm all by sending 0xFF?
		//Clear all marked FB 
		resetArmedFB();
		
		//Mark which to arm
		for (Squib s : step) {
			armedFB[s.getFirebox()] = true;
		}
		
		//Arm FB
		for (int i = 0; i < 12; i++) {
			if (armedFB[i]) {
				armFB((byte) i);
			}
		}
		
		armed = false;
		while (!armed) {
			armed = true;
			// I think we will need to ping each firebox with squibs to be fired
			// to make sure all are ready
			// Ping Firebox to see if charged & ready to fire

			for (int i = 0; i < 12; i++) {
				if (armedFB[i]) {
					sendCommand((byte) i, (byte) 0x22, (byte) 0x00);
					armed = false;

					Thread.sleep(5);
				}
			}
			 
			Thread.sleep(50);
		}

		// add squibs to be fired from time step here
		// Currently I think this will only work for 1 firebox hooked up with a
		// string of lunchboxes attached to it,
		// and it is only capable of firing a single squib at a time (because it
		// gets index 0 from squiblist)
		sendData(setBoxes((byte) step.getSquibList().get(0).getFirebox(),
						  (byte) 1, 
						  (byte) (step.getSquibList().get(0).getSquib() + 1)));
		Thread.sleep(50);
		fire();
		Thread.sleep(5);

	}
}