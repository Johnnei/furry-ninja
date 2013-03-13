package game.menu;

import engine.render.ColorHelper;


public class GuiSettings extends Gui {
	
	private GuiButton videoButton;
	private GuiButton controlsButton;
	private GuiButton audioButton;
	private GuiButton returnButton;
	
	public GuiSettings(Gui parent) {
		super(parent, 0);
		videoButton = new GuiButton(this, 300, 150, true, "Video Settings", new ColorHelper(0x66, 0x66, 0x66), null);
		audioButton = new GuiButton(this, 330, 150, true, "Audio Settings", new ColorHelper(0x66, 0x66, 0x66), null);
		controlsButton = new GuiButton(this, 360, 150, true, "Controls", new ColorHelper(0x66, 0x66, 0x66), null);
		returnButton = new GuiButton(this, 390, 150, true, "Return", new ColorHelper(0x66, 0x66, 0x66), null);
		addComponent(videoButton);
		addComponent(audioButton);
		addComponent(controlsButton);
		addComponent(returnButton);
	}
	
	@Override
	public void onTick() {
		if(returnButton.isClicked()) {
			switchGui(new GuiMenu(getParent()));
		}
	}

}
