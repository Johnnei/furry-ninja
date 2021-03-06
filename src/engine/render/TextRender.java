package engine.render;

import static engine.render.RenderObject.COLOR;
import static engine.render.RenderObject.VERTEX_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class TextRender {

	private RenderObject textRenderer;

	public TextRender() {
		textRenderer = new RenderObject(VERTEX_TEXTURE);
		generateTextureMapping();
		textRenderer.resetBuffers();
		textRenderer.updateTexture();
		generateVertex();
	}

	/**
	 * Loads the font.png file and prepares it for useage and registers a basic
	 * box for the images
	 */
	private void generateVertex() {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(8);

		final float width = 9F;
		final float height = 13F;

		vertexBuffer.put(new float[] { 0, height, width, height, width, 0, 0, 0 });
		vertexBuffer.flip();

		textRenderer.updateVertex(vertexBuffer);
	}

	/**
	 * Calculates the texture coords and saves those.
	 */
	private void generateTextureMapping() {
		Texture texture = new Texture("/res/font.png");
		texture.setSizeLimit(true);
		for (int i = 0; i <= 128; i++) {

			int xOffset = i % 13;
			int yOffset = i / 13;

			int width = 10;
			int height = 16;

			int x = xOffset * width;
			int y = yOffset * height;
			
			texture.addSubTexture(x, y, width, height);
		}
		textRenderer.setTexture(texture);
	}

	/**
	 * Draws a string with the center at the given location
	 * @param x The x-Coordinate
	 * @param y The y-Coordinate
	 * @param text The String to be drawn
	 * @param renderObject The Color in which this string should be drawn (Null = white)
	 */
	public void drawCentered(float x, float y, String text, RenderObject renderObject) {
		x -= (text.length() * 4.5F);
		draw(x, y, text, renderObject);
	}

	/**
	 * Draws a string with the left-top at the given location
	 * @param x The x-Coordinate
	 * @param y The y-Coordinate
	 * @param text The String to be drawn
	 * @param renderObject The Color in which this string should be drawn (Null = white)
	 */
	public void draw(float x, float y, String text, RenderObject renderObject) {
		if (renderObject != null && renderObject.hasFlag(COLOR))
			glEnableClientState(GL_COLOR_ARRAY);
		// Draw
		for (int i = 0; i < text.length(); i++) {
			glPushMatrix();
			glTranslatef(x + (i * 9F), y, 0F);
			char c = text.charAt(i);

			long textureOffset = 4 * (8 + (c * 8));
			
			if (renderObject != null && renderObject.hasFlag(COLOR)) {
				renderObject.bindColor();
			}
			
			textRenderer.render(textureOffset);
			glPopMatrix();
		}
		if (renderObject != null && renderObject.hasFlag(COLOR))
			glDisableClientState(GL_COLOR_ARRAY);
	}
	
	public static int getTextWidth(String s) {
		return s.length() * 9;
	}
	
	public static int getTextHeight() {
		return 16;
	}

}
