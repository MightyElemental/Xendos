package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.OSSettings;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;


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
public class GUIButton extends GUIComponent {

	private static final long serialVersionUID = -8958146127262539180L;

	private String text = "";

	public GUIButton(float x, float y, float width, float height, String uid) {
		super(x, y, width, height, uid);
		this.setTransparent(false);
		this.setAllowInvertedColor(true);
	}

	public GUIButton(float width, float height, String uid, AppWindow aw) {
		this(0, 0, width, height, uid);
		this.setLinkedWindow(aw);
		this.setAllowInvertedColor(true);
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.draw(gc, sbg, g);
		g.setFont(OSSettings.NORMAL_FONT);
		String tempText = text;
		while (OSSettings.NORMAL_FONT.getWidth(tempText) >= width) {
			tempText = tempText.substring(0, tempText.length() - 1);
		}
		if ( isSelected() ) {
			g.setColor(color);
		} else {
			g.setColor(getInvertColor(color));
		}
		g.drawString(tempText, x + (width / 2f) - OSSettings.NORMAL_FONT.getWidth(tempText) / 2f,
				y + (height / 2f) - OSSettings.NORMAL_FONT.getHeight(tempText) / 2f);
	}

	public String getText() {
		return text;
	}

	public GUIButton setText(String text) {
		this.text = text;
		return this;
	}

	public void onMousePressed(int button) {
		// Thread.dumpStack();
		this.setSelected(true);
	}

	public void onMouseReleased(int button) {
		this.setSelected(false);
	}

}
