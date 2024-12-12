package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;

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
public class Entry {

	private StringBuilder text;
	private boolean onRight, finalized;
	private Color color;

	/**
	 * Create a new Entry instance. An entry is displayed in an EntryPanel and
	 * can be rendered to either side of the panel. This allows for the
	 * appearance of user input and a response.
	 * 
	 * @param text    the text to be displayed
	 * @param onRight should be displayed on the right of the screen. Left is
	 *                default
	 * @param color   the color of the text to be displayed
	 */
	public Entry(String text, boolean onRight, Color color) {
		this(text);
		this.onRight = onRight;
		this.color = color;
	}

	/**
	 * Create a new Entry instance. An entry is displayed in an EntryPanel and
	 * can be rendered to either side of the panel. This allows for the
	 * appearance of user input and a response.<br>
	 * The default color is black<br>
	 * The default position is on the left
	 * 
	 * @param text the text to be displayed
	 */
	public Entry(String text) {
		this.text = new StringBuilder(text);
		onRight = false;
		color = null;
	}

	public String getText() {
		return text.toString();
	}

	public StringBuilder getBuilder() {
		if (finalized) {
			return null;
		}
		return text;
	}

	public boolean isOnRight() {
		return onRight;
	}

	public Color getColor() {
		return color;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public void setFinalized() {
		this.finalized = true;
	}

}
