package game.physics;

import game.data.Gamemode;
import game.entity.Entity;

public class GravityMotion implements IMotionVector {

	private int airTime;
	
	public GravityMotion() {
		airTime = 0;
	}
	
	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public void onTick(Entity entity) {
		if(entity.isOnGround()) {
			airTime = 0;
		} else {
			++airTime;
		}
	}

	@Override
	public float getMotionX() {
		return 0;
	}

	@Override
	public float getMotionY() {
		if(airTime > 0)
			return Gamemode.GRAVITY + ((airTime / 5) * Gamemode.GRAVITY);
		else
			return 0f;
	}

}
