import java.util.*;
import java.io.*;
import java.lang.*;

/*
	Twinkle Twinkle Little Star
	Time Signature: 4/4
	Key Signature: G
	Melody: (G4 1) (G4 1) (D5 1) (D5 1) (E5 1) (E5 1) (D5 2) (C5 1) (C5 1) (B4 1) (B4 1) (A4 1) (A4 1) (G4 2)
	Chord Progression: 	(G 4) (C 2) (G 2) (Am 2) (G 2) (D 2) (G 2)
*/

/*
	https://stackoverflow.com/questions/3850688/reading-midi-files-in-java
	http://automatic-pilot.com/midifile.html
	https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/spi/MidiFileReader.html
	https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/Sequence.html
	https://www.programcreek.com/java-api-examples/?class=javax.sound.midi.Sequence&method=PPQ
	https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/Track.html
	https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/MidiEvent.html
	https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/MidiMessage.html
	https://docs.oracle.com/javase/tutorial/sound/SPI-providing-MIDI.html
*/

public class Harmonizer
{
	String name;
	String key;

	/* implement later
	TimeSignature timeSignature;
	*/

	//use indexOf to check for octave rules

	ChoirNote[] chromaticWithSharps = new ChoirNote[]
	{new ChoirNote("E", 2, 0), new ChoirNote("F", 2, 0), new ChoirNote("F#", 2, 0), new ChoirNote("G", 2, 0),
	new ChoirNote("G#", 2, 0), new ChoirNote("A", 2, 0), new ChoirNote("A#", 2, 0), new ChoirNote("B", 2, 0),
	new ChoirNote("C", 3, 0), new ChoirNote("C#", 3, 0), new ChoirNote("D", 3, 0), new ChoirNote("D#", 3, 0),
	new ChoirNote("E", 3, 0), new ChoirNote("F", 3, 0), new ChoirNote("F#", 3, 0), new ChoirNote("G", 3, 0),
	new ChoirNote("G#", 3, 0), new ChoirNote("A", 3, 0), new ChoirNote("A#", 3, 0), new ChoirNote("B", 3, 0),
	new ChoirNote("C", 4, 0), new ChoirNote("C#", 4, 0), new ChoirNote("D", 4, 0), new ChoirNote("D#", 4, 0),
	new ChoirNote("E", 4, 0), new ChoirNote("F", 4, 0), new ChoirNote("F#", 4, 0), new ChoirNote("G", 4, 0),
	new ChoirNote("G#", 4, 0), new ChoirNote("A", 4, 0), new ChoirNote("A#", 4, 0), new ChoirNote("B", 4, 0),
	new ChoirNote("C", 5, 0), new ChoirNote("C#", 5, 0), new ChoirNote("D", 5, 0), new ChoirNote("D#", 5, 0),
	new ChoirNote("E", 5, 0), new ChoirNote("F", 5, 0), new ChoirNote("F#", 5, 0), new ChoirNote("G", 5, 0),
	new ChoirNote("G#", 5, 0), new ChoirNote("A", 5, 0)};

	ChoirNote[] chromaticWithFlats = new ChoirNote[]
	{new ChoirNote("E", 2, 0), new ChoirNote("F", 2, 0), new ChoirNote("Gb", 2, 0), new ChoirNote("G", 2, 0),
	new ChoirNote("Ab", 2, 0), new ChoirNote("A", 2, 0), new ChoirNote("Bb", 2, 0), new ChoirNote("B", 2, 0),
	new ChoirNote("C", 3, 0), new ChoirNote("Db", 3, 0), new ChoirNote("D", 3, 0), new ChoirNote("Eb", 3, 0),
	new ChoirNote("E", 3, 0), new ChoirNote("F", 3, 0), new ChoirNote("Gb", 3, 0), new ChoirNote("G", 3, 0),
	new ChoirNote("Ab", 3, 0), new ChoirNote("A", 3, 0), new ChoirNote("Bb", 3, 0), new ChoirNote("B", 3, 0),
	new ChoirNote("C", 4, 0), new ChoirNote("Db", 4, 0), new ChoirNote("D", 4, 0), new ChoirNote("Eb", 4, 0),
	new ChoirNote("E", 4, 0), new ChoirNote("F", 4, 0), new ChoirNote("Gb", 4, 0), new ChoirNote("G", 4, 0),
	new ChoirNote("Ab", 4, 0), new ChoirNote("A", 4, 0), new ChoirNote("Bb", 4, 0), new ChoirNote("B", 4, 0),
	new ChoirNote("C", 5, 0), new ChoirNote("Db", 5, 0), new ChoirNote("D", 5, 0), new ChoirNote("Eb", 5, 0),
	new ChoirNote("E", 5, 0), new ChoirNote("F", 5, 0), new ChoirNote("Gb", 5, 0), new ChoirNote("G", 5, 0),
	new ChoirNote("Ab", 5, 0), new ChoirNote("A", 5, 0)};

