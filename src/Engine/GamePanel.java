package Engine;

import GameObject.Rectangle;
import Screens.MenuScreen;
import Screens.PlayLevelScreen;
import SpriteFont.SpriteFont;
import Utils.Colors;

import javax.swing.*;

import Game.GameState;
import Game.ScreenCoordinator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * This is where the game loop starts
 * The JPanel uses a timer to continually call cycles of update and draw
 */
public class GamePanel extends JPanel {
	// loads Screens on to the JPanel
	// each screen has its own update and draw methods defined to handle a "section" of the game.
	private ScreenManager screenManager;

	// used to create the game loop and cycle between update and draw calls
	private Timer timer;

	// used to draw graphics to the panel
	private GraphicsHandler graphicsHandler;

	private boolean doPaint = false;
	
	private int currentX, currentY;
	//this is a change
	;
	/*
	 * The JPanel and various important class instances are setup here
	 */
	public GamePanel() {
		super();
		this.setDoubleBuffered(true);
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				currentX = (int) e.getX();
				currentY = (int) e.getY();
				
				if (ScreenCoordinator.getGameState() == GameState.MENU) {
					if (currentX >= 200 && currentX <= 372 && currentY <= 150 && currentY >= 130) {
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 200 && currentX <= 425 && currentY <= 200 && currentY >= 180) {
						ScreenCoordinator.setGameState(GameState.INSTRUCTIONS);
					}
					else if (currentX >= 200 && currentX <= 331 && currentY <= 250 && currentY >= 220) {
						ScreenCoordinator.setGameState(GameState.CREDITS);
					}
				} else if(ScreenCoordinator.getGameState() == GameState.INSTRUCTIONS) {
					
					if(currentX >= 20 && currentY >= 520 && currentX <= 300 && currentY <= 600) {
						ScreenCoordinator.setGameState(GameState.MENU);
					} 
				}else if(ScreenCoordinator.getGameState()== GameState.CREDITS) {
					if(currentX >= 20 && currentY >= 520 && currentX <= 300 && currentY <= 600) {
						ScreenCoordinator.setGameState(GameState.MENU);
					} 
				}
			}
		});

		// attaches Keyboard class's keyListener to this JPanel
		this.addKeyListener(Keyboard.getKeyListener());

		graphicsHandler = new GraphicsHandler();

		screenManager = new ScreenManager();
		

		// Every timer "tick" will call the update method as well as tell the JPanel to repaint
		// Remember that repaint "schedules" a paint rather than carries it out immediately
		// If the game is really laggy/slow, I would consider upping the FPS in the Config file.
		timer = new Timer(1000 / Config.FPS, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					update();
					repaint();
			}
		});
		timer.setRepeats(true);
	}

	// this is called later after instantiation, and will initialize screenManager
	// this had to be done outside of the constructor because it needed to know the JPanel's width and height, which aren't available in the constructor
	public void setupGame() {
		setBackground(Colors.CORNFLOWER_BLUE);
		screenManager.initialize(new Rectangle(getX(), getY(), getWidth(), getHeight()));
		doPaint = true;
	}

	// this starts the timer (the game loop is started here
	public void startGame() {
		timer.start();
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public void update() {
			screenManager.update();
		
	}

	public void draw() {
		screenManager.draw(graphicsHandler);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// every repaint call will schedule this method to be called
		// when called, it will setup the graphics handler and then call this class's draw method
		graphicsHandler.setGraphics((Graphics2D) g);
		if (doPaint) {
			draw();
		}
	}
}
