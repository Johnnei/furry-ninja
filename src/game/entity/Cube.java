package game.entity;

import static engine.render.RenderObject.COLOR;
import static engine.render.RenderObject.TEXTURE;
import static engine.render.RenderObject.VERTEX;
import engine.render.TextRender;
import engine.render.VertexHelper;
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
	/**
	 * if the cube "face" is looking to the left
	 */
	private boolean facingLeft;
	
	public Cube(int x, int y, int health, Team team, WormsGame wormsGame) {
		super(VERTEX | TEXTURE | COLOR, wormsGame, health, x, y, 16, 32);
		this.team = team;
		myTurn = false;
		facingLeft = (x > 640);
		angle = 0F;
		selectedWeapon = 0;
		crosshair = new Crosshair(this, x, y);
		generateVertexData();
		generateTextureData();
		generateColorData();
	}
	
	@Override
	public void generateColorData() {
		FloatBuffer color = BufferUtils.createFloatBuffer(3 * 4);
		float[] colors = team.getColor();
		color.put(new float[] { colors[0], colors[1], colors[2], colors[0], colors[1], colors[2], colors[0], colors[1], colors[2], colors[0], colors[1], colors[2] });
		color.flip();
		
		renderObject.updateColor(color);
	}
	
	@Override
	public void generateTextureData() {
		renderObject.setTexture("/res/ninja_stand.png");
		renderObject.updateTexture();
	}

	@Override
	public void generateVertexData() {
		VertexHelper vertex = new VertexHelper(2 * 4);
		vertex.put(new float[] { x, y + height, x + width, y + height, x + width, y, x, y });
		if(isFacingLeft())
			vertex.mirror();
		
		renderObject.updateVertex(vertex);
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
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				xMotion -= 5;
				facingLeft = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				xMotion += 5;
				facingLeft = false;
			}
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
				team.getWeapon(selectedWeapon).fire(this, crosshair);
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
		if(isDead())
			return;
		
		TextRender.getTextRender().drawCentered(x + (width / 2), y - 20, "" + getHealth(), renderObject);
		
		if(myTurn) {
			crosshair.render();
		}
		super.render();
	}
	
	public void setMyTurn(boolean b) {
		myTurn = b;
	}

	public Team getTeam() {
		return team;
	}
	
	public int getSelectedWeapon() {
		return selectedWeapon;
	}
	
	public boolean isFacingLeft() {
		return facingLeft;
	}

	/**
	 * Returns if the player has the current turn in the game
	 * @return
	 */
	public boolean hasTurn() {
		return myTurn;
	}

}
