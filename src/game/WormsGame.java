package game;

import engine.GameKeyboard;
import engine.render.RenderObject;
import engine.render.Renderable;
import engine.render.TextRender;
import game.data.Gamemode;
import game.data.TeamColor;
import game.data.TeamSpawn;
import game.data.TurnPhase;
import game.display.FloatingText;
import game.display.HUD;
import game.entity.Entity;
import game.entity.Projectile;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

public class WormsGame {
	
	/**
	 * The competing teams
	 */
	private Team[] teams;
	/**
	 * The world on which is being played
	 */
	private World world;
	/**
	 * The current game phase
	 */
	private TurnPhase turnPhase;
	/**
	 * The current team index which can play
	 */
	private int turnIndex;
	/**
	 * The remaining turnTime
	 */
	private int turnTime;
	/**
	 * All projectiles floating through the air
	 */
	private ArrayList<Projectile> projectiles;
	/**
	 * All generic entities
	 */
	private ArrayList<Entity> entities;
	private ArrayList<Renderable> renderables;
	/**
	 * The recorded FPS by the engine
	 */
	private int fps;
	/**
	 * The Heads-up display informations
	 */
	private HUD hud;
	
	public WormsGame() {
		//Initialising
		projectiles = new ArrayList<Projectile>();
		entities = new ArrayList<Entity>();
		renderables = new ArrayList<Renderable>();
		
		//Preparing Data
		teams = new Team[2];
		for(int i = 0; i < teams.length; i++) {
			teams[i] = new Team(4, TeamColor.values()[i].getColor(), this, TeamSpawn.values()[i].getSpawnsX(), TeamSpawn.values()[i].getSpawnsY());
		}
		world = new World();
		hud = new HUD(this);
		
		//Initialising First Turn
		turnIndex = 0;
		teams[turnIndex].onAdvanceTurn();
		turnTime = Gamemode.TURN_TIME;
		turnPhase = TurnPhase.PLAY;
		
		//Prepare Keyboard Tracking
		GameKeyboard.getInstance().trackKey(Keyboard.KEY_A);
		GameKeyboard.getInstance().trackKey(Keyboard.KEY_UP);
		GameKeyboard.getInstance().trackKey(Keyboard.KEY_LEFT);
		GameKeyboard.getInstance().trackKey(Keyboard.KEY_RIGHT);
		GameKeyboard.getInstance().trackKey(Keyboard.KEY_DOWN);
		GameKeyboard.getInstance().trackKey(Keyboard.KEY_RETURN);
	}
	
