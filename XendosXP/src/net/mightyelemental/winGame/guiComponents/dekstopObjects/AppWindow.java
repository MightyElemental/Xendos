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

	/** Size of the window */
	private float height, width;

	/** Thread that runs the update method within the program */
	private Thread programThread = new Thread(this);

	/** The taskbar entry that is linked to the program */
	private TaskbarApp linkedTaskbarApp;

	public Image windowButtons;
	public Image content;
	public Graphics contentGraphics;

	/** Title of the program that is shown to the user */
	private String displayTitle;
	/** Title of the program */
	private final String baseTitle;

	/** Used to calculate fps and when to update */
	protected long lastDrawTime = System.nanoTime(), lastUpdateTime;
	private boolean showFPS, canDrag;
	private int tickCount;

	/** amount of time the program should sleep for after each update */
	private int sleepTime = 9;

	protected int mousex, mousey;

	/** window states */
	public boolean toMinimise, isMinimised, fullscreen, toClose, isNotResponding;

	/** Minimize, Maixmize, Close */
	private GUIButton[] menuButtons = new GUIButton[3];
	/** List of components within the program */
	public List<GUIComponent> guiObjects = new ArrayList<GUIComponent>();

	/** the amount to minimize the program by. 0 is none. 1 is full. */
	private float minimizeScale = 0;

	/**
	 * Creates a new AppWindow instance
	 * 
	 * @param x
	 * coordinate of the window
	 * @param y
	 * coordinate of the window
	 * @param width
	 * of the program
	 * @param height
	 * of the program
	 * @param title
	 * of the program
	 */
	protected AppWindow(float x, float y, float width, float height, String title) {
		super(x, y, width, height, 3);
		this.height = height;
		this.width = width;
		this.displayTitle = title;
		this.baseTitle = title;
		try {
			content = new Image((int) (width - 4), (int) height - 28);
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

	/**
	 * calculate fps
	 * 
	 * @return fps of the program as string
	 */
	private String getFPSText() {
		long ms = (System.nanoTime() - lastDrawTime);
		return " (" + (Math.round(10000000000f / ms) / 10f) + "fps | " + (ms / 1000000) + "ms)";
	}

	/** Clears the graphical screen */
	public void clearScreen() {
		contentGraphics.clear();
	}

	/**
	 * render the program
	 **/
	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		if (toClose && contentGraphics != null) {
			contentGraphics.flush();
			contentGraphics.destroy();
			contentGraphics = null;
		}
		// Does the program need to be animated to minimize?
		if (toMinimise || isMinimised) { // toMinimise || (isMinimised && !toMinimise)
			animateMinimize(gc, sbg, g);
			return;
		}
		// Draw generic program window. Gray background with black border
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
		if (!isNotResponding && contentGraphics != null) {
			drawContent(contentGraphics, content.getWidth(), content.getHeight());
		}
		drawGUIObjects(gc, sbg, contentGraphics);
		g.drawImage(content, (int) getX() + 1, (int) getY() + 26);

		lastDrawTime = System.nanoTime();
	}

	private void drawGUIObjects(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for (GUIComponent gui : guiObjects) {
			gui.draw(gc, sbg, g);
		}
	}

	/**
	 * custom programs will use this method to render. This is so that the program
	 * window base is not changed
	 */
	protected abstract void drawContent(Graphics g, int width, int height);

	/**
	 * animates to minimization of the program<br>
	 * Uses minimizeScale as a ratio of the normal size and the minimized size.
	 */
	private void animateMinimize(GameContainer gc, StateBasedGame sbg, Graphics g) {
		// if ( (!isMinimised && toMinimise) || (isMinimised && !toMinimise) ) {
		g.setColor(Color.gray);
		float x = this.getX() * (1 - minimizeScale) + linkedTaskbarApp.getX() * minimizeScale;
		float y = this.getY() + Math.abs((gc.getHeight() - this.getY()) * minimizeScale * minimizeScale);
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
			try {// Attempts to keep the program running at the same speed regardless of lag
					// spikes
				int sleep = sleepTime - Math.abs(time - sleepTime);
				sleep = sleep < 0 ? sleepTime : sleep;
				if (isMinimised && toMinimise) {
					sleep += 400;
				}
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/** Used to update the minimizeScale value */
	public final void updateMinimize(int delta) {
		if (toMinimise) { // increase minimizeScale
			if (Math.round(minimizeScale * 100) / 100f < 1) {
				minimizeScale += 1f / (500f / delta);
			} else {
				isMinimised = true;
			}
		} else if (isMinimised) { // decrease minimizeScale
			if (Math.round(minimizeScale * 100) / 100f > 0) {
				//System.out.println(delta);
				minimizeScale -= 1f / (500f / delta);
			} else {
				isMinimised = false;
			}
		}
	}

	private final void update(int delta) {
		updateTitle();
		// If it is not responding then it will not attempt to update the program
		if (!isNotResponding)
			updateContent(delta);
		tickCount++;
	}

	/** adds fps or not responding message to the title */
	private final void updateTitle() {
		if (tickCount % 110 == 0) {
			displayTitle = baseTitle;
			if (showFPS()) {
				displayTitle += getFPSText();
			}
			if (isNotResponding) {
				displayTitle += " (Not Responding)";
			}
		}
	}

	/**
	 * Custom programs will use this method to update the program to avoid
	 * overwriting the default update method
	 */
	public abstract void updateContent(int delta);

	public void windowDragged(int x, int y) {
		this.changeXBy(x);
		this.changeYBy(y);
	}

	public void mouseDragged(int oldX, int oldY, int newX, int newY) {

	}

	public void onWindowReleased(int button) {
		canDrag = false;
	}

	/**
	 * When the mouse is pressed, the program will test which button has been
	 * pressed
	 */
	public final boolean onMousePressed(int button, int x, int y) {
		mousePressed(button, x, y);
		boolean flag = false;
		if (isMinimised)
			return false;
		for (int i = 0; i < menuButtons.length; i++) {
			if (menuButtons[i].contains(x, y)) {
				switch (menuButtons[i].getUID()) {
				case "#EXIT":
					if (!toMinimise) {
						closeWindow();
					}
					break;
				case "#MINIMISE":
					if (!toMinimise) {
						toMinimise = true;
					}
					//System.out.println(getLinkedTaskbarApp().getUID());
					break;
				case "#MAXIMISE":
					fullscreen = true;
					break;
				}
				flag = true;
			}
		}
		if (!flag && y < getY() + 27) {
			canDrag = true;
		}

		return flag;

	}

	public void mousePressed(int button, int x, int y) {

	}

	public final void changeXBy(float x) {
		super.setX(super.getX() + x);
		for (GUIButton c : menuButtons) {
			c.setX(c.getX() + x);
		}
	}

	public final void changeYBy(float y) {
		super.setY(super.getY() + y);
		for (GUIButton c : menuButtons) {
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
		for (GUIComponent g : guiObjects) {
			if (g.isSelected()) {
				g.onKeyPressed(key, c);
			}
		}
		if (key == Input.KEY_ESCAPE) {
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

	public void onMouseReleased(int button, int x, int y) {

	}

	public void setMousePos(int x, int y) {
		this.mousex = (int) (x - this.getX() - 3);
		this.mousey = (int) (y - this.getY() - 30);
	}

	public void mouseWheelMoved(int newValue) {
	}

}
