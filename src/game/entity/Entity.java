package game.entity;

import engine.WMath;
import engine.render.Renderable;
import game.WormsGame;

public abstract class Entity extends Renderable {
	
	//Position and Size
	protected float x, y, width, height;
	//Movement
	protected float xMotion, yMotion;
	protected boolean isFalling;
	protected float fallDistance;
	
	//Game Reference
	protected WormsGame wormsGame;
	
	
	public Entity(WormsGame wormsGame, int width, int height) {
		super();
		x = 0;
		y = 0;
		this.width = width;
		this.height = height;
		this.wormsGame = wormsGame;
		setFalling(false);
	}
	
	public void setFalling(boolean b) {
		isFalling = b;
	}
	
	public boolean isOnGround() {
		return false;
	}
	
	public void doMovement() {
		if(xMotion != 0 || yMotion != 0) {
			setRenderUpdate(true);
			x += xMotion;
			y -= yMotion;
			if(isFalling)
				fallDistance += WMath.abs_f(yMotion);
			xMotion = yMotion = 0;
		}
	}
	
	public abstract void onTick();

}
