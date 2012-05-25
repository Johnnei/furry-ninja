package game.entity;

import engine.render.TextRender;
import game.WormsGame;
import game.data.TurnPhase;

public class FloatingText extends Entity {
	
	private String text;
	private float floatingTime;

	public FloatingText(String text, int color, WormsGame wormsGame, float x, float y) {
		super(wormsGame, x, y, 0, 0);
		this.text = text;
		floatingTime = 10;
		glColorId = color;
	}

	@Override
	public void onTick(TurnPhase turn) {
		float f = floatingTime * 0.1F;
		if(f > 6)
			f = 6;
		floatingTime += f;
		if(y < 0)
			setCanDelete(true);
	}

	@Override
	public void onTurnChange(TurnPhase turn) {
	}

	@Override
	public void generateColorData() {
	}

	@Override
	public void generateVertexData() {
	}

	@Override
	public void generateTextureData() {
	}

	@Override
	public void render() {
		TextRender.getTextRender().drawCentered(x, y - floatingTime, text, glColorId);
	}

}
