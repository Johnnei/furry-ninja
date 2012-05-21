package game.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.math.Point;

import game.Team;
import game.WormsGame;
import game.data.Gamemode;
import game.data.TurnPhase;
import game.data.WeaponType;

public class Projectile extends Entity {
	
	private int minDamage;
	private int maxDamage;
	private float damageRange;
	private Cube owner;
	private boolean canDelete;
	
	public Projectile(WormsGame wormsGame, Cube owner, int x, int y, int id) {
		super(wormsGame, x, y, WeaponType.weaponWidth[id], WeaponType.weaponHeight[id]);
		minDamage = WeaponType.projectileMinDamage[id];
		maxDamage = WeaponType.projectileMaxDamage[id];
		damageRange = WeaponType.projectileDamageRange[id];
		damageRange *= damageRange; //Squared because I used squared distances
		
		this.owner = owner;
		
		generateVertexData();
		generateColorData();
		
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
	 * Gets the recieved damage based on distance
	 * @param distance
	 * @return
	 */
	private int getDamage(float distance) {
		if(distance > damageRange)
			return 0;
		float dmgLossPerUnit = (maxDamage - minDamage) / damageRange;
		return (int)(maxDamage - (dmgLossPerUnit * distance)); 
	}
	
	/**
	 * Explodes the projectile
	 */
	private void explode() {
		canDelete = true;
		Point explosionPoint = getPoint(); 
		for(int i = 0; i < owner.getWormsGame().getTeamCount(); i++) {
			Team t = owner.getWormsGame().getTeam(i);
			for(int j = 0; j < t.getCubeCount(); j++) {
				Cube c = t.getCube(j);
				Point cubePoint = c.getPoint();
				float dSquared = explosionPoint.getSquaredDistanceTo(cubePoint);
				int dmg = getDamage(dSquared);
				if(dmg > 0) {
					System.out.println("Taking Damage " + dmg + " from projectile at " + dSquared + " distance^2");
					c.takeDamgage(dmg);
				}
			}
		}
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
		FloatBuffer color = BufferUtils.createFloatBuffer(3 * 4);
		float[] colors = owner.getTeam().getColor();
		color.put(new float[] { colors[0], colors[1], colors[2], colors[0], colors[1], colors[2], colors[0], colors[1], colors[2], colors[0], colors[1], colors[2] });
		color.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glBufferData(GL_ARRAY_BUFFER, color, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertex = BufferUtils.createFloatBuffer(3 * 4);
		vertex.put(new float[] { x, y + height, 0, x + width, y + height, 0, x + width, y, 0, x, y, 0 });
		vertex.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void render() {
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glColorPointer(3, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}

	@Override
	public void generateTextureData() {
	}
	
	public boolean canDelete() {
		return canDelete;
	}

}
