package game.weapon.set;

import game.weapon.IWeapon;
import game.weapon.Weapon;

public interface IWeaponSet {
	
	/**
	 * Returns all weapon bases for this set of weapons
	 * @return The available weapons
	 */
	public IWeapon[] getWeapons();
	
	/**
	 * Return a random weapon to drop in crates for example
	 * @return a random weapon from the available weapons
	 */
	public Weapon getRandomWeapon();

}
