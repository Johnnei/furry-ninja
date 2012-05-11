package game.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import game.Team;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Cube extends Entity {

	private Team team;
	
	public Cube(int health, Team team) {
		super(health, 16, 16);
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
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void generateVertexData() {
		FloatBuffer vertex = BufferUtils.createFloatBuffer(3 * 4);
		vertex.put(new float[] { x, y + height, 0, x + width, y + height, 0, x + width, y, 0, x, y, 0 });
		vertex.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	@Override
	public void onTick() {
		//TODO Logic
		
		if(needRenderUpdate()) {
			generateVertexData();
			setRenderUpdate(false);
		}
	}

	@Override
	public void render() {
		
		glBindBuffer(GL_ARRAY_BUFFER, glVertexId);
		glVertexPointer(3, GL_FLOAT, 0, 0L);
		glBindBuffer(GL_ARRAY_BUFFER, glColorId);
		glColorPointer(3, GL_FLOAT, 0, 0L);
		
		glDrawArrays(GL_QUADS, 0, 4);
	}

}
