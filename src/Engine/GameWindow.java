package Engine;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

/*
 * The JFrame that holds the GamePanel
 * Just does some setup and exposes the gamePanel's screenManager to allow an external class to setup their own content and attach it to this engine.
 */
public class GameWindow {
	private JFrame gameWindow;
	private GamePanel gamePanel;
	private static Clip music;
	
	public GameWindow() {
		gameWindow = new JFrame("Game");
		gamePanel = new GamePanel();
		//creates sound holder
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		gameWindow.setContentPane(gamePanel);
		gameWindow.setResizable(false);
		gameWindow.setSize(Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT);
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // it'd be nice if this actually worked more than 1/3rd of the time
		gamePanel.setupGame();
		
		File soundFile = new File("Resources/backgroundMusic.wav");
		
		try {
			AudioInputStream gameSound = AudioSystem.getAudioInputStream(soundFile);
			music = AudioSystem.getClip();
			music.open(gameSound);
			//adds conditional if SoundHolder is true play music 
			if(GamePanel.sound.getSoundHolder()) {
				music.start();
			}
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("no sound");
		} catch (LineUnavailableException e) {
			System.out.println("no sound");
		}
	}

	public static void updateMusic() {
		if(GamePanel.sound.getSoundHolder()) {
			music.start();
		}else {
			music.stop();
		}
	}
	// triggers the game loop to start as defined in the GamePanel class
	public void startGame() {
		gamePanel.startGame();
	}

	public ScreenManager getScreenManager() {
		return gamePanel.getScreenManager();
	}
}
