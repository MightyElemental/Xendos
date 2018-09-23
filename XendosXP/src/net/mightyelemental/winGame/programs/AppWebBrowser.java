package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUITextBox;
import net.mightyelemental.winGame.guiComponents.GUIhtmlViewer;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppWebBrowser extends AppWindow {

	private static final long serialVersionUID = 7935648659277287522L;

	GUIhtmlViewer	panel;
	GUITextBox		text;

	public AppWebBrowser(float x, float y, float width, float height) {
		super(x, y, 1000, 1000 / 16f * 9f, "Corner (because edges are boring)");
		height = 1000 / 16f * 9f;
		width = 1000;
		text = new GUITextBox(width - 34, 20, "#URL_BAR");
		this.addGUIObject(text, 5, 5);
		this.addGUIObject(new GUIButton(20, 20, "#go", this).setText("->").setColor(Color.green), width - 25, 5);
		panel = new GUIhtmlViewer(width - 10, height - 85, "Panel", this);
		this.addGUIObject(panel, 5, 50);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		this.clearScreen();
	}

	@Override
	public void updateContent(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPressed(int key, char c) {

	}

	@Override
	public void onComponentPressed(int button, GUIComponent c) {
		if ( c.getUID().equals("#GO") ) {
			panel.displayWebsite(text.getText());
			text.clearText();
		}
	}

}
