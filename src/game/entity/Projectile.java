package game.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.render.TextureLoader;

import game.WormsGame;
import game.data.Gamemode;
import game.data.TurnPhase;
import game.data.WeaponType;

public class Projectile extends Entity {
	
	private int weaponId;
	private int minDamage;
	private int maxDamage;
	private float damageRange;
	private Cube owner;
	private boolean canDelete;
	
	public Projectile(WormsGame wormsGame, Cube owner, int x, int y, int id) {
		super(wormsGame, x, y, WeaponType.weaponWidth[id], WeaponType.weaponHeight[id]);
		weaponId = id;
		minDamage = WeaponType.projectileMinDamage[id];
		maxDamage = WeaponType.projectileMaxDamage[id];
		damageRange = WeaponType.projectileDamageRange[id];
		
		this.owner = owner;
		
		generateVertexData();
		generateTextureData();
		
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
	public void generateColorData() {
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertex = BufferUtils.createFloatBuffer(3 * 4);
		
		float yMotion = this.yMotion;
		if(xMotion < 0)
			yMotion = -yMotion;
		if(yMotion > 10)
			yMotion = 10;
		else if(yMotion < -10)
			yMotion = -10;
		
		vertex.put(new float[] { x, y + height + yMotion, 0, x + width, y + height - yMotion, 0, x + width, y - yMotion, 0, x, y + yMotion, 0 });
		vertex.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertex, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, glTextureId);
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glTexCoordPointer(3, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4);
		
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisable(GL_TEXTURE_2D);
	}

	@Override
	public void generateTextureData() {
		glTextureId = TextureLoader.loadTexture("/res/weapon/" + WeaponType.weaponName[weaponId] + "_shell.png");
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(8);
		textureBuffer.put(new float[] { 0, 1, 1, 1, 1, 0, 0, 0 });
		textureBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}
	
	public boolean canDelete() {
		return canDelete;
	}

}
