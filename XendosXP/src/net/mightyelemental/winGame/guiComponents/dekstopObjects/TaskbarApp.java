package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.OSSettings;
import net.mightyelemental.winGame.guiComponents.GUIButton;

public class TaskbarApp extends GUIButton {

	private static final long	serialVersionUID	= 8086670225644581843L;
	public static final String	TASKBARAPP			= "_taskbarapp";

	// private int index = -1;

	private final float startX;

	public AppWindow linkedWindow;

	public TaskbarApp(int x, AppWindow linkedWindow, int index) {
		super(x, 720 - 43, 86, 43, linkedWindow.getTitle() + TASKBARAPP);
		startX = x;
		this.linkedWindow = linkedWindow;
		this.linkedWindow.setLinkedTaskbarApp(this);
		setIndex(index);
	}

	public void setIndex(int index) {
		// this.index = index;
		float xOffset = index * (this.getWidth() + 2);
		this.setX(startX + xOffset);
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setColor(new Color(30, 79, 178));
		g.fillRoundRect(x, y, width, height, 5);
		g.setColor(new Color(30, 79, 178).darker());
		g.drawRoundRect(x, y, width, height, 5);
		g.setColor(Color.white);
		drawText(gc, sbg, g);
	}

	public void drawText(GameContainer gc, StateBasedGame sbg, Graphics g) {
		StringBuilder temp = new StringBuilder(linkedWindow.getTitle());
		boolean flag = false;
		while (OSSettings.NORMAL_FONT.getWidth(temp.toString()) > this.getWidth()) {
			if ( !flag ) {
				temp.append("...");
				flag = true;
			}
			temp.deleteCharAt(temp.length() - 4);
		}
		g.drawString(temp.toString(), x + 2, y - g.getFont().getHeight(temp.toString()) / 2f + height / 2f);
	}

	@Override
	public void onMousePressed(int button) {
		if ( button == 0 ) {
			this.linkedWindow.toMinimise = !this.linkedWindow.toMinimise;
		} else if ( button == 1 ) {

		}
	}

}
