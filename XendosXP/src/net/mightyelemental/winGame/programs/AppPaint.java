package net.mightyelemental.winGame.programs;

import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import net.mightyelemental.winGame.XendosMain;
import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUIPanel;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppPaint extends AppWindow {

	private static final long serialVersionUID = 2908623746541495832L;

	private Color[] colorList = { Color.black, Color.blue, Color.gray, Color.green, Color.magenta, Color.orange, Color.lightGray,
			Color.red, Color.yellow, Color.white };

	private Set<Integer> keyToggles = new HashSet<Integer>();

	private Image		undo;
	private Image		drawArea;
	private Graphics	drawGraphics;

	private int size = 5;

	private int colorPointer = 0;

	private GUIButton col, erase;

	public AppPaint(float x, float y, float width, float height) {
		super(x, y, width, height, "XenPaint");
		GUIPanel p = new GUIPanel(getWidth(), 40, this);
		p.setGridLayout(5, 1);
		p.setColor(Color.gray);

		GUIButton clear = new GUIButton(0, 0, "#CLEAR", this).setText("Clear");
		p.addGUIObject(clear, 0, 0);

		col = new GUIButton(0, 0, "#COL", this).setText("Set Color");
		col.setAllowInvertedColor(false);
		p.addGUIObject(col, 0, 0);

		GUIButton backSet = new GUIButton(0, 0, "#SET", this).setText("Set Wallpaper");
		p.addGUIObject(backSet, 0, 0);

		erase = new GUIButton(0, 0, "#DEL", this).setText("Erase");
		p.addGUIObject(erase, 0, 0);

		try {
			drawArea = new Image((int) getWidth(), (int) getHeight() - 40);
			undo = new Image(drawArea.getWidth(), drawArea.getHeight());
			clearDrawing();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		this.addGUIObject(p, -1, -1);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		g.drawImage(drawArea, 0, 40);
		g.setColor(Color.black);
		g.drawOval(mousex - size / 2f, mousey - size / 2f, size, size);
	}

	@Override
	public void updateContent(int delta) {
		if ( keyToggles.contains(Input.KEY_ADD) || keyToggles.contains(Input.KEY_EQUALS) ) {
			size++;
			if ( size < 2 ) size = 2;
			if ( size > 200 ) size = 200;
		}
		if ( keyToggles.contains(Input.KEY_MINUS) || keyToggles.contains(Input.KEY_SUBTRACT) ) {
			size--;
			if ( size < 2 ) size = 2;
			if ( size > 200 ) size = 200;
		}
	}

	@Override
	public void onComponentPressed(int button, GUIComponent c) {
		if ( c.getUID().equals("#CLEAR") ) {
			clearDrawing();
		}
		if ( c.getUID().equals("#COL") ) {
			colorPointer++;
			col.setColor(colorList[colorPointer % colorList.length]);
		}
		if ( c.getUID().equals("#SET") ) {
			try {
				XendosMain.desktopState.setBackground(drawArea);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		if ( c.getUID().equals("#DEL") ) {
			colorPointer = 6;
			col.setColor(colorList[colorPointer % colorList.length]);
		}
		if ( col.getColor().equals(Color.lightGray) ) {
			erase.setColor(Color.lightGray);
		} else {
			erase.setColor(Color.white);
		}
	}

	private void clearDrawing() {
		try {
			drawGraphics = drawArea.getGraphics();
			drawGraphics.setColor(Color.lightGray);
			drawGraphics.fillRect(0, 0, drawArea.getWidth(), drawArea.getHeight());
			drawGraphics.flush();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		x = (int) (x - this.getX() - 3);
		y = (int) (y - this.getY() - 70);
		if ( y < 0 ) return;
		drawRoundedLine(x, y, x, y);
		try {
			Graphics g = undo.getGraphics();
			g.drawImage(drawArea, 0, 0);
			g.flush();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		oldX = (int) (oldX - this.getX() - 3);
		oldY = (int) (oldY - this.getY() - 70);
		newX = (int) (newX - this.getX() - 3);
		newY = (int) (newY - this.getY() - 70);
		if ( newY < 0 ) return;
		drawRoundedLine(oldX, oldY, newX, newY);
	}

	private void drawRoundedLine(int oldX, int oldY, int newX, int newY) {
		try {
			drawGraphics = drawArea.getGraphics();
			drawGraphics.setColor(col.getColor());

			int maxX = Math.max(oldX, newX);
			int minX = Math.min(oldX, newX);
			int maxY = Math.max(oldY, newY);
			int minY = Math.min(oldY, newY);

			float grad = 1;
			if ( (minX - maxX) == 0 ) {
				for ( int y = minY; y <= maxY; y++ ) {
					drawGraphics.fillOval(minX - size / 2f, y - size / 2f, size, size);
				}
			} else {
				grad = (float) (oldY - newY) / (float) (oldX - newX);
				float c = oldY - (oldX * grad);
				float step = 1f / Math.abs(grad);
				if ( step > 0.5 ) step = 0.5f;
				// System.out.println(step);
				for ( float x = minX; x <= maxX; x += step ) {
					float y = (x * grad + c);
					drawGraphics.fillOval(x - size / 2f, y - size / 2f, size, size);
				}
			}

			drawGraphics.flush();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onKeyPressed(int key, char c) {
		keyToggles.add(key);
		if ( keyToggles.contains(Input.KEY_LCONTROL) && keyToggles.contains(Input.KEY_Z) ) {
			clearDrawing();
			try {
				drawGraphics = drawArea.getGraphics();
				drawGraphics.drawImage(undo, 0, 0);
				drawGraphics.flush();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		keyToggles.remove(key);
	}

	@Override
	public void mouseWheelMoved(int newValue) {
		size += Math.signum(newValue) * Math.pow(size, 1.0 / 3.0);
		if ( size < 2 ) size = 2;
		if ( size > 200 ) size = 200;
		super.mouseWheelMoved(newValue);
	}

}