	ChoirNote[] keyChromatic;

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

	/*
		==================================================================================================================================
		========================================================HOMOPHONIC================================================================
		==================================================================================================================================
	*/

	public void genHomophonic()
	{
		this.genSop();
		this.genBassHomophonic();
		this.genMiddleVoicesHomophonic();
		this.enforceRulesHomophonic();
	}

	private void genSop()
	{
		soprano = melody;
	}

	private void genBassHomophonic()
	{
		//bass range is E2 to E4
		int botOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("E", 2, 0));
		int topOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("E", 4, 0));
		int lastNoteInd = 0;
		int melodyInd = 0;
		ChoirNote[] bassRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);

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
				ChoirNote cur = bassRange[j];
				String curChromNote;
				int curOctave;

				//check that suitable note is the same as bass note
				if (note.equals(cur.note))
				{
					float tempCounts = 0;
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
							bass.add(new ChoirNote(cur.note, cur.octave, tempMelodyNote.duration));
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
		int botOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("C", 3, 0));
		int topOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("C", 5, 0));
		int tenorLastNoteInd = 0;
		ChoirNote[] tenorRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);	

		//alto range is F3 to F5
		botOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("F", 3, 0));
		topOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("F", 5, 0));
		int altoLastNoteInd = 0;
		ChoirNote[] altoRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);

		//for iterating through melody to find melody within chords
		int melodyInd = 0;

		//for iterating through melody to fill in empty voices
		int melodyInd2 = 0;

		//iterate through each chord
		for (int i = 0; i < chordProg.size(); i++)
		{
			Chord temp_chord = chordProg.get(i);

			//get melody notes within current chord's duration
			ArrayList<ChoirNote> chordMelody = new ArrayList<ChoirNote>();
			float duration = temp_chord.duration;
			float tempCounts = 0;
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
									ChoirNote cur = tenorRange[l];

									if (missingNote.equals(cur.note))
									{
											tenor.add(new ChoirNote(missingNote, cur.octave, chordMelody.get(j).duration));
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
										ChoirNote cur = altoRange[l];

										if (missingNote.equals(cur.note))
										{
												alto.add(new ChoirNote(missingNote, cur.octave, chordMelody.get(j).duration));
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
						for (int k = 0; k < tenorRange.length; k++)
						{
							ChoirNote cur = tenorRange[k];

							if (missingNote.equals(cur.note))
							{
									tenor.add(new ChoirNote(missingNote, cur.octave, chordMelody.get(j).duration));
									//lastNoteInd = k;
									break;
							}
						}

						for (int l = 0; l < altoRange.length; l++)
						{
							ChoirNote cur = altoRange[l];

							if (bass.get(melodyInd2).note.equals(cur.note))
							{
								//doubles root
								alto.add(new ChoirNote(bass.get(melodyInd2).note, cur.octave, chordMelody.get(j).duration));
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

	private void enforceRulesHomophonic()
	{
		this.enforceOctaveRuleHomophonic();
		//TODO:
		//- handle jumps
		//- check if alto is in same octave as soprano
		//- check if alto is lower than soprano
		//- check if tenor is in same octave as alto
		//- check if tenor is lower than alto
		//- check if tenor is higher than bass
	}

	private void enforceOctaveRuleHomophonic()
	{
		for (int i = 0; i < melody.size(); i++)
		{	
			float duration = soprano.get(i).duration;
			
			ChoirNote sNote = new ChoirNote(soprano.get(i).note, soprano.get(i).octave, 0);
			int sNoteInd = Arrays.asList(keyChromatic).indexOf(sNote);

			ChoirNote aNote = new ChoirNote(alto.get(i).note, alto.get(i).octave, 0);
			int aNoteInd = Arrays.asList(keyChromatic).indexOf(aNote);

			ChoirNote tNote = new ChoirNote(tenor.get(i).note, tenor.get(i).octave, 0);
			int tNoteInd = Arrays.asList(keyChromatic).indexOf(tNote);
			
			ChoirNote bNote = new ChoirNote(bass.get(i).note, bass.get(i).octave, 0);
			int bNoteInd = Arrays.asList(keyChromatic).indexOf(bNote);

			//check if alto is the correct octave
			if (Math.abs(sNoteInd - aNoteInd) > 13)
			{
				alto.set(i, (new ChoirNote(alto.get(i).note, alto.get(i).octave + 1, duration)));
				aNoteInd = Arrays.asList(keyChromatic).indexOf(new ChoirNote(alto.get(i).note, alto.get(i).octave, 0));
			}

			if (Math.abs(aNoteInd - tNoteInd) > 13)
			{
				tenor.set(i, (new ChoirNote(tenor.get(i).note, tenor.get(i).octave + 1, duration)));
			}
		}
	}


	/*
		==================================================================================================================================
		=====================================================NONHOMOPHONIC================================================================
		==================================================================================================================================
	*/

	public void genNonhomophonic()
	{
		this.genSop();
		this.genBass();
		this.genMiddleVoices();
	}

	private void genBass()
	{
		//bass range is E2 to E4
		int botOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("E", 2, 0));
		int topOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("E", 4, 0));
		int lastNoteInd = 0;
		ChoirNote[] bassRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);

		for (int i = 0; i < chordProg.size(); i++)
		{
			Chord temp_chord = chordProg.get(i);
			String note = temp_chord.bassNote.toString();

			//TODO: handle jumps over M6 (9 indexes)
			for (int j = 0; j < bassRange.length; j++)
			{
				ChoirNote cur = bassRange[j];
				String curChromNote;
				int curOctave;

				if (note.equals(cur.note))
				{
					bass.add(new ChoirNote(cur.note, cur.octave, temp_chord.duration));
					lastNoteInd = j;
					break;
				}
			}
		}
	}

	private void genMiddleVoices()
	{
		//tenor range is C3 to C5
		int botOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("C", 3, 0));
		int topOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("C", 5, 0));
		int tenorLastNoteInd = 0;
		ChoirNote[] tenorRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);	

		//alto range is F3 to F5
		botOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("F", 3, 0));
		topOfRange = Arrays.asList(keyChromatic).indexOf(new ChoirNote("F", 5, 0));
		int altoLastNoteInd = 0;
		ChoirNote[] altoRange = Arrays.copyOfRange(keyChromatic, botOfRange, topOfRange);

		int melodyInd = 0;

		//iterate through each chord
		for (int i = 0; i < chordProg.size(); i++)
		{
			Chord temp_chord = chordProg.get(i);

			//TODO: what if melody is rest?
			//get first melody note in current chord
			float duration = temp_chord.duration;
			float tempCounts = 0;
			while (tempCounts < duration && melodyInd < melody.size() - 1)
			{
				tempCounts += melody.get(melodyInd).duration;
				melodyInd++;
			}

			//find missing notes in current chord
			ArrayList<String> missingNotes = getMissingNotes(bass.get(i).note.toString(), 
															soprano.get(melodyInd).note.toString(), 
															temp_chord);

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
							ChoirNote cur = tenorRange[l];

							if (missingNote.equals(cur.note))
							{
									tenor.add(new ChoirNote(missingNote, cur.octave, temp_chord.duration));
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
								ChoirNote cur = altoRange[l];

								if (missingNote.equals(cur.note))
								{
										alto.add(new ChoirNote(missingNote, cur.octave, temp_chord.duration));
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
				for (int k = 0; k < tenorRange.length; k++)
				{
					ChoirNote cur = tenorRange[k];

					if (missingNote.equals(cur.note))
					{
							tenor.add(new ChoirNote(missingNote, cur.octave, temp_chord.duration));
							//lastNoteInd = k;
							break;
					}
				}

				for (int l = 0; l < altoRange.length; l++)
				{
					ChoirNote cur = altoRange[l];

					if (bass.get(i).note.equals(cur.note))
					{
							alto.add(new ChoirNote(bass.get(i).note, cur.octave, temp_chord.duration));
							//lastNoteInd = l;
							break;
					}
				}
			}
		}
	}

	private void enforceRules()
	{
		this.enforceOctaveRule();
		//TODO:
		//- handle jumps
		//- check if alto is in same octave as soprano
		//- check if alto is lower than soprano
		//- check if tenor is in same octave as alto
		//- check if tenor is lower than alto
		//- check if tenor is higher than bass
	}


	/*
		==================================================================================================================================
		============================================================HELPER================================================================
		==================================================================================================================================
	*/	
	private ArrayList<String> getMissingNotes(String bassNote, String sopranoNote, Chord chord)
	{
		ArrayList<String> missingNotes = new ArrayList<String>();
		String root = chord.root.toString();
		String third = chord.third.toString();
		String fifth = chord.fifth.toString();

		if (sopranoNote.equals("X"))
		{
			if (!(bassNote.equals(root)))
			{
				missingNotes.add(root);
			}
			if (!(bassNote.equals(third)))
			{
				missingNotes.add(third);
			}

			if (!(bassNote.equals(fifth)))
			{
				missingNotes.add(fifth);
			}
		}
		else
		{
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
		}

		return missingNotes;
	}
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