/*
(E4 2) (D4 2) (C4 4) (E4 2) (D4 2) (C4 4)
(C 2) (G 2) (C 4) (C 2) (G 2) (C 4)
*/

import java.io.*;
import java.util.*;
import org.jfugue.player.*;
import org.jfugue.midi.*;
import org.jfugue.pattern.*;

public class Generator
{
	public Generator(Harmonizer h, String midiTS, String midiKS)
  	{
  		MidiFileManager mfm = new MidiFileManager();
  		File output = new File(h.name + ".mid");
  		Player player = new Player();

  		Pattern allParts = new Pattern();
  		String sopPat = genPattern(h.soprano);
  		String altoPat = genPattern(h.alto);
  		String tenPat = genPattern(h.tenor);
  		String bassPat = genPattern(h.bass);

  		allParts.add(midiTS);
  		//allParts.add("KEY:Gmaj");
  		allParts.add("V0 " + sopPat + " | V1 " + altoPat + " | V2 " + tenPat + " | V3 " + bassPat);
  		System.out.println(allParts);
  		try 
  		{
  			mfm.savePatternToMidi(allParts, output);
  		}
  		catch (Exception e)
  		{
  			System.out.println("Exception: " + e);
  		}
	}

	private static String genPattern(ArrayList<ChoirNote> part)
	{
		//Pattern retPart;
		String strPart = "";

		for (int i = 0; i < part.size(); i++)
  		{
  			ChoirNote cur = part.get(i);

  			//rests are now 'R'
  			if (cur.note.equals("X"))
  			{
  				strPart += "R\\";
  				System.out.println("REST");
  			}
  			else
  			{
  				strPart += cur.note;
  				strPart += (cur.octave + 1);
  			}
  			
  			if (cur.duration == 0.0625) //64th
  			{
  				strPart += "x";
  			}
  			else if (cur.duration == 0.09375) //dotted 64th
  			{
  				strPart += "x.";
  			}
  			else if (cur.duration == 0.125) //32nd
  			{
  				strPart += "t";
  			}
  			else if (cur.duration == 0.1875) //dotted 32nd
  			{
  				strPart += "t.";
  			}
  			else if (cur.duration == 0.25) //16th
  			{
  				strPart += "s";
  			}
  			else if (cur.duration == 0.375) //dotted 16th
  			{
  				strPart += "s.";
  			}
  			else if (cur.duration == 0.5) //8th
  			{ 				
  				strPart += "i";
  			}
  			else if (cur.duration == 0.75) //dotted 8th
  			{
  				strPart += "i.";
  			}
  			else if (cur.duration == 1.0) //quarter
  			{
  				strPart += "q";
  			}	
  			else if (cur.duration == 1.5) //dotted quarter
  			{
  				strPart += "q.";
  			}
  			else if (cur.duration == 2) //half
  			{
  				strPart += "h";
  			}
  			else if (cur.duration == 3.0) //dotted half
  			{
  				strPart += "h.";
  			}
  			else if (cur.duration == 4.0) //whole
  			{
  				strPart += "w";
  			}
  			else if (cur.duration == 6.0) //dotted whole
  			{
  				strPart += "w.";
  			}

  			strPart += " ";
  		}

  		return strPart;
	}
}