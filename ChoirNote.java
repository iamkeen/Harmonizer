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
}