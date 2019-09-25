public class ChoirNote extends Note
{
	int duration;
	int octave;

	public ChoirNote(String note, int octave, int duration)
	{
		super(note);
		this.duration = duration;
		this.octave = octave;
	}
}