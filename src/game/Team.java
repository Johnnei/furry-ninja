package game;

import game.entity.Cube;

public class Team {
	
	private Cube[] cubes;
	private float[] color;
	
	public Team(int size, float[] color) {
		this.color = color;
		cubes = new Cube[size];
		for(int i = 0; i < size; i++) {
			cubes[i] = new Cube(100, this);
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

}
