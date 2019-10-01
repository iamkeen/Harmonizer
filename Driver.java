import java.util.*;
import java.io.*;
import java.lang.*;

public class Driver
{	
	public static void main(String args[])
	{
		// need to account for rests and notes not of full value
		// (i.e. eigth notes in 4/4 (0.5 would be its value))
		Scanner scanner = new Scanner(System.in);

		System.out.println("====================================");
		System.out.println("= SIMPLE HARMONIZER PROTOTYPE (v1) =");
		System.out.println("=       Keenan Noah Icarangal      =");
		System.out.println("====================================");
		System.out.println("This harmonizer prototype makes composing");
		System.out.println("a four part choral piece much more simpler.");
		System.out.println("Just provide us the necessary information");
		System.out.println("(name, time signature, key signature, melody,");
		System.out.println("and chord progression) and this program will");
		System.out.println("fill in the necessary gaps!\n");
		System.out.println("Notes: ");
		System.out.println("      - all compositions will be homophonic");
		System.out.println("        (non-homophonic option coming soon)");
		System.out.println("      - melody will always be given to Soprano");
		System.out.println("        so ensure melody inputted is within their");
		System.out.println("        respectable range (C4 to A5)");



		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.println();

		//check if time signature is valid
		System.out.print("Time Signature (i.e. 4/4, 3/4, 6/8, etc.): ");
		scanner.nextLine();
		System.out.println();

		//TODO: check if key is valid
		System.out.print("Key Signature: ");
		String key = scanner.nextLine();
		System.out.println();

		System.out.println("Melody must be inputted in the following format: ");
		System.out.println("([Note1][Octave] [Time Value]) ([Note2][Octave] [Time Value]) ...");
		System.out.println("Example: a melody starting with a middle C quarter note and");
		System.out.println("         a middle C half note in the time signature of 4/4");
		System.out.println("         would be formatted as (C4 1) (C4 2)");
		System.out.println("Notes: ");
		System.out.println("      - partial time values (eigth notes in 4/4 represented");
		System.out.println("        as ([Note][Octave] 0.25) are now supported!");
		System.out.println("      - rests are supported as (X0 [Time Value])");
		System.out.println("Melody: ");
		String rawMelody = scanner.nextLine();
		System.out.println();

		System.out.println("Chord Progression must be inputted in the following format: ");
		System.out.println("([Chord1][Time Value]) ([Chord2][Time Value]) ...");
		System.out.println("The following chord types are accepted: ");
		System.out.println("\t-major (i.e. (G 4))");
		System.out.println("\t-minor (i.e. (Em 2))");
		System.out.println("\t-augmented (i.e. (Caug 1))");
		System.out.println("\t-diminished (i.e. (F#dim 1))");
		System.out.println("Example: a chord progression starting with a G major for 4");
		System.out.println("         counts and an E minor for 2 counts in the time");
		System.out.println("         signature of 4/4 would be formatted as (G 4) (Em 2)");
		System.out.println("Chord Progression:");
		String rawChordProg = scanner.nextLine();
		System.out.println();

		String[] splitRawMelody = rawMelody.substring(1).split("\\(|\\s|\\)\\s\\(|\\)", 0);
		String[] splitRawChordProg = rawChordProg.substring(1).split("\\(|\\s|\\)\\s\\(|\\)", 0);

		ArrayList<ChoirNote> melody = new ArrayList<ChoirNote>();

		for (int i = 0; i <= splitRawMelody.length - 2; i += 2)
		{
			//System.out.print(i + " " + splitRawMelody[i] + "\n");
			String curRawNote = splitRawMelody[i];
			String curNote;
			int curOctave;

			if (curRawNote.charAt(1) == '#' || curRawNote.charAt(1) == 'b')
			{
				curNote = (curRawNote.charAt(0) + "" + curRawNote.charAt(1));
				curOctave = Integer.parseInt(curRawNote.charAt(2) + "");
			}
			else
			{
				curNote = curRawNote.charAt(0) + "";
				curOctave = Integer.parseInt(curRawNote.charAt(1) + "");
			}

			float curDuration = Float.parseFloat(splitRawMelody[i + 1]);

			melody.add(new ChoirNote(curNote, curOctave, curDuration));
		}


		ArrayList<Chord> chordProg = new ArrayList<Chord>();

		for (int i = 0; i <= splitRawChordProg.length - 2; i += 2)
		{
			//System.out.print(i + " " + splitRawChordProg[i] + "\n");
			String curChord = splitRawChordProg[i];
			float curDuration = Float.parseFloat(splitRawChordProg[i + 1]);
			chordProg.add(new Chord(curChord, curDuration, key));
		}

		Harmonizer h = new Harmonizer(name, melody, chordProg, key);
		h.genHomophonic();

		//check chord generation
		for (int i = 0; i < h.chordProg.size(); i++)
		{
			Chord temp = h.chordProg.get(i);
			System.out.print("Chord: " + temp.chord + "\n" + 
							//"Type: " + temp.type + "\n" +
							"Duration: " + temp.duration + "\n" + 
							"Bass Note: " + temp.bassNote.toString() + "\n" + 
							"Root: " + temp.root.toString() + "\n" +
							"Third: " + temp.third.toString() + "\n" +
							"Fifth: " + temp.fifth.toString() + "\n\n");
							//);
		}
		System.out.println();

		System.out.println("Bass:");
		for (int i = 0; i < h.bass.size(); i++)
		{
			ChoirNote temp = h.bass.get(i);
			System.out.print(temp.note + temp.octave + " " + temp.duration + "\n");
		}
		System.out.println();

		System.out.println("Tenor:");
		for (int i = 0; i < h.tenor.size(); i++)
		{
			ChoirNote temp = h.tenor.get(i);
			System.out.print(temp.note + temp.octave + " " + temp.duration + "\n");
		}
		System.out.println();

		System.out.println("Alto:");
		for (int i = 0; i < h.alto.size(); i++)
		{
			ChoirNote temp = h.alto.get(i);
			System.out.print(temp.note + temp.octave + " " + temp.duration + "\n");
		}
		System.out.println();

		System.out.println("Soprano:");
		for (int i = 0; i < h.soprano.size(); i++)
		{
			ChoirNote temp = h.soprano.get(i);
			System.out.print(temp.note + temp.octave + " " + temp.duration + "\n");
		}
		System.out.println();

		/*h.genNonhomophonic();

		for (int i = 0; i < h.chordProg.size(); i++)
		{
			Chord temp = h.chordProg.get(i);
			System.out.print("Chord: " + temp.chord + "\n" + 
							//"Type: " + temp.type + "\n" +
							"Duration: " + temp.duration + "\n" + 
							"Bass Note: " + temp.bassNote.toString() + "\n" + 
							"Root: " + temp.root.toString() + "\n" +
							"Third: " + temp.third.toString() + "\n" +
							"Fifth: " + temp.fifth.toString() + "\n\n");
							//);
		}

		System.out.println();
		System.out.println("Soprano:");
		for (int i = 0; i < h.soprano.size(); i++)
		{
			ChoirNote temp = h.soprano.get(i);
			System.out.print(temp.note + temp.octave + " " + temp.duration + "\n");
		}
		System.out.println();

		System.out.println("Bass:");
		for (int i = 0; i < h.bass.size(); i++)
		{
			ChoirNote temp = h.bass.get(i);
			System.out.print(temp.note + temp.octave + " " + temp.duration + "\n");
		}
		System.out.println();*/
	}
}

