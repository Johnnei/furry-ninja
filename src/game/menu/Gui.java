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
	
	public Gui(Gui parent, int flags) {
		super(flags);
		this.parent = parent;
		components = new ArrayList<>();
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
	
	public void setFps(int fps) {
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

}
