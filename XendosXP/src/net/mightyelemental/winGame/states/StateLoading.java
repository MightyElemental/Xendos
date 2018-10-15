package net.mightyelemental.winGame.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.XendosMain;

/**
 * XendosXP - A custom operating system that runs in a window Copyright (C) 2018
 * James Burnell
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
public class StateLoading extends BasicGameState {
	
	
	private int ID = 0;
	
	public StateLoading( int ID ) {
		this.ID = ID;
	}
	
	private Image loadingScreen, biosScreen, loadingBar;
	
	private float biosTime = 300f;
	
	private float xPos, counts;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		loadingScreen = ResourceLoader.loadImage("loading.loadScreen");
		loadingBar = ResourceLoader.loadImage("loading.loadingBar");
		biosScreen = ResourceLoader.loadImage("loading.loadBios");
		float scale = (XendosMain.WIDTH / 16.0f * 9.0f) / loadingScreen.getHeight();
		loadingScreen = loadingScreen.getScaledCopy(scale);
		loadingBar = loadingBar.getScaledCopy(scale);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		loadingScreen.draw(gc.getWidth() / 2 - loadingScreen.getWidth() / 2, 0);
		renderLoadingBar(gc, sbg, g);
		if (biosTime > 0) {
			biosScreen.draw(gc.getWidth() / 2 - biosScreen.getWidth() / 2, 0);
			if (biosTime < 80) {
				g.setColor(Color.black);
				g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			}
		}
		
	}
	
	public void renderLoadingBar(GameContainer gc, StateBasedGame sbg, Graphics g) {
		g.setColor(Color.blue);
		int startX = 500;
		g.fillRoundRect(startX + xPos, 530, 10, 15, 2);
		g.fillRoundRect(startX - 14 + xPos, 530, 10, 15, 2);
		g.fillRoundRect(startX - 28 + xPos, 530, 10, 15, 2);
		loadingBar.draw(392, 527);
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (biosTime > 0) {
			biosTime -= delta / 8f;
		} else {
			xPos += 1 * delta / 8f;
			xPos = xPos > 260 ? 0 : xPos;
			counts = xPos == 0 ? counts + 1 : counts;
		}
		
		if (counts >= 3) {
			sbg.enterState(XendosMain.STATE_LOGIN);
		}
	}
	
	@Override
	public int getID() {
		return ID;
	}
	
}
