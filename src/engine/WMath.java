package engine;

public class WMath {
	
	/**
	 * Gives the positive value of f
	 * @param f
	 * @return Positive value of f
	 */
	public static float abs_f(float f) {
		return (f < 0) ? -f : f;
	}
	
	/**
	 * Round the paramaters up to the highest nearest integer.
	 * @param d
	 * @return
	 */
	public static int ceil_i(double d) {
		int i = (int)d;
		return (i < d) ? i + 1 : i;
	}

}
