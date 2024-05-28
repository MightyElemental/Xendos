package net.mightyelemental.winGame.guiComponents;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.google.common.io.Files;

import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

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
public class GUIhtmlViewer extends GUIComponent {

	private static final long serialVersionUID = 3497817514161328855L;

	private Image		page;
	private boolean	loaded, loading;
	private boolean	couldNotResolve;

	private String	fileName;
	private Image	nullImg;

	private int tick;

	/** The thread executor to parallelize the web rendering */
	Executor executor;

	public GUIhtmlViewer( float width, float height, String uid, AppWindow aw ) {
		super(width, height, uid, aw);
		nullImg = ResourceLoader.loadImage("webcache.null");
		executor = Executors.newSingleThreadExecutor();
	}

	public void draw( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException {
		super.draw(gc, sbg, g);
		if (page != null) {
			// TODO: Add scroll feature by rendering section of screenshot
			g.drawImage(page.getScaledCopy((int) width, (int) height), x, y);
		} else if (loaded) {
			if (ResourceLoader.imageExists("webcache/" + fileName)) {
				page = ResourceLoader.loadImage("webcache/" + fileName);
				couldNotResolve = false;
			} else {
				loaded = false;
				couldNotResolve = true;
			}
		} else {
			g.drawImage(nullImg, (width - nullImg.getWidth()) / 2f, (height - nullImg.getHeight()) / 2f,
					new Color(0f, 0f, 0f, 0.5f));
		}
		g.setColor(Color.black);
		if (couldNotResolve) {
			g.drawString("Could not resolve webpage.\nEnsure you have typed the URL correctly.", 10, height - 15);
		}
		if (!isLoaded() && !loading && !couldNotResolve) {
			g.drawString("Provide a URL to get started", x + 5, y + 5);
		}
		if (loading) {
			animateLoading(g);
			tick += 5;
		} else {
			tick = 0;
		}
	}

	private void animateLoading( Graphics g ) {
		int oX = 20;
		int oY = 20;
		int scale = 7;
		g.fillOval((float) (x + oX + scale * Math.sin(Math.toRadians(tick))),
				(float) (y + oY + scale * Math.cos(Math.toRadians(tick))), 5, 5);
		g.fillOval((float) (x + oX + scale * Math.sin(Math.toRadians(tick + 120))),
				(float) (y + oY + scale * Math.cos(Math.toRadians(tick + 120))), 5, 5);
		g.fillOval((float) (x + oX + scale * Math.sin(Math.toRadians(tick + 240))),
				(float) (y + oY + scale * Math.cos(Math.toRadians(tick + 240))), 5, 5);
	}

	/**
	 * Load a URL and render it to an image to be displayed.
	 * 
	 * @param url the desired website URL
	 */
	public void displayWebsite( String url ) {
		fileName = url.replaceAll("[^A-Za-z0-9]", "");
		loaded = false;
		couldNotResolve = false;
		page = null;
		if (ResourceLoader.imageExists("webcache/" + fileName)) {
			loaded = true;
			page = ResourceLoader.loadImage("webcache/" + fileName);
		} else {

			executor.execute(() -> {

				try {
					WebDriver webDriver = getNewDriver();

					loading = true;
					Log.info("Requesting URL [" + url + "]");

					screenshotWebsite(webDriver, url, fileName);
					webDriver.quit();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					loaded = true;
					couldNotResolve = !ResourceLoader.imageExists("webcache/" + fileName);
					loading = false;
				}

			});

		}
	}

	/**
	 * Creates a new headless browser driver
	 * 
	 * @return A new headless driver
	 */
	public WebDriver getNewDriver() {
		ChromeOptions co = new ChromeOptions();
		co.addArguments("--headless");
		co.addArguments(String.format("--window-size=%d,%d", (int) (this.width * 1.5f), (int) (this.height * 1.5f)));
		WebDriver webDriver = new ChromeDriver(co);
		return webDriver;
	}

	/**
	 * Takes a screenshot of a website at a given URL using a given web driver.
	 * 
	 * @param webDriver the web driver used to take the screenshot
	 * @param url the URL to screenshot
	 * @param fileName the name of the image file to save as
	 */
	public void screenshotWebsite( WebDriver webDriver, String url, String fileName ) {
		webDriver.get(url);
		File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		webDriver.close();

		try {
			Files.move(screenshot, new File("./assets/textures/webcache/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	public boolean couldNotResolve() {
		return couldNotResolve;
	}

}
