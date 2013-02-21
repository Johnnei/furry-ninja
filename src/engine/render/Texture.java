package engine.render;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

/**
 * Texture class to handle textures and allow to change the render settings in
 * the future (TODO)
 * 
 * @author Johnnei
 * 
 */
public class Texture {

	private int glTextureId;
	private int width;
	private int height;
	private ArrayList<Rectangle> textures;

	public Texture(String filename) {
		textures = new ArrayList<>();
		loadTexture(filename);
	}
	
	public void addSubTexture(int x, int y, int width, int height) {
		textures.add(new Rectangle(x, y, width, height));
	}

	/**
	 * Adds the texture mapping into the given buffer
	 * 
	 * @param buffer
	 *            The buffer to add the mappings to
	 */
	public void fillBuffer(FloatBuffer buffer) {
		if(textures.size() == 0) {
			buffer.put(new float[] { 0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F});
		} else {
			for(Rectangle texture : textures) {
				float x = (float)texture.getX() * (1F / width);
				float tWidth = (float)texture.getWidth() * (1F / width);
				float tHeight = (float)texture.getHeight() * (1F / height);
				float y = (float)texture.getY() * (1F / height);
				buffer.put(new float[] { x, y + tHeight, x + tWidth, y + tHeight, x + tWidth, y, x, y });
			}
		}
	}
	
	public FloatBuffer fillBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(getBufferSize());
		fillBuffer(buffer);
		buffer.flip();
		return buffer;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, glTextureId);
	}
	
	public int getBufferSize() {
		if(textures.size() == 0)
			return 8;
		else
			return textures.size() * 8;
	}

	private void loadTexture(String file) {
		try {
			// Load Texture
			InputStream in = Texture.class.getResourceAsStream(file);
			PNGDecoder decoder = new PNGDecoder(in);
			
			width = decoder.getWidth();
			height = decoder.getHeight();
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(4 * width * height);
			decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
			buffer.flip();
			in.close();

			// Register Texture
			glActiveTexture(GL_TEXTURE0);
			glTextureId = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, glTextureId);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glBindTexture(GL_TEXTURE_2D, 0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void setParameter(int parameter, int value) {
		glActiveTexture(GL_TEXTURE0);
		bind();
		glTexParameteri(GL_TEXTURE_2D, parameter, value);
	}
	
	/**
	 * Releases the ID for this texture
	 */
	public void delete() {
		glDeleteTextures(glTextureId);
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}
