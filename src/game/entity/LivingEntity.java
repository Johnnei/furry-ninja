package game.entity;

import game.WormsGame;

public abstract class LivingEntity extends Entity {

	//Health
	private int health;
	
	public LivingEntity(WormsGame wormsGame, int health, int x, int y, int width, int height) {
		super(wormsGame, x, y, width, height);
		this.health = health;
	}
	
	public void onTick() {
		if(isFalling()) {
			fallDuration++;
		}
	}
	
	public void setFalling(boolean b) {
		if(isFalling() && !b && fallDistance > 0) {
			int fallDamage = (int)((fallDistance - 5) * 0.5);
			takeDamgage(fallDamage);
		}
		if(!isFalling())
			fallDuration = (b) ? 1 : 0;
	}
	
	public void takeDamgage(int dmg) {
		health -= dmg;
		if(health < 0)
			health = 0;
	}
	
	public boolean isDead() {
		return health == 0;
	}
	
	public int getHealth() {
		return health;
	}

}
