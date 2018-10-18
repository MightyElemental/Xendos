package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.guiComponents.GUIButton;
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
public abstract class AppWindow extends RoundedRectangle implements Runnable {

	// HIDE FIELDS
	private float height, width;

	private Thread programThread = new Thread(this);

	private TaskbarApp linkedTaskbarApp;

	public Image	windowButtons;
	public Image	content;
	public Graphics	contentGraphics;

	private String			displayTitle;
	private final String	baseTitle;

	protected long	lastDrawTime	= System.nanoTime(), lastUpdateTime;
	private boolean	showFPS, canDrag;
	private int		tickCount;

	private int sleepTime = 9;

	public boolean toMinimise, isMinimised, fullscreen, toClose, isNotResponding;

	private GUIButton[]			menuButtons	= new GUIButton[3];
	public List<GUIComponent>	guiObjects	= new ArrayList<GUIComponent>();

	private float minimizeScale = 0;

	protected AppWindow(float x, float y, float width, float height, String title) {
		super(x, y, width, height, 3);
		this.height = height;
		this.width = width;
		this.displayTitle = title;
		this.baseTitle = title;
		try {
			content = new Image((int) (width - 3), (int) height - 28);
			contentGraphics = content.getGraphics();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		windowButtons = ResourceLoader.loadImage("desktop.windowButtons");// .getScaledCopy(21f / 15f)
		menuButtons[0] = (new GUIButton(x + width - 85, y + 2, 21, 21, "#minimise"));
		menuButtons[1] = (new GUIButton(x + width - 60, y + 2, 21, 21, "#maximise"));
		menuButtons[2] = (new GUIButton(x + width - 35, y + 2, 21, 21, "#exit"));
		programThread.start();
	}

	private static final long serialVersionUID = 1L;

	private String getFPSText() {
		long ms = (System.nanoTime() - lastDrawTime);
		return " (" + (Math.round(10000000000f / ms) / 10f) + "fps | " + (ms / 1000000) + "ms)";
	}

	public void clearScreen() {
		contentGraphics.clear();
	}

	public /** final */
	void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if ( toClose && contentGraphics != null ) {
			contentGraphics.flush();
			contentGraphics.destroy();
			contentGraphics = null;
		}
		if ( toMinimise || isMinimised ) { // toMinimise || (isMinimised && !toMinimise)
			animateMinimize(gc, sbg, g);
			return;
		}
		g.setColor(Color.lightGray);
		g.fill(this);
		g.setColor(Color.black);
		g.draw(this);
		g.setColor(new Color(30, 79, 178));
		g.fillRoundRect(x, y, super.getWidth() - 2, 25, 3);

		// g.setColor(Color.black);//draw border
		// g.drawRoundRect(x - 1, y, super.getWidth() - 0.5f, 20, 3);

		g.setColor(new Color(30, 79, 178));
		g.fillRect(x, y + 10, super.getWidth() - 2, 15);
		windowButtons.draw(x + super.getWidth() - 85, y + 2);
		g.setColor(Color.white);
		g.drawString(displayTitle, x + 15, y + 22 / 2f - g.getFont().getHeight(displayTitle) / 2f);

		// for ( int i = 0; i < menuButtons.size(); i++ ) {
		// g.draw(menuButtons.get(i));
		// }
		if ( !isNotResponding && contentGraphics != null ) {
			drawContent(contentGraphics, content.getWidth(), content.getHeight());
		}
		drawGUIObjects(gc, sbg, contentGraphics);
		g.drawImage(content, (int) getX() + 1, (int) getY() + 26);

		lastDrawTime = System.nanoTime();
	}

	private void drawGUIObjects(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for ( GUIComponent gui : guiObjects ) {
			gui.draw(gc, sbg, g);
		}
	}

	protected abstract void drawContent(Graphics g, int width, int height);

	private void animateMinimize(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// if ( (!isMinimised && toMinimise) || (isMinimised && !toMinimise) ) {
		g.setColor(Color.gray);
		float x = this.getX() * (1 - minimizeScale) + linkedTaskbarApp.getX() * minimizeScale;
		float y = this.getY() + Math.abs((720 - this.getY()) * minimizeScale * minimizeScale);
		float width = this.getWidth() * (1 - minimizeScale) + linkedTaskbarApp.getWidth() * minimizeScale;
		float height = this.getHeight() * (1 - minimizeScale) + linkedTaskbarApp.getHeight() * minimizeScale;
		int rad = (int) (5 + 10 * (1 - minimizeScale));
		g.fillRoundRect(x, y, width, height, rad > 0 ? rad : 1);
		// }
	}

