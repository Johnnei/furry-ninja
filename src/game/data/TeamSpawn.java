package game.data;

public enum TeamSpawn {
	
	TeamA(new int[] { 10, 40, 70, 100}, new int[] {304, 304, 304, 304}),
	TeamB(new int[] { 1254, 1224, 1194, 1164}, new int[] {304, 304, 304, 304 });
	
	private int[] spawnsX;
	private int[] spawnsY;
	
	private TeamSpawn(int[] x, int[] y) {
		spawnsX = x;
		spawnsY = y;
	}
	
	public int[] getSpawnsY() {
		return spawnsY;
	}
	
	public int[] getSpawnsX() {
		return spawnsX;
	}

}
