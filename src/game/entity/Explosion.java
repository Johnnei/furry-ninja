package game.entity;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.math.Point;
import engine.render.FrameRenderable;
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
	 * 
	 * @param p
	 *            The point of explosion
	 * @param range
	 *            The range of the damage
	 * @param mDmg
	 *            The minimun amount of damage within the range
	 * @param xDmg
	 *            The maximum amount of damage at point p
	 */
	public Explosion(Entity e, Point p, float range, int mDmg, int xDmg, int weaponId) {
		super(6, 3, true);
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
		for (int i = 0; i < owner.getWormsGame().getTeamCount(); i++) {
			Team t = owner.getWormsGame().getTeam(i);
			for (int j = 0; j < t.getCubeCount(); j++) {
				Cube c = t.getCube(j);
				if (c.isDead())
					continue;
				Point cubePoint = c.getPoint();
				float dSquared = explosionPoint.getSquaredDistanceTo(cubePoint);
				int dmg = getDamage(dSquared);
				if (dmg > 0) {
					System.out.println("Taking Damage " + dmg + " from projectile at " + dSquared + " distance^2");
					c.takeDamgage(dmg);
				}
			}
		}
		for (int chunkY = 0; chunkY < (World.HEIGHT / World.CHUNK_HEIGHT); chunkY++) {
			for (int chunkX = 0; chunkX < (World.WIDTH / World.CHUNK_WIDTH); chunkX++) {
				Point chunk = owner.getWormsGame().getWorld().getPoint(chunkX, chunkY);
				float dSquared = explosionPoint.getSquaredDistanceTo(chunk);
				if (dSquared < landscapeRange * landscapeRange)
					owner.getWormsGame().getWorld().destroy(chunkX, chunkY);
			}
		}
	}

	/**
	 * Gets the recieved damage based on distance
	 * 
	 * @param distance
	 *            The squared distance from the object
	 * @return The floored amount of damage the object has to recieve
	 */
	private int getDamage(float distance) {
		if (distance > damageRange)
			return 0;
		float dmgLossPerUnit = (float) (maxDamage - minDamage) / damageRange;
		return (int) (maxDamage - (dmgLossPerUnit * distance));
	}

	@Override
	public void generateTextureData() {
		renderObject.setTexture("/res/weapon/explosion.png");
		super.generateTextureData();
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

		renderObject.updateVertex(vertex);
	}

}
