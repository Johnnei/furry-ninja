package game.entity;

import static engine.render.RenderObject.VERTEX_TEXTURE;
import engine.math.Point;
import engine.render.VertexHelper;
import game.WormsGame;
import game.data.TurnPhase;
import game.display.Explosion;
import game.weapon.IWeapon;

public class Projectile extends Entity {
	
	private IWeapon weapon;
	private Cube owner;
	private boolean canDelete;
	
	public Projectile(WormsGame wormsGame, Cube owner, int x, int y, IWeapon weapon) {
		super(VERTEX_TEXTURE, wormsGame, x, y, weapon.getTextureWidth(), weapon.getTextureHeight());
		this.weapon = weapon;
		this.owner = owner;
		generateTextureData();
		
		canDelete = false;
	}
	
	/**
	 * Explodes the projectile
	 */
	public void explode() {
		canDelete = true;
		Explosion e = new Explosion(owner, getExplosionPoint(), weapon);
		owner.getWormsGame().addRenderable(e);
		e.explode();
	}

	@Override
	public void onTick(TurnPhase turn) {
		doMovement();
		
		//Test if it should explode
		if(isOnGround()) {
			explode();
		}
		
		if(x < 0 || x > 1280 || y < 0 || y > 720)
			canDelete = true;
		
		generateVertexData();
	}

	@Override
	public void onTurnChange(TurnPhase turn) {
	}

	@Override
	public void generateVertexData() {
		VertexHelper vertex = new VertexHelper(2 * 4);
		vertex.put(x, y, width, height);
		
		Point dest = new Point(x + xMotion, y + yMotion);		
		vertex.rotate(getPoint().getAngleBetween(dest));
		
		renderObject.updateVertex(vertex);
	}

	@Override
	public void generateTextureData() {
		renderObject.setTexture("/res/weapon/" + weapon.getName() + "_shell.png");
		renderObject.updateTexture();
	}
	
	@Override
	public boolean canDelete() {
		return canDelete;
	}
	
	private Point getExplosionPoint() {
		return new Point(x + (width / 2), y - (height / 2));
	}

}
