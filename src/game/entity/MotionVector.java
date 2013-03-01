package game.entity;

public class MotionVector {
	
	public final int NO_DECREASE = 1;
	
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
	 * Use the value {@link #NO_DECREASE} (1) to have no degradation in a motion<br/>
	 * The degradation is defined in this starting from most to least
	 * @param xMotion
	 * @param yMotion
	 * @param xMotionLifetime
	 * @param yMotionLifetime
	 */
	public MotionVector(float xMotion, float yMotion, int xMotionLifetime, int yMotionLifetime) {
		lifetime = NO_DECREASE;
		this.xMotion = xMotion;
		this.yMotion = yMotion;
		this.xMotionLifetime = xMotionLifetime + NO_DECREASE;
		this.yMotionLifetime = yMotionLifetime + NO_DECREASE;
	}
	
	/**
	 * Creates a motionVector without degradation
	 * @param xMotion
	 * @param yMotion
	 */
	public MotionVector(float xMotion, float yMotion) {
		this(xMotion, yMotion, 0, 0);
	}
	
	public boolean canDelete() {
		if(xMotionLifetime == NO_DECREASE && yMotionLifetime == NO_DECREASE)
			return false;
		return (xMotionLifetime <= lifetime) && (yMotionLifetime <= lifetime);
	}
	
	public void onTick() {
		++lifetime;
	}
	
	public float getMotionY() {
		if(yMotionLifetime != NO_DECREASE)
			return yMotion / lifetime;
		return yMotion;
	}
	
	public float getMotionX() {
		if(xMotionLifetime != NO_DECREASE)
			return xMotion / lifetime;
		return xMotion;
	}

}
