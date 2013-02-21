package engine.render;

import static engine.render.RenderObject.VERTEX_TEXTURE;

public abstract class FrameRenderable extends Renderable {
	
	private static final long SIZE_GL_FLOAT = 4L;
	
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
		super(VERTEX_TEXTURE);
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
		if(renderObject.getTexture() == null) {
			throw new RuntimeException("[FrameRenderable.generateTextureData()] No Texture");
		} else {
			Texture texture = renderObject.getTexture();
			int frameWidth = texture.getWidth() / frameCount;
			for(int i = 0; i < frameCount; i++) {
				int x = i * frameWidth;
				texture.addSubTexture(x, 0, frameWidth, texture.getHeight());
			}
			renderObject.resetBuffers();
			renderObject.updateTexture();
		}
	}
	
	/**
	 * Updates the animation frame
	 */
	public void onTick() {
		if(++currentFrameTicks % frameDuration == 0) {
			if(++currentFrame == frameCount) {
				currentFrame = 0;
				if(destroyOnAnimationEnd)
					canDestroy = true;
			}
		}
	}
	
	@Override
	/**
	 * Attaches the correct frame to the buffer
	 * This should be called before glDrawArrays
	 */
	public void render() {
		long textureOffset = (currentFrame + 1) * 8 * SIZE_GL_FLOAT;
		renderObject.render(textureOffset);
	}
	
	@Override
	public boolean canDelete() {
		return canDestroy;
	}

}
