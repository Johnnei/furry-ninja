package game.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import engine.render.TextRender;
import game.Team;
import game.WormsGame;
import game.data.Gamemode;
import game.data.TurnPhase;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;

public class Cube extends LivingEntity {

	/**
	 * The Team this Cube is part of
	 */
	private Team team;
	/**
	 * Determines if this worm can be controlled
	 */
	private boolean myTurn;
	/**
	 * Aiming Angle
	 */
	private float angle;
	/**
	 * The currently selected weapon
	 */
	private int selectedWeapon;
	/**
	 * The cube aiming tool
	 */
	private Crosshair crosshair;
	
	public Cube(int x, int y, int health, Team team, WormsGame wormsGame) {
		super(wormsGame, health, x, y, 16, 16);
		this.team = team;
		myTurn = false;
		angle = 0F;
		selectedWeapon = 0;
		crosshair = new Crosshair(x, y);
		generateVertexData();
		generateColorData();
		generateTextureData();
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
	public void generateTextureData() {
	}
	
	@Override
	public void onTick(TurnPhase turn) {
		super.onTick(turn);
		//Calculate Movement
		if(!isOnGround()) {
			yMotion = -1;
			setFalling(true);
		} else {
			yMotion = 0;
			setFalling(false);
		}
		//Add Keyboard input
		if(myTurn && turn == TurnPhase.PLAY) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				xMotion -= 5;
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				xMotion += 5;
			if((Keyboard.isKeyDown(Keyboard.KEY_RETURN) && isOnGround()))
				setJumping(true);
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				angle += 0.5F;
				if(angle > 90F)
					angle = 90F;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				angle -= 0.5F;
				if(angle < 0F)
					angle = 0F;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				team.getWeapon(selectedWeapon).fire(this);
				myTurn = false;
			}
		}
		
		//Calculate Jumping
		if(isJumping() && fallDuration < 10) {
			yMotion += Gamemode.JUMP_SPEED / (fallDuration * 0.1D);
		}
		
		//Do movement
		doMovement();
		
		if(needRenderUpdate()) {
			generateVertexData();
			setRenderUpdate(false);
		}
		
		crosshair.onTick(getX(), getY());
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
		
		TextRender.getTextRender().drawCentered(x + (width / 2), y - 20, "" + getHealth(), glColorId);
		
		if(myTurn) {
			crosshair.render();
		}
		
		super.render();
	}
	
	public void setMyTurn(boolean b) {
		myTurn = b;
	}
	
	public WormsGame getWormsGame() {
		return wormsGame;
	}

	public Team getTeam() {
		return team;
	}

	/**
	 * Returns if the player has the current turn in the game
	 * @return
	 */
	public boolean hasTurn() {
		return myTurn;
	}

}
