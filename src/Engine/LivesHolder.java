package Engine;

//class keeping track of how many lives the player has
public class LivesHolder {
	private int lives;
	
	public LivesHolder() {
		lives = 3;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void subtractLife() {
		lives --;
	}
	
	public void setLives() {
		lives = 3;
	}
}
