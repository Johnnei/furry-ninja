package game.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import engine.render.TextRender;
import game.Team;
import game.WormsGame;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Cube extends LivingEntity {

	private Team team;
	
	public Cube(int x, int y, int health, Team team, WormsGame wormsGame) {
		super(wormsGame, health, x, y, 16, 16);
		this.team = team;
		generateVertexData();
		generateColorData();
	}
	
	@Override
	public void generateColorData() {
		FloatBuffer color = BufferUtils.createFloatBuffer(3 * 4);
		float[] colors = team.getColor();
		color.put(new float[] { colors[0], colors[1], colors[2], colors[0], colors[1], colors[2], colors[0], colors[1], colors[2], colors[0], colors[1], colors[2] });
		color.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glBufferData(GL_ARRAY_BUFFER, color, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertex = BufferUtils.createFloatBuffer(3 * 4);
		vertex.put(new float[] { x, y + height, 0, x + width, y + height, 0, x + width, y, 0, x, y, 0 });
		vertex.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, GL_NONE);
	}
	
	
	
	@Override
	public void onTick() {
		super.onTick();
		//Calculate Movement
		if(!isOnGround()) {
			float yAdjust = 1 + (float)Math.pow(1.1D, (double)fallDuration);
			if(yAdjust > 5)
				yAdjust = 5;
			yMotion -= yAdjust;
			setFalling(true);
		}
		
		//Do movement
		doMovement();
		
		if(needRenderUpdate()) {
			generateVertexData();
			setRenderUpdate(false);
		}
	}

	@Override
	public void render() {
		
		
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glColorPointer(3, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		
		//TextRender.getTextRender().draw(x, y - 40, "" + getHealth(), glColorId);
	}

}
