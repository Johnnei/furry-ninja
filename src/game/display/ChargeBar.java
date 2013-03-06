package game.display;

import static engine.render.RenderObject.VERTEX_COLOR;
import engine.render.ColorHelper;
import engine.render.Renderable;
import engine.render.TextRender;
import engine.render.VertexHelper;
import game.entity.Cube;

public class ChargeBar extends Renderable {

	private Cube owner;
	private Crosshair crosshair;
	
	public ChargeBar(Cube owner, Crosshair crosshair) {
		super(VERTEX_COLOR);
		this.owner = owner;
		this.crosshair = crosshair;
		generateVertexData();
		generateColorData();
	}
	
	@Override
	public void onTick() {
		if(owner.getSelectedWeapon().isCharging()) {
			generateVertexData();
			generateColorData();
		}
	}

	@Override
	public boolean canDelete() {
		return false;
	}
	
	@Override
	public void generateColorData() {
		float pCharged = 255 * getPercentCharged();
		float r = pCharged;
		float g = (255F / 2F) - (0.5F * pCharged);
		ColorHelper helper = new ColorHelper(r, g, g);
		renderObject.updateColor(helper);
	}
	
	@Override
	public void generateVertexData() {
		float angle = 90 - crosshair.getAngle();
		float charge = 32 * getPercentCharged();
		VertexHelper vertex = new VertexHelper(4 * 2);
		
		if(owner.isFacingLeft()) {
			charge = -charge;
			angle = -angle;
		}
		vertex.put(owner.getX() + (owner.getWidth() / 2), owner.getY() + (owner.getHeight() / 2), charge, 8);
		vertex.rotate(angle);
		renderObject.updateVertex(vertex);
	}
	
	private float getPercentCharged() {
		return owner.getSelectedWeapon().getCharge() / owner.getWeapon().getMaxFirePower();
	}
	
	@Override
	public void render(TextRender textRenderer) {
		if(owner.getSelectedWeapon().isCharging()) {
			super.render(textRenderer);
		}
	}

}
