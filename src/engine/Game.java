package engine;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import engine.render.TextRender;
import game.menu.Gui;
import game.menu.GuiGame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Game {

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
		gui = new Gui(null, 0);
		gui.addComponent(new GuiGame(gui));
		textRenderer = new TextRender();
		long lastTick = getCurrentMillis();
		long lastFps = getCurrentMillis();
		int fps = 0;
		
		while(!Display.isCloseRequested()) {
			
			//Game Logic Section
			while(getCurrentMillis() - lastTick >= 50) { //20 ticks per second
				lastTick += 50;
				gui.onTick();
			}
			
			//Game Render Section
			gui.render(textRenderer);
			
			//LWJGL Updating
			Display.update();
			//Display.sync(60);
			
			++fps;
			if(getCurrentMillis() - lastFps >= 1000) {
				gui.setFps(fps);
				fps = 0;
				lastFps += 1000;
			}
		}
	}
	
	/**
	 * Gets the current time in millis by dividing the current nano time by 100'000
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
			if((ms - timeSlept) > 0)
				sleep(ms - timeSlept);
		}
	}
	
	/**
	 * Initialising the display and OpenGL basic setup
	 */
	private void initOpenGL() {
		try {
			
			//Init Display
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.setTitle("Furry Ninja v0.1a");
			//Display.setVSyncEnabled(true);
			Display.create();
			
			//Init OpenGL Frame
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
