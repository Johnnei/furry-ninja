package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.awt.Rectangle;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

import engine.math.Point;

public class World {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 360;
	public static final int CHUNK_WIDTH = 4;
	public static final int CHUNK_HEIGHT = 4;
	public static final int chunksPerRow = (WIDTH / CHUNK_WIDTH);
	
	private WorldChunk[] chunks;
	private int glVertexId;
	private int glColorId;
	private boolean needVertexUpdate;
	private int vertexCount;
	
	/**
	 * Generates a new World and saves the data into the buffers
	 */
	public World() {
		int totalChunks = (HEIGHT / CHUNK_HEIGHT) * chunksPerRow;
		chunks = new WorldChunk[totalChunks];
		for(int y = 0; y < (HEIGHT / CHUNK_HEIGHT); y++) {
			for(int x = 0; x < (WIDTH / CHUNK_WIDTH); x++) {
				int index = (y * chunksPerRow) + x;
				chunks[index] = new WorldChunk(this, x * CHUNK_WIDTH, 360 + y * CHUNK_HEIGHT, CHUNK_WIDTH, CHUNK_HEIGHT);
			}
		}
		needVertexUpdate = false;
		vertexCount = 0;
		glVertexId = glGenBuffers();
		fillColor();
		fillVertexBuffer();
	}
	
	private void fillVertexBuffer() {
		vertexCount = 0;
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3 * 4 * chunks.length);
		for(int i = 0; i < chunks.length; i++) {
			if(chunks[i].fillBuffers(vertexBuffer))
				vertexCount++;
		}
		vertexBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void fillColor() {
		glColorId = glGenBuffers();
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(3 * 4 * chunks.length);
		for(int i = 0; i < chunks.length; i++) {
			colorBuffer.put(new float[] {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0});
		}
		colorBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void render() {
		if(needVertexUpdate || Keyboard.isKeyDown(Keyboard.KEY_R)) {
			fillVertexBuffer();
			needVertexUpdate = false;
		}
		
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glColorPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4 * vertexCount);
		
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
		needVertexUpdate = true;
	}

	public Point getPoint(int x, int y) {
		return chunks[(y * chunksPerRow) + x].getPoint(); 
	}

	public boolean isDestroyed(int x, int y) {
		int index = (y * chunksPerRow) + x;
		if(index < 0 || index >= chunks.length) {
			if(y < 0)
				return true;
			return false;
		}
		if(chunks[index] == null) {
			return false;
		}
		return chunks[index].isDestroyed();
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
