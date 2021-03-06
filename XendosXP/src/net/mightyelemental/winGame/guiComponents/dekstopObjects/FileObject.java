package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.OSSettings;
import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.guiComponents.GUIComponent;

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
public class FileObject extends GUIComponent {

	private static final long serialVersionUID = -1947171860799043091L;

	private String title;

	private Image icon;

	public FileType type;

	private Class<?> linkedClass;

	public FileObject(float x, float y, FileType ft, String title) {
		super(x, y, OSSettings.FILE_DISPLAY_SIZE, OSSettings.FILE_DISPLAY_SIZE, System.currentTimeMillis() + "_" + title);
		this.title = title;
		this.type = ft;
		if ( type.equals(FileType.Program) ) {
			icon = ResourceLoader.loadImage("!!.assets.programs." + title.toLowerCase() + ".icon");
		}
		if ( icon.equals(ResourceLoader.getNullImage()) ) {
			icon = ResourceLoader.loadImage("desktop.defaultProgram");
		}
		if ( icon.equals(ResourceLoader.getNullImage()) ) {
			icon = null;
		}
		this.setTransparent(false);
	}

	@Override
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// super.draw(gc, sbg, g);
		if ( OSSettings.FILE_FONT != null ) {
			g.setFont(OSSettings.FILE_FONT);
		}
		if ( icon != null ) {
			g.drawImage(icon.getScaledCopy((int) width, (int) height), x, y);
		} else {
			g.setColor(color);
			g.fillRoundRect(x, y, width, height, 3);
		}

		String tempText = getTitle();
		g.setColor(Color.black);
		g.drawString(tempText, x + (width / 2) - OSSettings.FILE_FONT.getWidth(tempText) / 2, y + height);
		g.setFont(OSSettings.NORMAL_FONT);

		if ( this.isSelected() ) {
			if ( selectedShape == null ) {
				g.drawRoundRect(x, y, width, height, 3);
			} else {
				g.draw(selectedShape);
			}
		}
	}

	public String getTitle() {
		return this.title;
	}

	public FileObject setLinkedClass(Class<?> c) {
		this.linkedClass = c;
		return this;
	}

	public Class<?> getLinkedClass() {
		return linkedClass;
	}

}
