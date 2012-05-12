package engine.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;

public class FontManager {
	
	private Font awtFont;
	private UnicodeFont font;
	private float curSize;
	private Color curCol;
	
	public FontManager() {
		try {
			awtFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("res/font/arial.ttf"));
			setFont(8f, Color.WHITE);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setFont(float size, Color c) {
		if(font != null) {
			font.destroy();
		}
		font = new UnicodeFont(awtFont.deriveFont(size));
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(c));
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		curSize = size;
		curCol = c;
	}
	
	public void drawString(float x, float y, String text, float size, Color color) {
		if(!curCol.equals(color) || size != curSize)
			setFont(size, color);
		font.drawString(x, y, text);
	}
	
	public int getWidth(String text, float size) {
		setFont(size, curCol);
		return font.getWidth(text);
	}
	
	public void unload() {
		font.destroy();
		font = null;
		curCol = null;
		awtFont = null;
	}

}
