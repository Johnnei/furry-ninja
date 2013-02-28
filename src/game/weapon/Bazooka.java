package game.weapon;

import game.entity.Crosshair;
import game.entity.Cube;

public class Bazooka implements IWeapon {

	@Override
	public String getName() {
		return "bazooka";
	}

	@Override
	public int getStartingAmmo() {
		return Weapon.INFINITIVE;
	}

	@Override
	public int getMaxAmmo() {
		return Weapon.INFINITIVE;
	}

	@Override
	public int getMinFirePower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxFirePower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLandscapeCut() {
		return 32;
	}

	@Override
	public int getDamageRange() {
		return 32;
	}

	@Override
	public int getInnerDamage() {
		return 50;
	}

	@Override
	public int getOuterDamage() {
		return 5;
	}

	@Override
	public float getExplosionScale() {
		return 1;
	}

	@Override
	public int getTextureWidth() {
		return 16;
	}

	@Override
	public int getTextureHeight() {
		return 8;
	}

	@Override
	public boolean isCustomFire() {
		return false;
	}

	@Override
	public void fire(Cube owner, Crosshair crosshair) {
	}

}
