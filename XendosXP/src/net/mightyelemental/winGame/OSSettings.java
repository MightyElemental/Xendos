package net.mightyelemental.winGame;

import org.newdawn.slick.UnicodeFont;

public class OSSettings {

	private OSSettings() {
	}

	public static final int FILE_DISPLAY_SIZE = 50;

	public static final int VERSION = 2;

	public static UnicodeFont	FILE_FONT	= ResourceLoader.loadFont("comic_sans", 10);
	public static UnicodeFont	NORMAL_FONT	= ResourceLoader.loadFont("comic_sans", 15);
}
