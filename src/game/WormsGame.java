package game;

import static org.lwjgl.opengl.GL11.*;
import engine.render.TeamColor;

public class WormsGame {
	
	private Team[] teams;
	
	public WormsGame() {
		teams = new Team[2];
		for(int i = 0; i < teams.length; i++) {
			teams[i] = new Team(4, TeamColor.values()[0].getColor());
		}
	}
	
	public void onTick() {
		
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);
		for(int i = 0; i < teams.length; i++) {
			teams[i].render();
		}
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}

}
