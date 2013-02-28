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
	
	/**
	 * Uses cosinus to calculate the angle between three points
	 * Note to self: COS: Cos = Overliggende / Schuine
	 * @param adjacentSide The adjacent side
	 * @param oposite The opposite side
	 * @return The angle in degrees	
	 */
	public float getAngleBetween(Point adjacentSide, Point oposite) {
		float opositeSideLength = adjacentSide.getY() - oposite.getY(); //Non-Squared!
		opositeSideLength *= opositeSideLength; //Squared
		float tiltedSideLength = (float)Math.sqrt(getSquaredDistanceTo(oposite));
		double ratio = WMath.divide(opositeSideLength, tiltedSideLength) % 1;
		float f = (float)Math.toDegrees(Math.acos(ratio));
		System.out.println("cos-1(" + opositeSideLength + "/" + tiltedSideLength + ") = cos-1(" + ratio + ") = " + f);
		return f;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

}
