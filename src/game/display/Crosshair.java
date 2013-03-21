package game.display;

import static engine.render.RenderObject.TEXTURE;
import static engine.render.RenderObject.VERTEX;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;

import org.lwjgl.input.Keyboard;

import engine.render.Renderable;
import engine.render.TextRender;
import engine.render.VertexHelper;
import game.data.Gamemode;
import game.entity.Cube;

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
		onTick();
	}

	@Override
	public void onTick() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && !owner.getTeam().isWeaponGuiOpen()) {
			angle += Gamemode.CROSSHAIR_SPEED;
			if(angle > owner.getWeapon().getMaxAngle()) {
				angle = owner.getWeapon().getMaxAngle();
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !owner.getTeam().isWeaponGuiOpen()) {
			angle = (angle - Gamemode.CROSSHAIR_SPEED) % 180;
			if(angle < owner.getWeapon().getMinAngle()) {
				angle = owner.getWeapon().getMinAngle();
			}
		}
		float dSin = (float) Math.sin(angle * (Math.PI / 180D));
		float dCos = (float) Math.cos(angle * (Math.PI / 180D));

		if (owner.isFacingLeft())
			x = owner.getX() - (dSin * 32);
		else
			x = owner.getX() + (dSin * 32);
		y = owner.getY() + (dCos * 32);

		generateVertexData();
	}

	@Override
	public void generateVertexData() {
		VertexHelper vertexBuffer = new VertexHelper(2 * 4);
		vertexBuffer.put(x, y, 16, 16);
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
	public void render(TextRender textRenderer) {
		glEnableClientState(GL_COLOR_ARRAY);
		owner.bindColor();
		super.render(textRenderer);
		glDisableClientState(GL_COLOR_ARRAY);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

}
