package net.mightyelemental.winGame.guiComponents;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import net.mightyelemental.winGame.ResourceLoader;
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
public class GUIhtmlViewer extends GUIComponent {

	private static final long serialVersionUID = 3497817514161328855L;

	private Image	page;
	private boolean	loaded;
	private String	fileName;

	public GUIhtmlViewer(float width, float height, String uid, AppWindow aw) {
		super(width, height, uid, aw);
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.draw(gc, sbg, g);
		if ( page != null ) {
			g.drawImage(page, x, y);
		} else if ( loaded ) {
			page = ResourceLoader.loadImage("webcache/" + fileName);
		}
	}

	public void displayWebsite(String url) {
		fileName = url.replaceAll("[^A-Za-z0-9]", "");
		Log.info("Requesting URL - " + url);
		loaded = false;
		// page = ResourceLoader.loadImage("webcache/" + fileName);
		new Thread() {
			public void run() {
				try {
					Process process = new ProcessBuilder("./lib/3rdparty/phantomjs.exe", "/lib/3rdparty/rasterize.js", url,
							"./assets/textures/webcache/" + fileName + ".png", "1280*720px").start();
					process.waitFor();
					loaded = true;
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
