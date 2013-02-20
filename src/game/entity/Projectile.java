package game.entity;

import game.WormsGame;
import game.data.Gamemode;
import game.data.TurnPhase;
import game.data.WeaponType;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static engine.render.RenderObject.VERTEX_TEXTURE;

public class Projectile extends Entity {
	
	private int weaponId;
	private int minDamage;
	private int maxDamage;
	private float damageRange;
	private Cube owner;
	private boolean canDelete;
	
	public Projectile(WormsGame wormsGame, Cube owner, int x, int y, int id) {
		super(VERTEX_TEXTURE, wormsGame, x, y, WeaponType.weaponWidth[id], WeaponType.weaponHeight[id]);
		weaponId = id;
		minDamage = WeaponType.projectileMinDamage[id];
		maxDamage = WeaponType.projectileMaxDamage[id];
		damageRange = WeaponType.projectileDamageRange[id];
		
		this.owner = owner;
		
		canDelete = false;
	}
	
	/**
	 * Set the motions on x and y axis
	 * @param x
	 * @param y
	 */
	public void setMotion(float x, float y) {
		xMotion = x;
		yMotion = y;
	}
	
	/**
	 * Execute Diffrent movement then the Entity
	 */
	public void doMovement() {
		x += xMotion;
		y -= yMotion;
		
		yMotion -= Gamemode.GRAVITY * fallDuration;
		fallDuration++;
	}
	
	/**
	 * Explodes the projectile
	 */
	public void explode() {
		canDelete = true;
		Explosion e = new Explosion(owner, getPoint(), damageRange, minDamage, maxDamage, owner.getSelectedWeapon());
		owner.getWormsGame().addRenderable(e);
		e.explode();
	}

	@Override
	public void onTick(TurnPhase turn) {
		doMovement();
		
		//Test if it should explode
		if(!owner.getCollisionBox().intersects(getCollisionBox())) { //Don't Selfkill
			if(wormsGame.collides(this)) { //Should do boom?
				explode();
			}
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
		FloatBuffer vertex = BufferUtils.createFloatBuffer(2 * 4);
		
		float yMotion = this.yMotion;
		if(xMotion < 0)
			yMotion = -yMotion;
		if(yMotion > 10)
			yMotion = 10;
		else if(yMotion < -10)
			yMotion = -10;
		
		vertex.put(new float[] { x, y + height + yMotion, x + width, y + height - yMotion, x + width, y - yMotion, x, y + yMotion });
		vertex.flip();
		
		renderObject.updateVertex(vertex);
	}

	@Override
	public void generateTextureData() {
		renderObject.setTexture("/res/weapon/" + WeaponType.weaponName[weaponId] + "_shell.png");
		renderObject.updateTexture();
	}
	
	@Override
	public boolean canDelete() {
		return canDelete;
	}

}
