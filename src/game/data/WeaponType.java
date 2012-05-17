package game.data;

public class WeaponType {
	
	public static String[] weaponName;
	public static int[] weaponWidth;
	public static int[] weaponHeight;
	
	public static void setWeapons(int size) {
		weaponName = new String[size];
		weaponWidth = new int[size];
		weaponHeight = new int[size];
	}
	
	public static void registerWeapon(int id, String name, int width, int height) {
		weaponName[id] = name;
		weaponWidth[id] = width;
		weaponHeight[id] = height;
	}

}
