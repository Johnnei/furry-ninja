package game;

import game.data.TurnPhase;
import game.entity.Cube;

public class Team {
	
	private Cube[] cubes;
	private float[] color;
	
	public Team(int size, float[] color, WormsGame wormsGame, int[] spawnsX, int[] spawnsY) {
		this.color = color;
		cubes = new Cube[size];
		for(int i = 0; i < size; i++) {
			cubes[i] = new Cube(spawnsX[i], spawnsY[i], 100, this, wormsGame);
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
	
	public Cube getCube(int index) {
		return cubes[index];
	}

	public int getSize() {
		return cubes.length;
	}

}
