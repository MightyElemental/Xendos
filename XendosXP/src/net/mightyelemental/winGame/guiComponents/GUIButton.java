package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.OSSettings;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

/**
 * XendosXP - A custom operating system that runs in a window Copyright (C) 2018 James
 * Burnell
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, version
 * 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */
public class GUIButton extends GUIComponent {

	private static final long serialVersionUID = -8958146127262539180L;

	/** The text desired to display on the button */
	private String text;
	/**
	 * The text to actually display on the button. This may be different to {@link #text}
	 * if the text is too long.
	 */
	private String displayText;
	/** The font used to render the text */
	private UnicodeFont font = OSSettings.NORMAL_FONT;

	/** Use this constructor if the button will be used stand-alone */
	public GUIButton(float x, float y, float width, float height, String uid) {
		super(x, y, width, height, uid);
		this.setTransparent(false);
		this.setAllowInvertedColor(true);
	}

	/** Use this constructor if the button will be used within a window */
	public GUIButton(float width, float height, String uid, AppWindow aw) {
		this(0, 0, width, height, uid);
		this.setLinkedWindow(aw);
		this.setAllowInvertedColor(true);
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		super.draw(gc, sbg, g);
		g.setFont(font);
		if (isSelected() && this.allowInvertColor) {
			// g.setColor(color);
		} else {
			if (this.getColor().equals(Color.black)) {
				g.setColor(Color.white);
			} else {
				g.setColor(Color.black);
			}
			// g.setColor(getInvertColor(color));
		}
		if (displayText == null) return;
		g.drawString(displayText, textCenterX(), textCenterY());
	}

	private float textCenterX() {
		return x + (width - font.getWidth(displayText)) / 2f;
	}

	private float textCenterY() {
		return y + (height - font.getHeight(displayText)) / 2f;
	}

	public String getText() {
		return text;
	}

	public GUIButton setText(String text) {
		this.text = text;
		setDisplayedText(text);
		return this;
	}

	/**
	 * Set the text to display on the button. This will trim the text so that it fits
	 * within the button area.
	 * 
	 * @param text the text desired to display
	 */
	private void setDisplayedText(String text) {
		StringBuilder tempText = new StringBuilder(text);
		while (font.getWidth(tempText.toString()) >= width && tempText.length() > 0) {
			tempText.deleteCharAt(tempText.length() - 1);
		}
		this.displayText = tempText.toString();
	}

	public void onMousePressed(int button) {
		// Thread.dumpStack();
		this.setSelected(true);
	}

	public void onMouseReleased(int button) {
		this.setSelected(false);
	}

	@Override
	public void onResize() {
		setDisplayedText(text);
	}

}
