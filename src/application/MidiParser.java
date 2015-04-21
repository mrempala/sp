package application;

import java.io.File;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiParser {
	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F",
			"F#", "G", "G#", "A", "A#", "B" };

	public ArrayList<Integer> run() throws Exception {
		javax.sound.midi.Sequence sequence = MidiSystem.getSequence(new File(
				"elise.mid"));
		ArrayList<Integer> ticks = new ArrayList<Integer>();
		ArrayList<Integer> keys = new ArrayList<Integer>();

		int trackNumber = 0;
		float resolution = sequence.getResolution();
		float ticksPerSecond = (float) (resolution * 2.0);
		long prev = 0;
        long dt = 0;
		
		for (Track track : sequence.getTracks()) {
			trackNumber++;
			// System.out.println("Track " + trackNumber + ": size = " +
			// track.size());
			// System.out.println();
			for (int i = 0; i < track.size(); i++) {
				MidiEvent event = track.get(i);

				// System.out.print(/*"@" +*/ event.getTick() + " ");
				// Thread.sleep(222);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;
					// System.out.print("Channel: " + sm.getChannel() + " ");
					if (sm.getCommand() == NOTE_ON) {
						int key = sm.getData1();
						int octave = (key / 12) - 1;
						int note = key % 12;
						String noteName = NOTE_NAMES[note];
						int velocity = sm.getData2();
						
						if (velocity > 0) {
							dt = event.getTick() - prev;
						    prev = event.getTick();
						    int repition = (int) (((dt / ticksPerSecond) * 1000) / 50);

                            for (int r = 0; r < repition; r++) {
                            	keys.add(key);
                            }
							//System.out.println(/*
									//			 * "Note on, " + noteName +
										//		 * octave + " key=" +
											//	 */key + " " + /*
												//				 * " velocity: "
													//			 * +
														//		 */velocity);
						}
					} else if (sm.getCommand() == NOTE_OFF) {
						/*
						 * int key = sm.getData1(); int octave = (key / 12)-1;
						 * int note = key % 12; String noteName =
						 * NOTE_NAMES[note]; int velocity = sm.getData2();
						 */
						// System.out.println("Note off, " + noteName + octave +
						// " key=" + key + " velocity: " + velocity);
					} else {
						// System.out.println("Command:" + sm.getCommand());
					}
				} else {
					// System.out.println("Other message: " +
					// message.getClass());
				}
			}

			System.out.println();
		}
		return keys;
	}
	
}