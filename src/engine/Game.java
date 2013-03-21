package engine;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import engine.render.TextRender;
import game.menu.Gui;
import game.menu.GuiMenu;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Game {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	private Gui gui;
	private TextRender textRenderer;

	/**
	 * Creates a new instance of the Game Engine
	 */
	public Game() {
	}

	/**
	 * Runs the entire game until close will be requested or crashes
	 */
	public void run() {
		initOpenGL();
		gui = new Gui(null, 0, 0, 0, Display.getWidth(), Display.getHeight());
		gui.addComponent(new GuiMenu(gui));
		textRenderer = new TextRender();
		long lastTick = getCurrentMillis();
		long lastFps = getCurrentMillis();
		int fps = 0;

		while (!Display.isCloseRequested() && !gui.isCloseRequested()) {

			// Game Logic Section
			while (getCurrentMillis() - lastTick >= 50) { // 20 ticks per second
				lastTick += 50;
				GameMouse.getInstance().poll();
				GameKeyboard.getInstance().poll();
				gui.onTick();
			}

			// Reset Screen
			glClear(GL_COLOR_BUFFER_BIT);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			// Game Render Section
			gui.render(textRenderer);

			// LWJGL Updating
			Display.update();
			// Display.sync(60);

			++fps;
			if (getCurrentMillis() - lastFps >= 1000) {
				gui.setFps(fps);
				fps = 0;
				lastFps += 1000;
			}
		}
	}

	/**
	 * Gets the current time in millis by dividing the current nano time by
	 * 100'000
	 * 
	 * @return
	 */
	public static long getCurrentMillis() {
		return System.nanoTime() / 1000000;
	}

	public static void sleep(long ms) {
		System.out.println("Sleeping for " + ms + "ms");
		long timeSlept = System.currentTimeMillis();
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			timeSlept = System.currentTimeMillis() - timeSlept;
			if ((ms - timeSlept) > 0)
				sleep(ms - timeSlept);
		}
	}

	/**
	 * Initialising the display and OpenGL basic setup
	 */
	private void initOpenGL() {
		try {

			// Init Display
			int maxWidth = Math.min(1280, Display.getDesktopDisplayMode().getWidth());
			int maxHeight = Math.min(720, Display.getDesktopDisplayMode().getHeight());
			DisplayMode selectedMode = new DisplayMode(0, 0);
			DisplayMode[] mode = Display.getAvailableDisplayModes();
			for (DisplayMode m : mode) {
				if (m.getWidth() <= maxWidth && m.getHeight() <= maxHeight) {
					if (m.getWidth() > selectedMode.getWidth() && m.getHeight() > selectedMode.getHeight()) {
						selectedMode = m;
					}
				}
			}
			Display.setDisplayMode(selectedMode);
			Display.setTitle("Furry Ninja v0.1a");
			// Display.setVSyncEnabled(true);
			Display.create();

			// Init OpenGL Frame
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, 1280, 720, 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
