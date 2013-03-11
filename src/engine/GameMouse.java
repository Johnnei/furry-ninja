package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameMouse {
	
	public static final int LEFT_BUTTON = 0;
	
	private static GameMouse instance = new GameMouse();
	public static GameMouse getInstance() { return instance; }
	
	private boolean[] buttonStates;
	
	private GameMouse() {
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		buttonStates = new boolean[Mouse.getButtonCount()];
	}
	
	/**
	 * Gets the current mouse position
	 * @return The mouse X position 
	 */
	public int getX() {
		return Mouse.getX();
	}
	
	/**
	 * Gets the delta between the currentX and the last time this function was called
	 * @return deltaX between now and last call
	 */
	public int getDX() {
		return Mouse.getDX();
	}
	
	/**
	 * Gets the current mouse position
	 * @return The mouse Y position 
	 */
	public int getY() {
		return Display.getHeight() - Mouse.getY();
	}
	
	/**
	 * Gets the delta between the currentX and the last time this function was called
	 * @return deltaY between now and last call
	 */
	public int getDY() {
		return Mouse.getDY();
	}
	
	public boolean isClicked(int buttonIndex) {
		if(buttonIndex < buttonStates.length)
			return buttonStates[buttonIndex];
		throw new IllegalArgumentException("The connected mouse only has " + buttonStates.length + " buttons");
	}
	
	/**
	 * Updates all mouse events
	 */
	public void poll() {
		while(Mouse.next()) {
			Mouse.poll();
			if(Mouse.getEventButton() >= 0) {
				buttonStates[Mouse.getEventButton()] = Mouse.getEventButtonState();
			}
		}
	}
}
