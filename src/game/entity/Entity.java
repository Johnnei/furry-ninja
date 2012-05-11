package game.entity;

import engine.render.Renderable;

public abstract class Entity extends Renderable {
	
	protected int health;
	protected float x, y, width, height;
	
	public Entity(int health, int width, int height) {
		super();
		this.health = health;
		x = 0;
		y = 0;
		this.width = width;
		this.height = height;
	}
	
	public abstract void onTick();

}
