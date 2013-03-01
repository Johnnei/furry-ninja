package game.entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import engine.WMath;
import engine.math.Point;
import engine.render.Renderable;
import game.WormsGame;
import game.data.Gamemode;
import game.data.TurnPhase;

public abstract class Entity extends Renderable {
	
	//Position and Size
	protected float x, y, width, height;
	//Movement
	private ArrayList<MotionVector> motions;
	protected float xMotion, yMotion;
	protected float fallDuration;
	protected float fallDistance;
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
		motions.add(new MotionVector(0, Gamemode.GRAVITY));
		setFalling(false);
		isJumping = false;
		canDelete = false;
	}
	
	public void setFalling(boolean fallState) {
		if(fallState) {
			if(!isJumping)
				fallDuration = 1;
		} else {
			if(!isJumping)
				fallDuration = 0;
		}
	}
	
	public boolean isOnGround() {
		return wormsGame.collides(this, 0, 1);
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
			MotionVector vector = motions.get(i);
			
			xMotion += vector.getMotionX();
			yMotion += vector.getMotionY();
			
			vector.onTick();
			if(vector.canDelete()) {
				motions.remove(i);
				i--;
			}
		}
	}
	
	public void addMotionVector(float motionX, float motionY) {
		addMotionVector(motionX, motionY, 0, 0);
	}
	
	public void addMotionVector(float motionX, float motionY, int xLifetime, int yLifetime) {
		motions.add(new MotionVector(motionX, motionY, xLifetime, yLifetime));
	}
	
	private float getStepSize(float motion) {
		if(WMath.abs_f(motion) < 0.01) {
			return 0f;
		} else {
			return (motion > 0) ? WMath.min_f(motion, 1) : WMath.max_f(motion, -1);
		}
	}
	
	public void doMovement() {
		calculateMotion();
		if(xMotion != 0 || yMotion != 0) {
			float allowedMoveX = 0;
			float allowedMoveY = 0;
			while(true) {
				float stepSizeX = getStepSize(xMotion - allowedMoveX);
				float stepSizeY = getStepSize(yMotion - allowedMoveY);
				System.out.println("xMotion: " + xMotion + ", yMotion: " + yMotion);
				boolean canMoveX = stepSizeX != 0 && !wormsGame.collides(this, allowedMoveX + stepSizeX, 0);
				boolean canMoveY = stepSizeY != 0 && !wormsGame.collides(this, 0, 1 + allowedMoveY + stepSizeY);
				System.out.println("collides(" + (int)(allowedMoveX + stepSizeX) + ", " + (int)(allowedMoveY + stepSizeY) + ") -> " + canMoveX + ", " + canMoveY + " Total Accepted Speed: (" + allowedMoveX + ", " + allowedMoveY + ")");
				if(canMoveX) {
					allowedMoveX += stepSizeX;
				}
				if(canMoveY) {
					allowedMoveY += stepSizeY;
				}
				if(!canMoveX && !canMoveY) { //No movement in any of the axis
					break;
				}
			}
			if(allowedMoveX != 0 || allowedMoveY != 0) {
				System.out.print("Updating position from (" + x + ", " +  y + ") to ");
				setRenderUpdate(true);
				x += allowedMoveX;
				y += allowedMoveY;
				System.out.println("(" + x + ", " +  y + ")");
			}
		}
	}
	
	/*public void doMovement() {
		if(xMotion != 0 || yMotion != 0) {
			if(yMotion != 0) {
				//Apply Global Gravity
				yMotion *= 1 + (fallDuration * Gamemode.GRAVITY);
			}
			
			setRenderUpdate(true);
			while(xMotion != 0) {
				if(xMotion > 0) {
					if(wormsGame.collides(this, 1, 0)) {
						xMotion = 0;
					} else {
						xMotion--;
						x++;
					}
				} else {
					if(wormsGame.collides(this, -1, 0)) {
						xMotion = 0;
					} else {
						xMotion++;
						x--;
					}
				}
			}
			y -= yMotion;
			while(wormsGame.collides(this)) {
				--y;
				if(yMotion < 0)
					++yMotion;
				else
					--yMotion;
				if(!wormsGame.collides(this, 0, 0)) {
					fallDuration = 0F;
					break;
				}
			}
			if(fallDuration > 0 && yMotion < 0)
				fallDistance += WMath.abs_f(yMotion);
			xMotion = 0;
			if(isOnGround()) {
				yMotion = 0;
				isJumping = false;
				setFalling(false);
			}
		}
	}*/
	
	public abstract void onTick(TurnPhase turn);
	public abstract void onTurnChange(TurnPhase turn);
	
	public boolean isFalling() {
		return fallDuration > 0;
	}
	
	public Rectangle getCollisionBox() {
		return new Rectangle((int)x, (int)y, (int)width, (int)height);
	}
	
	public void setJumping(boolean b) {
		isJumping = b;
		if(b)
			fallDuration = 1F;
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
