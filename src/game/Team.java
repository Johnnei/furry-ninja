package game;

import engine.GameKeyboard;
import engine.render.TextRender;
import game.data.Gamemode;
import game.data.TurnPhase;
import game.display.WeaponGui;
import game.entity.Cube;
import game.weapon.Weapon;
import game.weapon.set.WeaponFactory;

import org.lwjgl.input.Keyboard;

public class Team {
	
	/**
	 * The Cubes of this team
	 */
	private Cube[] cubes;
	/**
	 * The color array of this team
	 */
	private float[] color;
	/**
	 * The index of the current cube which turn it is, By default -1 so the first turn it will become index 0
	 */
	private int turnIndex;
	/**
	 * List of the shared team weapons
	 */
	private Weapon[] weapons;
	private WeaponGui weaponMenu;
	private boolean weaponMenuOpen;
	
	public Team(int size, float[] color, WormsGame wormsGame, int[] spawnsX, int[] spawnsY) {
		weapons = new WeaponFactory(Gamemode.weaponSet).createWeapons();
		this.color = color;
		cubes = new Cube[size];
		for(int i = 0; i < size; i++) {
			cubes[i] = new Cube(spawnsX[i], spawnsY[i], 100, this, wormsGame);
		}
		turnIndex = -1;
		weaponMenuOpen = false;
		weaponMenu = new WeaponGui(this);
	}
	
	public void render(TextRender textRenderer) {
		for(int i = 0; i < cubes.length; i++) {
			cubes[i].render(textRenderer);
		}
		renderWeaponGui(textRenderer);
	}
	
	public float[] getColor() {
		return color;
	}

	public void onTick(TurnPhase turn) {
		for(int i = 0; i < cubes.length; i++) {
			cubes[i].onTick(turn);
		}
		if(turn == TurnPhase.PLAY && getActiveCube().hasTurn()) {
			if(GameKeyboard.getInstance().isKeyPressed(Keyboard.KEY_A)) {
				weaponMenuOpen = !weaponMenuOpen;
			}
			if(weaponMenuOpen) {
				weaponMenu.onTick();
			}
		}
	}
	
	public void onTurnCompleted() {
		cubes[turnIndex].setMyTurn(false);
		closeWeaponMenu();
	}
	
	public void onAdvanceTurn() {
		closeWeaponMenu();
		while(true) {
			if(++turnIndex == cubes.length)
				turnIndex = 0;
			if(!cubes[turnIndex].isDead()) {
				cubes[turnIndex].setMyTurn(true);
				break;
			}
		}
	}
	
	public Cube getCube(int index) {
		return cubes[index];
	}

	public int getSize() {
		return cubes.length;
	}

	public boolean canAdvance() {
		for(int i = 0; i < cubes.length; i++) {
			if(!cubes[i].canAdvance())
				return false;
		}
		return true;
	}
	
	public Weapon[] getWeapons() {
		return weapons;
	}
	
	public Weapon getWeapon(int i) {
		return weapons[i];
	}

	public void onTurnPhaseChange(TurnPhase turnPhase) {
		for(int i = 0; i < cubes.length; i++) {
			cubes[i].onTurnChange(turnPhase);
		}
		
	}
	
	public Cube getActiveCube() {
		if(turnIndex < 0 || turnIndex >= cubes.length)
			return cubes[0];
		return cubes[turnIndex];
	}

	public int getCubeCount() {
		return cubes.length;
	}
	
	public void closeWeaponMenu() {
		weaponMenuOpen = false;
	}

	public void renderWeaponGui(TextRender textRenderer) {
		if(weaponMenuOpen) {
			weaponMenu.render(textRenderer);
		}
	}
	
	public boolean isWeaponGuiOpen() {
		return weaponMenuOpen;
	}

}
