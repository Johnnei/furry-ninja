package game.display;

import static engine.render.RenderObject.VERTEX_TEXTURE;
import engine.render.Renderable;
import engine.render.TextRender;
import engine.render.Texture;
import engine.render.VertexHelper;
import game.weapon.Weapon;

public class WeaponGuiOverlay extends Renderable {

	private Weapon[] weaponList;
	
	public WeaponGuiOverlay(Weapon[] weaponList) {
		super(VERTEX_TEXTURE);
		this.weaponList = weaponList;
		generateVertexData();
		generateTextureData();
	}
	
	@Override
	public void generateTextureData() {
		Texture texture = new Texture("/res/weaponmap.png");
		for(int i = 0; i < weaponList.length; i++) {
			final int WEAPON_PER_ROW_IN_TEXURE = 2;
			int xIndex = weaponList[i].getStats().getTextureMapId() % WEAPON_PER_ROW_IN_TEXURE;
			int yIndex = weaponList[i].getStats().getTextureMapId() / WEAPON_PER_ROW_IN_TEXURE;
			
			int x = 16 * xIndex;
			int y = 16 * yIndex;
			texture.addSubTexture(x, y, 16, 16);
		}
		renderObject.setTexture(texture);
		renderObject.updateTexture();
	}
	
	@Override
	public void generateVertexData() {
		renderObject.resetBuffers(weaponList.length);
		VertexHelper vertex = new VertexHelper(2 * 4 * weaponList.length);
		for(int i = 0; i < weaponList.length; i++) {
			int xIndex = i % WeaponGui.WEAPONS_PER_ROW;
			int yIndex = i / WeaponGui.WEAPONS_PER_ROW;
			
			float x = 32 * xIndex + 437;
			float y = 48 * yIndex + 295;
			vertex.put(x, y, 16, 16);
		}
		renderObject.updateVertex(vertex);
	}
	
	@Override
	public void render(TextRender textRenderer) {
		super.render(textRenderer);
	}

	@Override
	public boolean canDelete() {
		return false;
	}

}
