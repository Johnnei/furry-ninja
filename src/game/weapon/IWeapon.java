package game.weapon;

import game.entity.Crosshair;
import game.entity.Cube;

public interface IWeapon {
	
	//Information
	
	public String getName();
	
	/**
	 * If this weapon has a custom fire implementation
	 * @return if it has a custom fire method
	 */
	public boolean isCustomFire();
	
	/**
	 * If <tt>isCustomFire();</tt> returns true this fire method will be called instead of the default projectile launching
	 * @param owner The cube which fired this weapon
	 * @param crosshair The aiming the cube used
	 */
	public void fire(Cube owner, Crosshair crosshair);
	
	//Ammo
	
	/**
	 * The ammo which is granted on start
	 * @return The starting ammo
	 */
	public int getStartingAmmo();
	/**
	 * The maximum amount of ammo this weapon can stock
	 * @return The limit of ammo
	 */
	public int getMaxAmmo();
	
	//Weapon Stats
	
	/**
	 * The minimum amount of fire power
	 * @return The minimum fire power
	 */
	public int getMinFirePower();
	/**
	 * The maximum amount of fire power
	 * @return The maximum fire power
	 */
	public int getMaxFirePower();
	
	/**
	 * The range in which terrain will be destroyed
	 * @return The range of landscape cut
	 */
	public int getLandscapeCut();
	/**
	 * The range in which entities get damaged
	 * @return The range of damage
	 */
	public int getDamageRange();
	/**
	 * Gets the damage on the middle of the explosion (Highest)
	 * @return The maximum damage value
	 */
	public int getInnerDamage();
	/**
	 * Gets the damage on the outside of the explosion (lowest)
	 * @return The minimum damage value
	 */
	public int getOuterDamage();
	
	//Texture Information
	
	/**
	 * The scale of the explosion<br/>
	 * This is a multiplier on <tt>getExplosionScale() * 64F</tt>
	 * @return The multiplier for the explosion
	 */
	public float getExplosionScale();
	/**
	 * The width of the texture for this weapon
	 * @return The texture width
	 */
	public int getTextureWidth();
	/**
	 * The height of the texture for this weapon
	 * @return The texture height
	 */
	public int getTextureHeight();

}
