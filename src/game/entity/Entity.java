package game.entity;

import java.awt.Rectangle;

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
	
	public Entity(WormsGame wormsGame, float x, float y, int width, int height) {
		this(wormsGame, (int)x, (int)y, width, height);
		this.x = x;
		this.y = y;
	}
	
	public Entity(WormsGame wormsGame, int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.wormsGame = wormsGame;
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
	
	public void doMovement() {
		if(xMotion != 0 || yMotion != 0) {
			if(yMotion != 0) {
				//Apply Global Gravity
				yMotion *= 1 + (fallDuration * Gamemode.GRAVITY);
			}
			
			setRenderUpdate(true);
			x += xMotion;
			y -= yMotion;
			while(wormsGame.collides(this, 0, 0)) {
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
	}
	
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

}
