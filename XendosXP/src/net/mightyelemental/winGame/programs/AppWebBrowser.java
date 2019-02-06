package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUITextBox;
import net.mightyelemental.winGame.guiComponents.GUIhtmlViewer;
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
public class AppWebBrowser extends AppWindow {

	private static final long serialVersionUID = 7935648659277287522L;

	GUIhtmlViewer	panel;
	GUITextBox		text;

	public AppWebBrowser(float x, float y, float width, float height) {
		super(x, y, 1000, 1000 / 16f * 9f, "Corner (because edges are boring)");
		height = 1000 / 16f * 9f;
		width = 1000;
		text = new GUITextBox(width - 34, 20, "#URL_BAR");
		this.addGUIObject(text, 3, 5);
		this.addGUIObject(new GUIButton(20, 20, "#go", this).setText("->").setColor(Color.green), width - 27, 5);
		panel = new GUIhtmlViewer(width - 10, height - 62, "Panel", this);
		this.addGUIObject(panel, 3, 30);
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
		if ( key == Input.KEY_ENTER ) {
			panel.displayWebsite(text.getText());
		}
	}

	@Override
	public void onComponentPressed(int button, GUIComponent c) {
		if ( c.getUID().equals("#GO") ) {
			panel.displayWebsite(text.getText());
		}
	}

}
