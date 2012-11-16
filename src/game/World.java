package game;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;

import java.awt.Rectangle;

import engine.math.Point;

public class World {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 360;
	public static final int CHUNK_WIDTH = 8;
	public static final int CHUNK_HEIGHT = 8;
	public static final int chunksPerRow = (WIDTH / CHUNK_WIDTH);
	
	private WorldChunk[] chunks;
	
	/**
	 * Generates a new World and saves the data into the buffers
	 */
	public World() {
		int totalChunks = (HEIGHT / CHUNK_HEIGHT) * chunksPerRow;
		chunks = new WorldChunk[totalChunks];
		for(int y = 0; y < (HEIGHT / CHUNK_HEIGHT); y++) {
			for(int x = 0; x < (WIDTH / CHUNK_WIDTH); x++) {
				int index = (y * chunksPerRow) + x;
				chunks[index] = new WorldChunk(x * CHUNK_WIDTH, 360 + y * CHUNK_HEIGHT, CHUNK_WIDTH, CHUNK_HEIGHT);
			}
		}
	}
	
	public void render() {
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		
		for(WorldChunk c : chunks) {
			c.render();
		}
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}
	
	public Rectangle getCollisionBox(int x, int y) {
		return chunks[(y * chunksPerRow) + x].getBoundingBox();
	}

	/**
	 * Destroys a worldChunk at x, y
	 * @param x
	 * @param y
	 */
	public void destroy(int x, int y) {
		chunks[(y * chunksPerRow) + x].destroyed();
		System.out.println("Destroyed WorldChunk(" + x + ", " + y + ")");
	}

	public Point getPoint(int x, int y) {
		return chunks[(y * chunksPerRow) + x].getPoint(); 
	}

	public boolean isDestroyed(int x, int y) {
		return chunks[(y * chunksPerRow) + x].isDestroyed();
	}

	public boolean collides(Rectangle colBox) {
		for(WorldChunk c : chunks) {
			if(c.isDestroyed())
				continue;
			if(c.getBoundingBox().intersects(colBox))
				return true;
		}
		return false;
	}

}
