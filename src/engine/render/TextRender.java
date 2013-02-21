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

	/**
	 * Singleton instance to render text on VBO Level
	 */
	private static TextRender textRender = new TextRender();

	private RenderObject textRenderer;

	private TextRender() {
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
	 * Draws the text and will recalculate the x to make it centered if
	 * glColorId param is 0 (GL_NONE) it wont use any color. Therefore it will
	 * be white
	 * 
	 * @param x
	 * @param y
	 * @param text
	 * @param glColorId
	 */
	public void drawCentered(float x, float y, String text, RenderObject renderObject) {
		x -= (text.length() * 4.5F);
		draw(x, y, text, renderObject);
	}

	/**
	 * Draws the text if glColorId param is 0 (GL_NONE) it wont use any color.
	 * therefore it will be white
	 * 
	 * @param x
	 * @param y
	 * @param text
	 * @param glColorId
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

	/**
	 * Gets the singleton instance of TextRender
	 * 
	 * @return
	 */
	public static TextRender getTextRender() {
		return textRender;
	}

}
