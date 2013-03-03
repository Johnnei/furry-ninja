package engine.render;

/**
 * An Immutable Rectangle object with float precision
 * @author Johnnei LAP
 *
 */
public class FRectangle {
	
	private float x;
	private float y;
	private float width;
	private float height;
	/**
	 * The offset within the vertex helper
	 */
	private int offset;
	/**
	 * If the vertex data has been written before
	 */
	private boolean written;
	
	public FRectangle(float x, float y, float width, float height, int offset) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.offset = offset;
		written = false;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getOriginX() {
		return width / 2;
	}
	
	public float getOriginY() {
		return height / 2;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public boolean isWritten() {
		return written;
	}
	
	public void setWritten(boolean b) {
		written = b;
	}

}
