package game.entity;

import engine.WMath;
import engine.render.Renderable;
import game.WormsGame;

public abstract class Entity extends Renderable {
	
	//Position and Size
	protected float x, y, width, height;
	//Movement
	protected float xMotion, yMotion;
	private boolean isFalling;
	private float fallDistance;
	
	//Game Reference
	protected WormsGame wormsGame;
	
	//Health
	private int health;
	
	
	public Entity(WormsGame wormsGame, int health, int width, int height) {
		super();
		this.health = health;
		x = 0;
		y = 0;
		this.width = width;
		this.height = height;
		this.wormsGame = wormsGame;
		setFalling(false);
	}
	
	public void setFalling(boolean b) {
		if(isFalling && !b && fallDistance > 0) {
			int fallDamage = (int)((fallDistance - 5) * 0.5);
		}
		isFalling = b;
	}
	
	public void takeDamgage(int dmg) {
		health -= dmg;
		if(health < 0)
			health = 0;
	}
	
	public boolean isDead() {
		return health == 0;
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
