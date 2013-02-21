package game.entity;

import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static engine.render.RenderObject.TEXTURE;
import static engine.render.RenderObject.VERTEX;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

import engine.render.Renderable;
import game.data.Gamemode;

public class Crosshair extends Renderable {

	private float x, y;
	private int angle;
	private Cube owner;

	public Crosshair(Cube owner, float x, float y) {
		super(TEXTURE | VERTEX);
		this.x = x;
		this.y = y;
		this.owner = owner;
		angle = 0;
		generateTextureData();
		onTick(x, y);
	}

	public void onTick(float baseX, float baseY) {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			angle = (angle + Gamemode.CROSSHAIR_SPEED) % 180;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			angle = (angle - Gamemode.CROSSHAIR_SPEED) % 180;
		}
		float dSin = (float) Math.sin(angle * (Math.PI / 180D));
		float dCos = (float) Math.cos(angle * (Math.PI / 180D));

		if (owner.isFacingLeft())
			x = baseX - (dSin * 32);
		else
			x = baseX + (dSin * 32);
		y = baseY + (dCos * 32);

		generateVertexData();
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(2 * 4);
		vertexBuffer.put(new float[] { x, y + 16, x + 16, y + 16, x + 16, y, x, y });
		vertexBuffer.flip();
		renderObject.updateVertex(vertexBuffer);
	}

	@Override
	public void generateTextureData() {
		renderObject.setTexture("/res/crosshair.png");
		renderObject.updateTexture();
	}
	
	public boolean canDelete() {
		return false;
	}

	public int getAngle() {
		return angle;
	}
	
	@Override
	public void render() {
		glEnableClientState(GL_COLOR_ARRAY);
		owner.bindColor();
		super.render();
		glDisableClientState(GL_COLOR_ARRAY);
	}

}
