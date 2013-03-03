package game.physics;

import game.entity.Entity;

public interface IMotionVector {

	/**
	 * Check if this Motion wont affect the resulting sum of motions
	 * @return true if the motion is 0/0
	 */
	public boolean canDelete();

	/**
	 * A function which will be called each game update
	 * @param entity TODO
	 */
	public void onTick(Entity entity);

	/**
	 * The velocity on the x axis
	 * @return xMotion
	 */
	public float getMotionX();
	
	/**
	 * The velocity on the y axis
	 * @return yMotion
	 */
	public float getMotionY();

}