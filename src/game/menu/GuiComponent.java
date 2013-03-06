package game.menu;

import engine.render.Renderable;

public class GuiComponent extends Renderable {
	
	protected float x;
	protected float y;
	
	public GuiComponent(int flags) {
		super(flags);
	}

	@Override
	public boolean canDelete() {
		return false;
	}

}
