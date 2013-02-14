package game;

import java.awt.Rectangle;
import java.nio.FloatBuffer;

import engine.math.Point;

public class WorldChunk {
	
	private World world;
	
	private int x, y, width, height;
	private boolean destroyed;
	
	public WorldChunk(World world, int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		destroyed = false;
		this.world = world;
	}
	
	public boolean fillBuffers(FloatBuffer vertexBuffer) {
		if(destroyed)
			return false;
		
		int chunkX = x / World.CHUNK_WIDTH;
		int chunkY = (y - 360) / World.CHUNK_HEIGHT;
		
		boolean leftDestroyed = world.isDestroyed(chunkX - 1, chunkY);
		boolean rightDestroyed = world.isDestroyed(chunkX + 1, chunkY);
		boolean topDestroyed = world.isDestroyed(chunkX, chunkY - 1);
		boolean bottomDestroyed = world.isDestroyed(chunkX, chunkY + 1);
		
		float[] bottomLeft = {x, y};
		float[] topLeft = {x, y + height};
		float[] topRight = {x + width, y};
		float[] bottomRight = {x + width, y + height};
		
		if(!(leftDestroyed && topDestroyed && bottomDestroyed && rightDestroyed)) {
			if(topDestroyed && bottomDestroyed) {
				if(leftDestroyed) {
					bottomLeft[0] += (width / 2);
					topLeft[0] += (width / 2);
				} else if (rightDestroyed) {
					topRight[0] -= (width / 2);
					bottomRight[0] -= (width / 2);
				}
			} else {
				if(leftDestroyed) {
					if(topDestroyed) {
						bottomLeft[0] += width;
					} else if(bottomDestroyed) {
						topLeft[0] += width;
					}
				}
				if(rightDestroyed) {
					if(topDestroyed) {
						topRight[0] -= width;
					} else if(bottomDestroyed) {
						bottomRight[0] -= width;
					}
				}
			}
		}
		
		vertexBuffer.put(new float[] {bottomLeft[0], bottomLeft[1], topRight[0], topRight[1], bottomRight[0], bottomRight[1], topLeft[0], topLeft[1]});
		return true;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroyed() {
		destroyed = true;
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, width, height);
	}
	
	public Point getPoint() {
		return new Point(x, y);
	}

}
