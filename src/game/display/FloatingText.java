package game.display;

import engine.render.RenderObject;
import engine.render.TextRender;
import game.WormsGame;
import game.data.TurnPhase;
import game.entity.Entity;
import game.physics.InverseGravityMotion;
import game.physics.MotionVector;

import java.util.Random;

public class FloatingText extends Entity {
	
	private RenderObject renderObject;
	private String text;

	public FloatingText(String text, RenderObject renderObject, WormsGame wormsGame, float x, float y) {
		super(0, wormsGame, x, y, 0, 0);
		this.text = text;
		clearMotions();
		addMotionVector(new InverseGravityMotion());
		Random r = new Random();
		int xLifetime = 50 + r.nextInt(50);
		int xMotion = r.nextInt(3);
		if(r.nextBoolean()) {
			xMotion = -xMotion;
		}
		addMotionVector(new MotionVector(xMotion, 0, xLifetime, 0));
	}

	@Override
	public void onTick(TurnPhase turn) {
		doMovement();
		if(y < 0)
			setCanDelete(true);
	}

	@Override
	public void onTurnChange(TurnPhase turn) {
	}

	@Override
	public void render(TextRender textRenderer) {
		textRenderer.drawCentered(x, y, text, renderObject);
	}

}
