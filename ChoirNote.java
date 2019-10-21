public class ChoirNote extends Note
{
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
			this.octave == temp.octave)
			//&& this.duration == this.duration)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}