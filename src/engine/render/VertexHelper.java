package engine.render;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class VertexHelper {
	
	private final static int BOTTOM_LEFT_X = 0;
	private final static int BOTTOM_LEFT_Y = 1;
	private final static int BOTTOM_RIGHT_X = 2;
	private final static int BOTTOM_RIGHT_Y = 3;
	private final static int TOP_RIGHT_X = 4;
	private final static int TOP_RIGHT_Y = 5;
	private final static int TOP_LEFT_X = 6;
	private final static int TOP_LEFT_Y = 7;
	
	private ArrayList<FRectangle> rectangles;
	private float[] points;
	private int nextFree;
	
	public VertexHelper(int size) {
		points = new float[size];
		nextFree = 0;
		rectangles = new ArrayList<>();
	}
	
	public void put(float x, float y, float width, float height) {
		rectangles.add(new FRectangle(x, y, width, height, nextFree));
		nextFree += 8;
	}
	
	public void put(float f) {
		points[nextFree++] = f;
	}
	
	public void put(float[] f) {
		System.arraycopy(f, 0, points, nextFree, f.length);
		nextFree += f.length;
	}
	
	/**
	 * Mirrors the stored vertex data<br/>
	 */
	public void mirror() {
		updatePoints();
		for(int i = 0; i < points.length; i += 8) {
			//Swap Left Bottom with Right Bottom
			swap(i + BOTTOM_LEFT_X, i + BOTTOM_RIGHT_X);
			swap(i + BOTTOM_LEFT_Y, i + BOTTOM_RIGHT_Y);
			//Swap Right Top with Left Top
			swap(i + TOP_RIGHT_X, i + TOP_LEFT_X);
			swap(i + TOP_RIGHT_Y, i + TOP_LEFT_Y);
		}
	}
	
	/**
	 * Applies a rotation on the first square in the helper<br/>
	 * Only the last rotation will be applied per <tt>put(x, y, width, height)</tt>
	 * @param angle The amount of degrees to rotate
	 */
	public void rotate(float angle) {
		rotate(angle, 0);
	}
	
	/**
	 * Applies a rotation on the <tt>squareIndex</tt> square in the helper<br/>
	 * Only the last rotation will be applied per <tt>put(x, y, width, height)</tt>
	 * @param angle The amount of degrees to rotate
	 * @param squareIndex The zero-based number of the insertion (1st put = 0)
	 */
	public void rotate(float angle, int squareIndex) {
		FRectangle rectangle = rectangles.get(squareIndex);
		if(rectangle == null)
			throw new IndexOutOfBoundsException("Rectangle " + squareIndex + " is not defined");
		final float radians = (float)Math.PI / 180F;
		float sin = (float)Math.sin(angle * radians);
		float cos = (float)Math.cos(angle * radians);
		points[rectangle.getOffset() + TOP_LEFT_X] = rectangle.getX() + getRotationX(rectangle.getOriginX(), rectangle.getOriginY(), sin, cos);
		points[rectangle.getOffset() + TOP_LEFT_Y] = rectangle.getY() + getRotationY(rectangle.getOriginX(), rectangle.getOriginY(), sin, cos);
		points[rectangle.getOffset() + TOP_RIGHT_X] = rectangle.getX() + getRotationX(rectangle.getOriginX() + rectangle.getWidth(), rectangle.getOriginY(), sin, cos);
		points[rectangle.getOffset() + TOP_RIGHT_Y] = rectangle.getY() + getRotationY(rectangle.getOriginX() + rectangle.getWidth(), rectangle.getOriginY(), sin, cos);
		points[rectangle.getOffset() + BOTTOM_LEFT_X] = rectangle.getX() + getRotationX(rectangle.getOriginX(), rectangle.getOriginY() + rectangle.getHeight(), sin, cos);
		points[rectangle.getOffset() + BOTTOM_LEFT_Y] = rectangle.getY() + getRotationY(rectangle.getOriginX(), rectangle.getOriginY() + rectangle.getHeight(), sin, cos);
		points[rectangle.getOffset() + BOTTOM_RIGHT_X] = rectangle.getX() + getRotationX(rectangle.getOriginX() + rectangle.getWidth(), rectangle.getOriginY() + rectangle.getHeight(), sin, cos);
		points[rectangle.getOffset() + BOTTOM_RIGHT_Y] = rectangle.getY() + getRotationY(rectangle.getOriginX() + rectangle.getWidth(), rectangle.getOriginY() + rectangle.getHeight(), sin, cos);
		rectangle.setWritten(true);
	}
	
	private float getRotationX(float x, float y, float sin, float cos) {
		return cos * x - sin * y;
	}
	
	private float getRotationY(float x, float y, float sin, float cos) {
		return sin * x + cos * y;
	}
	
	private void swap(int index, int index2) {
		float temp = points[index];
		points[index] = points[index2];
		points[index2] = temp;
	}
	
	public void updatePoints() {
		for(FRectangle rectangle : rectangles) {
			if(rectangle.isWritten())
				continue;
			points[rectangle.getOffset() + BOTTOM_LEFT_X] = rectangle.getX();
			points[rectangle.getOffset() + BOTTOM_LEFT_Y] = rectangle.getY() + rectangle.getHeight();
			points[rectangle.getOffset() + BOTTOM_RIGHT_X] = rectangle.getX() + rectangle.getWidth();
			points[rectangle.getOffset() + BOTTOM_RIGHT_Y] = rectangle.getY() + rectangle.getHeight();
			points[rectangle.getOffset() + TOP_RIGHT_X] = rectangle.getX() + rectangle.getWidth();
			points[rectangle.getOffset() + TOP_RIGHT_Y] = rectangle.getY();
			points[rectangle.getOffset() + TOP_LEFT_X] = rectangle.getX();
			points[rectangle.getOffset() + TOP_LEFT_Y] = rectangle.getY();
			rectangle.setWritten(true);
		}
	}
	
	/**
	 * Converts the vertexdata to a floatBuffer
	 * @return a FloatBuffer with the data (buffer is flipped)
	 */
	public FloatBuffer toBuffer() {
		updatePoints();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(points.length);
		buffer.put(points);
		buffer.flip();
		return buffer;
	}

}
