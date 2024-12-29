package net.mightyelemental.winGame.programs;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;
import net.mightyelemental.winGame.states.StateDesktop;

/**
 * XendosXP - A custom operating system that runs in a window Copyright (C) 2018 James Burnell
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
public class AppTest extends AppWindow {

    private Random rand = new Random(System.currentTimeMillis());

    private String text = "";

    /** The time when a splatter was last painted */
    private long lastPaint;
    /** The time when the canvas was last reset */
    private long lastReset;
    /** How many milliseconds between attempting to splatter paint */
    private int  msPerSplatter = 50;
    /** The number of splatters since the last reset */
    private int  splatters     = 0;

    private boolean[][] bannedPixels = new boolean[201][151];

    public AppTest(StateDesktop context, float x, float y, float width, float height) {
        super(context, x, y, width, height, "Random Splatter Painter");
        this.setShowFPS(true);
        this.addGUIObject(new GUIButton(150, 30, "#reset", this).setText("Reset Screen"), 10, 100);
        this.lastReset = System.currentTimeMillis();
    }

    private Color getRandomColor() {
        return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1f);
    }

    public void updateContent(float delta) {
        // System.out.println(delta);
//        float elapsed = (System.currentTimeMillis() - lastReset) / 1000f;
//        elapsed = elapsed <= 0 ? 1 : elapsed; // Ensure positive value
//        float splatsPerSecond = splatters/elapsed;
//        text = String.format("Splatters=%d, or %.1f sp/s", splatters, splatsPerSecond);
    }

    @Override
    public void drawContent(Graphics g, int width, int height) {
        if (System.currentTimeMillis() - lastPaint >= msPerSplatter) {
            Color c = getRandomColor();
            boolean banFlag = true;
            g.setColor(c);
            int i = rand.nextInt(200);
            int y = rand.nextInt(150);
            int count = 0;
            for (int x = 0; x < 50; x++) {
                count++;
                if (count > 50000) {
                    reset();
                    // this.setNotResponding(true);
                    break;
                }
                i += rand.nextInt(3) - 1;
                i = Math.abs(i);
                y += rand.nextInt(3) - 1;
                y = Math.abs(y);
                if (y > 150) y = 150;
                if (i > 200) i = 200;
                if (bannedPixels[i][y]) {
                    x--;
                    continue;
                }
                g.fillRect(i * 4, y * 4, 4, 4);
                bannedPixels[i][y] = banFlag;
            }

            lastPaint = System.currentTimeMillis();
            splatters++;
        }

        // Draw text box
        g.setColor(Color.white);
        g.fillRect(0, 0, 800, 20);
        g.setColor(Color.black);
        g.drawString(text, 2, 2);
    }

    @Override
    public void onKeyPressed(int key, char c) {
        text += c;
    }

    public void reset() {
        this.contentGraphics.clear();
        this.bannedPixels = new boolean[201][151];
        this.splatters = 0;
        this.lastReset = System.currentTimeMillis();
    }

    @Override
    public void onComponentPressed(int button, GUIComponent c) {
        if (c.getUID().equals("#RESET")) {
            reset();
        }
    }

}
