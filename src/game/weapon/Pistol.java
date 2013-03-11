package game.weapon;

import game.display.Crosshair;
import game.entity.Cube;
import game.entity.Projectile;
import game.physics.MotionVector;

public class Pistol implements IWeapon {

	@Override
	public String getName() {
		return "pistol";
	}
	
	@Override
	public boolean hasExplosion() {
		return false;
	}

	@Override
	public void fire(Cube owner, Crosshair crosshair, float charge) {
		Projectile p = new Projectile(owner.getWormsGame(), owner, (int)crosshair.getX(), (int)crosshair.getY(), this);
		float dCos = (float)Math.cos(crosshair.getAngle() * (Math.PI / 180));
		float dSin = (float)Math.sin(crosshair.getAngle() * (Math.PI / 180));
		
		float yPower = charge;
		if(owner.isFacingLeft())
			charge = -charge;
		p.addMotionVector(new MotionVector(1.5F * charge * dSin, 0.75F * yPower * dCos, 150, 150));
		owner.getWormsGame().addProjectile(p);
	}

	@Override
	public int getStartingAmmo() {
		return 10;
	}

	@Override
	public int getMaxAmmo() {
		return 25;
	}

	@Override
	public int getMinFirePower() {
		return 3;
	}

	@Override
	public int getMaxFirePower() {
		return 7;
	}

	@Override
	public int getLandscapeCut() {
		return 4;
	}

	@Override
	public int getDamageRange() {
		return 4;
	}

	@Override
	public int getInnerDamage() {
		return 30;
	}

	@Override
	public int getOuterDamage() {
		return 30;
	}

	@Override
	public int getMaxAngle() {
		return 180;
	}

	@Override
	public int getMinAngle() {
		return 0;
	}

	@Override
	public float getExplosionScale() {
		return 0;
	}

	@Override
	public int getTextureWidth() {
		return 8;
	}

	@Override
	public int getTextureHeight() {
		return 8;
	}
	
	@Override
	public String getExplosionTextureName() {
		return null;
	}

}
