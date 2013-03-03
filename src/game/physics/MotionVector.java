package game.physics;

import game.entity.Entity;

public class MotionVector implements IMotionVector {
	
	/**
	 * The xMotion gained from this vector
	 */
	private float xMotion;
	/**
	 * The yMotion gained from this vector
	 */
	private float yMotion;
	
	/**
	 * The lifetime of the xMotion
	 */
	private int xMotionLifetime;
	/**
	 * The lifetime of the yMotion
	 */
	private int yMotionLifetime;
	
	/**
	 * The amount of ticks this motionVector is alive
	 */
	private int lifetime;
	
	/**
	 * Creates a motionVector with degradation<br/>
	 * The degradation is defined in this starting from most to least
	 * @param xMotion
	 * @param yMotion
	 * @param xMotionLifetime
	 * @param yMotionLifetime
	 */
	public MotionVector(float xMotion, float yMotion, int xMotionLifetime, int yMotionLifetime) {
		lifetime = 1;
		this.xMotion = xMotion;
		this.yMotion = yMotion;
		this.xMotionLifetime = xMotionLifetime + 1;
		this.yMotionLifetime = yMotionLifetime + 1;
	}
	
	/* (non-Javadoc)
	 * @see game.physics.IMotionVector#canDelete()
	 */
	@Override
	public boolean canDelete() {
		return (xMotionLifetime <= lifetime) && (yMotionLifetime <= lifetime);
	}
	
	/* (non-Javadoc)
	 * @see game.physics.IMotionVector#onTick()
	 */
	@Override
	public void onTick(Entity entity) {
		++lifetime;
	}
	
	/* (non-Javadoc)
	 * @see game.physics.IMotionVector#getMotionY()
	 */
	@Override
	public float getMotionY() {
		final int ticks = Math.min(lifetime, yMotionLifetime);
		final float reductionPerTick = yMotion / yMotionLifetime; 
		return yMotion - (ticks * reductionPerTick);
	}
	
	/* (non-Javadoc)
	 * @see game.physics.IMotionVector#getMotionX()
	 */
	@Override
	public float getMotionX() {
		final int ticks = Math.min(lifetime, xMotionLifetime);
		final float reductionPerTick = xMotion / xMotionLifetime; 
		return xMotion - (ticks * reductionPerTick);
	}

}
