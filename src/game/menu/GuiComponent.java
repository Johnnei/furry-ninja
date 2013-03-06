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
	
	public void setFps(int fps) {
	}

	@Override
	public boolean canDelete() {
		return false;
	}

}
