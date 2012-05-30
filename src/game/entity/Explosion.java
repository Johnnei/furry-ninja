package game.entity;

import engine.math.Point;
import game.Team;

public class Explosion {
	
	private Point explosionPoint;
	private Entity owner;
	
	private float damageRange;
	private int minDamage;
	private int maxDamage;
	
	/**
	 * Creates a new explosion
	 * @param p
	 * The point of explosion
	 * @param range
	 * The range of the damage 
	 * @param mDmg
	 * The minimun amount of damage within the range
	 * @param xDmg
	 * The maximum amount of damage at point p
	 */
	public Explosion(Entity e, Point p, float range, int mDmg, int xDmg) {
		owner = e;
		damageRange = range * range;
		maxDamage = xDmg;
		minDamage = mDmg;
		explosionPoint = p;
	}
	
	/**
	 * Causes explosion
	 */
	public void explode() { 
		for(int i = 0; i < owner.getWormsGame().getTeamCount(); i++) {
			Team t = owner.getWormsGame().getTeam(i);
			for(int j = 0; j < t.getCubeCount(); j++) {
				Cube c = t.getCube(j);
				if(c.isDead())
					continue;
				Point cubePoint = c.getPoint();
				float dSquared = explosionPoint.getSquaredDistanceTo(cubePoint);
				int dmg = getDamage(dSquared);
				if(dmg > 0) {
					System.out.println("Taking Damage " + dmg + " from projectile at " + dSquared + " distance^2");
					c.takeDamgage(dmg);
				}
			}
		}
	}
	
	/**
	 * Gets the recieved damage based on distance
	 * @param distance
	 * The squared distance from the object
	 * @return
	 * The floored amount of damage the object has to recieve
	 */
	private int getDamage(float distance) {
		if(distance > damageRange)
			return 0;
		float dmgLossPerUnit = (float)(maxDamage - minDamage) / damageRange;
		return (int)(maxDamage - (dmgLossPerUnit * distance)); 
	}

}
