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
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

/*
 * This is where the game loop starts
 * The JPanel uses a timer to continually call cycles of update and draw
 */
public class GamePanel extends JPanel {
	// loads Screens on to the JPanel
	// each screen has its own update and draw methods defined to handle a "section" of the game.
	private ScreenManager screenManager;
	private KeyLocker keyLocker = new KeyLocker();
	public static final SoundHolder sound = new SoundHolder();
	private Key MUTE_BUTTON = Key.M;
	// used to create the game loop and cycle between update and draw calls
	private Timer timer;
	private int scaleWidth,scaleHeight = 50;
	// used to draw graphics to the panel
	private GraphicsHandler graphicsHandler;

	private boolean doPaint = false;
	
	private int currentX, currentY;
	BufferedImage muteButton = ImageLoader.load("mutebutton.png");
	BufferedImage muteButtonLine = ImageLoader.load("muteButtonLine.png");
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
						ScreenCoordinator.setLevel("test_map.txt"); // reset the level to the first one
						ScreenCoordinator.setGameState(GameState.LEVEL);
					} 
					else if (currentX >= 200 && currentX <= 425 && currentY <= 200 && currentY >= 180) {
						ScreenCoordinator.setGameState(GameState.INSTRUCTIONS);
					}
					else if (currentX >= 200 && currentX <= 331 && currentY <= 250 && currentY >= 220) {
						ScreenCoordinator.setGameState(GameState.CREDITS);
					}
					else if (currentX >= 200 && currentX <= 425 && currentY <= 300 && currentY >= 280) {
						ScreenCoordinator.setGameState(GameState.LEVELSELECT);
					}
				} 
				else if(ScreenCoordinator.getGameState() == GameState.INSTRUCTIONS) {
					if(currentX >= 500 && currentY >= 520 && currentX <= 780 && currentY <= 560) {
						ScreenCoordinator.setGameState(GameState.MENU);
					} 
				}
				else if(ScreenCoordinator.getGameState()== GameState.CREDITS) {
					if(currentX >= 500 && currentY >= 520 && currentX <= 780 && currentY <= 560) {
						ScreenCoordinator.setGameState(GameState.MENU);
					} 
				}
				else if (ScreenCoordinator.getGameState() == GameState.LEVELSELECT) {
					if (currentX >= 40 && currentX <= 120 && currentY >= 70 && currentY <= 100) {
						ScreenCoordinator.setLevel("test_map.txt");
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 40 && currentX <= 120 && currentY >= 170 && currentY <= 200) {
						ScreenCoordinator.setLevel("test_map_2.txt");
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 40 && currentX <= 120 && currentY >= 270 && currentY <= 300) {
						ScreenCoordinator.setLevel("test_map_3.txt");
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 40 && currentX <= 120 && currentY >= 370 && currentY <= 400) {
						ScreenCoordinator.setLevel("test_map_4.txt");
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 200 && currentX <= 280 && currentY >= 70 && currentY <= 100) {
						ScreenCoordinator.setLevel("test_map_5.txt");
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 200 && currentX <= 280 && currentY >= 170 && currentY <= 200) {
						ScreenCoordinator.setLevel("test_map_6.txt");
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 200 && currentX <= 280 && currentY >= 270 && currentY <= 300) {
						ScreenCoordinator.setLevel("test_map_7.txt");
						ScreenCoordinator.setGameState(GameState.LEVEL);
					}
					else if (currentX >= 500 && currentY >= 520 && currentX <= 780 && currentY <= 560) {
						ScreenCoordinator.setGameState(GameState.MENU);
					}
				}
				
				//if statement that checks to see if the user has clicked on the mute button, applies for all screens
				if(currentX <= 50 && currentX >= 0 && currentY <= 50 && currentY >= 0) {
					sound.setSoundHolder(!sound.getSoundHolder());
				}
			}
			
				
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {	
				currentX = (int) e.getX();
				currentY = (int) e.getY();
				
				if (ScreenCoordinator.getGameState() == GameState.MENU) {
					if (currentX >= 200 && currentX <= 372 && currentY <= 150 && currentY >= 130) {
						screenManager.selectMenuOption(0);
					}
					else if (currentX >= 200 && currentX <= 425 && currentY <= 200 && currentY >= 180) {
						screenManager.selectMenuOption(1);
					}
					else if (currentX >= 200 && currentX <= 331 && currentY <= 250 && currentY >= 220) {
						screenManager.selectMenuOption(2);
					}
					else if (currentX >= 200 && currentX <= 425 && currentY <= 300 && currentY >= 280) {
						screenManager.selectMenuOption(3);
					}
				} 
				else if (ScreenCoordinator.getGameState() == GameState.INSTRUCTIONS) {
					if (currentX >= 500 && currentY >= 520 && currentX <= 780 && currentY <= 560) {
						screenManager.selectMenuOption(0);
					} 
					else {
						screenManager.deselectMenuOption(0);
					}
				}
				else if (ScreenCoordinator.getGameState() == GameState.CREDITS) {
					if (currentX >= 500 && currentY >= 520 && currentX <= 780 && currentY <= 560) {
						screenManager.selectMenuOption(0);
					} 
					else {
						screenManager.deselectMenuOption(0);
					} 
				}
				else if (ScreenCoordinator.getGameState() == GameState.LEVELSELECT) {
					if (currentX >= 40 && currentX <= 120 && currentY >= 70 && currentY <= 100) {
						screenManager.selectMenuOption(0);
					}
					else if (currentX >= 40 && currentX <= 120 && currentY >= 170 && currentY <= 200) {
						screenManager.selectMenuOption(1);
					}
					else if (currentX >= 40 && currentX <= 120 && currentY >= 270 && currentY <= 300) {
						screenManager.selectMenuOption(2);
					}
					else if (currentX >= 40 && currentX <= 120 && currentY >= 370 && currentY <= 400) {
						screenManager.selectMenuOption(3);
					}
					else if (currentX >= 200 && currentX <= 280 && currentY >= 70 && currentY <= 100) {
						screenManager.selectMenuOption(4);
					}
					else if (currentX >= 200 && currentX <= 280 && currentY >= 170 && currentY <= 200) {
						screenManager.selectMenuOption(5);
					}
					else if (currentX >= 200 && currentX <= 280 && currentY >= 270 && currentY <= 300) {
						screenManager.selectMenuOption(6);
					}
					else if (currentX >= 500 && currentY >= 520 && currentX <= 780 && currentY <= 560) {
						screenManager.selectMenuOption(7);
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
			//code here will have m button be mute button for all screens 
			if(Keyboard.isKeyDown(MUTE_BUTTON) && !keyLocker.isKeyLocked(MUTE_BUTTON)) {
				keyLocker.lockKey(MUTE_BUTTON);
				sound.setSoundHolder(!sound.getSoundHolder());
				//System.out.println(sound.getSoundHolder());
			}
			if(!Keyboard.isKeyDown(MUTE_BUTTON)) {
				keyLocker.unlockKey(MUTE_BUTTON);
			}
		
	}
	

	public void draw() {
		screenManager.draw(graphicsHandler);
		
	if(sound.getSoundHolder()) {
	graphicsHandler.drawImage(muteButton, 0, 0, 50, 50);
	}else {
	graphicsHandler.drawImage(muteButtonLine, 0, 0, 50, 50);
	}
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
