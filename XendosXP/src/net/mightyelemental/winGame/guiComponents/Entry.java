package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;

public class Entry {

	private StringBuilder text;
	private boolean onRight, finalized;
	private Color color;

	/**
	 * Create a new Entry instance. An entry is displayed in an EntryPanel and can
	 * be rendered to either side of the panel. This allows for the appearance of
	 * user input and a response.
	 * 
	 * @param text
	 *            - the text to be displayed
	 * @param onRight
	 *            - whether or not the text should be displayed on the left or right
	 *            of the screen. Left is default
	 * @param c
	 *            - the color of the text to be displayed
	 */
	public Entry(String text, boolean onRight, Color c) {
		this(text);
		this.onRight = onRight;
		this.color = c;
	}

	/**
	 * Create a new Entry instance. An entry is displayed in an EntryPanel and can
	 * be rendered to either side of the panel. This allows for the appearance of
	 * user input and a response.<br>
	 * The default color is black<br>
	 * The default position is on the left
	 * 
	 * @param text
	 *            - the text to be displayed
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
		if (finalized) {// Prevent users from changing the content
			return new StringBuilder("Finalized");
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
