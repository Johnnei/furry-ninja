package game.display;

import static engine.render.RenderObject.VERTEX_TEXTURE;
import engine.render.Renderable;
import engine.render.Texture;
import engine.render.VertexHelper;
import game.entity.Cube;
import game.weapon.Weapon;

public class WeaponRenderer extends Renderable {
	
	/**
	 * We remember the texture so we can trash it once we switch to a different weapon 
	 */
	private Texture texture;
	/**
	 * The cube which is wielding this weapon
	 */
	private Cube owner;
	/**
	 * The aiming system of the weapon wielder
	 */
	private Crosshair ownerCrosshair;
	
	public WeaponRenderer(Cube owner, Crosshair ownerCrosshair) {
		super(VERTEX_TEXTURE);
		this.owner = owner;
		this.ownerCrosshair = ownerCrosshair;
		generateVertexData();
	}

	@Override
	public boolean canDelete() {
		return false;
	}
	
	public void onTick() {
		generateVertexData();
	}
	
	@Override
	public void generateVertexData() {
		VertexHelper vertex = new VertexHelper(2 * 4);
		if(owner.isFacingLeft()) {
			vertex.put(owner.getX() - 8, owner.getY() + 4, 16, 16);
			vertex.mirror();
			vertex.rotate(ownerCrosshair.getAngle());
		} else {
			vertex.put(owner.getX() + 8, owner.getY() + 4, 16, 16);
			vertex.rotate(-ownerCrosshair.getAngle());
		}
		renderObject.updateVertex(vertex);
	}

	/**
	 * Sets the weapon to a new weapon
	 * @param weapon The new weapon
	 */
	public void setWeapon(Weapon weapon) {
		if(texture != null) {
			texture.delete();
		}
		texture = new Texture("/res/weapon/" + weapon.getStats().getName() + ".png");
		renderObject.setTexture(texture);
		renderObject.updateTexture();
	}

}
