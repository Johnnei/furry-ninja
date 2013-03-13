package game.menu;

import engine.render.Renderable;

public class GuiComponent extends Renderable {

	/**
	 * The parent Gui
	 */
	private Gui parent;
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	
	public GuiComponent(Gui parent, int flags) {
		super(flags);
		this.parent = parent;
	}
	
	final protected Gui getParent() {
		return parent;
	}
	
	/**
	 * Sets the measured fps<br/>
	 * Only used in {@link GuiGame}
	 * @param fps The measured fps
	 */
	public void setFps(int fps) {
	}

	@Override
	public boolean canDelete() {
		return false;
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
}
