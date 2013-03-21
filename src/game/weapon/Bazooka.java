package game.weapon;

import game.display.Crosshair;
import game.entity.Cube;
import game.entity.Projectile;
import game.physics.MotionVector;

public class Bazooka implements IWeapon {

	@Override
	public String getName() {
		return "bazooka";
	}
	
	@Override
	public int getTextureMapId() {
		return 0;
	}
	
	@Override
	public String getExplosionTextureName() {
		return "explosion";
	}
	
	@Override
	public boolean hasExplosion() {
		return true;
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
		return 2;
	}

	@Override
	public int getMaxFirePower() {
		return 10;
	}

	@Override
	public int getLandscapeCut() {
		return 32;
	}

	@Override
	public int getDamageRange() {
		return 48;
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
	public void fire(Cube owner, Crosshair crosshair, float charge) {
		Projectile p = new Projectile(owner.getWormsGame(), owner, (int)crosshair.getX(), (int)crosshair.getY(), this);
		float dCos = (float)Math.cos(crosshair.getAngle() * (Math.PI / 180));
		float dSin = (float)Math.sin(crosshair.getAngle() * (Math.PI / 180));
		
		float yPower = charge;
		if(owner.isFacingLeft())
			charge = -charge;
		p.addMotionVector(new MotionVector(charge * dSin, yPower * dCos, 150, 150));
		owner.getWormsGame().addProjectile(p);
	}

	@Override
	public int getMaxAngle() {
		return 180;
	}

	@Override
	public int getMinAngle() {
		return 0;
	}

}
