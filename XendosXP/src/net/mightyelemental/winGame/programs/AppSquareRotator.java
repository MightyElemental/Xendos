package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import net.mightyelemental.winGame.OSSettings;
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
public class AppSquareRotator extends AppWindow {

	private static final long serialVersionUID = -5362114223313401429L;
	private Rectangle rect = new Rectangle(50, 50, 50, 50);
	private Shape transformedRect = rect;
	private double angle = 0;

	private float xLoc = 50, yLoc = 50;
	private double yVel = 0.0, xVel = 0.0;

	public AppSquareRotator(float x, float y, float width, float height) {
		super(x, y, width, height, "Cube Fall");
		this.setSleepTime(2);
	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {
		// g.setAntiAlias(true);
		this.clearScreen();
		g.setFont(OSSettings.NORMAL_FONT);
		g.drawString("This should be in comic sans....", 20, 20);
		g.setColor(Color.white);
		g.fill(transformedRect);
		g.drawLine(0, transformedRect.getMaxY(), width, transformedRect.getMaxY());
		g.drawLine(0, transformedRect.getCenterY(), width, transformedRect.getCenterY());
		// g.setColor(Color.black);
		// g.drawLine(0, height - 1, width, height - 1);
		if (xLoc > width) {
			g.setColor(Color.red);
			g.fillRect(0, 0, 10, 10);
		}
		if (xLoc < 0) {
			g.setColor(Color.green);
			g.fillRect(0, 0, 10, 10);
		}
	}

	private float gravity = 0.05f;
	private boolean onGround;

	@Override
	public void updateContent(int delta) {
		if (Math.abs(delta - getSleepTime()) > 10)
			return;
		if (on[0] || on[2]) {
			angle -= delta / 5.0;
			xLoc -= delta / 5.0;
		} else if (on[1] || on[3]) {
			angle += delta / 5.0;
			xLoc += delta / 5.0;
		} else if (onGround && angle % 90 != 0) {
			double tempAng = Math.abs(angle) % 90;
			// System.out.println(angle + "|" + tempAng);
			if (tempAng > 45) {
				angle += ((tempAng + 1.0) * (tempAng + 5.0)) / 3000.0 * (gravity * 20) * (delta / 8f);
				// angle += Math.cos(Math.toRadians(angle % 90)) * (gravity * 25 * Math.PI) *
				// (delta / 10f);
				xLoc += delta / 8f;
			} else {
				angle -= ((tempAng - 91.0) * (tempAng - 95.0)) / 3000.0 * (gravity * 20) * (delta / 8f);
				xLoc -= delta / 8f;
				// angle -= Math.cos(Math.toRadians(angle % 90)) * (gravity * 25 * Math.PI) *
				// (delta / 10f);
			}
			if (tempAng <= 2f || tempAng >= 88f) {
				angle = (float) Math.floor(angle / 90f) * 90f;
			}
		}
		if (angle < 0)
			angle += 360;
		angle = angle % 360;
		updateSquare();
		if (xVel > 2f)
			xVel = 2f;
		if (xVel < -2f)
			xVel = -2f;
		this.setSleepTime(10);
		// xVel *= 0.90f/Math.sqrt(delta);
		float difference = getFrameHeight() - transformedRect.getMaxY();
		onGround = difference <= 0.0f;
		if (transformedRect.getMaxY() < getFrameHeight()) {
			yVel += gravity * delta / 10f;
		} else if (transformedRect.getMaxY() > getFrameHeight()) {
			yLoc += difference;
			// System.out.println(difference + "|" + yVel);
			if (Math.abs(difference) > 0.1) {
				yVel = -gravity * (1 - difference) * (delta / 10f);
			} else {
				yVel = 0;
			}
		}
		if (Math.abs(yVel) < 0.001)
			yVel = 0;

		// for ( float i = 0; i < yVel; i += gravity ) {
		// if ( transformedRect.getMaxY() + i > getFrameHeight() ) {
		// yVel = i;
		// break;
		// }
		// }
		yLoc += yVel;
		updateSquare();
	}

	private void updateSquare() {
		// if ( on[0] || on[1] || on[2] || on[3] ) {
		transformedRect = rect.transform(Transform.createRotateTransform((float) Math.toRadians(angle)));
		// }
		transformedRect.setCenterX(xLoc);
		transformedRect.setCenterY(yLoc);
	}

	private boolean[] on = new boolean[4];

	@Override
	public void onKeyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_Q:
			on[0] = true;
			break;
		case Input.KEY_E:
			on[1] = true;
			break;
		case Input.KEY_A:
			on[2] = true;
			break;
		case Input.KEY_D:
			on[3] = true;
			break;
		}
		if (key == Input.KEY_SPACE) {
			if (onGround) {
				yLoc -= 1;
				yVel = -2;
				System.out.println("yes");
			}
			System.out.println("asd");
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_Q:
			on[0] = false;
			break;
		case Input.KEY_E:
			on[1] = false;
			break;
		case Input.KEY_A:
			on[2] = false;
			break;
		case Input.KEY_D:
			on[3] = false;
			break;
		}

	}

}
