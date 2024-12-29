package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;

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
public class StartWindow extends AppWindow {

	private static final long serialVersionUID = 6373909456505514103L;

	public StartWindow() {
		super(0, 219, 430, 458, "Start");
	}

	private Image menu;

	public void init(GameContainer gc, StateBasedGame delta) throws SlickException {
		menu = ResourceLoader.loadImage("desktop.startMenu");
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		menu.draw(x, y);
		// g.setColor(new Color(30, 79, 178));
		// g.fill(this);
		// g.setColor(Color.white);
		// g.fillRect(1, gc.getHeight() - 440, 420, 350);
		// g.setColor(Color.white.darker());
		// g.fillRect(211, gc.getHeight() - 440, 210, 350);
	}

	@Override
	public void drawContent(Graphics g, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateContent(float delta) {
		// TODO Auto-generated method stub

	}

}
