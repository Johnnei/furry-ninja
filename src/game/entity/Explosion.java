package game.entity;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.math.Point;
import engine.render.FrameRenderable;
import engine.render.TextureLoader;
import game.Team;
import game.World;
import game.data.WeaponType;

public class Explosion extends FrameRenderable {
	
	private Point explosionPoint;
	private Entity owner;
	
	private float damageRange;
	private float landscapeRange; 
	private int minDamage;
	private int maxDamage;
	
	/**
	 * Creates a new explosion
	 * @param p
	 * The point of explosion
	 * @param range
	 * The range of the damage 
	 * @param mDmg
	 * The minimun amount of damage within the range
	 * @param xDmg
	 * The maximum amount of damage at point p
	 */
	public Explosion(Entity e, Point p, float range, int mDmg, int xDmg, int weaponId) {
		super(5, 5, true);
		owner = e;
		damageRange = range * range;
		landscapeRange = WeaponType.projectileLandscapeCut[weaponId];
		maxDamage = xDmg;
		minDamage = mDmg;
		explosionPoint = p;
		generateTextureData();
		generateVertexData();
	}
	
	/**
	 * Causes explosion
	 */
	public void explode() { 
		for(int i = 0; i < owner.getWormsGame().getTeamCount(); i++) {
			Team t = owner.getWormsGame().getTeam(i);
			for(int j = 0; j < t.getCubeCount(); j++) {
				Cube c = t.getCube(j);
				if(c.isDead())
					continue;
				Point cubePoint = c.getPoint();
				float dSquared = explosionPoint.getSquaredDistanceTo(cubePoint);
				int dmg = getDamage(dSquared);
				if(dmg > 0) {
					System.out.println("Taking Damage " + dmg + " from projectile at " + dSquared + " distance^2");
					c.takeDamgage(dmg);
				}
			}
		}
		for(int chunkY = 0; chunkY < (World.HEIGHT / World.CHUNK_HEIGHT); chunkY++) {
			for(int chunkX = 0; chunkX < (World.WIDTH / World.CHUNK_WIDTH); chunkX++) {
				Point chunk = owner.getWormsGame().getWorld().getPoint(chunkX, chunkY);
				float dSquared = explosionPoint.getSquaredDistanceTo(chunk);
				if(dSquared < landscapeRange * landscapeRange)
					owner.getWormsGame().getWorld().destroy(chunkX, chunkY);
			}
		}
	}
	
	/**
	 * Gets the recieved damage based on distance
	 * @param distance
	 * The squared distance from the object
	 * @return
	 * The floored amount of damage the object has to recieve
	 */
	private int getDamage(float distance) {
		if(distance > damageRange)
			return 0;
		float dmgLossPerUnit = (float)(maxDamage - minDamage) / damageRange;
		return (int)(maxDamage - (dmgLossPerUnit * distance)); 
	}
	
	@Override
	public void generateTextureData() {
		System.out.println("Loading Texture Data");
		glTextureId = TextureLoader.loadTexture("/res/weapon/explosion.png");
		super.generateTextureData();
	}

	@Override
	public void generateColorData() {
	}

	@Override
	public void generateVertexData() {
		float height = 64F;
		float width = 64F;
		float x = explosionPoint.getX() - (width / 2F);
		float y = explosionPoint.getY() - (height / 2F);
		FloatBuffer vertex = BufferUtils.createFloatBuffer(2 * 4);
		vertex.put(new float[] { x, y + height, x + width, y + height, x + width, y, x, y });
		vertex.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertex, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}
	
	@Override
	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, glTextureId);
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(2, GL_FLOAT, 0, 0L);
		super.render();
		
		glDrawArrays(GL_QUADS, 0, 4);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisable(GL_TEXTURE_2D);
	}

}
