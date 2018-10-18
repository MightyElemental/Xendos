package net.mightyelemental.winGame.guiComponents;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
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
public class GUIEntryPanel extends GUIComponent {

	private static final long serialVersionUID = -8339400027648252407L;

	private List<Entry> entries = new ArrayList<Entry>();

	public GUIEntryPanel(float x, float y, float width, float height, String uid) {
		super(x, y, width, height, uid);
	}

	public GUIEntryPanel(float width, float height, String uid, AppWindow aw) {
		super(0, 0, width, height, uid);
		this.setLinkedWindow(aw);
	}

	public void addEntry(String text) {
		addEntry(text, false, null);
	}

	public void addEntry(String text, boolean onRight) {
		addEntry(text, onRight, null);
	}

	public void addEntry(String text, boolean onRight, boolean finalize) {
		Entry e = new Entry(text, onRight, null);
		e.setFinalized();
		entries.add(e);
	}

	public void addEntry(String text, boolean onRight, Color c) {
		entries.add(new Entry(text, onRight, c));
	}

	public Entry getLatestEntry() {
		if ( entries.size() > 0 ) return entries.get(entries.size() - 1);
		return null;
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(this.color);
		g.fillRoundRect(x, y, width, height, 3);
		g.setFont(OSSettings.NORMAL_FONT);
		int totalHeight = 0;
		if ( entries.size() > 0 ) {
			g.setColor(Color.black);
			// g.drawString(entries.size() + "", 0, 0);
			for ( int i = entries.size() - 1; i >= 0; i-- ) {
				Entry e = entries.get(i);
				int height = OSSettings.NORMAL_FONT.getHeight(e.getText());
				totalHeight += height;
				if ( totalHeight > this.getHeight() ) break;
				if ( e.isOnRight() ) {
					int width = OSSettings.NORMAL_FONT.getWidth(e.getText());
					g.drawString(e.getText(), this.getX() + this.getWidth() - width - 8, this.getHeight() - totalHeight);
				} else {
					g.drawString(e.getText(), this.getX() + 4, this.getHeight() - totalHeight);
				}
			}
		}
	}

	public void clearEntries() {
		entries.clear();
	}

}
