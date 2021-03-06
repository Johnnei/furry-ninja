package game.weapon;

import game.display.Crosshair;
import game.entity.Cube;

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
		weapon.fire(owner, crosshair, charge);
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
	
	public float getCharge() {
		return charge;
	}

}
