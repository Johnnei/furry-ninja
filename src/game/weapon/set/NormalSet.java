package game.weapon.set;

import game.weapon.Bazooka;
import game.weapon.IWeapon;
import game.weapon.Pistol;
import game.weapon.Weapon;

public class NormalSet implements IWeaponSet {

	private IWeapon[] weapons;
	
	public NormalSet() {
		weapons = new IWeapon[2];
		weapons[0] = new Bazooka();
		weapons[1] = new Pistol();
	}
	
	@Override
	public IWeapon[] getWeapons() {
		return weapons;
	}

	@Override
	public Weapon getRandomWeapon() {
		return null;
	}

}
