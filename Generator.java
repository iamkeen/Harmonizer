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
	public Generator(ChoirNote[] keyChromatic, ArrayList<ChoirNote> soprano, ArrayList<ChoirNote> alto, ArrayList<ChoirNote> tenor, ArrayList<ChoirNote> bass)
  	{
  		MidiFileManager mfm = new MidiFileManager();
  		File output = new File("final.mid");
  		Player player = new Player();

  		Pattern allParts = new Pattern();
  		String sopPat = genPattern(soprano);
  		String altoPat = genPattern(alto);
  		String tenPat = genPattern(tenor);
  		String bassPat = genPattern(bass);

  		//allParts.add("KEY:Gmaj");
  		allParts.add("TIME:6/8");
  		allParts.add("V0 " + sopPat + " | V1 " + altoPat + " | V2 " + tenPat + " | V3 " + bassPat);

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

  			if (cur.note.equals("X"))
  			{
  				strPart += "R";
  			}
  			else
  			{
  				strPart += cur.note;
  				strPart += (cur.octave + 1);
  			}
  			
  			if (cur.duration == 0.25)
  			{
  				strPart += "s";
  			}
  			else if (cur.duration == 0.5) 
  			{ 				
  				strPart += "i";
  			}
  			else if (cur.duration == 0.75)
  			{
  				strPart += "i.";
  			}
  			else if (cur.duration == 1.0)
  			{
  				strPart += "q";
  			}	
  			else if (cur.duration == 1.5)
  			{
  				strPart += "q.";
  			}
  			else if (cur.duration == 2)
  			{
  				strPart += "h";
  			}
  			else if (cur.duration == 3.0)
  			{
  				strPart += "h.";
  			}
  			else if (cur.duration == 4.0)
  			{
  				strPart += "w";
  			}

  			strPart += " ";
  		}

  		return strPart;
	}
}