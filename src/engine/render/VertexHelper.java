package engine.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class VertexHelper {
	
	private float[] points;
	private int nextFree;
	
	public VertexHelper(int size) {
		points = new float[size];
		nextFree = 0;
	}
	
	public void put(float f) {
		points[nextFree++] = f;
	}
	
	public void put(float[] f) {
		System.arraycopy(f, 0, points, nextFree, f.length);
	}
	
	/**
	 * Mirrors the stored vertex data
	 */
	public void mirror() {
		for(int i = 0; i < points.length; i += 8) {
			//Swap Left Bottom with Right Bottom
			swap(i, i + 2);
			swap(i + 1, i + 3);
			//Swap Right Top with Left Top
			swap(i + 4, i + 6);
			swap(i + 5, i + 7);
		}
	}
	
	/**
	 * Applies a rotation to the vertex data
	 * @param angle The amount of degrees to rotate
	 */
	public void rotate(int angle, int width, int height) {
		angle %= 360;
		float xOffset = (float)(width * Math.sin(angle * (Math.PI / 360D)));
		float yOffset = (float)(height * Math.cos(-angle * (Math.PI / 180D)));
		//float yOffset = (float)(height * Math.cos(angle * (Math.PI / 360D)));
		System.out.println("rotate(" + angle + ", " + width + ", " + height + "); " + xOffset + ", " + yOffset);
		for(int i = 0; i < points.length; i += 8) {
			//Left Bottom
			points[i    ] += xOffset;
			points[i + 1] -= yOffset;
			//Right Bottom
			points[i + 2] -= xOffset;
			points[i + 3] += yOffset;
			//Right Top
			points[i + 4] -= xOffset;
			points[i + 5] += yOffset;
			//Left Top
			points[i + 6] += xOffset;
			points[i + 7] -= yOffset;
		}
	}
	
	private void swap(int index, int index2) {
		float temp = points[index];
		points[index] = points[index2];
		points[index2] = temp;
	}
	
	/**
	 * Converts the vertexdata to a floatBuffer
	 * @return a FloatBuffer with the data (buffer is flipped)
	 */
	public FloatBuffer toBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(points.length);
		buffer.put(points);
		buffer.flip();
		return buffer;
	}

}
