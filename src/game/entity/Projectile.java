package game.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.render.TextRender;

import game.WormsGame;
import game.data.TurnPhase;

public class Projectile extends Entity {
	
	private int minDamage;
	private int maxDamage;
	private float damageRange;
	private Cube owner;
	
	public Projectile(WormsGame wormsGame, Cube owner, int x, int y, int width, int height) {
		super(wormsGame, x, y, width, height);
	}
	
	/**
	 * Gets the recieved damage based on distance
	 * @param distance
	 * @return
	 */
	private int getDamage(float distance) {
		if(distance > damageRange)
			return 0;
		float dmgLossPerUnit = (maxDamage - minDamage) / damageRange;
		return (int)(maxDamage - (dmgLossPerUnit * distance)); 
	}
	
	private void explode() {
		
	}

	@Override
	public void onTick(TurnPhase turn) {
		//Do movement but maintain x speed
		float xMove = xMotion;
		doMovement();
		xMotion = xMove;
		
		//Test if it should explode
		if(!owner.getCollisionBox().intersects(getCollisionBox())) { //Don't Selfkill
			if(wormsGame.collides(this)) { //Should do boom?
				explode();
			}
		}
	}

	@Override
	public void onTurnChange(TurnPhase turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateColorData() {
		FloatBuffer color = BufferUtils.createFloatBuffer(3 * 4);
		float[] colors = owner.getTeam().getColor();
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
	}

}
