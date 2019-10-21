public class Chord
{
	String chord;
	float duration;
	String inversion;
	String type;
	String key;
	//(A for A major)
	//(Am for A minor)
	//(Aaug for A augmented)
	//(Adim for A diminished)
	Note root;
	Note third;
	Note fifth;
	Note bassNote;
	//C: C E G, [0, 4, 7]
	//Cm: C D# G, [0, 3, 7]
	//Caug: C F G#, [0, 5, 8]
	//Cdim: C D# F#, [0, 3, 6]
	String[] keysWithSharps = new String[]{"C", "C#", "D", "D#", "E", "F", "F#", 
											"G", "G#", "A", "A#", "B", "C", "C#", 
											"D", "D#", "E", "F", "F#", "G"};

	String[] keysWithFlats = new String[]{"F", "Gb", "G", "Ab", "A", "Bb", "B",
											"C", "Db", "D", "Eb", "E", "F", "Gb", 
											"G", "Ab", "A", "Bb", "B", "C"};

	String[] keyNotes;
	

	public Chord(String chord, float duration, String key)
	{
		this.chord = chord;
		this.duration = duration;
		this.key = key;

		if (chord.contains("m"))
		{
			this.type = "minor";
		}
		else if (chord.contains("aug"))
		{
			this.type = "augmented";
		}
		else if (chord.contains("dim"))
		{
			this.type = "diminished";
		}
		else
		{
			this.type = "major";
		}

		this.root = new Note(String.valueOf(this.chord.charAt(0)));

		//check if root has accidental
		if (this.chord.length() > 1)
		{
			if (this.chord.charAt(1) == '#' || this.chord.charAt(1) == 'b')
			{
				this.root = new Note(this.chord.charAt(0) + "" + this.chord.charAt(1));
			}
		}

		//set bass note
		if (this.chord.contains("/"))
		{
			int i = this.chord.indexOf("/");
			if (this.chord.charAt(i + 2) == '#' || this.chord.charAt(i + 2) == 'b')
			{
				this.bassNote = new Note(this.chord.charAt(i + 1) + "" + this.chord.charAt(i + 2));
			}
			else
			{
				this.bassNote = new Note(this.chord.charAt(i + 1) + "");
			}
		}
		else
		{
			this.bassNote = this.root;
		}

		if ((key.equals("C") || key.equals("Am")) ||
			(key.equals("G") || key.equals("Em")) ||
			(key.equals("D") || key.equals("Bm")) ||
			(key.equals("A") || key.equals("F#m")) ||
			(key.equals("E") || key.equals("C#m")) ||
			(key.equals("B") || key.equals("G#m")) ||
			(key.equals("F#") || key.equals("D#m")) ||
			(key.equals("C#") || key.equals("A#m")))
		{
			keyNotes = keysWithSharps;
		}
		else
		{
			keyNotes = keysWithFlats;
		}


		//find index of root note
		//used when finding thirds and fifths
		int iOfRoot = 0;
		for (int i = 0; i < keyNotes.length; i++)
		{
			if (this.root.toString().equals(keyNotes[i]))
			{
				iOfRoot = i;
				break;
			}
		}

		if (this.type.equals("major"))
		{
			this.third = new Note(keyNotes[iOfRoot + 4]);
			this.fifth = new Note(keyNotes[iOfRoot + 7]);
		}
		else if (this.type.equals("minor"))
		{
			this.third = new Note(keyNotes[iOfRoot + 3]);
			this.fifth = new Note(keyNotes[iOfRoot + 7]);
		}
		else if (this.type.equals("augmented"))
		{
			this.third = new Note(keyNotes[iOfRoot + 5]);
			this.fifth = new Note(keyNotes[iOfRoot + 8]);
		}
		else if (this.type.equals("diminished"))
		{
			this.third = new Note(keyNotes[iOfRoot + 3]);
			this.fifth = new Note(keyNotes[iOfRoot + 6]);
		}


		if (bassNote.equals(third))
		{
			inversion = "first";
			//double soprano
		}
		else if (bassNote.equals(fifth))
		{
			inversion = "second";
			//double fifth
		}
		else
		{
			inversion = "none";
			//double root
		}
	}
}