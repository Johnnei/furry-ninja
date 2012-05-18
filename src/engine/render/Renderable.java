package engine.render;

import static org.lwjgl.opengl.GL15.*;

public abstract class Renderable {
	
	/**
	 * VBO Vertex data id
	 */
	protected int glVertexId;
	/**
	 * VBO Color data id
	 */
	protected int glColorId;
	/**
	 * Texture id
	 */
	protected int glTextureId;
	/**
	 * VBO Texture Coord Id
	 */
	protected int glTextureCoordId;
	/**
	 * Registers if the VBO data needs to be updated
	 */
	private boolean needRenderUpdate;
	
	public Renderable() {
		glVertexId = glGenBuffers();
		glColorId = glGenBuffers();
		needRenderUpdate = false;
	}
	
	public void overrideGlColorId(int glColorId) {
		glDeleteBuffers(glColorId);
		this.glColorId = glColorId;
	}
	
	/**
	 * Generates the VBO Color data to render
	 */
	public abstract void generateColorData();
	
	/**
	 * Generates the VBO Vertex data to render
	 */
	public abstract void generateVertexData();
	
	/**
	 * Generates the VBO Texture Data to render
	 */
	public abstract void generateTextureData();
	
	/**
	 * Renders the VBO to the screen
	 */
	public abstract void render();
	
	/**
	 * Sets the render update flag
	 * @param b The value for the flag
	 */
	public void setRenderUpdate(boolean b) {
		needRenderUpdate = b;
	}
	
	/**
	 * Gets the render update flag
	 * @return needRenderUpdate flag
	 */
	public boolean needRenderUpdate() {
		return needRenderUpdate;
	}

}
