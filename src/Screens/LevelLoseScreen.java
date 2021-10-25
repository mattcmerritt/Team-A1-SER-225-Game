package Screens;

import Engine.*;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the level lose screen
public class LevelLoseScreen extends Screen {
    protected SpriteFont loseMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;

    public LevelLoseScreen(PlayLevelScreen playLevelScreen) {
        this.playLevelScreen = playLevelScreen;
    }

    @Override
    public void initialize() {
        loseMessage = new SpriteFont("You lose!", 350, 270, "Comic Sans", 30, Color.white);
        instructions = new SpriteFont("Press Enter to try again or Escape to go back to the main menu", 120, 300,"Comic Sans", 20, Color.white);
        keyLocker.lockKey(Key.ENTER);
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // if space is pressed, reset level. if escape is pressed, go back to main menu
        if (Keyboard.isKeyDown(Key.ENTER)) {
            playLevelScreen.resetLevel();
        } else if (Keyboard.isKeyDown(Key.ESC)) {
            playLevelScreen.goBackToMenu();
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        loseMessage.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
    }
    
    // additional unused method
  	@Override
  	public void selectMenuOption(int option)
  	{
  		// do nothing
  	}
  	
  	// additional unused method
   	@Override
   	public void deselectMenuOption(int option)
   	{
   		// do nothing
   	}
}
