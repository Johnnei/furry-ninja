package game.data;

import game.weapon.set.IWeaponSet;
import game.weapon.set.NormalSet;

public class Gamemode {
	
	//GameMode
	//TODO Add screen to change these values!
	public static int TURN_TIME = 200;
	public static int CHANGE_TIME = 100;
	public static int FREE_FALL_DISTANCE = 100;
	public static float JUMP_SPEED = 1.25F;
	public static int CROSSHAIR_SPEED = 5;
	
	//Game Data - final
	public static final float GRAVITY = 0.20F;
	public static IWeaponSet weaponSet = new NormalSet();

}
