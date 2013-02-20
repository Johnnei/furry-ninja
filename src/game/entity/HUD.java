package game.entity;

import static engine.render.RenderObject.VERTEX_TEXTURE;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.WMath;
import engine.render.Renderable;
import engine.render.TextRender;
import engine.render.Texture;
import game.Team;
import game.WormsGame;

public class HUD extends Renderable {

	private WormsGame wormsGame;

	public HUD(WormsGame wormsGame) {
		super(VERTEX_TEXTURE, false, 5);
		this.wormsGame = wormsGame;
		generateVertexData();
		generateTextureData();
	}

	public void onTick() {
		generateVertexData();
	}

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(2 * 4 * 5);

		final int headerX = 576; // (1280 / 2) - (128 / 2)
		final int barY = 28;
		final int width = 128;
		final int height = 64;
		final int barHeight = 16;
		final int barWidth = 8;
		
		int[] teamHealth = new int[2];
		for(int i = 0; i < teamHealth.length; i++) {
			Team team = wormsGame.getTeam(i);
			for(int j = 0; j < team.getCubeCount(); j++) {
				teamHealth[i] += team.getCube(j).getHealth();
			}
			teamHealth[i] = (teamHealth[i] - 8) / (team.getCubeCount() / 2);
		}
		//Healthbar
		int barX = headerX - teamHealth[0] + 18;
		int barX2 = headerX + width - 18;
		vertexBuffer.put(new float[] { barX, barY + barHeight, barX + teamHealth[0], barY + barHeight, barX + teamHealth[0], barY, barX, barY});
		vertexBuffer.put(new float[] { barX2, barY + barHeight, barX2 + teamHealth[1], barY + barHeight, barX2 + teamHealth[1], barY, barX2, barY});
		//Healthbar ends
		vertexBuffer.put(new float[] { barX - barWidth, barY + barHeight, barX, barY + barHeight, barX, barY, barX - barWidth, barY});
		vertexBuffer.put(new float[] { barX2 + teamHealth[1], barY + barHeight, barX2 + teamHealth[1] + barWidth, barY + barHeight, barX2 + teamHealth[1] + barWidth, barY, barX2 + teamHealth[1], barY});
		//Header
		vertexBuffer.put(new float[] { headerX, height, headerX + width, height, headerX + width, 0, headerX, 0 });
		vertexBuffer.flip();

		renderObject.updateVertex(vertexBuffer);
	}

	@Override
	public void generateTextureData() {
		Texture texture = new Texture("/res/header.png");
		texture.addSubTexture(0, 0, 128, 64);
		texture.addSubTexture(128, 0, 1, 16);
		texture.addSubTexture(129, 0, 1, 16);
		texture.addSubTexture(145, 0, 8, 16);
		texture.addSubTexture(153, 0, 8, 16);
		renderObject.setTexture(texture);
		renderObject.updateTexture();
	}
	
	@Override
	public void render() {
		super.render();
		renderText();
	}

	private void renderText() {
		int time = WMath.ceil_i(wormsGame.getTurnTime() / 20D);
		TextRender.getTextRender().drawCentered(640, 10, "Time left", null);
		TextRender.getTextRender().drawCentered(640, 30, "" + time, null);
	}

}
