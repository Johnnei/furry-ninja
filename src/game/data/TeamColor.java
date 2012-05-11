package game.data;

public enum TeamColor {
	
	RED(new float[] { 1, 0, 0}), BLUE(new float[] {0, 0, 1});
	
	private float[] colors;
	
	private TeamColor(float[] colorArray) {
		colors = colorArray;
	}
	
	public float[] getColor() {
		return colors;
	}

}
