package net.mightyelemental.winGame.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.XendosMain;
import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUITextBox;

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
public class StateLogin extends BasicGameState {

	private List<GUIComponent> guiComponents = new ArrayList<GUIComponent>();

	private Image loginScreen, startingScreen, blankScreen, welcomeScreen;

	private Sound startup;

	private String selectedUID = "";

	private float pauseTime = 200f, startTime = 420f, welcomeTime = 700f;

	boolean showWelcome;

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		loginScreen = ResourceLoader.loadImage("login.loginScreen");
		startingScreen = ResourceLoader.loadImage("login.startingScreen");
		blankScreen = ResourceLoader.loadImage("login.blankScreen");
		welcomeScreen = ResourceLoader.loadImage("login.welcomeScreen");
		startup = ResourceLoader.loadSound("startup");
		guiComponents.add(new GUIButton(832, 496, 44, 44, "#go").setTransparent(true));
		guiComponents.add(new GUITextBox(505, 495, 294, 45, "#password").setPasswordChar('*')
				.setSelectedShape(new RoundedRectangle(503, 492, 298, 51, 10)));
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		loginScreen.draw();
		if ( pauseTime > 0 ) {
			g.setColor(Color.black);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		} else if ( startTime > 0 ) {
			startingScreen.draw();
		}
		if ( startTime < 120 && startTime > 0 ) {
			blankScreen.draw();
		}
		if ( startTime < 0 ) for ( GUIComponent c : guiComponents ) {
			c.draw(gc, sbg, g);
		}
		if ( showWelcome ) {
			welcomeScreen.draw();
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if ( pauseTime > 0 ) {
			pauseTime -= delta / 8f;
		} else if ( startTime > 0 ) {
			startTime -= delta / 8f;
		}
		if ( showWelcome ) {
			if ( !startup.playing() && welcomeTime < 620 && welcomeTime > 200 ) startup.play();
			welcomeTime -= delta / 8f;
		}
		if ( welcomeTime < 0 ) {
			sbg.enterState(XendosMain.STATE_DESKTOP);
		}
	}

	@Override
	public int getID() {
		return XendosMain.STATE_LOGIN;
	}

	public void onComponentPressed(int button, GUIComponent c) {
		if ( showWelcome ) return;
		if ( c.getUID().equals("#GO") && button == 0 ) {
			if ( ((GUITextBox) guiComponents.get(1)).getText().equals("password") ) {
				System.out.println("GO!");
				//((GUITextBox) guiComponents.get(1)).setText("Correct!");
				showWelcome = true;
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if ( startTime < 0 ) for ( GUIComponent c : guiComponents ) {
			c.setSelected(false);
			if ( c.contains(x, y) ) {
				c.onMousePressed(button);
				onComponentPressed(button, c);
				selectedUID = c.getUID();
				c.setSelected(true);
				// System.out.println(selectedUID);
			}
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if ( startTime < 0 ) for ( GUIComponent c : guiComponents ) {
			if ( c.contains(x, y) ) c.onMouseReleased(button);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		for ( GUIComponent g : guiComponents ) {
			if ( g.getUID().equals(selectedUID) ) {
				g.onKeyPressed(key, c);
			}
		}
	}

}
