package game;

import game.data.Gamemode;
import game.data.TurnPhase;
import game.entity.Cube;
import game.weapon.Weapon;
import game.weapon.set.WeaponFactory;

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
	
	public Team(int size, float[] color, WormsGame wormsGame, int[] spawnsX, int[] spawnsY) {
		this.color = color;
		cubes = new Cube[size];
		for(int i = 0; i < size; i++) {
			cubes[i] = new Cube(spawnsX[i], spawnsY[i], 100, this, wormsGame);
		}
		turnIndex = -1;
		weapons = new WeaponFactory(Gamemode.weaponSet).createWeapons();
	}
	
	public void render() {
		for(int i = 0; i < cubes.length; i++) {
			cubes[i].render();
		}
	}
	
	public float[] getColor() {
		return color;
	}

	public void onTick(TurnPhase turn) {
		for(int i = 0; i < cubes.length; i++) {
			cubes[i].onTick(turn);
		}
	}
	
	public void onTurnCompleted() {
		cubes[turnIndex].setMyTurn(false);
	}
	
	public void onAdvanceTurn() {
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
	
	public Weapon getWeapon(int i) {
		return weapons[i];
	}

	public void onTurnPhaseChange(TurnPhase turnPhase) {
		for(int i = 0; i < cubes.length; i++) {
			cubes[i].onTurnChange(turnPhase);
		}
		
	}

	public int getCubeCount() {
		return cubes.length;
	}

}
