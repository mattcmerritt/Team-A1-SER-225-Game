package Screens;

import java.awt.Color;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Engine.ScreenManager;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Maps.TestMap;
import Maps.TestMap2;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

// This class is for when the platformer game is actually being played
public class PlayLevelScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected PlayLevelScreenState playLevelScreenState;
    protected Stopwatch screenTimer = new Stopwatch();
    protected LevelClearedScreen levelClearedScreen;
    protected LevelLoseScreen levelLoseScreen;
    private boolean isGamePaused = false;
	private SpriteFont pauseLabel;
	private KeyLocker keyLocker = new KeyLocker();
	private final Key pauseKey = Key.P;
	protected String fileName;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        fileName = "test_map.txt";
    }

    public void initialize() {
        // define/setup map

        this.map = new TestMap(fileName);
        map.reset();
    	

        // setup player
        this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.player.setMap(map);
        this.player.addListener(this);
        this.player.setLocation(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.playLevelScreenState = PlayLevelScreenState.RUNNING;
        
        pauseLabel = new SpriteFont("PAUSE", 365, 280, "Comic Sans", 24, Color.white);
		pauseLabel.setOutlineColor(Color.black);
		pauseLabel.setOutlineThickness(2.0f);
        
    }

    public void update() {
    	if (Keyboard.isKeyDown(pauseKey) && !keyLocker.isKeyLocked(pauseKey)) {
			isGamePaused = !isGamePaused;

			keyLocker.lockKey(pauseKey);
    	}
		
    	if (Keyboard.isKeyUp(pauseKey)) {
    		keyLocker.unlockKey(pauseKey);
    	}
        // based on screen state, perform specific actions
    	if(!isGamePaused) {
        switch (playLevelScreenState) {
            // if level is "running" update player and map to keep game logic for the platformer level going
            case RUNNING:
                player.update();
                map.update(player);
                break;
            // if level has been completed, bring up level cleared screen
            case LEVEL_COMPLETED:
            	if(fileName == "test_map.txt") {
            		fileName = "test_map_2.txt";
            		this.map = new TestMap(fileName);
            		this.initialize();
            	} else if (fileName == "test_map_2.txt") {
            		fileName = "test_map_3.txt";
            		this.map = new TestMap(fileName);
            		this.initialize();
            	} else if (fileName == "test_map_3.txt") {
            		fileName = "test_map_4.txt";
            		this.map = new TestMap(fileName);
            		this.initialize();
            	} else if (fileName == "test_map_4.txt") {
            		fileName = "test_map_5.txt";
            		this.map = new TestMap(fileName);
            		this.initialize();
            	} else {
                levelClearedScreen = new LevelClearedScreen();
                levelClearedScreen.initialize();
                screenTimer.setWaitTime(2500);
                playLevelScreenState = PlayLevelScreenState.LEVEL_WIN_MESSAGE;
            	}
                break;
            // if level cleared screen is up and the timer is up for how long it should stay out, go back to main menu
            case LEVEL_WIN_MESSAGE:
                if (screenTimer.isTimeUp()) {
                    levelClearedScreen = null;
                    goBackToMenu();
                }
                break;
            // if player died in level, bring up level lost screen
            case PLAYER_DEAD:
                levelLoseScreen = new LevelLoseScreen(this);
                levelLoseScreen.initialize();
                playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE_MESSAGE;
                break;
            // wait on level lose screen to make a decision (either resets level or sends player back to main menu)
            case LEVEL_LOSE_MESSAGE:
                levelLoseScreen.update();
                break;
            //case PAUSE:
            	
            	//break;
        }
    	}
    }

    public void draw(GraphicsHandler graphicsHandler) {
    	
        // based on screen state, draw appropriate graphics
    	if(!isGamePaused) {
        switch (playLevelScreenState) {
            case RUNNING:
            case LEVEL_COMPLETED:
            case PLAYER_DEAD:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
                break;
            case LEVEL_WIN_MESSAGE:
                levelClearedScreen.draw(graphicsHandler);
                break;
            case LEVEL_LOSE_MESSAGE:
                levelLoseScreen.draw(graphicsHandler);
                break;
        }
    	}
    	else {
    		pauseLabel.draw(graphicsHandler);
			graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), new Color(0, 0, 0, 0));
    	}
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    @Override
    public void onLevelCompleted() {
        playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
    }

    @Override
    public void onDeath() {
        playLevelScreenState = PlayLevelScreenState.PLAYER_DEAD;
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, PLAYER_DEAD, LEVEL_WIN_MESSAGE, LEVEL_LOSE_MESSAGE
    }
}
