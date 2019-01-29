package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

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
public class GUIComponent extends Rectangle {

	private static final long serialVersionUID = 5967548527327574045L;

	public Color color = Color.white;
	private String UID;

	private boolean selected, transparent = false, allowInvertColor = false;

	private AppWindow linkedWindow;

	protected Shape selectedShape;

	private long lastClicked = 0;

	public GUIComponent setColor(Color c) {
		color = c;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public GUIComponent setTransparent(boolean t) {
		this.transparent = t;
		return this;
	}

	public GUIComponent(float x, float y, float width, float height, String uid) {
		super(x, y, width, height);
		if (uid.startsWith("#")) {
			UID = uid.toUpperCase();
		} else {
			UID = (System.currentTimeMillis() % 15937) + "_" + uid.toUpperCase();
		}
	}

	public GUIComponent(float width, float height, String uid, AppWindow aw) {
		this(0, 0, width, height, uid);
		this.linkedWindow = aw;
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		if (this.isSelected() && allowInvertColor) {
			g.setColor(getInvertColor(color));
		} else {
			g.setColor(color);
		}
		if (!transparent) {
			g.fillRoundRect(x, y, width, height, 3);

			if (this.isSelected()) {
				g.setColor(color.darker());
			}

			if (selectedShape == null) {
				g.drawRoundRect(x, y, width, height, 3);
			} else {
				g.draw(selectedShape);
			}
		}
	}

	public void onMousePressed(int button) {
		lastClicked = System.currentTimeMillis();
	}

	public void onMouseReleased(int button) {

	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public String getUID() {
		return UID;
	}

	public String getNID() {
		if (!getUID().startsWith("#")) {
			return getUID().split("_", 2)[0];
		} else {
			return getUID().replaceFirst("#", "");
		}
	}

	public void onKeyPressed(int key, char c) {

	}

	public Shape getSelectedShape() {
		return selectedShape;
	}

	public GUIComponent setSelectedShape(Shape selectedShape) {
		this.selectedShape = selectedShape;
		return this;
	}

	public AppWindow getLinkedWindow() {
		return linkedWindow;
	}

	public void setLinkedWindow(AppWindow aw) {
		this.linkedWindow = aw;
	}

	public long lastClicked() {
		return lastClicked;
	}

	public void setAllowInvertedColor(boolean c) {
		this.allowInvertColor = c;
	}

	public Color getInvertColor(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return new Color(255 - r, 255 - g, 255 - b);
	}

}
