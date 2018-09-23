package net.mightyelemental.winGame.guiComponents;

import org.newdawn.slick.Color;

public class Entry {

	private StringBuilder text;
	private boolean onRight, finalized;
	private Color color;

	public Entry(String text, boolean onRight, Color c) {
		this.text = new StringBuilder(text);
		this.onRight = onRight;
		this.color = c;
	}

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
