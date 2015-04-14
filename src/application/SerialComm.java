package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;

public class SerialComm implements SerialPortEventListener {
	SerialPort serialPort;
	String data = new String();
	String[] buffer = new String[9];
	int numEvents = 0;
	boolean armed = false;
	
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac
																				// OS
																				// X
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM6", // Windows
	};
	
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */

	private static final int DATA_RATE = 250000;

	public void initialize() {
		System.setProperty("gnu.io.rxtx.SerialPorts", "COM6");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

	    // First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();

			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
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
			System.out.println("Here");
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
					data+=inputLine + "|";
					buffer[i++] = inputLine; 
					//System.out.println("Here");
					numEvents++;
				}
				
				//Hack to get last byte of transmission
				while (numEvents < 9) {
					inputLine = input.readLine();
					data+=inputLine+ "|";
					buffer[i++] = inputLine;
			
					numEvents++;
				}
				
				//System.out.println(buffer[5]);
				
				if (buffer[6].equals("2"))
					armed = true;
				
				System.out.println(data); 
				
				numEvents = 0;
				data = new String();
				
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}

	public void sendData(byte[] data) {
        try {
            output = serialPort.getOutputStream();
            output.write( data );
        } 
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }
	
	private byte byteSum(byte victim[],int pos) //sum the elements of an array from 0 upto and inclusing pos. IE: check checksum at pos.
	{
	  byte sum=0;
	  for(int i = 0; i <=pos;i++)
	  {
	    sum += victim[i];
	  }
	  return sum;
	}
	
	public byte[] ping(byte box)
	{
	  int i = 0;
	  byte[] outBuffer = new byte[9];
	  outBuffer[i++] = (byte) 0xAA;
	  outBuffer[i++] = box;
	  outBuffer[i++] = 0x07;
	  outBuffer[i++] = (byte) 0x80;
	  outBuffer[i++] = (byte) 0x80;
	  outBuffer[i++] = 0x77; //ping command
	  outBuffer[i++] = 0x00;
	  outBuffer[i++] = (byte) (~byteSum(outBuffer,i-1)+1);
	  outBuffer[i++] = 0x0A;
	  //UniBus.write(payload,i);
	  return outBuffer;
	}
    
	public byte[] setBoxes(byte firebox,byte numBoxes, byte squib)//set n number of boxes.
	{  
	  //clearMemory(outBuffer,
	  int i = 0;
	  byte[] outBuffer = new byte[9 + numBoxes];
	  outBuffer[i++] = (byte) 0xAA;//header //0
	  outBuffer[i++] = (byte) firebox;//address//1
	  i++;//2
	  outBuffer[i++] = (byte) 0x80;//r //3
	  outBuffer[i++] = (byte) 0x80;//r //4
	  outBuffer[i++] = (byte) 0xCF;//SAC command //5
	  outBuffer[i++] = 0x00;//prmtr //6
	  
	  for(int k=0; k < numBoxes; k++)
	  {                                                             
	    outBuffer[i++] = squib;
	  }
	  
	  outBuffer[2] = (byte) i;//pos of chksum
	  outBuffer[i++] = (byte)((~byteSum(outBuffer,i-1))+1); //the sum from 0 to the last payload byte
	  outBuffer[i++] = 0x0A;
	  return outBuffer;
	}


	void sendCommand(byte address, byte command, byte parameter)
	{  
		byte[] twoBytebuffer = {(byte)0xAA, (byte)0x00, (byte)0x07, (byte)0x80, (byte) 0x80, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0A};
		twoBytebuffer[1] = address;
		twoBytebuffer[5] = command;
		twoBytebuffer[6] = parameter;
		twoBytebuffer[7] = (byte) ((~byteSum(twoBytebuffer,6))+1);
	    sendData(twoBytebuffer);
	}

	void fire()
	{
	  sendCommand((byte)0xFF,(byte)0xFA,(byte)0x00); //every address, fire all
	}

	public static void main(String[] args) throws Exception {
		SerialComm main = new SerialComm();
		    //Setup serial communication of host
			main.initialize();  
            
			//send null command, no responds expected
			main.sendCommand((byte)0x00, (byte)0x00, (byte)0x00);
			
			//Clear/Set all lunchbox address, expect pong packet 
			main.sendCommand((byte)0x00, (byte)0x77, (byte)0x00);
			
			Thread.sleep(12);
			for (byte i = 1; i < 9; i++) {
				
				//Arms firebox and charge all attached lunchbox
				main.sendCommand((byte)0x00, (byte)0xA2, (byte)0x01);
				Thread.sleep(60);
			
				while (!main.armed){
					main.sendCommand((byte)0x00, (byte)0x22, (byte)0x00);
					Thread.sleep(50);
				}
		
				main.sendData(main.setBoxes((byte) 0, (byte) 1, i));
				Thread.sleep(50);
				main.fire();
				Thread.sleep(5);
			}
			
            try { Thread.sleep(2000); } catch (InterruptedException ie) {}

            main.close();        
    }
}