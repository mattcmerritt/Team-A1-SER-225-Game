package Engine;

//class to store whether or not the music should be playing 
public class SoundHolder {
	private Boolean soundHolder;
	
	public SoundHolder() {
		soundHolder = true;
	}
	
	public boolean getSoundHolder() {
		return soundHolder;
	}
	
	public void setSoundHolder(boolean sound) {
		soundHolder = sound;
		//call to stop background music
		GameWindow.updateMusic();
	}
}
