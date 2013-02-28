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
	 * Gives the higher value of the three
	 * @param f1
	 * @param f2
	 * @param f3
	 * @return
	 */
	public static float max_f(float f1, float f2, float f3) {
		if(f1 > f2) {
			return max_f(f1, f3);
		} else {
			return max_f(f2, f3);
		}
	}
	
	/**
	 * Returns the bigger one of the two
	 * @param f1
	 * @param f2
	 * @return
	 */
	public static float max_f(float f1, float f2) {
		return (f1 > f2) ? f1 : f2;
	}
	
	/**
	 * Returns the smaller one of the two
	 * @param f1
	 * @param f2
	 * @return
	 */
	public static float min_f(float f1, float f2) {
		return (f1 < f2) ? f1 : f2;
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
	
	/**
	 * Divides <tt>f/f2</tt><br/>
	 * If <tt>f2</tt> is 0 it will return 0 instead of DivideByZeroException
	 * @param f
	 * @param f2
	 * @return <tt>f/f2</tt> or 0 if <tt>f2</tt> is 0
	 */
	public static double divide(double f, double f2) {
		if(f2 == 0)
			return 0f;
		return f / f2;
	}

}
