package game.menu;

import static engine.render.RenderObject.COLOR;
import engine.render.ColorHelper;
import engine.render.RenderObject;
import engine.render.TextRender;

public class GuiButton extends GuiComponent {
	
	private boolean centered;
	private String text;
	private ColorHelper color;
	
	public GuiButton(int x, int y, boolean centered, String text, ColorHelper color) {
		super(COLOR);
		this.centered = centered;
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		if(color != null)
			generateColorData();
	}
	
	@Override
	public void generateColorData() {
		renderObject.updateColor(color);
	}
	
	@Override
	public void render(TextRender textRenderer) {
		RenderObject color = (this.color == null) ? null : renderObject;
		if(centered)
			textRenderer.drawCentered(x, y, text, color);
		else
			textRenderer.draw(x, y, text, color);
	}

}
