package game.weapon;

import game.display.Crosshair;
import game.entity.Cube;
import game.entity.Projectile;
import game.physics.MotionVector;

public class Weapon {
	
	/**
	 * Used for weapons with infinitive amounts of ammo
	 */
	public static final int INFINITIVE = 255;
	
	private IWeapon weapon;
	private int ammo;
	/**
	 * The firepower of this gun
	 */
	private float charge;
	
	public Weapon(IWeapon weapon) {
		this.weapon = weapon;
		ammo = weapon.getStartingAmmo();
		charge = 0;
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
		
		float yPower = charge;
		if(owner.isFacingLeft())
			charge = -charge;
		p.addMotionVector(new MotionVector(charge * dSin, yPower * dCos, 150, 150));
		owner.getWormsGame().addProjectile(p);
		addAmmo(-1);
		charge = 0; //Reset Charge
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
	
	public void charge() {
		if(charge < weapon.getMaxFirePower()) {
			charge += 0.25F;
			if(charge < weapon.getMinFirePower()) {
				charge = weapon.getMinFirePower();
			}
		}
	}
	
	public boolean isCharging() {
		return charge > 0;
	}
	
	public boolean isMaxCharge() {
		return charge == weapon.getMaxFirePower();
	}
	
	public IWeapon getStats() {
		return weapon;
	}

}
