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
	 * Uses cosinus to calculate the angle between three points<br/>
	 * WARNING: I assume that all textures are rotated 90 degrees by default
	 * Note to self: COS: Cos = Overliggende / Schuine
	 * @param dest The point the entity is heading to
	 * @return The angle in degrees	
	 */
	public float getAngleBetween(Point dest) {
		//Calculate Triangle Points and side-lengths
		Point adjacentSide = new Point(dest.getX(), getY());
		float opositeSideLength = adjacentSide.getY() - dest.getY();
		opositeSideLength *= opositeSideLength;
		float tiltedSideLength = getSquaredDistanceTo(dest);
		//Use Math to calculate the angle.
		double ratio = WMath.divide(opositeSideLength, tiltedSideLength);
		float f = (float)(180 / Math.PI * Math.acos(ratio)) - 90;
		System.out.println("Angle: " + (f + 90));
		//Apply some corrections dealing with negative rotation errors and right to left motion rotation
		if(dest.getX() - getX() < 0) {
			f -= 180;
			if(dest.getY() > getY()) {
				f = -f;
			}
		} else if(f < 0 && dest.getY() < getY()) {
			f = -f;
		}
		return f;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

}
