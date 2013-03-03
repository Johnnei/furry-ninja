package game.entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import engine.WMath;
import engine.math.Point;
import engine.render.Renderable;
import game.WormsGame;
import game.data.TurnPhase;
import game.physics.GravityMotion;
import game.physics.IMotionVector;

public abstract class Entity extends Renderable {
	
	//Position and Size
	protected float x, y, width, height;
	//Movement
	private ArrayList<IMotionVector> motions;
	protected float xMotion, yMotion;
	/**
	 * If the Entity is jumping
	 */
	private boolean isJumping;
	/**
	 * If the entity can be deleted
	 */
	private boolean canDelete;
	
	//Game Reference
	protected WormsGame wormsGame;
	
	public Entity(int flags, WormsGame wormsGame, float x, float y, int width, int height) {
		this(flags, wormsGame, (int)x, (int)y, width, height);
		this.x = x;
		this.y = y;
	}
	
	public Entity(int flags, WormsGame wormsGame, int x, int y, int width, int height) {
		super(flags);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.wormsGame = wormsGame;
		motions = new ArrayList<>();
		motions.add(new GravityMotion());
		isJumping = false;
		canDelete = false;
	}
	
	public boolean isOnGround() {
		return wormsGame.collides(this, 0, 2);
	}
	
	/**
	 * Calculates the sum of all motionVectors on this entity
	 */
	private void calculateMotion() {
		//Reset motions
		xMotion = 0;
		yMotion = 0;
		//Calculate new motions
		for(int i = 0; i < motions.size(); i++) {
			IMotionVector vector = motions.get(i);
			
			xMotion += vector.getMotionX();
			yMotion += vector.getMotionY();
			
			vector.onTick(this);
			if(vector.canDelete()) {
				motions.remove(i);
				i--;
			}
		}
	}
	
	public void addMotionVector(IMotionVector vector) {
		motions.add(vector);
	}
	
	protected float getStepSize(float motion) {
		if(WMath.abs_f(motion) < 0.01) {
			return 0f;
		} else {
			return (motion > 0) ? WMath.min_f(motion, 1) : WMath.max_f(motion, -1);
		}
	}
	
	/**
	 * Gets called on movement collision
	 */
	public void onCollide() {
	}
	
	public void doMovement() {
		calculateMotion();
		if(xMotion != 0 || yMotion != 0) {
			float allowedMoveX = 0;
			float allowedMoveY = 0;
			boolean hasCollided = false;
			while(true) {
				float stepSizeX = getStepSize(xMotion - allowedMoveX);
				float stepSizeY = getStepSize(yMotion - allowedMoveY);
				boolean canMoveX = stepSizeX != 0 && !wormsGame.collides(this, allowedMoveX + stepSizeX, 0);
				boolean canMoveY = stepSizeY != 0 && !wormsGame.collides(this, 0, 1 + allowedMoveY + stepSizeY);
				if(canMoveX) {
					allowedMoveX += stepSizeX;
				} else if(stepSizeX != 0 && !hasCollided) {
					onCollide();
					hasCollided = true;
				}
				if(canMoveY) {
					allowedMoveY += stepSizeY;
				} else if(stepSizeY != 0 && !hasCollided) {
					onCollide();
					onFall();
					hasCollided = true;
				}
				if(!canMoveX && !canMoveY) { //No movement in any of the axis
					break;
				}
			}
			if(allowedMoveX != 0 || allowedMoveY != 0) {
				setRenderUpdate(true);
				x += allowedMoveX;
				y += allowedMoveY;
			}
		}
	}
	
	/**
	 * Is called when the player has landed
	 */
	public void onFall() {
		
	}
	
	public abstract void onTick(TurnPhase turn);
	public abstract void onTurnChange(TurnPhase turn);
	
	public boolean isFalling() {
		return yMotion > 0;
	}
	
	public Rectangle getCollisionBox() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
	
	public void setCanDelete(boolean b) {
		canDelete = b;
	}
	
	public boolean isJumping() {
		return isJumping;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Point getPoint() {
		return new Point(x, y);
	}
	
	public WormsGame getWormsGame() {
		return wormsGame;
	}
	
	public boolean canDelete() {
		return canDelete;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

}
