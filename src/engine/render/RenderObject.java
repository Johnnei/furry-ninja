package engine.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;

public class RenderObject {
	
	private static final int BYTES_PER_FLOAT = 4;
	private static final int VERTEX_POINTS_PER_SQUARE = 4;
	private static final int TEXTURE_PER_VERTEX = 2;
	private static final int COLOR_PER_VERTEX = 3;
	
	private static final int OFFSET_TEXTURE = 0x0;
	private static final int OFFSET_COLOR = 0x1;
	
	public static final int VERTEX = 0x01;
	public static final int TEXTURE = 0x02;
	public static final int COLOR = 0x04;
	
	public static final int VERTEX_TEXTURE = VERTEX | TEXTURE;
	public static final int VERTEX_COLOR = VERTEX | COLOR;
	
	/**
	 * The flags for this render object
	 */
	private int flags;
	/**
	 * The openGL Id for this VBO
	 */
	private int glId;
	/**
	 * The amount of coords point per vertex<br/>
	 * 2D (2 points X, Y), 3D (3 Points: X, Y, Z)
	 */
	private int verticesPoints;
	/**
	 * The amount of shapes in this object
	 */
	private int vertexCount;
	/**
	 * The texture to be drawn on this object
	 */
	private Texture texture;
	/**
	 * The offsets of color/texture
	 */
	private long[] bufferOffset;
	
	public RenderObject(int flags, boolean is3D, int vertexCount, Texture texture) {
		this.flags = flags;
		this.vertexCount = vertexCount;
		if(texture != null)
			this.texture = texture;
		verticesPoints = (is3D) ? 3 : 2;
		bufferOffset = new long[2];
		if(flags != 0) {
			glId = glGenBuffers();
			calculateOffsets();
		}
	}
	
	public RenderObject(int flags, boolean is3D, int vertexCount) {
		this(flags, is3D, vertexCount, null);
	}
	
	public RenderObject(int flags, boolean is3D, Texture texture) {
		this(flags, is3D, 1, texture);
	}
	
	public RenderObject(int flags, boolean is3D) {
		this(flags, is3D, 1, null);
	}
	
	public RenderObject(int flags) {
		this(flags, false, 1, null);
	}
	
	/**
	 * Redefines the buffers with a new buffer and also recalculates the bufferSize<br/>
	 * This also allows to resize the vertexCount
	 */
	public void resetBuffers(int vertexCount) {
		this.vertexCount = vertexCount;
		resetBuffers();
	}
	
	/**
	 * Redefines the buffers with a new buffer and also recalculates the bufferSize
	 */
	public void resetBuffers() {
		glDeleteBuffers(glId);
		glId = glGenBuffers();
		calculateOffsets();
	}

