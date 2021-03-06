package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the credits screen
public class InstructionsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont instructionsLabel;
    //protected SpriteFont beginningSentenceLabel;
    protected SpriteFont actualInstructionsLabel;
    protected SpriteFont returnInstructionsLabel;
    protected SpriteFont returnButton;
    
    protected boolean returnButtonSelected;

    public InstructionsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);

        instructionsLabel = new SpriteFont("Instructions", 65, 35, "Times New Roman", 30, Color.black);
       // beginningSentenceLabel = new SpriteFont("The instructions for this game are:", 130, 140, "Times New Roman", 20, Color.white);
        actualInstructionsLabel = new SpriteFont("Goal: reach the ball of yarn at the right of the level \nwithout touching any enemies or falling off the map \n\nControls:\nUse the up arrow key and/or W key to jump \nUse the down arrow key and/or S key to duck"
        		+ "\nUse the left arrow key and/or A key to move left \nUse the right arrow key and/or D key to move right "
            + "\nWalk on the fish to gain an attack power \nUse the spacebar to attack the dog and mouse enemies", 60, 190, "Times New Roman",20, Color.black);
        returnInstructionsLabel = new SpriteFont("Press Enter to return to the menu", 20, 560, "Times New Roman", 30, Color.black);
        returnButton = new SpriteFont("RETURN TO MENU", 500, 560, "Comic Sans", 30, new Color(49, 207, 240));
        returnButton.setOutlineColor(Color.black);
        returnButton.setOutlineThickness(3);
        keyLocker.lockKey(Key.ENTER);
    }

    public void update() {
        background.update(null);
        
        // changing the color of the button when hovered
        if (returnButtonSelected) {
        	returnButton.setColor(new Color(255, 215, 0));
        }
        else {
        	returnButton.setColor(new Color(49,207,240));
        }

        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }

        // if enter is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.ENTER) && Keyboard.isKeyDown(Key.ENTER)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        instructionsLabel.draw(graphicsHandler);
        //beginningSentenceLabel.draw(graphicsHandler);
        actualInstructionsLabel.drawWithParsedNewLines(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        returnButton.draw(graphicsHandler);
    }
    
    // receive a selection from the mouse input
  	@Override
  	public void selectMenuOption(int option)
  	{
  		if (option == 0) {
  			returnButtonSelected = true;
  		}
  	}
  	
  	// remove highlight when the option is not hovered anymore
   	@Override
   	public void deselectMenuOption(int option)
   	{
   		if (option == 0) {
   			returnButtonSelected = false;
   		}
   	}
}
