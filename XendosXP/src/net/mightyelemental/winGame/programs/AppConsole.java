package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import net.mightyelemental.winGame.guiComponents.GUIEntryPanel;
import net.mightyelemental.winGame.guiComponents.GUITextBox;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppConsole extends AppWindow {

	private static final long serialVersionUID = 2634029866337582927L;

	GUIEntryPanel	entryPanel;
	GUITextBox		textPanel;

	public AppConsole(float x, float y, float width, float height) {
		super(x, y, width, height, "Console");
		entryPanel = new GUIEntryPanel(width - 4, height - 70, "#Entry", this);
		entryPanel.setColor(Color.black);
		textPanel = new GUITextBox(width - 5, 40, "#text");

		this.addGUIObject(entryPanel, 0, 0);
		this.addGUIObject(textPanel, 0, height - 69);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {

	}

	@Override
	public void updateContent(int delta) {

	}

	@Override
	public void onKeyPressed(int key, char c) {
		if ( key == Input.KEY_ENTER ) {
			entryPanel.addEntry(textPanel.getText());
			textPanel.clearText();
		}else if(key == Input.KEY_UP) {
			textPanel.setText(entryPanel.getLatestEntry().getText());
		}
	}

}
