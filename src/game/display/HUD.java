package game.display;

import static engine.render.RenderObject.VERTEX_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import engine.WMath;
import engine.render.Renderable;
import engine.render.TextRender;
import engine.render.Texture;
import engine.render.VertexHelper;
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
		VertexHelper vertexBuffer = new VertexHelper(2 * 4 * 5);

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
		vertexBuffer.put(barX, barY, teamHealth[0], barHeight);
		vertexBuffer.put(barX2, barY, teamHealth[1], barHeight);
		//Healthbar ends
		vertexBuffer.put(barX - barWidth, barY, barWidth, barHeight);
		vertexBuffer.put(barX2 + teamHealth[1], barY, barWidth, barHeight);
		//Header
		vertexBuffer.put(headerX, 0, width, height);

		renderObject.updateVertex(vertexBuffer);
	}

	@Override
	public void generateTextureData() {
		Texture texture = new Texture("/res/header.png");
		texture.addSubTexture(128, 0, 1, 16); //Healthbar
		texture.addSubTexture(129, 0, 1, 16); //Healthbar
		texture.addSubTexture(130, 0, 8, 16); //Healthbar end
		texture.addSubTexture(139, 0, 8, 16); //healthbar end
		texture.addSubTexture(0, 0, 128, 64); //Main texture
		texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST); //Repeat texture instead of scaling
		renderObject.setTexture(texture);
		renderObject.updateTexture();
	}
	
	@Override
	public void render(TextRender textRenderer) {
		super.render(textRenderer);
		renderText(textRenderer);
	}

	private void renderText(TextRender textRenderer) {
		int time = WMath.ceil_i(wormsGame.getTurnTime() / 20D);
		textRenderer.drawCentered(640, 10, "Time left", null);
		textRenderer.drawCentered(640, 30, "" + time, null);
	}

}
