package net.mightyelemental.winGame.guiComponents;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;
import net.mightyelemental.winGame.guiComponents.layouts.GUILayout;

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
public class GUIPanel extends GUIComponent {

	private static final long serialVersionUID = -8348255318186951216L;

	private GUILayout layoutType = GUILayout.AbsoluteLayout;

	private int gridCol = 1, gridRow = 1;

	private List<GUIComponent> guiObjects = new ArrayList<GUIComponent>();

	public GUIPanel(float x, float y, float width, float height) {
		super(x, y, width, height, "panel");
	}

	public GUIPanel(float width, float height, AppWindow aw) {
		super(width, height, "panel", aw);
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(color);
		g.fillRoundRect(x, y, width, height, 3);
		for (GUIComponent gui : getGuiObjects()) {
			gui.draw(gc, sbg, g);
		}
	}

	public void addGUIObject(GUIComponent com, float x, float y) {
		if (com instanceof GUIPanel) {
			System.err.println("Cannot add a panel inside a panel");
			return;
		}
		switch (layoutType) {
		default:
		case AbsoluteLayout:
			break;
		case BorderLayout:
			break;
		case GridLayout:
			int items = guiObjects.size();
			if (items > gridCol * gridRow)
				return;
			int newX = (items % gridCol);
			int newY = (items) / gridCol;
			// System.out.println(items + "|" + newX + "|" + newY);
			com.setWidth(width / (float) gridCol - 4);
			com.setHeight(height / (float) gridRow - 4);
			com.setX(newX * (width / (float) gridCol) + this.getX() + 2);
			com.setY(newY * (height / (float) gridRow) + this.getY() + 2);
			guiObjects.add(com);
			break;
		}
	}

	public GUIPanel setLayout(GUILayout lay) {
		layoutType = lay;
		return this;
	}

	public GUIPanel setGridLayout(int col, int row) {
		layoutType = GUILayout.GridLayout;
		this.gridCol = col;
		this.gridRow = row;
		return this;
	}

	public List<GUIComponent> getGuiObjects() {
		return guiObjects;
	}

	public GUIPanel setColor(Color c) {
		color = c;
		return this;
	}

}
