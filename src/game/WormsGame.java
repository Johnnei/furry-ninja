package game;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Rectangle;

import engine.render.TextRender;

import game.data.TeamColor;
import game.data.TeamSpawn;
import game.entity.Entity;

public class WormsGame {
	
	private Team[] teams;
	private World world;
	
	public WormsGame() {
		teams = new Team[2];
		for(int i = 0; i < teams.length; i++) {
			teams[i] = new Team(4, TeamColor.values()[i].getColor(), this, TeamSpawn.values()[i].getSpawnsX(), TeamSpawn.values()[i].getSpawnsY());
		}
		world = new World();
		TextRender.getTextRender().load(48, 57);
	}
	
	/**
	 * Executes all onTick events
	 */
	public void onTick() {
		for(int i = 0; i < teams.length; i++) {
			teams[i].onTick();
		}
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
		
		TextRender.getTextRender().draw(10, 10, "0", 1);
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
