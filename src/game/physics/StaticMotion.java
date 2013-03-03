package game.physics;

import game.entity.Entity;

public class StaticMotion implements IMotionVector {

	private float xMotion;
	private float yMotion;
	
	public StaticMotion(float xMotion, float yMotion) {
		this.xMotion = xMotion;
		this.yMotion = yMotion;
	}
	
	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public void onTick(Entity entity) {
	}

	@Override
	public float getMotionX() {
		return xMotion;
	}

	@Override
	public float getMotionY() {
		return yMotion;
	}

}
