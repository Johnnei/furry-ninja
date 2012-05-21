package engine.math;

import engine.WMath;

public class Point {
	
	private float x;
	private float y;
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the distance between this point and point p and gives the squared value (distance * distance)
	 * @param p The point to messure the distance with
	 * @return Squared distance from this point to point p
	 */
	public float getSquaredDistanceTo(Point p) {
		
		Point left = new Point(WMath.min_f(p.getX(), getX()), WMath.min_f(p.getY(), getY()));
		Point up = new Point(left.getX(), WMath.max_f(p.getY(), getY()));
		Point right = new Point(WMath.max_f(p.getX(), getX()), left.getY());
		
		float a = up.getY() - left.getY();
		float b = right.getX() - left.getX();

		return (a * a) + (b * b);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

}
