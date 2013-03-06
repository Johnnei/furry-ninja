package engine.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class ColorHelper {
	
	private float r;
	private float g;
	private float b;
	
	public ColorHelper(float r, float g, float b) {
		this.r = r / 255F;
		this.g = g / 255F;
		this.b = b / 255F;
	}
	
	/**
	 * Generates the color buffer for the given colors
	 * @return Flipped FloatBuffer
	 */
	public FloatBuffer getBuffer() {
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4 * 3);
		for(int i = 0; i < 4; i++) {
			colorBuffer.put(new float[] {r, g, b});
		}
		colorBuffer.flip();
		return colorBuffer;
	}

}
