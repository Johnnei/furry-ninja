package game;

import game.data.TurnPhase;
import game.data.WeaponType;
import game.entity.Cube;
import game.weapon.Weapon;

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
	private Weapon[] weapons;
	
	public Team(int size, float[] color, WormsGame wormsGame, int[] spawnsX, int[] spawnsY) {
		this.color = color;
		cubes = new Cube[size];
		for(int i = 0; i < size; i++) {
			cubes[i] = new Cube(spawnsX[i], spawnsY[i], 100, this, wormsGame);
		}
		turnIndex = -1;
		weapons = new Weapon[WeaponType.weaponName.length];
		for(int i = 0; i < weapons.length; i++) {
			weapons[i] = new Weapon(i, WeaponType.weaponStartingAmmo[i]);
		}
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
		if(++turnIndex == cubes.length)
			turnIndex = 0;
		cubes[turnIndex].setMyTurn(true);
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

	public void onTurnPhaseChange(TurnPhase turnPhase) {
		for(int i = 0; i < cubes.length; i++) {
			cubes[i].onTurnChange(turnPhase);
		}
		
	}

}
