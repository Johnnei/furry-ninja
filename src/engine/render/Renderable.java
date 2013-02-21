package engine.render;

public abstract class Renderable {
	
	/**
	 * The VBO RenderObject
	 */
	protected RenderObject renderObject;
	/**
	 * Registers if the VBO data needs to be updated
	 */
	private boolean needRenderUpdate;
	
	public Renderable(int flags, boolean is3D, int vertexCount, Texture texture) {
		this();
		renderObject = new RenderObject(flags, is3D, vertexCount, texture);
	}
	
	public Renderable(int flags, boolean is3D, int vertexCount) {
		this();
		renderObject = new RenderObject(flags, is3D, vertexCount);
	}
	
	public Renderable(int flags, boolean is3D, Texture texture) {
		this();
		renderObject = new RenderObject(flags, is3D, texture);
	}
	
	public Renderable(int flags, boolean is3D) {
		this();
		renderObject = new RenderObject(flags, is3D);
	}
	
	public Renderable(int flags) {
		this();
		renderObject = new RenderObject(flags);
	}
	
	private Renderable() {
		needRenderUpdate = false;
	}
	
	public abstract boolean canDelete();
	
	/**
	 * Deletes the buffered data
	 */
	public void onDelete() {
		renderObject.delete();
	}
	
	/**
	 * Generates the VBO Color data to render
	 */
	public void generateColorData() {
		
	}
	
	/**
	 * Generates the VBO Vertex data to render
	 */
	public void generateVertexData() {
		
	}
	
	/**
	 * Generates the VBO Texture Data to render
	 */
	public void generateTextureData() {
		
	}
	
	/**
	 * Renders the VBO to the screen
	 */
	public void render() {
		renderObject.render();
	}
	
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
	
	public void bindColor() {
		renderObject.bindColor();
	}

}
