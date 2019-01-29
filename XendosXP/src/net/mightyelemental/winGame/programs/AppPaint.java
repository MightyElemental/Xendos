package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import net.mightyelemental.winGame.XendosMain;
import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUIPanel;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

public class AppPaint extends AppWindow {

	private static final long serialVersionUID = 2908623746541495832L;

	private Color[] colorList = { Color.black, Color.blue, Color.gray, Color.green, Color.magenta, Color.orange,
			Color.pink, Color.red, Color.yellow, Color.white };

	private Image drawArea;
	private Graphics drawGraphics;

	private int colorPointer = 0;

	private GUIButton col;

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

		try {
			drawArea = new Image((int) getWidth(), (int) getHeight() - 40);
			drawGraphics = drawArea.getGraphics();
			drawGraphics.setColor(Color.lightGray);
			drawGraphics.fillRect(0, 0, drawArea.getWidth(), drawArea.getHeight());
			drawGraphics.flush();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		this.addGUIObject(p, -1, -1);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		g.drawImage(drawArea, 0, 40);
	}

	@Override
	public void updateContent(int delta) {

	}

	@Override
	public void onComponentPressed(int button, GUIComponent c) {
		if (c.getUID().equals("#CLEAR")) {
			try {
				drawGraphics = drawArea.getGraphics();
				drawGraphics.setColor(Color.lightGray);
				drawGraphics.fillRect(0, 0, drawArea.getWidth(), drawArea.getHeight());
				drawGraphics.flush();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		if (c.getUID().equals("#COL")) {
			colorPointer++;
			col.setColor(colorList[colorPointer % colorList.length]);
		}
		if (c.getUID().equals("#SET")) {
			try {
				XendosMain.desktopState.setBackground(drawArea);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		x = (int) (x - this.getX() - 3);
		y = (int) (y - this.getY() - 70);
		try {
			drawGraphics = drawArea.getGraphics();
			drawGraphics.setColor(col.getColor());
			drawGraphics.fillRect(x, y, 5, 5);
			drawGraphics.flush();
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
		try {
			drawGraphics = drawArea.getGraphics();
			drawGraphics.setColor(col.getColor());
			drawGraphics.setLineWidth(5);
			drawGraphics.drawLine(oldX, oldY, newX, newY);
			drawGraphics.flush();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
