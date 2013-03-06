package game.menu;

import static engine.render.RenderObject.VERTEX_COLOR;
import engine.GameMouse;
import engine.render.ColorHelper;
import engine.render.RenderObject;
import engine.render.TextRender;
import engine.render.VertexHelper;

public class GuiButton extends GuiComponent {
	
	private final int PADDING = 5;
	private final int TOTAL_PADDING = 2 * PADDING;
	private final int WIDTH = 1280;
	
	private boolean centered;
	private String text;
	private RenderObject textColor;
	private ColorHelper buttonColor;
	
	/**
	 * Creates a button on the center of the screen (x alignment)
	 * @param centered
	 * @param text
	 * @param buttonCollor
	 * @param color
	 */
	public GuiButton(int y, String text, ColorHelper buttonColor, RenderObject textColor) {
		super(VERTEX_COLOR);
		width = TOTAL_PADDING + TextRender.getTextWidth(text);
		height = TOTAL_PADDING + TextRender.getTextHeight();
		this.x = (WIDTH / 2) - (width / 2);
		this.y = y;
		this.centered = false;
		this.text = text;
		this.textColor = textColor;
		this.buttonColor = buttonColor;
		generateVertexData();
		generateColorData();
	}
	
	public GuiButton(int y, int width, boolean centered, String text, ColorHelper buttonColor, RenderObject textColor) {
		super(VERTEX_COLOR);
		this.width = width;
		this.height = TOTAL_PADDING + TextRender.getTextHeight();
		this.x = (WIDTH / 2) - (width / 2);
		this.y = y;
		this.centered = centered;
		this.text = text;
		this.textColor = textColor;
		this.buttonColor = buttonColor;
		generateVertexData();
		generateColorData();
	}
	
	/**
	 * Checks if the mouse is clicked within the borders of this button
	 * @return true if the mouse is clicked on this button
	 */
	public boolean isClicked() {
		if(GameMouse.getInstance().getX() < x || GameMouse.getInstance().getX() > x + width)
			return false; //X is outside our button
		if(GameMouse.getInstance().getY() < y || GameMouse.getInstance().getY() > y + height)
			return false; //X is outside our button
		return GameMouse.getInstance().isClicked(GameMouse.LEFT_BUTTON);
	}
	
	@Override
	public void generateVertexData() {
		VertexHelper vertex = new VertexHelper(2 * 4);
		vertex.put(x, y, width, height);
		renderObject.updateVertex(vertex);
	}
	
	@Override
	public void generateColorData() {
		renderObject.updateColor(buttonColor);
	}
	
	@Override
	public void render(TextRender textRenderer) {
		super.render(textRenderer);
		if(centered)
			textRenderer.drawCentered(x + (width / 2), y + PADDING, text, textColor);
		else
			textRenderer.draw(x + PADDING, y + PADDING, text, textColor);
	}

}
