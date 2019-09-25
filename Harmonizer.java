import java.util.*;
import java.io.*;
import java.lang.*;

/*
	Twinkle Twinkle Little Star
	Time Signature: 4/4
	Key Signature: G
	Melody: (G4 1) (G4 1) (D5 1) (D5 1) 
			(E5 1) (E5 1) (D5 2) 
			(C5 1) (C5 1) (B4 1) (B4 1)
			(A4 1) (A4 1) (G4 2)
	Chord Progression: 	(G 4) 
						(C 2) (G 2)
						(Am 2) (G 2)
						(D 2) (G 2)

	- Frontend takes input
	- Adds name, time signature, and key to harmonizer
	- Adds melody to Harmonizer
	- Adds chord progression to Harmonizer
	- Harmonizer gens notes needed for each chord in progression
	- Harmonizer sets melody for Soprano
	- Harmonizer sets bass notes for Bass
	- Harmonizer sets notes for Alto and Tenor using Bass and Soprano
	- Harmonizer prints out each part

*/

public class Harmonizer
{
	String name;
	String key;

	/* implement later
	TimeSignature timeSignature;
	*/

	//use indexOf to check for octave rules
	
	String[] chromaticWithSharps = new String[]
	{"E2", "F2", "F#2", "G2", "G#2", "A2", "A#2", "B2", "C3", "C#3", "D3", "D#3",
	 "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4", "C#4", "D4", "D#4",
	 "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5",
	 "E5", "F5", "F#5", "G5", "G#5", "A5"};

	String[] chromaticWithFlats = new String[]
	{"E2", "F2", "Gb2", "G2", "Ab2", "A2", "Bb2", "B2", "C3", "Db3", "D3", "Eb3",
	 "E3", "F3", "Gb3", "G3", "Ab3", "A3", "Bb3", "B3", "C4", "Db4", "D4", "Eb4",
	 "E4", "F4", "Gb4", "G4", "Ab4", "A4", "Bb4", "B4", "C5", "Db5", "D5", "Eb5",
	 "E5", "F5", "Gb5", "G5", "Ab5", "A5"};

	String[] keyChromatic;

	ArrayList<Chord> chordProg = new ArrayList<Chord>();
	ArrayList<ChoirNote> melody = new ArrayList<ChoirNote>();

	//soprano range is C4 to A5
	ArrayList<ChoirNote> soprano = new ArrayList<ChoirNote>();

	//alto range is F3 to F5
	ArrayList<ChoirNote> alto = new ArrayList<ChoirNote>();

	//tenor range is C3 to C5
	ArrayList<ChoirNote> tenor = new ArrayList<ChoirNote>();

	ArrayList<ChoirNote> bass = new ArrayList<ChoirNote>();
	

	public Harmonizer(String name, ArrayList<ChoirNote> melody, 
					ArrayList<Chord> chordProg, String key)
	{
		this.name = name;
		this.melody = melody;
		this.chordProg = chordProg;
		this.key = key;

		if ((key.equals("C") || key.equals("Am")) ||
			(key.equals("G") || key.equals("Em")) ||
			(key.equals("D") || key.equals("Bm")) ||
			(key.equals("A") || key.equals("F#m")) ||
			(key.equals("E") || key.equals("C#m")) ||
			(key.equals("B") || key.equals("G#m")) ||
			(key.equals("F#") || key.equals("D#m")) ||
			(key.equals("C#") || key.equals("A#m")))
		{
			keyChromatic = chromaticWithSharps;
		}
		else
		{
			keyChromatic = chromaticWithFlats;
		}
	}

	public void genHomophonic()
	{
		this.genSop();
		this.genBassHomophonic();
		this.genMiddleVoicesHomophonic();
		//this.finalizeAllParts();
	}

	private void genSop()
	{
		soprano = melody;
	}

	private void genBass()
	{
		//bass range is E2 to E4
		int botOfRange = Arrays.asList(keyChromatic).indexOf("E2");
		int topOfRange = Arrays.asList(keyChromatic).indexOf("E4");
		int lastNoteInd = 0;
		String[] bassRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);

