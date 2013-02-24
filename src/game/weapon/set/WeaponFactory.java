package game.weapon.set;

import game.weapon.IWeapon;
import game.weapon.Weapon;

public class WeaponFactory {
	
	private IWeaponSet weaponSet;
	
	public WeaponFactory(IWeaponSet weaponSet) {
		this.weaponSet = weaponSet;
	}
	
	public Weapon[] createWeapons() {
		IWeapon[] weaponBases = weaponSet.getWeapons();
		Weapon[] weapons = new Weapon[weaponBases.length];
		for(int i = 0; i < weapons.length; i++) {
			weapons[i] = new Weapon(weaponBases[i]);
		}
		return weapons;
	}

}
