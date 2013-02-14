package game.entity;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.WMath;
import engine.render.Renderable;
import engine.render.TextRender;
import engine.render.TextureLoader;
import game.Team;
import game.WormsGame;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;

public class HUD extends Renderable {

	private WormsGame wormsGame;
	private int headerTexture;
	private int healthBarTexture;
	private int healthBarEndTexture;

	public HUD(WormsGame wormsGame) {
		super();
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
	public void generateColorData() {
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(2 * 4 * 5);

		int headerX = 576; // (1280 / 2) - (128 / 2)
		int barY = 28;
		int width = 128;
		int height = 64;
		int barHeight = 16;
		int barWidth = 8;
		
		int[] teamHealth = new int[2];
		for(int i = 0; i < teamHealth.length; i++) {
			Team team = wormsGame.getTeam(i);
			for(int j = 0; j < team.getCubeCount(); j++) {
				teamHealth[i] += team.getCube(j).getHealth();
			}
			teamHealth[i] = (teamHealth[i] - 8) / team.getCubeCount(); 
		}
		System.out.println("Health[0]: " + teamHealth[0] + ", Health[1]: " + teamHealth[1]);

		//Header
		vertexBuffer.put(new float[] { headerX, height, headerX + width, height, headerX + width, 0, headerX, 0 });
		//Healthbar
		int barX = headerX - teamHealth[0];
		int barX2 = headerX + width;
		barX = 0;
		barX2 = 0;
		vertexBuffer.put(new float[] { barX, barY + barHeight, barX + teamHealth[0], barY + barHeight, barX + teamHealth[0], barY, barX, barY});
		barY += barHeight;
		vertexBuffer.put(new float[] { barX2, barY + barHeight, barX2 + teamHealth[1], barY + barHeight, barX2 + teamHealth[1], barY, barX2, barY});
		barY += barHeight;
		//Healthbar ends
		vertexBuffer.put(new float[] { 0, barY + barHeight, 0 + barWidth, barY + barHeight, 0 + barWidth, 0, 0, 0});
		barY += barHeight;
		vertexBuffer.put(new float[] { 0, barY + barHeight, 0 + barWidth, barY + barHeight, 0 + barWidth, 0, 0, 0});
		vertexBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void generateTextureData() {
		headerTexture = TextureLoader.loadTexture("/res/header.png");
		healthBarTexture = TextureLoader.loadTexture("/res/healthbar.png");
		healthBarEndTexture = TextureLoader.loadTexture("/res/healthbar_end.png");
		/*
		 * Floats 0-8: Header
		 * Floats 8 - 24: Healthbar A + B
		 * Floats 24 - 40: Healthbarend A + B
		 */
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(5 * 8);
		textureBuffer.put(new float[] { 0   , 1,    1, 1,    1, 0,    0, 0 });
		textureBuffer.put(new float[] { 0   , 1, 0.5F, 1, 0.5F, 0,    0, 0 });
		textureBuffer.put(new float[] { 0.5F, 1,    1, 1,    1, 0, 0.5F, 0 });
		textureBuffer.put(new float[] { 0   , 1, 0.5F, 1, 0.5F, 0,    0, 0 });
		textureBuffer.put(new float[] { 0.5F, 1,    1, 1,    1, 0, 0.5F, 0 });
		textureBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glActiveTexture(GL_TEXTURE0);

		renderHealthbars();
		renderHealthbarsEnds();
		renderHeader();

		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisable(GL_TEXTURE_2D);

		renderText();
	}

	private void renderHeader() {
		glBindTexture(GL_TEXTURE_2D, headerTexture);

		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(2, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glTexCoordPointer(2, GL_FLOAT, 0, 0L);

		glDrawArrays(GL_QUADS, 0, 4);
	}
	
	private void renderHealthbars() {
		glBindTexture(GL_TEXTURE_2D, healthBarTexture);
		
		glDrawArrays(GL_QUADS, 0, 8);
	}
	
	private void renderHealthbarsEnds() {
		glBindTexture(GL_TEXTURE_2D, healthBarEndTexture);
		
		glDrawArrays(GL_QUADS, 0, 8);
	}

	private void renderText() {
		int time = WMath.ceil_i(wormsGame.getTurnTime() / 20D);
		TextRender.getTextRender().drawCentered(640, 10, "Time left", GL_NONE);
		TextRender.getTextRender().drawCentered(640, 30, "" + time, GL_NONE);
	}

}
