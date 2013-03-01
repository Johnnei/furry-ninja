package game.weapon;

import game.entity.Crosshair;
import game.entity.Cube;
import game.entity.Projectile;

public class Weapon {
	
	/**
	 * Used for weapons with infinitive amounts of ammo
	 */
	public static final int INFINITIVE = 255;
	
	private IWeapon weapon;
	private int ammo;
	
	public Weapon(IWeapon weapon) {
		this.weapon = weapon;
		ammo = weapon.getStartingAmmo();
	}
	
	/**
	 * Fires the weapon
	 * @param owner
	 * @param crosshair 
	 */
	public void fire(Cube owner, Crosshair crosshair) {
		Projectile p = new Projectile(owner.getWormsGame(), owner, (int)crosshair.getX(), (int)crosshair.getY(), weapon);
		float dCos = (float)Math.cos(crosshair.getAngle() * (Math.PI / 180));
		float dSin = (float)Math.sin(crosshair.getAngle() * (Math.PI / 180));
		
		int power = 10; //TODO Add Crosshair power
		if(owner.isFacingLeft())
			power = -power;
		p.addMotionVector(power * dSin, 15 * -dCos, 0, 100);
		owner.getWormsGame().addProjectile(p);
		addAmmo(-1);
	}
	
	/**
	 * Add the given amount to the ammo
	 * @param amount
	 */
	public void addAmmo(int amount) {
		if(ammo == INFINITIVE)
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
	
	public IWeapon getStats() {
		return weapon;
	}

}
