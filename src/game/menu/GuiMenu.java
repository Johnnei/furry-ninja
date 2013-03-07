package game.menu;

import engine.render.ColorHelper;

public class GuiMenu extends Gui {

	private GuiButton startGame;
	private GuiButton customGame;
	private GuiButton settings;
	private GuiButton quit;
	
	public GuiMenu(Gui parent) {
		super(parent, 0);
		startGame = new GuiButton(390, 125, true, "Quick Match", new ColorHelper(0x66, 0x66, 0x66), null);
		customGame = new GuiButton(420, 125, true, "Custom Game", new ColorHelper(0x66, 0x66, 0x66), null);
		settings = new GuiButton(450, 125, true, "Settings", new ColorHelper(0x66, 0x66, 0x66), null);
		quit = new GuiButton(480, 125, true, "Quit", new ColorHelper(0x66, 0x66, 0x66), null);
		addComponent(startGame);
		addComponent(customGame);
		addComponent(settings);
		addComponent(quit);
	}
	
	@Override
	public void onTick() {
		if(startGame.isClicked()) {
			switchGui(new GuiGame(getParent()));
		} else if(quit.isClicked()) {
			requestClose();
		}
	}

}
