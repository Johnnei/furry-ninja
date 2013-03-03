package game.data;

import game.weapon.set.IWeaponSet;
import game.weapon.set.NormalSet;

public class Gamemode {
	
	//GameMode
	//TODO Add screen to change these values!
	public static int TURN_TIME = 200;
	public static int CHANGE_TIME = 100;
	public static float FREE_FALL_SPEED = 4.5F;
	public static int CROSSHAIR_SPEED = 5;
	
	//Game Data - final
	public static final float GRAVITY = 9.87F / 20F;
	public static IWeaponSet weaponSet = new NormalSet();

}
