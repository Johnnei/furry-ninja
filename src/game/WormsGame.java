package game;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Rectangle;

import engine.render.TextRender;

import game.data.TeamColor;
import game.data.TeamSpawn;
import game.data.TurnPhase;
import game.entity.Entity;

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
	
	public WormsGame() {
		//Preparing Data
		teams = new Team[2];
		for(int i = 0; i < teams.length; i++) {
			teams[i] = new Team(4, TeamColor.values()[i].getColor(), this, TeamSpawn.values()[i].getSpawnsX(), TeamSpawn.values()[i].getSpawnsY());
		}
		world = new World();
		//Loading Data
		TextRender.getTextRender().load(48, 57);
		//Initialising First Turn
		turnIndex = 0;
		teams[turnIndex].onAdvanceTurn();
		turnTime = 1200; //60 seconds
		turnPhase = TurnPhase.PLAY;
	}
	
	/**
	 * Executes all onTick events
	 */
	public void onTick() {
		boolean canAdvance = true;
		for(int i = 0; i < teams.length; i++) {
			teams[i].onTick(turnPhase);
			if(turnPhase == TurnPhase.DAMAGE) {
				if(!teams[i].canAdvance())
					canAdvance = false;
			}
		}
		
		if(turnTime > 0)
			--turnTime;
		
		//Override the canAdvance if the time has passed
		if((turnPhase == TurnPhase.PLAY || turnPhase == TurnPhase.CUBE_CHANGE ) && turnTime == 0) 
			canAdvance = true;
		
		if(canAdvance) {
			for(int i = 0; i < teams.length; i++) {
				advanceTurnPhase();
				teams[i].onTurnPhaseChange(turnPhase);
			}
		}
	}
	
	/**
	 * Updates the new Team Turn
	 */
	private void onAdvanceTurn() {
		teams[turnIndex].onTurnCompleted();
		if(++turnIndex == teams.length)
			turnIndex = 0;
		teams[turnIndex].onAdvanceTurn();
		turnTime = 100;
	}
	
	/**
	 * Updates the new TurnPhase
	 */
	private void advanceTurnPhase() {
		if(turnPhase == TurnPhase.CUBE_CHANGE) {
			turnPhase = TurnPhase.PLAY;
		} else if(turnPhase == TurnPhase.PLAY) {
			turnPhase = TurnPhase.DAMAGE;
		} else if(turnPhase == TurnPhase.DAMAGE) {
			turnPhase = TurnPhase.CUBE_CHANGE;
			onAdvanceTurn();
		} else
			throw new RuntimeException("Illegal Turn Phase!");
	}
	
	/**
	 * Renders everything to the screen
	 */
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		//Render World
		world.render();
		
		//Render Cubes
		for(int i = 0; i < teams.length; i++) {
			teams[i].render();
		}
		
		//Render GUI
		int time = turnTime / 20;
		TextRender.getTextRender().draw(40, 40, "" + time, GL_NONE);
	}
	
	public boolean collides(Entity entity) {
		return collides(entity, 0, 0);
	}
	
	public boolean collides(Entity entity, float xOffset, float yOffset) {
		return collides(entity, (int)xOffset, (int)yOffset);
	}

	public boolean collides(Entity entity, int xOffset, int yOffset) {
		Rectangle colBox = entity.getCollisionBox();
		colBox.x += xOffset;
		colBox.y += yOffset;
		
		if(colBox.intersects(world.getCollisionBox()))
			return true;
		
		for(int i = 0; i < teams.length; i++) {
			for(int j = 0; j < teams[i].getSize(); j++) {
				if(!entity.equals(teams[i].getCube(j))) {
					if(colBox.intersects(teams[i].getCube(j).getCollisionBox()))
						return true;
				}
			}
		}
		return false;
	}

}
