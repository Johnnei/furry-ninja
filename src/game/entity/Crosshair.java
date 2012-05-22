package game.entity;

import java.nio.FloatBuffer;

import engine.render.Renderable;
import engine.render.TextureLoader;
import game.data.Gamemode;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;

public class Crosshair extends Renderable {
	
	private float x, y;
	private int angle;
	
	public Crosshair(float x, float y) {
		this.x = x;
		this.y = y;
		angle = 0;
		generateTextureData();
		onTick(x, y);
	}
	
	public void onTick(float baseX, float baseY) {
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			angle = (angle + Gamemode.CROSSHAIR_SPEED) % 360;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			angle = (angle - Gamemode.CROSSHAIR_SPEED) % 360;
		}
		float dSin = (float)Math.sin(angle * (Math.PI / 180D));
		float dCos = (float)Math.cos(angle * (Math.PI / 180D));
		
		x = baseX + (dSin * 32);
		y = baseY + (dCos * 32);
		
		generateVertexData();
	}

	@Override
	public void generateColorData() {
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(12);
		vertexBuffer.put(new float[] { x, y + 16, 0, x + 16, y + 16, 0, x + 16, y, 0, x, y, 0 });
		vertexBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void generateTextureData() {
		glTextureId = TextureLoader.loadTexture("/res/crosshair.png");
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(8);
		textureBuffer.put(new float[] { 0, 1, 1, 1, 1, 0, 0, 0 });
		textureBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void render() {
		glEnable(GL_TEXTURE_2D);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, glTextureId);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glTexCoordPointer(2, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4);
		
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisable(GL_TEXTURE_2D);
	}
	
	public int getAngle() {
		return angle;
	}

}
