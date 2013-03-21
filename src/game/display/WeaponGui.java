package game.display;

import static engine.render.RenderObject.VERTEX_TEXTURE;

import org.lwjgl.input.Keyboard;

import engine.GameKeyboard;
import engine.render.Renderable;
import engine.render.TextRender;
import engine.render.Texture;
import engine.render.VertexHelper;
import game.Team;

public class WeaponGui extends Renderable {
	
	public static final int ROWS = 4;
	public static final int WEAPONS_PER_ROW = 13;
	/*
	 * Background
	 * Per Weapon Slot: 1
	 */
	private static final int GUI_ELEMENTS = 1;
	private static final int TOTAL_ELEMENTS = GUI_ELEMENTS + (WEAPONS_PER_ROW * ROWS);
	
	/**
	 * The team associated with this WeaponGui
	 */
	private Team team;
	/**
	 * The weapon overlay
	 */
	private WeaponGuiOverlay weaponOverlay;
	private int chosenWeapon;
	
	public WeaponGui(Team team) {
		super(VERTEX_TEXTURE, false, TOTAL_ELEMENTS);
		this.team = team;
		weaponOverlay = new WeaponGuiOverlay(team.getWeapons());
		generateVertexData();
		generateTextureData();
		chosenWeapon = team.getActiveCube().getSelectedWeaponIndex();
	}
	
	private void addVertex(VertexHelper vertex, int i) {
		int xIndex = i % WEAPONS_PER_ROW;
		int yIndex = i / WEAPONS_PER_ROW;
		
		float x = 32 * xIndex + 433;
		float y = 48 * yIndex + 283;
		vertex.put(x, y, 32, 32);
	}
	
	@Override
	public void generateTextureData() {
		Texture texture = new Texture("/res/weapon_bg.png");
		texture.addSubTexture(0, 0, 448, 203); //BG
		texture.addSubTexture(0, 203, 32, 32); //Selected Weapon
		final int weaponsSlots = ROWS * WEAPONS_PER_ROW;
		for(int i = 0; i < weaponsSlots; i++) {
			texture.addSubTexture(32, 203, 32, 32);
		}
		renderObject.setTexture(texture);
		renderObject.updateTexture();
	}
	
	@Override
	public void generateVertexData() {
		VertexHelper vertex = new VertexHelper(2 * 4 * TOTAL_ELEMENTS);
		//Background
		vertex.put(416, 242, 448, 235);
		//Select Weapon Icon
		addVertex(vertex, chosenWeapon);
		//Weapon Slots
		for(int i = 0; i < TOTAL_ELEMENTS - 1; i++) {
			if(i != chosenWeapon) {
				addVertex(vertex, i);
			}
		}
		renderObject.updateVertex(vertex);
	}
	
	public void onTurnChange() {
		
	}
	
	@Override
	public void onTick() {
		if(GameKeyboard.getInstance().isKeyPressed(Keyboard.KEY_RIGHT)) {
			if(chosenWeapon + 1 < team.getWeapons().length) {
				++chosenWeapon;
				generateVertexData();
			}
		}
		if(GameKeyboard.getInstance().isKeyPressed(Keyboard.KEY_LEFT)) {
			if(chosenWeapon - 1 >= 0) {
				--chosenWeapon;
				generateVertexData();
			}
		}
		if(GameKeyboard.getInstance().isKeyPressed(Keyboard.KEY_RETURN)) {
			team.getActiveCube().setSelectedWeaponIndex(chosenWeapon);
			team.closeWeaponMenu();
		}
	}
	
	@Override
	public void render(TextRender textRenderer) {
		super.render(textRenderer);
		weaponOverlay.render(textRenderer);
		textRenderer.drawCentered(640, 260, "Weapon Selection", null);
	}

	@Override
	public boolean canDelete() {
		return false;
	}

}
