package Level;

import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import GameObject.GameObject;
import GameObject.IntersectableRectangle;
import GameObject.SpriteSheet;
import Utils.AirGroundState;
import Utils.Direction;
import Utils.Point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Enemies.Fireball;
import Enemies.FriendlyFire;
import Enemies.DinosaurEnemy.DinosaurState;

public abstract class Player extends GameObject {
	// values that affect player movement
	// these should be set in a subclass
	protected float walkSpeed = 0;
	protected float gravity = 0;
	protected float jumpHeight = 0;
	protected float jumpDegrade = 0;
	protected float terminalVelocityY = 0;
	protected float momentumYIncrease = 0;

	// values used to handle player movement
	protected float jumpForce = 0;
	protected float momentumY = 0;
	protected float moveAmountX, moveAmountY;

	// values used to keep track of player's current state
	protected PlayerState playerState;
	protected PlayerState previousPlayerState;
	protected Direction facingDirection;
	protected AirGroundState airGroundState;
	protected AirGroundState previousAirGroundState;
	protected LevelState levelState;
	protected FriendlyFire currentFireball;

	// classes that listen to player events can be added to this list
	protected ArrayList<PlayerListener> listeners = new ArrayList<>();

	// define keys
	protected KeyLocker keyLocker = new KeyLocker();
	protected Key JUMP_KEY = Key.UP;
	protected Key MOVE_LEFT_KEY = Key.LEFT;
	protected Key MOVE_RIGHT_KEY = Key.RIGHT;
	protected Key CROUCH_KEY = Key.DOWN;
	protected Key JUMP_ALT = Key.W;
	protected Key LEFT_ALT = Key.A;
	protected Key RIGHT_ALT = Key.D;
	protected Key CROUCH_ALT = Key.S;
	protected Key SHOOT_KEY = Key.SPACE;

	// if true, player cannot be hurt by enemies (good for testing)
	protected boolean isInvincible = false;
	protected boolean hasPowerUp = false;

	// Sound Effects
	File bubbleSound = new File("Resources/bubbles.wav");
	File powerUp = new File("Resources/powerUp.wav");

