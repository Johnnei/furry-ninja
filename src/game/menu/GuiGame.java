package game.menu;

import engine.render.TextRender;
import game.WormsGame;

public class GuiGame extends Gui {

	private WormsGame game;
	
	public GuiGame(Gui parent) {
		super(parent, 0);
		game = new WormsGame();
	}
	
	@Override
	public void onTick() {
		game.onTick();
	}
	
	@Override
	public void render(TextRender textRenderer) {
		game.render(textRenderer);
	}
	
	@Override
	public void setFps(int fps) {
		game.setFps(fps);
	}

}
