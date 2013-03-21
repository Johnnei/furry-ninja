package game.entity;

import static engine.render.RenderObject.COLOR;
import static engine.render.RenderObject.TEXTURE;
import static engine.render.RenderObject.VERTEX;
import engine.render.ColorHelper;
import engine.render.TextRender;
import engine.render.VertexHelper;
import game.Team;
import game.WormsGame;
import game.data.TurnPhase;
import game.display.ChargeBar;
import game.display.Crosshair;
import game.physics.MotionVector;
import game.weapon.IWeapon;
import game.weapon.Weapon;

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
	 * The currently selected weapon
	 */
	private int selectedWeapon;
	/**
	 * The cube aiming tool
	 */
	private Crosshair crosshair;
	private ChargeBar chargeBar;
	/**
	 * if the cube "face" is looking to the left
	 */
	private boolean facingLeft;
	
	public Cube(int x, int y, int health, Team team, WormsGame wormsGame) {
		super(VERTEX | TEXTURE | COLOR, wormsGame, health, x, y, 16, 32);
		this.team = team;
		myTurn = false;
		facingLeft = (x > 640);
		selectedWeapon = 0;
		crosshair = new Crosshair(this, x, y);
		chargeBar = new ChargeBar(this, crosshair);
		generateVertexData();
		generateTextureData();
		generateColorData();
	}
	
	@Override
	public void generateColorData() {
		float[] colors = team.getColor();
		ColorHelper helper = new ColorHelper(colors[0], colors[1], colors[2]);
		renderObject.updateColor(helper);
	}
	
	@Override
	public void generateTextureData() {
		renderObject.setTexture("/res/ninja_stand.png");
		renderObject.updateTexture();
	}

	@Override
	public void generateVertexData() {
		VertexHelper vertex = new VertexHelper(2 * 4);
		vertex.put(x, y, width, height);
		if(isFacingLeft())
			vertex.mirror();
		
		renderObject.updateVertex(vertex);
	}
	
	@Override
	public void onTick(TurnPhase turn) {
		super.onTick(turn);
		//Add Keyboard input
		if(myTurn && turn == TurnPhase.PLAY && !team.isWeaponGuiOpen()) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				//xMotion -= 5;
				addMotionVector(new MotionVector(-0.75F, 0, 10, 0));
				facingLeft = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				addMotionVector(new MotionVector(0.75F, 0, 10, 0));
				facingLeft = false;
			}
			if((Keyboard.isKeyDown(Keyboard.KEY_RETURN) && isOnGround())) {
				addMotionVector(new MotionVector(0, -7.5F, 0, 20));
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				team.getWeapon(selectedWeapon).charge();
				if(team.getWeapon(selectedWeapon).isMaxCharge()) {
					fireWeapon();
				}
			} else {
				if(team.getWeapon(selectedWeapon).isCharging()) {
					fireWeapon();
				}
			}
		}
		
		//Do movement
		doMovement();
		
		if(myTurn) {
			crosshair.onTick();
			chargeBar.onTick();
		}
		
		if(needRenderUpdate()) {
			generateVertexData();
			setRenderUpdate(false);
		}
	}
	
	private void fireWeapon() {
		team.getWeapon(selectedWeapon).fire(this, crosshair);
		myTurn = false;
	}

	@Override
	public void render(TextRender textRenderer) {
		if(isDead())
			return;
		
		textRenderer.drawCentered(x + (width / 2), y - 20, "" + getHealth(), renderObject);
		
		if(myTurn) {
			chargeBar.render(textRenderer);
			crosshair.render(textRenderer);
		}
		super.render(textRenderer);
	}
	
	public void setMyTurn(boolean b) {
		if(myTurn) {
			if(getSelectedWeapon().isCharging()) {
				fireWeapon();
				team.closeWeaponMenu();
			}
		}
		myTurn = b;
	}
	
	public void setSelectedWeaponIndex(int weaponIndex) {
		selectedWeapon = weaponIndex;
	}

	public Team getTeam() {
		return team;
	}
	
	public Weapon getSelectedWeapon() {
		return team.getWeapon(selectedWeapon);
	}
	
	public IWeapon getWeapon() {
		return team.getWeapon(selectedWeapon).getStats();
	}
	
	public int getSelectedWeaponIndex() {
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