	public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName) {
		super(spriteSheet, x, y, startingAnimationName);
		facingDirection = Direction.RIGHT;
		airGroundState = AirGroundState.AIR;
		previousAirGroundState = airGroundState;
		playerState = PlayerState.STANDING;
		previousPlayerState = playerState;
		levelState = LevelState.RUNNING;
		currentFireball = null;
		
	}

	public void update() {
		moveAmountX = 0;
		moveAmountY = 0;

		// if player is currently playing through level (has not won or lost)
		if (levelState == LevelState.RUNNING) {
			applyGravity();

			// update player's state and current actions, which includes things like
			// determining how much it should move each frame and if its walking or jumping
			do {
				previousPlayerState = playerState;
				handlePlayerState();
			} while (previousPlayerState != playerState);

			previousAirGroundState = airGroundState;

			// update player's animation
			super.update();

			// move player with respect to map collisions based on how much player needs to
			// move this frame
			super.moveYHandleCollision(moveAmountY);
			super.moveXHandleCollision(moveAmountX);

			updateLockedKeys();
		}

		// if player has beaten level
		else if (levelState == LevelState.LEVEL_COMPLETED) {
			updateLevelCompleted();
		}

		// if player has lost level
		else if (levelState == LevelState.PLAYER_DEAD) {
			updatePlayerDead();
		}
	}

	// add gravity to player, which is a downward force
	protected void applyGravity() {
		moveAmountY += gravity + momentumY;
	}

	// based on player's current state, call appropriate player state handling
	// method
	protected void handlePlayerState() {
		switch (playerState) {
		case STANDING:
			playerStanding();
			break;
		case WALKING:
			playerWalking();
			break;
		case CROUCHING:
			playerCrouching();
			break;
		case JUMPING:
			playerJumping();
			break;
		case SHOOTING:
			playerShooting();
			break;
		}
	}

	// player STANDING state logic
	protected void playerStanding() {
		// sets animation to a STAND animation based on which way player is facing
		currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";

		// if walk left or walk right key is pressed, player enters WALKING state
		if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(LEFT_ALT)
				|| Keyboard.isKeyDown(RIGHT_ALT)) {
			playerState = PlayerState.WALKING;
		}

		// if jump key is pressed, player enters JUMPING state
		else if (Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) {
			keyLocker.lockKey(JUMP_KEY);
			playerState = PlayerState.JUMPING;
		} else if (Keyboard.isKeyDown(JUMP_ALT) && !keyLocker.isKeyLocked(JUMP_ALT)) {
			keyLocker.lockKey(JUMP_ALT);
			playerState = PlayerState.JUMPING;
		}

		// if crouch key is pressed, player enters CROUCHING state
		else if (Keyboard.isKeyDown(CROUCH_KEY)) {
			playerState = PlayerState.CROUCHING;
		} else if (Keyboard.isKeyDown(CROUCH_ALT)) {
			playerState = PlayerState.CROUCHING;
		}

		// if spacebar pressed, shoot fireball
		else if (Keyboard.isKeyDown(SHOOT_KEY)) {
			playerState = PlayerState.SHOOTING;
		}

		if (Keyboard.isKeyDown(SHOOT_KEY)) {
			keyLocker.lockKey(SHOOT_KEY);
			playerState = PlayerState.SHOOTING;
		}
	}

	// player WALKING state logic
	protected void playerWalking() {
		// sets animation to a WALK animation based on which way player is facing
		currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";

		// if walk left key is pressed, move player to the left
		if (Keyboard.isKeyDown(MOVE_LEFT_KEY) && x > -19 || Keyboard.isKeyDown(LEFT_ALT) && x > -19) {
			moveAmountX -= walkSpeed;
			facingDirection = Direction.LEFT;
		}
		// if walk right key is pressed, move player to the right
		if (Keyboard.isKeyDown(MOVE_RIGHT_KEY) && x < map.getEndBoundX() -50 || Keyboard.isKeyDown(RIGHT_ALT) && x < map.getEndBoundX() -50) {
			moveAmountX += walkSpeed;
			facingDirection = Direction.RIGHT;
		} else if (Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY)) {
			playerState = PlayerState.STANDING;
		} else if (Keyboard.isKeyDown(LEFT_ALT) && Keyboard.isKeyDown(RIGHT_ALT)) {
			moveAmountX += walkSpeed;
			facingDirection = Direction.RIGHT;
		}
		// if player is in air (currently in a jump) and has more jumpForce, continue
		// sending player upwards
		else if (airGroundState == AirGroundState.AIR) {
			if (jumpForce > 0) {
				moveAmountY -= jumpForce;
				jumpForce -= jumpDegrade;
				if (jumpForce < 0) {
					jumpForce = 0;
				}
			}

			// if player is moving upwards, set player's animation to jump. if player moving
			// downwards, set player's animation to fall
			if (previousY > Math.round(y)) {
				currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
			} else {
				currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
			}

			// allows you to move left and right while in the air

			if ((Keyboard.isKeyDown(MOVE_LEFT_KEY) && x > -19) || (Keyboard.isKeyDown(LEFT_ALT) && x > -19)) {
				moveAmountX -= walkSpeed;
			} else if ((Keyboard.isKeyDown(MOVE_RIGHT_KEY) && x < map.getEndBoundX() -50)
					|| (Keyboard.isKeyDown(RIGHT_ALT) && x < map.getEndBoundX() -50)) {
				moveAmountX += walkSpeed;
			}

			// if player is falling, increases momentum as player falls so it falls faster
			// over time
			if (moveAmountY > 0) {
				increaseMomentum();
			}
			if (y > 700) {
				levelState = LevelState.PLAYER_DEAD;
			}
		}

		// if jump key is pressed, player enters JUMPING state
		if (Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)) {
			keyLocker.lockKey(JUMP_KEY);
			playerState = PlayerState.JUMPING;
		} else if (Keyboard.isKeyDown(JUMP_ALT) && !keyLocker.isKeyLocked(JUMP_ALT)) {
			keyLocker.lockKey(JUMP_ALT);
			playerState = PlayerState.JUMPING;
		}

		// if crouch key is pressed,
		else if (Keyboard.isKeyDown(CROUCH_KEY)) {
			playerState = PlayerState.CROUCHING;
		} else if (Keyboard.isKeyDown(CROUCH_ALT)) {
			playerState = PlayerState.CROUCHING;
		}

		// if shoot key is pressed, enter SHOOTING state
		if (Keyboard.isKeyDown(SHOOT_KEY)) {
			keyLocker.lockKey(SHOOT_KEY);
			playerState = PlayerState.SHOOTING;
		}
	}

	// player CROUCHING state logic
	protected void playerCrouching() {
		// sets animation to a CROUCH animation based on which way player is facing
		currentAnimationName = facingDirection == Direction.RIGHT ? "CROUCH_RIGHT" : "CROUCH_LEFT";

		// if crouch key is released, player enters STANDING state
		if (Keyboard.isKeyUp(CROUCH_KEY) && Keyboard.isKeyUp(CROUCH_ALT)) {
			playerState = PlayerState.STANDING;
		}

		// if jump key is pressed, player enters JUMPING state
		if (Keyboard.isKeyDown(JUMP_KEY) && !keyLocker.isKeyLocked(JUMP_KEY)
				|| Keyboard.isKeyDown(JUMP_ALT) && !keyLocker.isKeyLocked(JUMP_ALT)) {
			keyLocker.lockKey(JUMP_KEY);
			keyLocker.lockKey(JUMP_ALT);
			playerState = PlayerState.JUMPING;
		}
	}

	// player JUMPING state logic
	protected void playerJumping() {
		// if last frame player was on ground and this frame player is still on ground,
		// the jump needs to be setup
		if (previousAirGroundState == AirGroundState.GROUND && airGroundState == AirGroundState.GROUND) {

			// sets animation to a JUMP animation based on which way player is facing
			currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";

			// player is set to be in air and then player is sent into the air
			airGroundState = AirGroundState.AIR;
			jumpForce = jumpHeight;
			if (jumpForce > 0) {
				moveAmountY -= jumpForce;
				jumpForce -= jumpDegrade;
				if (jumpForce < 0) {
					jumpForce = 0;
				}
			}
		}

		// if player is in air (currently in a jump) and has more jumpForce, continue
		// sending player upwards
		else if (airGroundState == AirGroundState.AIR) {
			if (jumpForce > 0) {
				moveAmountY -= jumpForce;
				jumpForce -= jumpDegrade;
				if (jumpForce < 0) {
					jumpForce = 0;
				}
			}

			// if player is moving upwards, set player's animation to jump. if player moving
			// downwards, set player's animation to fall
			if (previousY > Math.round(y)) {
				currentAnimationName = facingDirection == Direction.RIGHT ? "JUMP_RIGHT" : "JUMP_LEFT";
			} else {
				currentAnimationName = facingDirection == Direction.RIGHT ? "FALL_RIGHT" : "FALL_LEFT";
			}

			// allows you to move left and right while in the air
			if (Keyboard.isKeyDown(MOVE_LEFT_KEY) && x > -19 || Keyboard.isKeyDown(LEFT_ALT) && x > -19) {
				moveAmountX -= walkSpeed;
			} else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY) && x < map.getEndBoundX() -50 || Keyboard.isKeyDown(RIGHT_ALT) && x < map.getEndBoundX() -50) {
				moveAmountX += walkSpeed;
			}

			// if player is falling, increases momentum as player falls so it falls faster
			// over time
			if (moveAmountY > 0) {
				increaseMomentum();
			}

			if (y > 700) {
				levelState = LevelState.PLAYER_DEAD;
			}
		}

		// if player last frame was in air and this frame is now on ground, player
		// enters STANDING state
		else if (previousAirGroundState == AirGroundState.AIR && airGroundState == AirGroundState.GROUND) {
			playerState = PlayerState.STANDING;
		}
	}

	// while player is in air, this is called, and will increase momentumY by a set
	// amount until player reaches terminal velocity
	protected void increaseMomentum() {
		momentumY += momentumYIncrease;
		if (momentumY > terminalVelocityY) {
			momentumY = terminalVelocityY;
		}
	}

	protected void playerShooting() {

		if (playerState == PlayerState.SHOOTING) {
			if (previousPlayerState == PlayerState.WALKING || previousPlayerState == PlayerState.STANDING) {
				currentAnimationName = facingDirection == Direction.RIGHT ? "SHOOT_RIGHT" : "SHOOT_LEFT";
			} else {

				// define where fireball will spawn on map (x location) relative to player's
				// location
				// and define its movement speed
				int fireballX;
				float movementSpeed;
				if (facingDirection == Direction.RIGHT) {
					fireballX = Math.round(getX()) - 8 + getScaledWidth();
					movementSpeed = 1.5f;
				} else {
					fireballX = Math.round(getX()) - 8;
					movementSpeed = -1.5f;
				}

				// define where fireball will spawn on the map (y location) relative to dinosaur
				// enemy's location
				int fireballY = Math.round(getY()) + 15;

				// create Fireball
				FriendlyFire fireball = new FriendlyFire(new Point(fireballX, fireballY), movementSpeed, 1000);
				currentFireball = fireball;

				// add fireball enemy to the map for it to offically spawn in the level
				if (hasPowerUp == true) {
					map.addEnemy(fireball);
				}
				// change dinosaur back to its WALK state after shooting, reset shootTimer to
				// wait another 2 seconds before shooting again
				playerState = PlayerState.WALKING;
			}
			previousPlayerState = playerState;
		}
	}

	protected void updateLockedKeys() {
		if (Keyboard.isKeyUp(JUMP_KEY) || Keyboard.isKeyUp(JUMP_ALT)) {
			keyLocker.unlockKey(JUMP_KEY);
			keyLocker.unlockKey(JUMP_ALT);
		} else if (Keyboard.isKeyUp(SHOOT_KEY)) {
			keyLocker.unlockKey(SHOOT_KEY);
		}
	}

	@Override
	public void onEndCollisionCheckX(boolean hasCollided, Direction direction) {

	}

	@Override
	public void onEndCollisionCheckY(boolean hasCollided, Direction direction) {
		// if player collides with a map tile below it, it is now on the ground
		// if player does not collide with a map tile below, it is in air
		if (direction == Direction.DOWN) {
			if (hasCollided) {
				momentumY = 0;
				airGroundState = AirGroundState.GROUND;
			} else {
				playerState = PlayerState.JUMPING;
				airGroundState = AirGroundState.AIR;
			}
		}

		// if player collides with map tile upwards, it means it was jumping and then
		// hit into a ceiling -- immediately stop upwards jump velocity
		else if (direction == Direction.UP) {
			if (hasCollided) {
				jumpForce = 0;
			}
		}
	}

	// other entities can call this method to hurt the player
	public void hurtPlayer(MapEntity mapEntity) {
		if (!isInvincible) {
			// if map entity is an enemy, kill player on touch
			if (mapEntity instanceof Enemy) {
				if (mapEntity instanceof FriendlyFire) {
					levelState = LevelState.RUNNING;
				} else {
					levelState = LevelState.PLAYER_DEAD;
				}
			} else if (mapEntity instanceof MapTile) {
				MapTile mapTile = (MapTile) mapEntity;
				if (mapTile.getTileType() == TileType.KILLER) {
					makeSound(bubbleSound);
				}
			}
		}
	}

	public void powerUp(MapEntity mapEntity) {
		if (mapEntity instanceof MapTile) {
			MapTile mapTile = (MapTile) mapEntity;
			if (mapTile.getTileType() == TileType.POWER_UP) {
				hasPowerUp = true;
				makeSound(powerUp);
			}
		}
	}

	// other entities can call this to tell the player they beat a level
	public void completeLevel() {
		levelState = LevelState.LEVEL_COMPLETED;
	}

	// if player has beaten level, this will be the update cycle
	public void updateLevelCompleted() {
		// if player is not on ground, player should fall until it touches the ground
		if (airGroundState != AirGroundState.GROUND && map.getCamera().containsDraw(this)) {
			currentAnimationName = "FALL_RIGHT";
			applyGravity();
			increaseMomentum();
			super.update();
			moveYHandleCollision(moveAmountY);
		}
		// move player to the right until it walks off screen
		else if (map.getCamera().containsDraw(this)) {
			currentAnimationName = "WALK_RIGHT";
			super.update();
			moveXHandleCollision(walkSpeed);
		} else {
			// tell all player listeners that the player has finished the level
			for (PlayerListener listener : listeners) {
				listener.onLevelCompleted();
			}
		}
	}

	// if player has died, this will be the update cycle
	public void updatePlayerDead() {
		// change player animation to DEATH
		if (!currentAnimationName.startsWith("DEATH")) {
			if (facingDirection == Direction.RIGHT) {
				currentAnimationName = "DEATH_RIGHT";
			} else {
				currentAnimationName = "DEATH_LEFT";
			}
			super.update();
		}
		// if death animation not on last frame yet, continue to play out death
		// animation
		else if (currentFrameIndex != getCurrentAnimation().length - 1) {
			super.update();
		}
		// if death animation on last frame (it is set up not to loop back to start),
		// player should continually fall until it goes off screen
		else if (currentFrameIndex == getCurrentAnimation().length - 1) {
			if (map.getCamera().containsDraw(this)) {
				moveY(3);
			} else {
				// tell all player listeners that the player has died in the level
				for (PlayerListener listener : listeners) {
					listener.onDeath();
				}
			}
		}
	}

	public PlayerState getPlayerState() {
		return playerState;
	}

	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}

	public void setFireball(FriendlyFire fire) {
		currentFireball = fire;
	}

	public AirGroundState getAirGroundState() {
		return airGroundState;
	}

	public Direction getFacingDirection() {
		return facingDirection;
	}

	public void setFacingDirection(Direction facingDirection) {
		this.facingDirection = facingDirection;
	}

	public void setLevelState(LevelState levelState) {
		this.levelState = levelState;
	}

	public void addListener(PlayerListener listener) {
		listeners.add(listener);
	}

	public IntersectableRectangle getFire() {
		return currentFireball.getBounds();
	}

}
