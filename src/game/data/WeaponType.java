package game.data;

public class WeaponType {

	/**
	 * Used for weapons with infinitive amounts of ammo
	 */
	public static final int INFINITIVE = 255;
	
	/**
	 * The name of the weapon
	 */
	public static String[] weaponName;
	/**
	 * The width of the weapon texture
	 */
	public static int[] weaponWidth;
	/**
	 * The height of the weapon texture
	 */
	public static int[] weaponHeight;
	/**
	 * The base ammo of a weapon
	 */
	public static byte[] weaponStartingAmmo;
	/**
	 * The minimum amount of damage of a weapon
	 */
	public static byte[] projectileMinDamage;
	/**
	 * the maximum amount of damage for a weapon
	 */
	public static byte[] projectileMaxDamage;
	/**
	 * The range in which the weapon will apply damage
	 */
	public static float[] projectileDamageRange;
	/**
	 * The radius in which it will destroy the world
	 */
	public static float[] projectileLandscapeCut;
	
	
	public static void setWeapons(int size) {
		weaponName = new String[size];
		weaponWidth = new int[size];
		weaponHeight = new int[size];
		weaponStartingAmmo = new byte[size];
		weaponStartingAmmo = new byte[size];
		projectileMinDamage = new byte[size];
		projectileMaxDamage = new byte[size];
		projectileDamageRange = new float[size];
		projectileLandscapeCut = new float[size];
	}
	
	public static void registerWeapon(int id, String name, int width, int height, int startingAmmo, int minDamage, int maxDamage, float range, float landscapeCut) {
		weaponName[id] = name;
		weaponWidth[id] = width;
		weaponHeight[id] = height;
		weaponStartingAmmo[id] = (byte)startingAmmo;
		projectileMinDamage[id] = (byte)minDamage;
		projectileMaxDamage[id] = (byte)maxDamage;
		projectileDamageRange[id] = maxDamage;
		projectileLandscapeCut[id] = landscapeCut;
	}

}
