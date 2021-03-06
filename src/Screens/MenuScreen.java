package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

import java.awt.*;

// This is the class for the main menu screen
public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont instructions;
    protected SpriteFont levelselect;
    protected Map background;
    protected Stopwatch keyTimer = new Stopwatch();
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        playGame = new SpriteFont("PLAY GAME", 200, 150, "Comic Sans", 30, new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);
        credits = new SpriteFont("CREDITS", 200, 250, "Comic Sans", 30, new Color(49, 207, 240));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);
        instructions = new SpriteFont("INSTRUCTIONS", 200, 200, "Comic Sans", 30, new Color(49, 207, 240));
        instructions.setOutlineColor(Color.black);
        instructions.setOutlineThickness(3);
        levelselect = new SpriteFont("LEVEL SELECT", 200, 300, "Comic Sans", 30, new Color(49, 207, 240));
        levelselect.setOutlineColor(Color.black);
        levelselect.setOutlineThickness(3);
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        keyTimer.setWaitTime(200);
        menuItemSelected = -1;
        keyLocker.lockKey(Key.ENTER);
    }

    public void update() {
        // update background map (to play tile animations)
        background.update(null);

        // if down or up is pressed, change menu item "hovered" over (blue square in front of text will move along with currentMenuItemHovered changing)
        if (Keyboard.isKeyDown(Key.DOWN) && keyTimer.isTimeUp()) {
            keyTimer.reset();
            currentMenuItemHovered++;
        } else if (Keyboard.isKeyDown(Key.UP) && keyTimer.isTimeUp()) {
            keyTimer.reset();
            currentMenuItemHovered--;
        }

        // if down is pressed on last menu item or up is pressed on first menu item, "loop" the selection back around to the beginning/end
        if (currentMenuItemHovered > 3) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 3;
        }

        // sets location for blue square in front of text (pointerLocation) and also sets color of spritefont text based on which menu item is being hovered
        if (currentMenuItemHovered == 0) {
            playGame.setColor(new Color(255, 215, 0));
            credits.setColor(new Color(49, 207, 240));
            instructions.setColor(new Color(49,207,240));
            levelselect.setColor(new Color(49,207,240));
            pointerLocationX = 170;
            pointerLocationY = 130;
        } else if (currentMenuItemHovered == 1) {
            playGame.setColor(new Color(49, 207, 240));
            levelselect.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            instructions.setColor(new Color(255,215,0));
            pointerLocationX = 170;
            pointerLocationY = 180;
        }
         else if (currentMenuItemHovered == 2) {
            playGame.setColor(new Color(49, 207, 240));
            levelselect.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(255, 215, 0));
            instructions.setColor(new Color(49,207,240));
            pointerLocationX = 170;
            pointerLocationY = 230;
        }
         else if (currentMenuItemHovered == 3){
            playGame.setColor(new Color(49, 207, 240));
            credits.setColor(new Color(49, 207, 240));
            levelselect.setColor(new Color(255, 215, 0));
            instructions.setColor(new Color(49,207,240));
            pointerLocationX = 170;
            pointerLocationY = 280;
        }

        // if space is pressed on menu item, change to appropriate screen based on which menu item was chosen
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }
        if (!keyLocker.isKeyLocked(Key.ENTER) && Keyboard.isKeyDown(Key.ENTER)) {
            menuItemSelected = currentMenuItemHovered;
            if (menuItemSelected == 0) {
            	screenCoordinator.setLevel("test_map.txt"); // reset to first level
                screenCoordinator.setGameState(GameState.LEVEL);
            }
            else if(menuItemSelected == 1) {
            	screenCoordinator.setGameState(GameState.INSTRUCTIONS);
            }
            else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.CREDITS);
            } else if(menuItemSelected == 3){
                screenCoordinator.setGameState(GameState.LEVELSELECT);
            }
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        playGame.draw(graphicsHandler);
        credits.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
        levelselect.draw(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }

    public int getMenuItemSelected() {
        return menuItemSelected;
    }
    
    // receive a selection from the mouse input
  	@Override
  	public void selectMenuOption(int option)
  	{
  		if (option >= 0 && option < 4)
  		{
  			currentMenuItemHovered = option;
  		}
  	}
  	
  	// additional unused method
   	@Override
   	public void deselectMenuOption(int option)
   	{
   		// do nothing
   	}
}
