package game.weapon;

import game.data.WeaponType;
import game.entity.Crosshair;
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
	 * @param crosshair 
	 */
	public void fire(Cube owner, Crosshair crosshair) {
		Projectile p = new Projectile(owner.getWormsGame(), owner, (int)owner.getX(), (int)owner.getY(), id);
		float dCos = (float)Math.cos(crosshair.getAngle() * (Math.PI / 180));
		float dSin = (float)Math.sin(crosshair.getAngle() * (Math.PI / 180));
		
		if(owner.isFacingLeft())
			p.setMotion(-10 * dSin, 15 * -dCos);
		else
			p.setMotion(10 * dSin, 15 * -dCos);
		owner.getWormsGame().addProjectile(p);
		addAmmo(-1);
	}
	
	/**
	 * Add the given amount to the ammo
	 * @param amount
	 */
	public void addAmmo(int amount) {
		if(ammo == WeaponType.INFINITIVE)
			return;
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