	public void run() {
		while ((!toClose || !isMinimised)) {
			int time = (int) (System.currentTimeMillis() - lastUpdateTime);
			// time = time <= 50 ? time : 1;
			time = time == 0 ? 1 : time;
			update(time);
			lastUpdateTime = System.currentTimeMillis();
			try {
				int sleep = sleepTime - Math.abs(time - sleepTime);
				sleep = sleep < 0 ? sleepTime : sleep;
				if ( isMinimised && toMinimise ) {
					sleep += 400;
				}
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public final void updateMinimize(int delta) {
		if ( toMinimise ) {
			if ( Math.round(minimizeScale * 100) / 100f < 1 ) {
				minimizeScale += 1f / (500f / delta);
			} else {
				isMinimised = true;
			}
		} else if ( isMinimised ) {
			if ( Math.round(minimizeScale * 100) / 100f > 0 ) {
				System.out.println(delta);
				minimizeScale -= 1f / (500f / delta);
			} else {
				isMinimised = false;
			}
		}
	}

	private final void update(int delta) {
		updateTitle();
		if ( !isNotResponding ) updateContent(delta);
		tickCount++;
	}

	private final void updateTitle() {
		if ( tickCount % 110 == 0 ) {
			displayTitle = baseTitle;
			if ( showFPS() ) {
				displayTitle += getFPSText();
			}
			if ( isNotResponding ) {
				displayTitle += " (Not Responding)";
			}
		}
	}

	public abstract void updateContent(int delta);

	public void mouseDragged(int x, int y) {
		this.changeXBy(x);
		this.changeYBy(y);
	}

	public void onMouseReleased(int button) {
		canDrag = false;
	}

	public final boolean onMousePressed(int button, int x, int y) {
		boolean flag = false;
		if ( isMinimised ) return false;
		for ( int i = 0; i < menuButtons.length; i++ ) {
			if ( menuButtons[i].contains(x, y) ) {
				switch (menuButtons[i].getUID()) {
				case "#EXIT":
					if ( !toMinimise ) {
						closeWindow();
					}
					break;
				case "#MINIMISE":
					if ( !toMinimise ) {
						toMinimise = true;
					}
					System.out.println(getLinkedTaskbarApp().getUID());
					break;
				case "#MAXIMISE":
					fullscreen = true;
					break;
				}
				flag = true;
			}
		}
		if ( !flag && y < getY() + 27 ) {
			canDrag = true;
		}

		return flag;

	}

	public final void changeXBy(float x) {
		super.setX(super.getX() + x);
		for ( GUIButton c : menuButtons ) {
			c.setX(c.getX() + x);
		}
	}

	public final void changeYBy(float y) {
		super.setY(super.getY() + y);
		for ( GUIButton c : menuButtons ) {
			c.setY(c.getY() + y);
		}
	}

	public TaskbarApp getLinkedTaskbarApp() {
		return linkedTaskbarApp;
	}

	public void setLinkedTaskbarApp(TaskbarApp linkedTaskbarApp) {
		this.linkedTaskbarApp = linkedTaskbarApp;
	}

	public String getTitle() {
		return this.displayTitle;
	}

	public boolean isDraggable() {
		return canDrag;
	}

	public final void keyPressed(int key, char c) {
		for ( GUIComponent g : guiObjects ) {
			if ( g.isSelected() ) {
				g.onKeyPressed(key, c);
			}
		}
		if ( key == Input.KEY_ESCAPE ) {
			closeWindow();
		}
		onKeyPressed(key, c);
	}

	public void onKeyPressed(int key, char c) {
	}

	public void keyReleased(int key, char c) {
	}

	public boolean showFPS() {
		return showFPS;
	}

	public void setShowFPS(boolean showFPS) {
		this.showFPS = showFPS;
	}

	public boolean isNotResponding() {
		return isNotResponding;
	}

	public void setNotResponding(boolean isNotResponding) {
		this.isNotResponding = isNotResponding;
	}

	public void addGUIObject(GUIComponent g) {
		g.setLinkedWindow(this);
		guiObjects.add(g);
	}

	public void addGUIObject(GUIComponent g, float x, float y) {
		g.setX(x);
		g.setY(y);
		g.setLinkedWindow(this);
		guiObjects.add(g);
	}

	public void onComponentPressed(int button, GUIComponent c) {
	}

	public float getFrameHeight() {
		return this.content.getHeight();
	}

	public float getFrameWidth() {
		return this.content.getWidth();
	}

	public float getWindowHeight() {
		return height;
	}

	public float getWindowWidth() {
		return width;
	}

	public void setSleepTime(int mil) {
		this.sleepTime = mil;
	}

	public float getSleepTime() {
		return this.sleepTime;
	}

	public void closeWindow() {
		toMinimise = true;
		toClose = true;
	}

}
