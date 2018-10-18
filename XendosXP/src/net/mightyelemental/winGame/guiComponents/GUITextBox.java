package net.mightyelemental.winGame.guiComponents;

import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.OSSettings;

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
public class GUITextBox extends GUIComponent {

	private static final long serialVersionUID = 7295479133012661247L;

	private StringBuilder	text		= new StringBuilder("");
	private char			passwordSym	= ' ';

	public GUITextBox(float x, float y, float width, float height, String uid) {
		super(x, y, width, height, uid);
	}

	public GUITextBox(float width, float height, String uid) {
		super(-1, -1, width, height, uid);
	}

	@Override
	public void onKeyPressed(int key, char c) {
		if ( OSSettings.NORMAL_FONT.getWidth(text.toString()) < width * 0.95f ) {
			text.append((c + "").replaceAll("[^A-Za-z0-9 -_+=./|\\;:\"'`~!@#$%^&*(){}]", ""));
		}
		if ( key == 14 && text.length() > 0 ) {
			text.deleteCharAt(text.length() - 1);
		}
		if ( key == 211 && text.length() > 0 ) {
			text.delete(0, text.length());
		}
		// System.out.println(text.toString());
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.draw(gc, sbg, g);
		g.setFont(OSSettings.NORMAL_FONT);

		g.setColor(Color.black);
		String textToDraw = text.toString();
		if ( passwordSym != ' ' ) {
			char[] count = new char[text.length()];
			Arrays.fill(count, passwordSym);
			textToDraw = new String(count);
		}
		g.drawString(textToDraw + (isSelected() ? "|" : ""), x + (width / 2) - g.getFont().getWidth(textToDraw) / 2,
				y + (height / 2) - (g.getFont().getLineHeight() / 2));
	}

	public String getText() {
		return text.toString();
	}

	public void setText(String newText) {
		text.delete(0, text.length());
		text.append(newText);
	}

	public GUITextBox setPasswordChar(char c) {
		this.passwordSym = c;
		return this;
	}

	public void onMousePressed(int button) {
		this.setSelected(true);
	}

	public void clearText() {
		text.delete(0, text.length());
	}
}
