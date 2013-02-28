package game.weapon;

import game.entity.Crosshair;
import game.entity.Cube;

public class EntityExplode implements IWeapon {

	@Override
	public String getName() {
		return "entity_death";
	}

	@Override
	public int getStartingAmmo() {
		return 0;
	}

	@Override
	public int getMaxAmmo() {
		return 0;
	}

	@Override
	public int getMinFirePower() {
		return 0;
	}

	@Override
	public int getMaxFirePower() {
		return 0;
	}

	@Override
	public int getLandscapeCut() {
		return 0;
	}

	@Override
	public int getDamageRange() {
		return 0;
	}

	@Override
	public int getInnerDamage() {
		return 0;
	}

	@Override
	public int getOuterDamage() {
		return 0;
	}

	@Override
	public float getExplosionScale() {
		return 0.25F;
	}

	@Override
	public int getTextureWidth() {
		return 0;
	}

	@Override
	public int getTextureHeight() {
		return 0;
	}

	@Override
	public boolean isCustomFire() {
		return true;
	}

	@Override
	public void fire(Cube owner, Crosshair crosshair) {
		//Don't fire any projectiles
	}

	@Override
	public int getMaxAngle() {
		return 0;
	}

	@Override
	public int getMinAngle() {
		return 0;
	}

}
