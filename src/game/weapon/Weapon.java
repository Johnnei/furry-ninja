package game.weapon;

import game.entity.Cube;
import game.entity.Projectile;

public class Weapon {
	
	private final int id;
	private int ammo;
	
	public Weapon(int id, int ammo) {
		this.id = id;
		this.ammo = ammo;
	}
	
	/**
	 * Fires the weapon
	 * @param owner
	 */
	public void fire(Cube owner) {
		Projectile p = new Projectile(owner.getWormsGame(), owner, (int)owner.getX(), (int)owner.getY(), id); 
		owner.getWormsGame().addProjectile(p);
	}
	
	/**
	 * Add the given amount to the ammo
	 * @param amount
	 */
	public void addAmmo(int amount) {
		ammo += amount;
	}
	
	/**
	 * Gets the current ammo amount
	 * @return
	 */
	public int getAmmo() {
		return ammo;
	}

}
