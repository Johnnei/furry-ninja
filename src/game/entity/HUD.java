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

		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void generateTextureData() {
		glTextureId = TextureLoader.loadTexture("/res/header.png");
		final float headerWidth = 128 / 256F;
		final float pixelWidth = 1 / 256F;
		final float headerBarAX = headerWidth + pixelWidth;
		final float headerBarBX = headerWidth + (2 * pixelWidth);
		final float headerBarCX = headerWidth + (3 * pixelWidth);
		final float endsWidth = 8 / 256F;
		final float headerEndA = headerBarCX + endsWidth;
		final float headerEndB = headerBarCX + (2 * endsWidth);
		final float height = 16 / 64F;
		
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(5 * 8);
		//Headerbar
		textureBuffer.put(new float[] { headerBarAX, height, headerBarBX, height, headerBarBX, 0, headerBarAX, 0 });
		textureBuffer.put(new float[] { headerBarBX, height, headerBarCX, height, headerBarCX, 0, headerBarBX, 0 });
		//Headerbar Ends
		textureBuffer.put(new float[] { headerBarCX, height, headerEndA , height, headerBarCX, 0, 0          , 0 });
		textureBuffer.put(new float[] { headerEndA , height, headerEndB , height, headerEndB,  0, headerEndA , 0 });
		//Header
		textureBuffer.put(new float[] { 0   	   , 1	   , headerWidth, 1	    , headerWidth, 0, 0		     , 0 });
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

		glBindTexture(GL_TEXTURE_2D, glTextureId);

		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(2, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glTextureCoordId);
		glTexCoordPointer(2, GL_FLOAT, 0, 0L);

		glDrawArrays(GL_QUADS, 0, 20);

		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisable(GL_TEXTURE_2D);

		renderText();
	}

	private void renderText() {
		int time = WMath.ceil_i(wormsGame.getTurnTime() / 20D);
		TextRender.getTextRender().drawCentered(640, 10, "Time left", GL_NONE);
		TextRender.getTextRender().drawCentered(640, 30, "" + time, GL_NONE);
	}

}