		for (int i = 0; i < chordProg.size(); i++)
		{
			Chord temp_chord = chordProg.get(i);
			String note = temp_chord.bassNote.toString();

			//TODO: handle jumps over M6 (9 indexes)
			for (int j = 0; j < bassRange.length; j++)
			{
				String cur = bassRange[j];
				String curChromNote;
				int curOctave;

				if (cur.charAt(1) == '#' || cur.charAt(1) == 'b') 
				{
					curChromNote = (cur.charAt(0) + "" + cur.charAt(1));
					curOctave = Integer.parseInt(cur.charAt(2) + "");
				}
				else
				{
					curChromNote = cur.charAt(0) + "";
					curOctave = Integer.parseInt(cur.charAt(1) + "");
				}

				if (note.equals(curChromNote))
				{
					bass.add(new ChoirNote(note, temp_chord.duration, curOctave));
					lastNoteInd = j;
					break;
				}
			}
		}
	}


	private void genBassHomophonic()
	{
		//bass range is E2 to E4
		int botOfRange = Arrays.asList(keyChromatic).indexOf("E2");
		int topOfRange = Arrays.asList(keyChromatic).indexOf("E4");
		int lastNoteInd = 0;
		int melodyInd = 0;
		String[] bassRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);

		//go through chord progression
		for (int i = 0; i < chordProg.size(); i++)
		{
			//get bass note of chord
			Chord temp_chord = chordProg.get(i);
			String note = temp_chord.bassNote.toString();

			//TODO: handle jumps over M6 (9 indexes)
			//find suitable bass note
			for (int j = 0; j < bassRange.length; j++)
			{
				String cur = bassRange[j];
				String curChromNote;
				int curOctave;

				if (cur.charAt(1) == '#' || cur.charAt(1) == 'b') 
				{
					curChromNote = (cur.charAt(0) + "" + cur.charAt(1));
					curOctave = Integer.parseInt(cur.charAt(2) + "");
				}
				else
				{
					curChromNote = cur.charAt(0) + "";
					curOctave = Integer.parseInt(cur.charAt(1) + "");
				}

				//check that suitable note is the same as bass note
				if (note.equals(curChromNote))
				{
					int tempCounts = 0;
					while (tempCounts < temp_chord.duration)
					{
						//get soprano note for homophonic rhythm
						ChoirNote tempMelodyNote = soprano.get(melodyInd);
						tempCounts += tempMelodyNote.duration;
						melodyInd++;

						//check if melody has a rest
						if (tempMelodyNote.note.equals("X"))
						{
							bass.add(new ChoirNote("X", 0, tempMelodyNote.duration));
						}
						else
						{
							bass.add(new ChoirNote(note, curOctave, tempMelodyNote.duration));
						}
						lastNoteInd = j;
					}
					break;
				}
			}
		}
	}


	private void genMiddleVoicesHomophonic()
	{
		//tenor range is C3 to C5
		int botOfRange = Arrays.asList(keyChromatic).indexOf("C3");
		int topOfRange = Arrays.asList(keyChromatic).indexOf("C5");
		int tenorLastNoteInd = 0;
		String[] tenorRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);	

		//alto range is F3 to F5
		botOfRange = Arrays.asList(keyChromatic).indexOf("F3");
		topOfRange = Arrays.asList(keyChromatic).indexOf("F5");
		int altoLastNoteInd = 0;
		String[] altoRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);

		//for iterating through melody to find melody within chords
		int melodyInd = 0;

		//for iterating through melody to fill in empty voices
		int melodyInd2 = 0;

		//iterate through each chord
		for (int i = 0; i < chordProg.size(); i++)
		{
			Chord temp_chord = chordProg.get(i);
			//String root = temp_chord.root.toString();
			//String third = temp_chord.third.toString();
			//String fifth = temp_chord.fifth.toString();
			//System.out.println("Chord: " + temp_chord.chord + " " + temp_chord.duration);

			//get melody notes within current chord's duration
			ArrayList<ChoirNote> chordMelody = new ArrayList<ChoirNote>();
			int duration = temp_chord.duration;
			int tempCounts = 0;
			while (tempCounts < duration)
			{
				ChoirNote tempMelodyNote = melody.get(melodyInd);
				tempCounts += tempMelodyNote.duration;
				melodyInd++;
				chordMelody.add(tempMelodyNote);
			}

			//find missing notes in current chord
			for (int j = 0; j < chordMelody.size(); j++)
			{
				if (bass.get(melodyInd2).note.toString().equals("X") && 
					soprano.get(melodyInd2).note.toString().equals("X"))
				{
					tenor.add(new ChoirNote("X", 0, chordMelody.get(j).duration));
					alto.add(new ChoirNote("X", 0, chordMelody.get(j).duration));
					melodyInd2++;
				}
				else
				{
					ArrayList<String> missingNotes = getMissingNotes(bass.get(melodyInd2).note.toString(), 
																	soprano.get(melodyInd2).note.toString(), 
																	temp_chord);
					/*System.out.print("Missing notes: ");
					for (int l = 0; l < missingNotes.size(); l++)
					{
						System.out.print(missingNotes.get(l) + "\t");
					}
					System.out.println();*/

					//fill in missing notes with middle voices
					if (missingNotes.size() > 1)
					{
						boolean tenorSet = false;
						boolean altoSet = false;

						for (int k = 0; k < missingNotes.size(); k++)
						{
							String missingNote = missingNotes.get(k);

							if (!tenorSet)
							{
								for (int l = 0; l < tenorRange.length; l++)
								{
									String cur = tenorRange[l];
									String curChromNote;
									int curOctave;

									if (cur.charAt(1) == '#' || cur.charAt(1) == 'b') 
									{
										curChromNote = (cur.charAt(0) + "" + cur.charAt(1));
										curOctave = Integer.parseInt(cur.charAt(2) + "");
									}
									else
									{
										curChromNote = cur.charAt(0) + "";
										curOctave = Integer.parseInt(cur.charAt(1) + "");
									}

									if (missingNote.equals(curChromNote))
									{
											tenor.add(new ChoirNote(missingNote, curOctave, chordMelody.get(j).duration));
											//lastNoteInd = k;
											tenorSet = true;
											break;
									}
								}
							}
							else if (!altoSet)
							{
									for (int l = 0; l < altoRange.length; l++)
									{
										String cur = altoRange[l];
										String curChromNote;
										int curOctave;

										if (cur.charAt(1) == '#' || cur.charAt(1) == 'b') 
										{
											curChromNote = (cur.charAt(0) + "" + cur.charAt(1));
											curOctave = Integer.parseInt(cur.charAt(2) + "");
										}
										else
										{
											curChromNote = cur.charAt(0) + "";
											curOctave = Integer.parseInt(cur.charAt(1) + "");
										}

										if (missingNote.equals(curChromNote))
										{
												alto.add(new ChoirNote(missingNote, curOctave, chordMelody.get(j).duration));
												//lastNoteInd = l;
												altoSet = true;
												break;
										}
									}
							}
							else
							{
								break;
							}
						}
					}
					else
					{
						String missingNote = missingNotes.get(0);

						//TODO:
						//- handle jumps
						//- check if alto is in same octave as soprano
						//- check if alto is lower than soprano
						//- check if tenor is in same octave as alto
						//- check if tenor is lower than alto
						for (int k = 0; k < tenorRange.length; k++)
						{
							String cur = tenorRange[k];
							String curChromNote;
							int curOctave;

							if (cur.charAt(1) == '#' || cur.charAt(1) == 'b') 
							{
								curChromNote = (cur.charAt(0) + "" + cur.charAt(1));
								curOctave = Integer.parseInt(cur.charAt(2) + "");
							}
							else
							{
								curChromNote = cur.charAt(0) + "";
								curOctave = Integer.parseInt(cur.charAt(1) + "");
							}

							if (missingNote.equals(curChromNote))
							{
									tenor.add(new ChoirNote(missingNote, curOctave, chordMelody.get(j).duration));
									//lastNoteInd = k;
									break;
							}
						}

						for (int l = 0; l < altoRange.length; l++)
						{
							String cur = altoRange[l];
							String curChromNote;
							int curOctave;

							if (cur.charAt(1) == '#' || cur.charAt(1) == 'b') 
							{
								curChromNote = (cur.charAt(0) + "" + cur.charAt(1));
								curOctave = Integer.parseInt(cur.charAt(2) + "");
							}
							else
							{
								curChromNote = cur.charAt(0) + "";
								curOctave = Integer.parseInt(cur.charAt(1) + "");
							}

							if (missingNote.equals(curChromNote))
							{
									alto.add(new ChoirNote(missingNote, curOctave, chordMelody.get(j).duration));
									//lastNoteInd = l;
									break;
							}
						}
					}

					melodyInd2++;
				}
			}
		}
	}

	private ArrayList<String> getMissingNotes(String bassNote, String sopranoNote, Chord chord)
	{
		ArrayList<String> missingNotes = new ArrayList<String>();
		String root = chord.root.toString();
		String third = chord.third.toString();
		String fifth = chord.fifth.toString();

		if (!(bassNote.equals(root)))
		{
			if (!(sopranoNote.equals(root)))
			{
				missingNotes.add(root);
			}
		}
		if (!(bassNote.equals(third)))
		{
			if (!(sopranoNote.equals(third)))
			{
				missingNotes.add(third);
			}
		}

		if (!(bassNote.equals(fifth)))
		{
			if (!(sopranoNote.equals(fifth)))
			{
				missingNotes.add(fifth);
			}
		}

		return missingNotes;
	}

	private void finalizeAllParts()
	{

	}

	/*public void genTenor()
	{
		//tenor range is C3 to C5
		int botOfRange = Arrays.asList(keyChromatic).indexOf("C3");
		int topOfRange = Arrays.asList(keyChromatic).indexOf("C5");
		int lastNoteInd = 0;
		String[] tenorRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);	
	}

	public void genAlto()
	{
		//alto range is F3 to F5
		int botOfRange = Arrays.asList(keyChromatic).indexOf("F3");
		int topOfRange = Arrays.asList(keyChromatic).indexOf("F5");
		int lastNoteInd = 0;
		String[] alto = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);	
	}*/
}


/* Maybe implement this later?

public class TimeSignature
{
	int beatsPerBar;
	int typeOfNote;

	public TimeSignature(int beatsPerBar, int typeOfNote)
	{
		this.beatsPerBar = beatsPerBar;
		this.typeOfNote = typeOfNote;
	}
}
*/