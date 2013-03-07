package game.entity;

import static engine.render.RenderObject.COLOR;
import static game.data.TurnPhase.DAMAGE;
import engine.render.ColorHelper;
import engine.render.RenderObject;
import engine.render.TextRender;
import game.WormsGame;
import game.data.Gamemode;
import game.data.TurnPhase;
import game.display.Explosion;
import game.weapon.EntityExplode;

public abstract class LivingEntity extends Entity {

	// Health
	private int health;
	private int takeDamage;

	// Render
	/**
	 * The full amount of damage that has been given this turn
	 */
	private int fullDamage;
	/**
	 * Determines if the damage shoud be shown
	 */
	private boolean showDamage;

	public LivingEntity(int flags, WormsGame wormsGame, int health, int x, int y, int width, int height) {
		super(flags, wormsGame, x, y, width, height);
		this.health = health;
		showDamage = false;
		fullDamage = 0;
	}

	public void onTick(TurnPhase turn) {
		if (turn == DAMAGE) {
			if (takeDamage > 0) {
				if (health <= 0) {
					takeDamage = 0;
					onDeath();
				} else {
					--health;
					--takeDamage;
				}
			}
		}
		if (showDamage) {
			if (takeDamage == 0) {
				fullDamage = 0;
				showDamage = false;
			}
		}
	}

	public void takeDamage(int dmg) {
		if(isDead())
			return;
		takeDamage += dmg;
		fullDamage = takeDamage;
	}

	public boolean isDead() {
		return health == 0;
	}

	public int getHealth() {
		return health;
	}
	
	@Override
	public void onFall() {
		float fallSpeed = yMotion - Gamemode.FREE_FALL_SPEED;
		if(fallSpeed > 0) {
			takeDamage((int)(fallSpeed * 2));
		}
	}

	@Override
	public void onTurnChange(TurnPhase turn) {
		if (turn == DAMAGE) {
			if (fullDamage > 0) {
				RenderObject color = null;
				if(fullDamage >= 10) {
					color = new RenderObject(COLOR);
					int r = Math.min(255, 128 + (int)((fullDamage / (float)health) * 227));
					color.updateColor(new ColorHelper(r, 0x22, 0x22));
				}
				getWormsGame().addText(x, y - 20, "" + fullDamage, color);
				showDamage = true;
			}
		}
	}

	/**
	 * Process the death of this cube
	 */
	public void onDeath() {
		takeDamage = 0;
		Explosion e = new Explosion(this, getPoint(), new EntityExplode());
		getWormsGame().addRenderable(e);
		e.explode();
	}

	/**
	 * Returns if the damage has been deducted
	 * 
	 * @return
	 */
	public boolean canAdvance() {
		return takeDamage == 0;
	}

	@Override
	public void render(TextRender textRenderer) {
		if (isDead())
			return;
		renderObject.render();
	}
	
	public boolean hasDamage() {
		return takeDamage > 0;
	}

}
