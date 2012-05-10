package engine;

import game.WormsGame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Game {

	private WormsGame game;
	
	/**
	 * Creates a new instance of the Game Engine
	 */
	public Game() {
		game = new WormsGame();
	}
	
	/**
	 * Runs the entire game until close will be requested or crashes
	 */
	public void run() {
		initOpenGL();
		long lastTick = getCurrentMillis();
		
		while(!Display.isCloseRequested()) {
			
			//Game Logic Section
			while(getCurrentMillis() - lastTick >= 50) { //20 ticks per second
				lastTick += 50;
				game.onTick();
			}
			
			//Game Render Section
			game.render();
			
			//LWJGL Updating
			Display.update();
			Display.sync(60);
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
			Display.setTitle("Furry Ninja");
			Display.setVSyncEnabled(true);
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