/*Chord gChordWhole = new Chord("G", 4, key);
		Chord cChordHalf = new Chord("C", 2, key);
		Chord gChordHalf = new Chord("G", 2, key);
		Chord amChordHalf = new Chord("Am", 2, key);
		Chord dChordHalf = new Chord("D", 2, key);
		chordProg.add(gChordWhole);
		chordProg.add(cChordHalf);
		chordProg.add(gChordHalf);
		chordProg.add(amChordHalf);
		chordProg.add(gChordHalf);
		chordProg.add(dChordHalf);
		chordProg.add(gChordHalf);

		ChoirNote g4Quarter = new ChoirNote("G", 4, 1);
		ChoirNote d5Quarter = new ChoirNote("D", 5, 1);
		ChoirNote e5Quarter = new ChoirNote("E", 5, 1);
		ChoirNote d5Half = new ChoirNote("D", 5, 2);
		ChoirNote c5Quarter = new ChoirNote("C", 5, 1);
		ChoirNote b4Quarter = new ChoirNote("B", 4, 1);
		ChoirNote a4Quarter = new ChoirNote("A", 4, 1);
		ChoirNote g4Half = new ChoirNote("G", 4, 2);
		melody.add(g4Quarter);
		melody.add(g4Quarter);
		melody.add(d5Quarter);
		melody.add(d5Quarter);
		melody.add(e5Quarter);
		melody.add(e5Quarter);
		melody.add(d5Half);
		melody.add(c5Quarter);
		melody.add(c5Quarter);
		melody.add(b4Quarter);
		melody.add(b4Quarter);
		melody.add(a4Quarter);
		melody.add(a4Quarter);
		melody.add(g4Half);*/


		/*ChoirNote c4_3 = new ChoirNote("C", 4, 3);
		ChoirNote c4_6 = new ChoirNote("C", 4, 6);
		ChoirNote c4_2 = new ChoirNote("C", 4, 2);
		ChoirNote c4_1 = new ChoirNote("C", 4, 1);
		ChoirNote d4_1 = new ChoirNote("D", 4, 1);
		ChoirNote e4_3 = new ChoirNote("E", 4, 3);
		ChoirNote e4_2 = new ChoirNote("E", 4, 2);
		ChoirNote f4_1 = new ChoirNote("F", 4, 1);
		ChoirNote g4_6 = new ChoirNote("G", 4, 6);
		ChoirNote c5_1 = new ChoirNote("C", 5, 1);
		ChoirNote g4_1 = new ChoirNote("G", 4, 1);
		ChoirNote e4_1 = new ChoirNote("E", 4, 1);
		ChoirNote g4_2 = new ChoirNote("G", 4, 2);

		//row
		melody.add(c4_3);
		//row
		melody.add(c4_3);
		//row
		melody.add(c4_2);
		//your
		melody.add(d4_1);
		//boat
		melody.add(e4_3);
		//gen
		melody.add(e4_2);
		//tly
		melody.add(d4_1);
		//down
		melody.add(e4_2);
		//the
		melody.add(f4_1);
		//stream
		melody.add(g4_6);
		//mer
		melody.add(c5_1);
		//ri
		melody.add(c5_1);
		//ly
		melody.add(c5_1);
		//me
		melody.add(g4_1);
		//ri
		melody.add(g4_1);
		//ly
		melody.add(g4_1);
		//me
		melody.add(e4_1);
		//ri
		melody.add(e4_1);
		//ly
		melody.add(e4_1);
		//mer
		melody.add(c4_1);
		//ri
		melody.add(c4_1);
		//ly
		melody.add(c4_1);
		//life
		melody.add(g4_2);
		//is
		melody.add(f4_1);
		//but
		melody.add(e4_2);
		//a
		melody.add(d4_1);
		//dream
		melody.add(c4_6);
		

		Chord cChord6 = new Chord("C", 6, key);
		Chord cChord3 = new Chord("C", 3, key);
		Chord gChord3 = new Chord("G", 3, key);
		chordProg.add(cChord6);
		chordProg.add(cChord6);
		chordProg.add(cChord6);
		chordProg.add(cChord6);
		chordProg.add(cChord3);
		chordProg.add(gChord3);
		chordProg.add(cChord6);
		chordProg.add(cChord3);
		chordProg.add(gChord3);
		chordProg.add(cChord6);*/