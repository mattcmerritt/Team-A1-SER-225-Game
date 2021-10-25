package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import java.awt.*;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont creditsLabel;
    protected SpriteFont createdByLabel;
    protected SpriteFont contributorsLabel;
    protected SpriteFont returnInstructionsLabel;
    protected SpriteFont musicContributorLabel, soundEffectLabel;
    protected SpriteFont returnbutton;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);

        returnInstructionsLabel = new SpriteFont("Click to return to menu", 20, 560, "Times New Roman", 30, Color.white);

        creditsLabel = new SpriteFont("Credits", 15, 35, "Times New Roman", 30, Color.black);
        createdByLabel = new SpriteFont("Created by Alex Thimineur for Quinnipiac's SER225 Course.", 125, 110, "Times New Roman", 20, Color.black);
        contributorsLabel = new SpriteFont("Thank you to QU Alumni Brian Carducci, Joseph White,\nand Alex Hutman for their contributions.", 125, 230, "Times New Roman",20, Color.black);
        musicContributorLabel = new SpriteFont("Music: Guitalele's Happy Place by Stefan Kartenberg (c) copyright 2017 \nLicensed under a Creative Commons Attribution (3.0) license. \nhttp://dig.ccmixter.org/files/JeffSpeed68/56194 Ft: Kara Square (mindmapthat)", 125, 140, "Times New Roman", 20, Color.black);
        soundEffectLabel = new SpriteFont ("Sound Effects: Bubble Sound by Mike Koenig", 125, 300, "Times New Roman", 20, Color.black);
        returnInstructionsLabel = new SpriteFont("Press Enter to return to the menu", 20, 560, "Times New Roman", 30, Color.black);
        returnbutton = new SpriteFont("Click to return to menu", 500, 560, "Times New Roman", 30, Color.black);
        keyLocker.lockKey(Key.ENTER);
    }

    public void update() {
        background.update(null);

        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }

        // if ENTER is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.ENTER) && Keyboard.isKeyDown(Key.ENTER)) {
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        creditsLabel.draw(graphicsHandler);
        createdByLabel.draw(graphicsHandler);
        contributorsLabel.drawWithParsedNewLines(graphicsHandler);
        musicContributorLabel.drawWithParsedNewLines(graphicsHandler);
        soundEffectLabel.drawWithParsedNewLines(graphicsHandler);
        returnInstructionsLabel.draw(graphicsHandler);
        returnbutton.draw(graphicsHandler);
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