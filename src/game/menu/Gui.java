package game.menu;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

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
		this(parent, flags, 0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public Gui(Gui parent, int flags, float x, float y, float height, float width) {
		super(parent, flags);
		this.parent = parent;
		gameClose = false;
		components = new ArrayList<>();
	}
	
	/**
	 * Requests a close of the entire game<br/>
	 * This function will cascade up to all Gui's
	 */
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
	
	/**
	 * Adds a component to the Gui
	 * @param component The component to be added
	 */
	public void addComponent(GuiComponent component) {
		components.add(component);
	}
	
	/**
	 * Removes a component from the Gui
	 * @param component The component to be removed
	 */
	public void removeComponent(GuiComponent component) {
		components.remove(component);
	}
	
	@Override
	public void setFps(int fps) {
		for(GuiComponent component : components) {
			component.setFps(fps);
		}
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
		super.render(textRenderer);
		for(GuiComponent component : components) {
			component.render(textRenderer);
		}
	}
	
	/**
	 * Checks if a close has been requested
	 * @return true on closeRequest else false
	 */
	public boolean isCloseRequested() {
		return gameClose;
	}

}
