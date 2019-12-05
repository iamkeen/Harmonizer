import java.util.*;
import java.io.*;
import java.lang.*;
import javax.sound.midi.MidiSystem;
import org.jfugue.player.*;
import org.jfugue.parser.*;
import org.staccato.*;
import org.jfugue.midi.*;
import org.jfugue.pattern.*;

//compile with: javac -cp "jfugue.jar" *.java
//run with: java -cp jfugue.jar;. Driver

public class Driver
{	
	public static void main(String args[])
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("===============================================================================================");
		System.out.println("=                             SIMPLE HARMONIZER PROTOTYPE (v2)                                =");
		System.out.println("=                                  Keenan Noah Icarangal                                      =");
		System.out.println("===============================================================================================");
		System.out.println("This harmonizer prototype makes composing a four part choral piece much more simpler.\n");
		System.out.println("Just provide this program with two files: the melody file (as a .mid) and the chord progression");
		System.out.println("file (as a .txt).  *** Simply place them in the same directory as this program ***.");
		System.out.println("If you haven't done this already, press \'CTRL + C\' to quit.\n");
		System.out.println("The MELODY FILE should only contain the melody line on one musical staff!\n");
		System.out.println("The CHORD PROGRESSION FILE should contain the chords used throughout the entire arrangement.");
		System.out.println("The contents of the CHORD PROGRESSION FILE must be inputted in the following format on one line: ");
		System.out.println("([Chord1][Time Value]) ([Chord2][Time Value]) ...");
		System.out.println("The following chord types are accepted: ");
		System.out.println("\t-major (i.e. (G 4))");
		System.out.println("\t-minor (i.e. (Em 2))");
		System.out.println("\t-augmented (i.e. (Caug 1))");
		System.out.println("\t-diminished (i.e. (F#dim 1))");
		System.out.println("Example: a chord progression starting with a G major for a whole note (4 counts) and then an");
		System.out.println("\tE minor for a half note (2 counts) would be formatted as (G 4) (Em 2).");
		System.out.println("\tThe chord progression for \'Amazing Grace\' would be formatted as (G 5) (D 1) (Em 1) (C 1) (G 2)\n");
		System.out.println("Notes: ");
		System.out.println("\t- all compositions will be homophonic (non-homophonic option coming soon)");
		System.out.println("\t- melody will always be given to Soprano so ensure melody inputted is within their respectable");
		System.out.println("\trange (C4 to A5)");
		System.out.println("\t- this program uses the JFUGUE library.  Because of this, no key signature will be");
		System.out.println("\tin the final MIDI but the notes will still be correct!  Support for rests will");
		System.out.println("\tbe added soon!\n");

