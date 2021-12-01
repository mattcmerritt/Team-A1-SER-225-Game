package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import Utils.Stopwatch;

import java.awt.*;
import Engine.Screen;

public class LevelSelectScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont Level1;
    protected SpriteFont Level2;
    protected SpriteFont Level3;
    protected SpriteFont Level4;
    protected SpriteFont Level5;
    protected SpriteFont Level6;
    protected SpriteFont Level7;
    protected SpriteFont returnButton;
    protected Map background;
    protected Stopwatch keyTimer = new Stopwatch();
    protected int pointerLocationX, pointerLocationY;
    protected KeyLocker keyLocker = new KeyLocker();
    protected SpriteFont titleLabel;

    public LevelSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }


    public void initialize() {
    	titleLabel = new SpriteFont("Level Select", 65, 35, "Times New Roman", 30, Color.black);
    	
        Level1 = new SpriteFont("Level 1", 40, 100, "Comic Sans", 30, new Color(49, 207, 240));
        Level1.setOutlineColor(Color.black);
        Level1.setOutlineThickness(3);

        Level2 = new SpriteFont("Level 2", 40, 200, "Comic Sans", 30, new Color(49, 207, 240));
        Level2.setOutlineColor(Color.black);
        Level2.setOutlineThickness(3);

        Level3 = new SpriteFont("Level 3", 40, 300, "Comic Sans", 30, new Color(49, 207, 240));
        Level3.setOutlineColor(Color.black);
        Level3.setOutlineThickness(3);

        Level4 = new SpriteFont("Level 4", 40, 400, "Comic Sans", 30, new Color(49, 207, 240));
        Level4.setOutlineColor(Color.black);
        Level4.setOutlineThickness(3);

        Level5 = new SpriteFont("Level 5", 200, 100, "Comic Sans", 30, new Color(49, 207, 240));
        Level5.setOutlineColor(Color.black);
        Level5.setOutlineThickness(3);

        Level6 = new SpriteFont("Level 6", 200, 200, "Comic Sans", 30, new Color(49, 207, 240));
        Level6.setOutlineColor(Color.black);
        Level6.setOutlineThickness(3);

        Level7 = new SpriteFont("Level 7", 200, 300, "Comic Sans", 30, new Color(49, 207, 240));
        Level7.setOutlineColor(Color.black);
        Level7.setOutlineThickness(3);
        
        returnButton = new SpriteFont("RETURN TO MENU", 500, 560, "Comic Sans", 30, new Color(49, 207, 240));
        returnButton.setOutlineColor(Color.black);
        returnButton.setOutlineThickness(3);

        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        keyTimer.setWaitTime(200);
        menuItemSelected = -1;
        keyLocker.lockKey(Key.ENTER);
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        titleLabel.draw(graphicsHandler);
        Level1.draw(graphicsHandler);
        Level2.draw(graphicsHandler);
        Level3.draw(graphicsHandler);
        Level4.draw(graphicsHandler);
        Level5.draw(graphicsHandler);
        Level6.draw(graphicsHandler);
        Level7.draw(graphicsHandler);
        returnButton.drawWithParsedNewLines(graphicsHandler);
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }

    public int getMenuItemSelected() {
        return menuItemSelected;
    }

    // receive a selection from the mouse input
    @Override
    public void selectMenuOption(int option) {
        if (option >= 0 && option < 8) {
            currentMenuItemHovered = option;
        }
    }

    // additional unused method
    @Override
    public void deselectMenuOption(int option) {
        // do nothing
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
        if (currentMenuItemHovered > 7) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = 7;
        }


        Level1.setColor(new Color(41,207,240));
        Level2.setColor(new Color(41,207,240));
        Level3.setColor(new Color(41,207,240));
        Level4.setColor(new Color(41,207,240));
        Level5.setColor(new Color(41,207,240));
        Level6.setColor(new Color(41,207,240));
        Level7.setColor(new Color(41,207,240));
        returnButton.setColor(new Color(41,207,240));
        if (currentMenuItemHovered == 0) {
            Level1.setColor(new Color (250,215,0));
            pointerLocationX = 10;
            pointerLocationY = 80;
        } else if (currentMenuItemHovered == 1) {
            Level2.setColor(new Color (250,215,0));
            pointerLocationX = 10;
            pointerLocationY = 180;
        } else if (currentMenuItemHovered == 2){
            Level3.setColor(new Color (250,215,0));
            pointerLocationX = 10;
            pointerLocationY = 280;
        } else if (currentMenuItemHovered == 3){
            Level4.setColor(new Color (250,215,0));
            pointerLocationX = 10;
            pointerLocationY = 380;
        } else if (currentMenuItemHovered == 4){
            Level5.setColor(new Color (250,215,0));
            pointerLocationX = 170;
            pointerLocationY = 80;
        } else if (currentMenuItemHovered == 5){
            Level6.setColor(new Color (250,215,0));
            pointerLocationX = 170;
            pointerLocationY = 180;
        } else if (currentMenuItemHovered == 6){
            Level7.setColor(new Color (250,215,0));
            pointerLocationX = 170;
            pointerLocationY = 280;
        } else if (currentMenuItemHovered == 7){
            returnButton.setColor(new Color (250,215,0));
            pointerLocationX = 470;
            pointerLocationY = 540;
        } 
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER);
        }
        if (!keyLocker.isKeyLocked(Key.ENTER) && Keyboard.isKeyDown(Key.ENTER)) {
            menuItemSelected = currentMenuItemHovered;

            if(currentMenuItemHovered == 0) {
                screenCoordinator.loadlevel("test_map.txt");
            } else if (currentMenuItemHovered == 1){
                screenCoordinator.loadlevel("test_map_2.txt");
            }else if (currentMenuItemHovered == 2) {
                screenCoordinator.loadlevel("test_map_3.txt");
            } else if (currentMenuItemHovered == 3) {
                screenCoordinator.loadlevel("test_map_4.txt");
            } else if (currentMenuItemHovered == 4) {
                screenCoordinator.loadlevel("test_map_5.txt");
            }else if (currentMenuItemHovered == 5) {
                screenCoordinator.loadlevel("test_map_6.txt");
            }else if (currentMenuItemHovered == 6) {
                screenCoordinator.loadlevel("test_map_7.txt");
            }else if (currentMenuItemHovered == 7) {
                ScreenCoordinator.setGameState(GameState.MENU);
            }

        }
    }
}