	private void calculateOffsets() {
		int offset = BYTES_PER_FLOAT * verticesPoints * vertexCount * VERTEX_POINTS_PER_SQUARE;
		bufferOffset[OFFSET_TEXTURE] = offset;
		if(hasFlag(TEXTURE)) {
			if(texture == null) {
				offset += BYTES_PER_FLOAT * TEXTURE_PER_VERTEX * vertexCount * VERTEX_POINTS_PER_SQUARE;
			} else {
				offset += BYTES_PER_FLOAT * texture.getBufferSize();
			}
		}
		bufferOffset[OFFSET_COLOR] = offset;
		offset += BYTES_PER_FLOAT * COLOR_PER_VERTEX * VERTEX_POINTS_PER_SQUARE * vertexCount;
		
		glBindBuffer(GL_ARRAY_BUFFER, glId);
		glBufferData(GL_ARRAY_BUFFER, offset, GL_DYNAMIC_DRAW);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void setTexture(String filename) {
		texture = new Texture(filename);
	}
	
	public Texture getTexture() {
		return texture;
	}

	/**
	 * Checks if this RenderObject has a flag
	 * @param flag
	 * @return
	 */
	public boolean hasFlag(int flag) {
		return (flags & flag) == flag;
	}
	
	public void updateVertex(VertexHelper helper) {
		updateVertex(helper.toBuffer());
	}
	
	public void updateVertex(FloatBuffer buffer) {
		if(hasFlag(VERTEX)) {
			glBindBuffer(GL_ARRAY_BUFFER, glId);
			glBufferSubData(GL_ARRAY_BUFFER, 0L, buffer);
		} else {
			System.err.println("RenderObject.updateVertex() No vertex data defined");
		}
	}
	
	public void updateColor(ColorHelper helper) {
		updateColor(helper.getBuffer());
	}
	
	public void updateColor(FloatBuffer buffer) {
		if(hasFlag(COLOR)) {
			glBindBuffer(GL_ARRAY_BUFFER, glId);
			glBufferSubData(GL_ARRAY_BUFFER, bufferOffset[OFFSET_COLOR], buffer);
		} else {
			System.err.println("RenderObject.updateColor() No color data defined");
		}
	}
	
	public void updateTexture() {
		if(hasFlag(TEXTURE) && texture != null) {
			glBindBuffer(GL_ARRAY_BUFFER, glId);
			glBufferSubData(GL_ARRAY_BUFFER, bufferOffset[OFFSET_TEXTURE], texture.fillBuffer());
		} else {
			System.err.println("RenderObject.updateTexture() No texture data defined");
		}
	}
	
	/**
	 * Binds the color data
	 */
	public void bindColor() {
		if(hasFlag(COLOR)) {
			glBindBuffer(GL_ARRAY_BUFFER, glId);
			glColorPointer(3, GL_FLOAT, 0, bufferOffset[OFFSET_COLOR]);
		} else {
			System.err.println("RenderObject.bindColor() No Color data defined");
		}
	}
	
	/**
	 * Renders the object with the default calculated offsets
	 */
	public void render() {
		render(bufferOffset[OFFSET_TEXTURE], bufferOffset[OFFSET_COLOR]);
	}
	
	/***
	 * Renders the object with the default calculated color offset and a custom texture Offset
	 * @param textureOffset The offset in the entire buffer on which the texture coord data is expected
	 */
	public void render(long textureOffset) {
		render(textureOffset, bufferOffset[OFFSET_COLOR]);
	}
	
	/**
	 * Renders the object with custom offsets
	 * @param textureOffset The offset in the entire buffer on which the texture coord data is expected
	 * @param colorOffset The offset in the entire buffer on which the color data is expected
	 */
	public void render(long textureOffset, long colorOffset) {
		bind();
		glBindBuffer(GL_ARRAY_BUFFER, glId);
		if(hasFlag(VERTEX))
			glVertexPointer(verticesPoints, GL_FLOAT, 0, 0L);
		if(hasFlag(TEXTURE)) {
			texture.bind();
			glTexCoordPointer(TEXTURE_PER_VERTEX, GL_FLOAT, 0, textureOffset);
		}
		if(hasFlag(COLOR))
			glColorPointer(COLOR_PER_VERTEX, GL_FLOAT, 0, colorOffset);
		
		glDrawArrays(GL_QUADS, 0, VERTEX_POINTS_PER_SQUARE * vertexCount);
		
		unbind();
	}
	
	private void bind() {
		if(hasFlag(VERTEX)) {
			glEnableClientState(GL_VERTEX_ARRAY);
		}
		if(hasFlag(TEXTURE)) {
			glEnable(GL_TEXTURE_2D);
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glActiveTexture(GL_TEXTURE0);
		}
		if(hasFlag(COLOR)) {
			glEnableClientState(GL_COLOR_ARRAY);
		}
	}
	
	private void unbind() {
		if(hasFlag(COLOR)) {
			glDisableClientState(GL_COLOR_ARRAY);
		}
		if(hasFlag(TEXTURE)) {
			glDisable(GL_TEXTURE_2D);
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		}
		if(hasFlag(VERTEX)) {
			glDisableClientState(GL_VERTEX_ARRAY);
		}
	}
	
	/**
	 * Releases the openGL ID but maintains the texture ID's
	 */
	public void delete() {
		delete(false);
	}
	
	/**
	 * Releases the openGL ID and if <tt>deleteTexture</tt> is true it will also delete the texture Id
	 * @param deleteTexture
	 */
	public void delete(boolean deleteTexture) {
		if(deleteTexture && texture != null) {
			texture.delete();
		}
		glDeleteBuffers(glId);
	}
}
