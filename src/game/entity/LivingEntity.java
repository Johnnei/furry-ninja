package game.entity;

import engine.render.TextRender;
import game.WormsGame;
import game.data.TurnPhase;
import static game.data.TurnPhase.*;

public abstract class LivingEntity extends Entity {

	//Health
	private int health;
	private int takeDamage;
	
	//Render
	/**
	 * The full amount of damage that has been given this turn
	 */
	private int fullDamage;
	/**
	 * Determines if the damage shoud be shown
	 */
	private boolean showDamage;
	/**
	 * The time that the damage has been shown
	 */
	private int showDamageTime;
	
	public LivingEntity(WormsGame wormsGame, int health, int x, int y, int width, int height) {
		super(wormsGame, x, y, width, height);
		this.health = health;
		showDamage = false;
		fullDamage = 0;
		showDamageTime = 0;
	}
	
	public void onTick(TurnPhase turn) {
		if(turn == PLAY) {
			if(isFalling()) {
				fallDuration++;
			}
		} else if(turn == DAMAGE) {
			if(takeDamage > 0) {
				if(health <= 0) {
					takeDamage = 0;
				} else {
					--health;
					--takeDamage;
				}
			}
		}
	}
	
	public void setFalling(boolean b) {
		if(isFalling() && !b && fallDistance > 0) {
			int fallDamage = (int)((fallDistance - 5) * 0.5);
			takeDamgage(fallDamage);
		}
		if(!isFalling())
			fallDuration = (b) ? 1 : 0;
	}
	
	public void takeDamgage(int dmg) {
		takeDamage += dmg;
		fullDamage = takeDamage;
	}
	
	public boolean isDead() {
		return health == 0;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void onTurnChange(TurnPhase turn) {
		if(turn == DAMAGE) {
			if(fullDamage > 0) {
				showDamage = true;
			}
		}
	}
	
	/**
	 * Returns if the damage has been deducted
	 * @return
	 */
	public boolean canAdvance() {
		return !showDamage;
	}
	
	public void render() {
		if(showDamage) {
			TextRender.getTextRender().drawCentered(x, y - 40 - showDamageTime, "-" + fullDamage, glColorId);
			if(takeDamage == 0) {
				fullDamage = 0;
				showDamageTime = 0;
				showDamage = false;
			} else {
				showDamageTime++;
			}
		}
	}

}
