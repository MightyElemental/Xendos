package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.ResourceLoader;

public class StartWindow extends AppWindow {

	private static final long serialVersionUID = 6373909456505514103L;

	public StartWindow() {
		super(0, 219, 430, 458, "Start");
	}

	private Image menu;

	public void init(GameContainer gc, StateBasedGame delta) throws SlickException {
		menu = ResourceLoader.loadImage("desktop.startMenu");
	}

	public void draw(GameContainer gc, StateBasedGame sbg, Graphics g) {
		menu.draw(x, y);
		// g.setColor(new Color(30, 79, 178));
		// g.fill(this);
		// g.setColor(Color.white);
		// g.fillRect(1, gc.getHeight() - 440, 420, 350);
		// g.setColor(Color.white.darker());
		// g.fillRect(211, gc.getHeight() - 440, 210, 350);
	}

	@Override
	public void drawContent(Graphics g, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateContent(int delta) {
		// TODO Auto-generated method stub

	}

}
