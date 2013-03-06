package game.menu;

import java.util.ArrayList;

import engine.render.TextRender;


public class Gui extends GuiComponent {
	
	/**
	 * The components on this Gui
	 */
	private ArrayList<GuiComponent> components;
	/**
	 * The parent Gui
	 */
	private Gui parent;
	/**
	 * Requests the game to close
	 */
	private boolean gameClose;
	
	public Gui(Gui parent, int flags) {
		super(flags);
		this.parent = parent;
		gameClose = false;
		components = new ArrayList<>();
	}
	
	public void requestClose() {
		if(parent != null)
			parent.requestClose();
		gameClose = true;
	}
	
	/**
	 * Removes itself from the parent and adds the given Gui to the parent
	 * @param newGui
	 */
	public void switchGui(Gui newGui) {
		parent.removeComponent(this);
		parent.addComponent(newGui);
	}
	
	public void addComponent(GuiComponent component) {
		components.add(component);
	}
	
	public void removeComponent(GuiComponent component) {
		components.remove(component);
	}
	
	@Override
	public void setFps(int fps) {
		for(GuiComponent component : components) {
			component.setFps(fps);
		}
	}
	
	protected Gui getParent() {
		return parent;
	}
	
	@Override
	public void onTick() {
		for(GuiComponent component : components) {
			component.onTick();
		}
	}

	@Override
	public boolean canDelete() {
		return false;
	}
	
	@Override
	public void render(TextRender textRenderer) {
		for(GuiComponent component : components) {
			component.render(textRenderer);
		}
	}
	
	public boolean isCloseRequested() {
		return gameClose;
	}

}
