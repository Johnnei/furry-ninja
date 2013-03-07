package game.physics;

import game.data.Gamemode;
import game.entity.Entity;

public class InverseGravityMotion implements IMotionVector {

	private int lifetime;
	
	public InverseGravityMotion() {
	}
	
	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public void onTick(Entity entity) {
		++lifetime;
	}

	@Override
	public float getMotionX() {
		return 0;
	}

	@Override
	public float getMotionY() {
		if(lifetime > 0)
			return - (Gamemode.GRAVITY + ((lifetime) / 10) * Gamemode.GRAVITY);
		return 0;
	}

}
