public class ChoirNote extends Note
{
	//TODO: add support for dotted and smaller valued time values
	//ex. 0.5 duration as en eighth note in 4/4
	float duration;
	int octave;

	public ChoirNote(String note, int octave, float duration)
	{
		super(note);
		this.duration = duration;
		this.octave = octave;
	}

	public boolean equals(Object o)
	{
		//if compared to itself
		if (o == this)
		{
			return true;
		}

		//if not a ChoirNote
		if (!(o instanceof ChoirNote))
		{
			return false;
		}

		ChoirNote temp = (ChoirNote)o;

		if (super.note.equals(temp.note) && 
			this.octave == temp.octave && 
			this.duration == this.duration)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}