		try
		{
			System.out.print("Arrangement name (this will be used as the final MIDI file's name): ");
			String finalName = scanner.nextLine().replaceAll("\\s+","");
			//String finalName = "Ultralight Beam";

			String key = "C";

			boolean validKey = false;
			while (!validKey)
			{
				System.out.print("Key Signature: ");
				key = scanner.nextLine();
				if ((key.equals("C") || key.equals("Am")) ||
					(key.equals("G") || key.equals("Em")) ||
					(key.equals("D") || key.equals("Bm")) ||
					(key.equals("A") || key.equals("F#m")) ||
					(key.equals("E") || key.equals("C#m")) ||
					(key.equals("B") || key.equals("G#m")) ||
					(key.equals("F#") || key.equals("D#m")) ||
					(key.equals("C#") || key.equals("A#m")))
				{
					validKey = true;
				}
				else
				{
					System.out.println("Please enter a valid key.");
				}
			}

			System.out.print("MELODY FILE name (must be a .mid): ");
			String melodyFileName = scanner.nextLine();
			//String melodyFileName = "UBproj.mid";

			System.out.print("CHORD PROGRESSION FILE name (must be a .txt): ");
			String chordProgFileName = scanner.nextLine();
			//String chordProgFileName = "UBcp.txt";

			MidiParser parser = new MidiParser();
	        StaccatoParserListener listener = new StaccatoParserListener();
	        StaccatoParserPatternHelper patternHelper = new StaccatoParserPatternHelper();
	        parser.addParserListener(listener);
	        parser.parse(MidiSystem.getSequence(new File(melodyFileName)));
	        Pattern staccatoPattern = listener.getPattern();
	        List<Token> tokens = patternHelper.getTokens(staccatoPattern);
	        for (int i = 0; i < tokens.size(); i++)
	        {
	        	System.out.println(tokens.get(i));
	        }
	        
	        ArrayList<ChoirNote> melody = parseMelody(tokens);
	        for (int i = 0; i < melody.size(); i++)
	        {
				ChoirNote temp = melody.get(i);
				System.out.print(temp.note + temp.octave + " " + temp.duration + "\n");
	        }

	        String midiTS = parseMidiTS(tokens);
	        String midiKS = parseMidiKS(tokens);

	        String chordProgStr = "";
	        File chordProgFile = new File(chordProgFileName);
	        Scanner fileScan = new Scanner(chordProgFile);
	        while (fileScan.hasNextLine())
	        {
	        	chordProgStr += fileScan.nextLine();
	        }
	        fileScan.close();
			System.out.println(chordProgStr);

	        ArrayList<Chord> chordProg = new ArrayList<Chord>();
	        //String rawChordProg = "(G 4) (C 2) (G 2) (Am 2) (G 2) (D 2) (G 2)";
	        //String rawChordProg = "(C 13.5) (G 1.5) (C 4.5) (G 1.5) (C 3)";
	        String[] splitRawChordProg = chordProgStr.substring(1).split("\\(|\\s|\\)\\s\\(|\\)", 0);
			for (int i = 0; i <= splitRawChordProg.length - 2; i += 2)
			{
				//System.out.print(i + " " + splitRawChordProg[i] + "\n");
				String curChord = splitRawChordProg[i];
				float curDuration = Float.parseFloat(splitRawChordProg[i + 1]);
				chordProg.add(new Chord(curChord, curDuration, key));
			}

			Harmonizer h = new Harmonizer(finalName, melody, chordProg, key);
			h.genHomophonic();

			/*if (mode == 1)
			{	
				h.genHomophonic();
			}
			else if (mode == 2)
			{
				h.genNonhomophonic();
			}*/

			Generator generator = new Generator(h, midiTS, midiKS);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	private static ArrayList<ChoirNote> parseMelody(List<Token> tokens)
	{
		ArrayList<ChoirNote> melody = new ArrayList<ChoirNote>();

		for (int i = 0; i < tokens.size(); i++)
		{
			Token curTok = tokens.get(i);
			String curTokStr = curTok.toString();

			if (curTok.getType() == Token.TokenType.NOTE && curTokStr.charAt(0) != 'R')
			{
				String curNote;
				int curOctave;
				String strDuration;

				if (curTokStr.charAt(1) == '#' || curTokStr.charAt(1) == 'b')
				{
					curNote = (curTokStr.charAt(0) + "" + curTokStr.charAt(1));
					curOctave = Integer.parseInt(curTokStr.charAt(2) + "") - 1;
					strDuration = curTokStr.substring(4);
				}
				else
				{
					curNote = curTokStr.charAt(0) + "";
					curOctave = Integer.parseInt(curTokStr.charAt(1) + "") - 1;
					strDuration = curTokStr.substring(3);
				}

				float curDuration = 0.0f;

				if (strDuration.contains("0.02916")) //32nd
				{
					curDuration = 0.125f;
				}
				else if (strDuration.contains("0.04375A80D0")) //dotted 32nd
				{
					curDuration = 0.1875f;
				}
				else if (strDuration.contains("0.05885416")) //16th
				{
					curDuration = 0.25f;
				}
				else if (strDuration.contains("0.0885416")) //dotted 16th
				{
					curDuration = 0.375f;
				}
				else if (strDuration.contains("0.11822916") || strDuration.contains("0.12447916")) //8th
				{
					curDuration = 0.5f;
				}
				else if (strDuration.contains("0.17760416")) //dotted 8th
				{
					curDuration = 0.25f;
				}
				else if (strDuration.contains("0.23697916")) //quarter
				{
					curDuration = 1.0f;
				}
				else if (strDuration.contains("0.35572916")) //dotted quarter
				{
					curDuration = 1.5f;
				}
				else if (strDuration.contains("0.47447916")) //half
				{
					curDuration = 2.0f;
				}
				else if (strDuration.contains( "0.71197916")) //dotted half
				{
					curDuration = 3.0f;
				}
				else if (strDuration.contains("0.94947916")) //whole
				{
					curDuration = 4.0f;
				}
				else if (strDuration.contains("1.42447916")) //dotted whole
				{
					curDuration = 6.0f;
				}
			
				melody.add(new ChoirNote(curNote, curOctave, curDuration));
			}
		}

		return melody;
	}

	private static String parseMidiTS(List<Token> tokens)
	{
		String ts = "";
		for (int i = 0; i < tokens.size(); i++)
		{
			Token curTok = tokens.get(i);
			String curTokStr = curTok.toString();

			if (curTok.getType() == Token.TokenType.TIME_SIGNATURE)
			{
					ts = curTok.toString();
			}
		}
		return ts;
	}

	private static String parseMidiKS(List<Token> tokens)
	{
		String ks = "";

		for (int i = 0; i < tokens.size(); i++)
		{
			Token curTok = tokens.get(i);
			String curTokStr = curTok.toString();

			if (curTok.getType() == Token.TokenType.KEY_SIGNATURE)
			{
					ks = curTok.toString();
			}
		}
		return ks;
	}
}
/*System.out.println("====================================");
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

		//TODO: check if time signature is valid
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

		System.out.println("Homophonic[1] or non-homophonic[2]?");
		int mode = Integer.parseInt(scanner.nextLine());
		System.out.println();*/

