package game.entity;

import game.WormsGame;
import game.data.TurnPhase;

public class Projectile extends Entity {
	
	private int minDamage;
	private int maxDamage;
	private float damageRange;
	private Cube owner;
	
	public Projectile(WormsGame wormsGame, Cube owner, int x, int y, int width, int height) {
		super(wormsGame, x, y, width, height);
	}
	
	/**
	 * Gets the recieved damage based on distance
	 * @param distance
	 * @return
	 */
	private int getDamage(float distance) {
		if(distance > damageRange)
			return 0;
		float dmgLossPerUnit = (maxDamage - minDamage) / damageRange;
		return (int)(maxDamage - (dmgLossPerUnit * distance)); 
	}
	
	private void explode() {
		
	}

	@Override
	public void onTick(TurnPhase turn) {
		if(!owner.getCollisionBox().intersects(getCollisionBox())) { //Don't Selfkill
			if(wormsGame.collides(this)) { //Should do boom?
				explode();
			}
		}
	}

	@Override
	public void onTurnChange(TurnPhase turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateColorData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateVertexData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

}
