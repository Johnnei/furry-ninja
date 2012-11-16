package game;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.awt.Rectangle;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.math.Point;

public class WorldChunk {
	
	private int glVertexId;
	private int glColorId;
	private int x, y, width, height;
	
	private boolean destroyed;
	
	public WorldChunk(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		glVertexId = glGenBuffers();
		glColorId = glGenBuffers();
		fillBuffers();
		destroyed = false;
	}
	
	public void fillBuffers() {
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3 * 4);
		vertexBuffer.put(new float[] {x, y, 0, x + width, y, 0, x + width, y + height, 0, x, y + height, 0});
		vertexBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(3 * 4);
		colorBuffer.put(new float[] {0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0});
		colorBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void render() {
		if(isDestroyed())
			return;
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glColorPointer(3, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4);
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroyed() {
		destroyed = true;
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}
	
	public Point getPoint() {
		return new Point(x, y);
	}

}
