package game;

import static engine.render.RenderObject.VERTEX_COLOR;

import java.awt.Rectangle;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.WMath;
import engine.math.Point;
import engine.render.RenderObject;

public class World {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 360;
	public static final int CHUNK_WIDTH = 4;
	public static final int CHUNK_HEIGHT = 4;
	public static final int chunksPerRow = (WIDTH / CHUNK_WIDTH);
	
	private WorldChunk[] chunks;
	private RenderObject renderObject;
	private boolean needUpdate;
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
		needUpdate = false;
		vertexCount = 0;
		renderObject = new RenderObject(VERTEX_COLOR, false, totalChunks);
		fillVertexBuffer();
		fillColorBuffer();
	}
	
	private void fillVertexBuffer() {
		vertexCount = 0;
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(2 * 4 * chunks.length);
		for(int i = 0; i < chunks.length; i++) {
			if(chunks[i].fillBuffers(vertexBuffer))
				vertexCount++;
		}
		vertexBuffer.flip();
		renderObject.resetBuffers(vertexCount);
		renderObject.updateVertex(vertexBuffer);
	}
	
	private void fillColorBuffer() {
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(3 * 4 * vertexCount);
		for(int i = 0; i < vertexCount; i++) {
			colorBuffer.put(new float[] {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0});
		}
		colorBuffer.flip();
		
		renderObject.updateColor(colorBuffer);
	}
	
	public void render() {
		if(needUpdate) {
			fillVertexBuffer();
			fillColorBuffer();
			needUpdate = false;
		}
		
		renderObject.render();
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
		needUpdate = true;
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

	/**
	 * Test if the given box is colliding with the world
	 * @param colBox The box which has to be tested
	 * @return true if the box is colliding, false if not
	 */
	public boolean collides(Rectangle colBox) {
		int chunkWidth = WMath.ceil_i(colBox.width / (double)CHUNK_WIDTH);
		int chunkHeight = WMath.ceil_i(colBox.height / (double)CHUNK_HEIGHT);
		int chunkX = colBox.x / CHUNK_WIDTH;
		int chunkY = (colBox.y - 360) / CHUNK_HEIGHT;
		for(int xOffset = 0; xOffset < chunkWidth; xOffset++) {
			for(int yOffset = 0; yOffset < chunkHeight; yOffset++) {
				int x = chunkX + xOffset;
				int y = chunkY + yOffset;
				if(!isDestroyed(x, y)) {
					if(getCollisionBox(x, y).intersects(colBox)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
