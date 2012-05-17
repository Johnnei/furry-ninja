package game.data;

public class WeaponType {
	
	public static String[] weaponName;
	public static int[] weaponWidth;
	public static int[] weaponHeight;
	public static int[] weaponStartingAmmo;
	public static int[] projectileMinDamage;
	public static int[] projectileMaxDamage;
	public static float[] projectileDamageRange;
	
	
	public static void setWeapons(int size) {
		weaponName = new String[size];
		weaponWidth = new int[size];
		weaponHeight = new int[size];
		weaponStartingAmmo = new int[size];
	}
	
	public static void registerWeapon(int id, String name, int width, int height, int startingAmmo, int minDamage, int maxDamage, float range) {
		weaponName[id] = name;
		weaponWidth[id] = width;
		weaponHeight[id] = height;
		weaponStartingAmmo[id] = startingAmmo;
		projectileMinDamage[id] = minDamage;
		projectileMaxDamage[id] = maxDamage;
		projectileDamageRange[id] = maxDamage;
	}

}