		//6/8
		//String rawMelody = "(C4 1.5) (C4 1.5) (C4 1) (D4 0.5) (E4 1.5) (E4 1) (D4 0.5) (E4 1) (F4 0.5) (G4 3) (C5 0.5) (C5 0.5) (C5 0.5) (G4 0.5) (G4 0.5) (G4 0.5) (E4 0.5) (E4 0.5) (E4 0.5) (C4 0.5) (C4 0.5) (C4 0.5) (G4 1) (F4 0.5) (E4 1) (D4 0.5) (C4 3)";
		//String rawChordProg = "(C 13.5) (G 1.5) (C 4.5) (G 1.5) (C 3)";

		//String name = "";
		//4/4
		//String rawMelody = "(G4 1) (G4 1) (D5 1) (D5 1) (E5 1) (E5 1) (D5 2) (C5 1) (C5 1) (B4 1) (B4 1) (A4 1) (A4 1) (G4 2)";
		//String rawChordProg = "(G 4) (C 2) (G 2) (Am 2) (G 2) (D 2) (G 2)";
		//String key = "G";
		//int mode = 1;		

		/*String[] splitRawMelody = rawMelody.substring(1).split("\\(|\\s|\\)\\s\\(|\\)", 0);
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
		}*/

		//check chord generation
		/*for (int i = 0; i < h.chordProg.size(); i++)
		{
			Chord temp = h.chordProg.get(i);
			System.out.print("Chord: " + temp.chord + "\n" + 
							"Duration: " + temp.duration + "\n" + 
							"Bass Note: " + temp.bassNote.toString() + "\n" + 
							"Root: " + temp.root.toString() + "\n" +
							"Third: " + temp.third.toString() + "\n" +
							"Fifth: " + temp.fifth.toString() + "\n\n");
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
		System.out.println();*/