	/**
	 * Executes all onTick events
	 */
	public void onTick() {
		boolean canAdvance = false;
		boolean noAdvance = false;
		boolean hasPlayingCube = false;
		for(int i = 0; i < teams.length; i++) {
			teams[i].onTick(turnPhase);
			if(turnPhase == TurnPhase.DAMAGE) {
				if(!teams[i].canAdvance()) {
					noAdvance = true;
				}
			} else if (turnPhase == TurnPhase.PLAY) {
				for(int j = 0; j < teams[i].getCubeCount(); j++) {
					if(teams[i].getCube(j).hasTurn()) {
						hasPlayingCube = true;
					}
				}
			}
		}
		
		
		for(int i = 0; i < projectiles.size(); i++) {
			if(projectiles.get(i).canDelete()) {
				Projectile p = projectiles.remove(i);
				p.onDelete(); 
				--i;
			} else
				projectiles.get(i).onTick(turnPhase);
		}
		
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).onTick(turnPhase);
			if(entities.get(i).canDelete()) {
				Entity e = entities.remove(i);
				e.onDelete();
				--i;
			}
		}
		
		for(int i = 0; i < renderables.size(); i++) {
			renderables.get(i).onTick();
			if(renderables.get(i).canDelete()) {
				Renderable r = renderables.remove(i--);
				r.onDelete();
			}
		}
		
		if(turnTime > 0)
			--turnTime;
		
		//Override the canAdvance if the time has passed
		if((turnPhase == TurnPhase.PLAY || turnPhase == TurnPhase.CUBE_CHANGE ) && turnTime == 0) 
			canAdvance = true;
		
		if(turnPhase == TurnPhase.PLAY && !hasPlayingCube)
			turnTime = 1;
		
		if(turnPhase == TurnPhase.PROJECTILE_WAIT && projectiles.size() == 0)
			canAdvance = true;
		
		if(turnPhase == TurnPhase.DAMAGE) {
			canAdvance = !noAdvance;
		}
		
		
		if(canAdvance) {
			advanceTurnPhase();
			for(int i = 0; i < teams.length; i++) {
				teams[i].onTurnPhaseChange(turnPhase);
			}
		}
		
		//Update the HUD as the last thing so all changes can be displayed instantly
		hud.onTick();
	}
	
	/**
	 * Updates the new Team Turn
	 */
	private void onAdvanceTurn() {
		teams[turnIndex].onTurnCompleted();
		System.out.println("Completed Turn for Team: " + turnIndex);
		if(++turnIndex == teams.length)
			turnIndex = 0;
		System.out.println("Started Turn for Team: " + turnIndex);
		teams[turnIndex].onAdvanceTurn();
		turnTime = Gamemode.CHANGE_TIME;
	}
	
	private void onTurnPlay() {
		turnTime = Gamemode.TURN_TIME; //60 seconds
	}
	
	/**
	 * Updates the new TurnPhase
	 */
	private void advanceTurnPhase() {
		if(turnPhase == TurnPhase.CUBE_CHANGE) {
			System.out.println("Changed phase from CUBE_CHANGE to PLAY");
			turnPhase = TurnPhase.PLAY;
			onTurnPlay();
		} else if(turnPhase == TurnPhase.PLAY) {
			System.out.println("Changed phase from PLAY to PROJECTILE_WAIT");
			turnPhase = TurnPhase.PROJECTILE_WAIT;
		} else if(turnPhase == TurnPhase.PROJECTILE_WAIT) {
			System.out.println("Changed phase from PROJECTILE_WAIT to DAMAGE");
			turnPhase = TurnPhase.DAMAGE;
		} else if(turnPhase == TurnPhase.DAMAGE) {
			System.out.println("Changed phase from DAMAGE to CUBE_CHANGE");
			turnPhase = TurnPhase.CUBE_CHANGE;
			onAdvanceTurn();
		} else
			throw new RuntimeException("Illegal Turn Phase!");
	}
	
	/**
	 * Renders everything to the screen
	 */
	public void render(TextRender textRenderer) {
		//Render World
		world.render();
		
		//Render Entities
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(textRenderer);
		}
		
		//Render Cubes
		for(int i = 0; i < teams.length; i++) {
			teams[i].render(textRenderer);
		}
		
		//Render Projectiles
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(textRenderer);
		}
		
		//Render generic renderables
		for(int i = 0; i < renderables.size(); i++) {
			renderables.get(i).render(textRenderer);
		}
		
		teams[turnIndex].renderWeaponGui(textRenderer);
		
		//Render GUI
		hud.render(textRenderer);
		textRenderer.draw(1100, 40, "FPS: " + fps, null);
	}
	
	/**
	 * Add the projectile the projectileList
	 * @param p
	 */
	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}
	
	/**
	 * Returns whether the entity collides
	 * @param entity
	 * @return
	 */
	public boolean collides(Entity entity) {
		return collides(entity, 0, 0);
	}
	
	/**
	 * Returns whether the entity collides
	 * @param entity
	 * @param xOffset
	 * @param yOffset
	 * @return
	 */
	public boolean collides(Entity entity, float xOffset, float yOffset) {
		return collides(entity, (int)xOffset, (int)yOffset);
	}

	/**
	 * Returns whether the entity collides
	 * @param entity
	 * @param xOffset
	 * @param yOffset
	 * @return
	 */
	public boolean collides(Entity entity, int xOffset, int yOffset) {
		Rectangle colBox = entity.getCollisionBox();
		colBox.x += xOffset;
		colBox.y += yOffset;
		
		//Inside the game field
		if(colBox.x < 0 || colBox.x + colBox.width > 1280 || colBox.y >= 720 - colBox.height)
			return true;
		
		//World
		if(world.collides(colBox)) {
			return true;
		}
		
		for(int i = 0; i < teams.length; i++) {
			for(int j = 0; j < teams[i].getSize(); j++) {
				if(!teams[i].getCube(j).isDead()) {
					if(!entity.equals(teams[i].getCube(j))) {
						if(colBox.intersects(teams[i].getCube(j).getCollisionBox()))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	public int getTurnTime() {
		return turnTime;
	}
	
	public int getTeamCount() {
		return teams.length;
	}
	
	public Team getTeam(int i) {
		return teams[i];
	}
	
	public World getWorld() {
		return world;
	}

	public void addText(float x, float y, String text, RenderObject renderObject) {
		entities.add(new FloatingText(text, renderObject, this, x, y));
	}
	
	public void addRenderable(Renderable renderable) {
		renderables.add(renderable);
	}
	
	public void setFps(int fps) {
		this.fps = fps;
	}

	public TurnPhase getTurnPhase() {
		return turnPhase;
	}

}
