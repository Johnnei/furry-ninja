package engine.render;

import static org.lwjgl.opengl.GL15.*;

public abstract class Renderable {
	
	protected int glVertexId;
	protected int glColorId;
	private boolean needRenderUpdate;
	
	public Renderable() {
		glVertexId = glGenBuffers();
		glColorId = glGenBuffers();
		needRenderUpdate = false;
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
