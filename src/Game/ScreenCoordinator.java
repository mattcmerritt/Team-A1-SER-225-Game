package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Screens.*;

/*
 * Based on the current game state, this class determines which Screen should be shown
 * There can only be one "currentScreen", although screens can have "nested" screens
 */

public class ScreenCoordinator extends Screen {
	// currently shown Screen
	protected Screen currentScreen = new DefaultScreen();
	protected static String levelname = "test_map.txt";
	// keep track of gameState so ScreenCoordinator knows which Screen to show
	protected static GameState gameState; 
	protected GameState previousGameState;

	public static GameState getGameState() {
		return gameState;
	}

	// Other Screens can set the gameState of this class to force it to change the currentScreen
	public static void setGameState(GameState gameState) { 
		ScreenCoordinator.gameState = gameState;
	}

	@Override
	public void initialize() {
		// start game off with Menu Screen
		gameState = GameState.MENU;
	}

	@Override
	public void update() {
		do {
			// if previousGameState does not equal gameState, it means there was a change in gameState
			// this triggers ScreenCoordinator to bring up a new Screen based on what the gameState is
			if (previousGameState != gameState) {
				switch(gameState) {
					case MENU:
						currentScreen = new MenuScreen(this);
						break;
					case LEVEL:
						currentScreen = new PlayLevelScreen(this, levelname);
						break;
					case CREDITS:
						currentScreen = new CreditsScreen(this);
						break;
					case INSTRUCTIONS:
						currentScreen = new InstructionsScreen(this);
						break;
					case LEVELSELECT:
						currentScreen = new LevelSelectScreen(this);
						break;
				}
				currentScreen.initialize();
			}
			previousGameState = gameState;

			// call the update method for the currentScreen
			currentScreen.update();
		} while (previousGameState != gameState);
	}

	@Override
	public void draw(GraphicsHandler graphicsHandler) {
		// call the draw method for the currentScreen
		currentScreen.draw(graphicsHandler);
	}
	
	// relaying information from screenManager to the actual screen
	@Override
	public void selectMenuOption(int option)
	{
		currentScreen.selectMenuOption(option);
	}
	
	// relaying information from screenManager to the actual screen
	@Override
	public void deselectMenuOption(int option)
	{
		currentScreen.deselectMenuOption(option);
	}
	
	public void loadlevel(String level){
		levelname = level;
		setGameState(GameState.LEVEL);
	}
	
	public static void setLevel(String level) {
		levelname = level;
	}
}
