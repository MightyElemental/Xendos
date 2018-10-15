package net.mightyelemental.winGame;

import org.newdawn.slick.UnicodeFont;

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
public class OSSettings {

	private OSSettings() {
	}

	public static final int FILE_DISPLAY_SIZE = 50;

	public static final int VERSION = 2;

	public static UnicodeFont	FILE_FONT	= ResourceLoader.loadFont("comic_sans", 10);
	public static UnicodeFont	NORMAL_FONT	= ResourceLoader.loadFont("comic_sans", 15);
}
