package game.menu;

import engine.render.Renderable;

public class GuiComponent extends Renderable {
	
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	
	public GuiComponent(int flags) {
		super(flags);
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

}
