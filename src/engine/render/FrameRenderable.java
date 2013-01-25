package engine.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public abstract class FrameRenderable extends Renderable {
	
	private static long SIZE_GL_FLOAT = 4L;
	
	/**
	 * The amount of frames to render
	 */
	private int frameCount;
	private int currentFrame;
	private int currentFrameTicks;
	private int frameDuration;
	private boolean destroyOnAnimationEnd;
	private boolean canDestroy;
	
	public FrameRenderable(int frameCount, int frameDuration) {
		this(frameCount, frameDuration, false);
	}
	
	public FrameRenderable(int frameCount, int frameDuration, boolean destroyOnEnd) {
		super();
		this.frameCount = frameCount;
		this.destroyOnAnimationEnd = destroyOnEnd;
		this.canDestroy = false;
		this.frameDuration = frameDuration;
	}
	
	@Override
	/**
	 * This function generates the textureCoordId and packs them into a single buffer
	 */
	public void generateTextureData() {
		if(glTextureId == GL_NONE) {
			throw new RuntimeException("[FrameRenderable.generateTextureData()] No Texture ID");
		} else {
			FloatBuffer textureCoordBuffer = BufferUtils.createFloatBuffer(8 * frameCount);
			for(int i = 0; i < frameCount; i++) {
				float frameWidth = (1F / frameCount);
				float minX = i * frameWidth;
				float maxX = (i + 1) * frameWidth;
				System.out.println("frameWidth: " + frameWidth + ", minX: " + minX + ", maxX: " + maxX);
				textureCoordBuffer.put(new float[] { minX, 1 }); //Left Bottom
				textureCoordBuffer.put(new float[] { maxX, 1 }); //Right Bottom
				textureCoordBuffer.put(new float[] { maxX, 0 }); //Right Top
				textureCoordBuffer.put(new float[] { minX, 0 }); //Left Top
			}
			textureCoordBuffer.flip();
			glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
			glBufferData(GL_ARRAY_BUFFER, textureCoordBuffer, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
		}
	}
	
	/**
	 * Gets the frame number and increments it for the next frame
	 * @return
	 * The current frame number
	 */
	private int getFrameId() {
		int id = 0;
		if(currentFrameTicks++ >= frameDuration) {
			id = currentFrame++;
			currentFrameTicks = 0;
		} else {
			id = currentFrame;
		}
		if(currentFrame >= frameCount) {
			currentFrame = 0;
			if(destroyOnAnimationEnd)
				canDestroy = true;
			return 0;
		}
		return id;
	}
	
	@Override
	/**
	 * Attaches the correct frame to the buffer
	 * This should be called before glDrawArrays
	 */
	public void render() {
		long bufferOffset = getFrameId() * 8 * SIZE_GL_FLOAT;
		System.out.println("Current Frame Offset: " + bufferOffset);
		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glTexCoordPointer(2, GL_FLOAT, 0, bufferOffset);
	}
	
	@Override
	public boolean canDelete() {
		return canDestroy;
	}

}
