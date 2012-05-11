package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.awt.Rectangle;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class World {
	
	private int glVertexId;
	private int glColorId;
	
	/**
	 * Generates a new World and saves the data into the buffers
	 */
	public World() {
		glVertexId = glGenBuffers();
		glColorId = glGenBuffers();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3 * 4);
		vertexBuffer.put(new float[] {0, 720, 0, 1280, 720, 0, 1280, 360, 0, 0, 360, 0});
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
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glColorPointer(3, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4);
	}
	
	public Rectangle getCollisionBox() {
		return new Rectangle(0, 360, 1280, 360);
	}

}
