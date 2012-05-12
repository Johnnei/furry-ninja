package engine.render;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextRender {
	
	/**
	 * Singleton instance to render text on VBO Level
	 */
	private static TextRender textRender = new TextRender();
	
	/**
	 * Array to keep the vertexStuff in
	 */
	private int[] glTextureCoordId;
	
	/**
	 * Texture id for the font map
	 */
	private int glTextureId;
	
	/**
	 * Vertex id for the box
	 */
	private int glVertexId;
	
	private TextRender() {
		glTextureCoordId = new int[128];
		init();
	}
	
	/**
	 * Loads the font.png file and prepares it for useage and registers a basic box for the images
	 */
	private void init() {
		try {
			//Load Texture
			InputStream in = TextRender.class.getResourceAsStream("/res/font.png");
			PNGDecoder decoder = new PNGDecoder(in);
	        ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
	        decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
	        buffer.flip();
	        in.close();
	        
	        //Register Texture
	        glTextureId = glGenTextures();
	        glBindTexture(GL_TEXTURE_2D, glTextureId);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glBindTexture(GL_TEXTURE_2D, 0);
			
			//Register Basic Box
			glVertexId = glGenBuffers();
			
			FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(12);
			vertexBuffer.put(new float[] {0, 13, 0, 9, 13, 0, 9, 0, 0, 0, 0, 0});
			vertexBuffer.flip();
			
			glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
			glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Calculates the texture coords and saves those.
	 * @param charStart
	 * @param charEnd
	 */
	public void load(int charStart, int charEnd) {
		for(int i = charStart; i <= charEnd; i++) {
			glTextureCoordId[i] = glGenBuffers();
			
			FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(8); //2 (x, y) * 4
			int xOffset = (i - 48) % 13;
			int yOffset = (i - 48) / 13;
			
			float x = 1F / xOffset;
			float xMax = x + 0.07692307692307692307692307692308F; // x + 1/13
			float y = 1F / yOffset;
			float yMax = y + 0.07692307692307692307692307692308F; // y + 1/13
			
			textureBuffer.put(new float[] { x, yMax, xMax, yMax, xMax, y, x, y });
			textureBuffer.flip();
			
			glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId[i]);
			glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
		}
	}
	
	public void draw(float x, float y, String text, int glColorId) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, glTextureId);
		//Draw
		for(int i = 0; i < text.length(); i++) {
			glPushMatrix();
			glTranslatef(x + (i * 14), y, 0F);
			char c = text.charAt(i);
			
			if(glTextureCoordId[c] == GL_NONE) {
				System.out.println(c + " is unloaded!");
				System.exit(1);
			}
			
			glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
			glVertexPointer(3, GL_FLOAT, 0, 0L);
			glBindBuffer(GL_ARRAY_BUFFER, glColorId);
			glColorPointer(3, GL_FLOAT, 0, 0L);
			glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId[c]);
			glTexCoordPointer(2, GL_FLOAT, 0, 0L);
			
			glDrawArrays(GL_QUADS, 0, 4);
			glPopMatrix();
		}
		
		glBindTexture(GL_TEXTURE_2D, GL_NONE);
	}
	
	public static TextRender getTextRender() {
		return textRender;
	}

}
