package game.data;

public enum TeamSpawn {
	
	TeamA(new int[] { 10, 110, 210, 310}, new int[] {344, 344, 344, 344}),
	TeamB(new int[] { 1254, 1154, 1054, 954}, new int[] {344, 344, 344, 344 });
	
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
