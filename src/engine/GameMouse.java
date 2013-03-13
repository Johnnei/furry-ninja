package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GameMouse {
	
	public static final int LEFT_BUTTON = 0;
	
	public static final byte CLICK_ON_DOWN = 0x1;
	public static final byte CLICK_ON_RELEASE = 0x2;
	
	private static GameMouse instance = new GameMouse();
	public static GameMouse getInstance() { return instance; }
	
	private boolean[] buttonStates;
	private boolean[] clickStates;
	private byte mode;
	/**
	 * Modifier which changes relocates the mouse internally so it all will fit within 1280x720
	 */
	private float mouseRatioWidth;
	private float mouseRatioHeight;
	
	private GameMouse() {
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		if(Mouse.getButtonCount() < 1) {
			throw new IllegalArgumentException("Mouse with 0 or less buttons detected");
		}
		mouseRatioWidth = (float)Game.WIDTH / Display.getWidth();
		mouseRatioHeight = (float)Game.HEIGHT / Display.getHeight();
		System.out.println("Mouse Ratio: " + mouseRatioWidth + ":" + mouseRatioHeight);
		mode = CLICK_ON_RELEASE;
		buttonStates = new boolean[Mouse.getButtonCount()];
		clickStates = new boolean[Mouse.getButtonCount()];
	}
	
	/**
	 * Changes the method on how a button press gets registers as a click<br/>
	 * @see 
	 */
	public void changeClickTrigger(byte mode) {
		this.mode = mode;
	}
	
	/**
	 * Gets the current mouse position
	 * @return The mouse X position 
	 */
	public int getX() {
		return (int)(Mouse.getX() * mouseRatioWidth);
	}
	
	/**
	 * Gets the delta between the currentX and the last time this function was called
	 * @return deltaX between now and last call
	 */
	public int getDX() {
		return (int)(Mouse.getDX() * mouseRatioWidth);
	}
	
	/**
	 * Gets the current mouse position
	 * @return The mouse Y position 
	 */
	public int getY() {
		return (int)(mouseRatioHeight * (Display.getHeight() - Mouse.getY()));
	}
	
	/**
	 * Gets the delta between the currentX and the last time this function was called
	 * @return deltaY between now and last call
	 */
	public int getDY() {
		return (int)(mouseRatioHeight * Mouse.getDY());
	}
	
	public boolean isButtonDown(int buttonIndex) {
		if(buttonIndex < buttonStates.length)
			return buttonStates[buttonIndex];
		throw new IllegalArgumentException(getMouseButtonError());
	}
	
	public boolean isButtonUp(int buttonIndex) {
		return !isButtonDown(buttonIndex);
	}
	
	public boolean isClicked(int buttonIndex, boolean consumeClick) {
		if(buttonIndex < buttonStates.length) {
			if(clickStates[buttonIndex]) {
				if(consumeClick) {
					clickStates[buttonIndex] = false;
				}
				return true;
			} else {
				return false;
			}
		}
		throw new IllegalArgumentException(getMouseButtonError());
	}
	
	public boolean isClicked(int buttonIndex) {
		return isClicked(buttonIndex, true);
	}
	
	/**
	 * Updates all mouse events
	 */
	public void poll() {
		for(int i = 0; i < clickStates.length; i++) {
			clickStates[i] = false;
		}
		while(Mouse.next()) {
			Mouse.poll();
			if(Mouse.getEventButton() >= 0) {
				boolean newState = Mouse.getEventButtonState();
				boolean oldState = buttonStates[Mouse.getEventButton()];
				buttonStates[Mouse.getEventButton()] = Mouse.getEventButtonState();
				if(mode == CLICK_ON_RELEASE) {
					clickStates[Mouse.getEventButton()] = !newState && oldState;
				} else if(mode == CLICK_ON_DOWN) {
					clickStates[Mouse.getEventButton()] = !oldState && newState;
				}
				System.out.println("Mousebutton clickState: " + clickStates[Mouse.getEventButton()]);
			}
		}
	}
	
	private final String getMouseButtonError() {
		return "The connected mouse only has " + buttonStates.length + " buttons";
	}
}
