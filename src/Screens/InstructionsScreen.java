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
    protected SpriteFont beginningSentenceLabel;
    protected SpriteFont actualInstructionsLabel;
    protected SpriteFont returnInstructionsLabel;

    public InstructionsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        instructionsLabel = new SpriteFont("Instructions", 15, 35, "Times New Roman", 30, Color.white);
        beginningSentenceLabel = new SpriteFont("The instructions for this game are:", 130, 140, "Times New Roman", 20, Color.white);
        actualInstructionsLabel = new SpriteFont("Enter is for menu selection \nThe Up arrow key and/or W is for Jumping \nThe Down arrow key and/or S is for Ducking"
        		+ "\nThe Left arrow key and/or A is for moving left \nThe Right arrow key and/or D is for moving right "
            + "\nSpace is used for attack", 60, 220, "Times New Roman",20, Color.white);
        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 560, "Times New Roman", 30, Color.white);
        keyLocker.lockKey(Key.ENTER);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.ENTER) && Keyboard.isKeyDown(Key.ENTER)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        instructionsLabel.draw(graphicsHandler);
        beginningSentenceLabel.draw(graphicsHandler);
        actualInstructionsLabel.drawWithParsedNewLines(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
    }
}
