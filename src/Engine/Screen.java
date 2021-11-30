package Engine;

// Base Screen class
// This game engine runs off the idea of "screens", which are classes that contain their own update/draw methods for a particular piece of the game
// For example, there may be a "MenuScreen" or a "PlayGameScreen"
public abstract class Screen {
    public abstract void initialize();
    public abstract void update();
    public abstract void draw(GraphicsHandler graphicsHandler);
    public static LivesHolder life = new LivesHolder();
    public abstract void selectMenuOption(int option);
    public abstract void deselectMenuOption(int option);
}
