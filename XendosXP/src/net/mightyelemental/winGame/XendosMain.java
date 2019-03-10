package net.mightyelemental.winGame;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;
import net.mightyelemental.winGame.programs.AppCalculator;
import net.mightyelemental.winGame.programs.AppConsole;
import net.mightyelemental.winGame.programs.AppPaint;
import net.mightyelemental.winGame.programs.AppSquareRotator;
import net.mightyelemental.winGame.programs.AppTest;
import net.mightyelemental.winGame.programs.AppWebBrowser;
import net.mightyelemental.winGame.states.StateDesktop;
import net.mightyelemental.winGame.states.StateLoading;
import net.mightyelemental.winGame.states.StateLogin;
import net.mightyelemental.winGame.util.ProgramLoader;

/**
 * XendosXP - A custom operating system that runs in a window Copyright (C) 2018
 * James Burnell
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
public class XendosMain extends StateBasedGame {

	public static Map<Class<? extends AppWindow>, String> programs = new HashMap<Class<? extends AppWindow>, String>();

	public static final int WIDTH = 1280;

	public static final int	STATE_LOADING	= 0;
	public static final int	STATE_LOGIN		= 1;
	public static final int	STATE_DESKTOP	= 2;

	private static boolean review = false;

	public StateLoading			loadState		= new StateLoading(STATE_LOADING);
	public StateLogin			loginState		= new StateLogin();
	public static StateDesktop	desktopState	= new StateDesktop();

	/**
	 * Set up the program</br>
	 * Loads external libraries, programs and initializes the render window.
	 */
	private XendosMain() {
		super("XendosXP");

		loadLibraries();
		loadPrograms();

		this.addState(loadState);
		this.addState(loginState);
		this.addState(desktopState);
		AppGameContainer appGc;
		try {
			appGc = new AppGameContainer(this);
			appGc.setDisplayMode(WIDTH, (int) (WIDTH / 16.0 * 9.0), false);
			// appGc.setTargetFrameRate(120);
			appGc.setVSync(true);
			appGc.setAlwaysRender(true);
			appGc.setFullscreen(false);
			appGc.setShowFPS(false);
			appGc.start();
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Registers interior and external programs. */
	private void loadPrograms() {
		registerProgram(AppWebBrowser.class, "Corner");
		registerProgram(AppTest.class, "Splatter");
		registerProgram(AppSquareRotator.class, "Cube Game");
		registerProgram(AppCalculator.class, "Calculator");
		registerProgram(AppPaint.class, "XenPaint");
		registerProgram(AppConsole.class, "Console");
		// registerProgram(AppHarmony.class, "Harmony");
		// registerProgram(AppSomething.class, "Component Test");

		File dir = new File("assets/programs");
		if ( dir.canRead() ) {
			// System.out.println(dir.getAbsolutePath().replaceFirst("[A-Z]{1}:", ""));
			File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));

			for ( File f : files ) {
				ProgramLoader.loadJar(f.getAbsolutePath().replaceFirst("[A-Z]{1}:", ""));
			}
		} else {
			createProgramsDirectory(dir);
		}
	}

	/**
	 * Loads a external libraries to be imported into Xendos
	 * 
	 * @author Emiflake
	 * @since 23/09/18
	 */
	private void loadLibraries() {
		File dir = new File("assets/libraries");
		if ( dir.canRead() ) {
			// System.out.println(dir.getAbsolutePath().replaceFirst("[A-Z]{1}:", ""));
			File[] libraryJars = dir.listFiles((d, name) -> name.endsWith(".jar"));
			for ( File f : libraryJars ) {
				ProgramLoader.loadLib(f);
			}
		} else {
			createProgramsDirectory(dir);
		}
	}

	/**
	 * Creates a new directory in the event one does not exist.<br>
	 * Tests if directory exists and attempts to create one in the even it does not
	 * exist. It will notify the user what is happening.
	 */
	private void createProgramsDirectory(File dir) {
		String path = dir.getAbsolutePath().replaceFirst("[A-Z]{1}:", "");
		Log.warn("Cannot read directory " + path);
		if ( !dir.exists() ) {
			Log.info("Creating new folder " + path);
			boolean success = dir.mkdir();
			if ( success ) {
				Log.info("Successfully created folder");
			} else {
				Log.warn("Could not create new folder!");
			}
		}
	}

	/** Used to set native paths so libraries can work properly */
	private static void resetLib() {
		String os = System.getProperty("os.name").toLowerCase();
		Log.info("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
		Log.info("Java Version: " + System.getProperty("java.version"));
		String path = "windows";
		if ( os.contains("mac") ) {
			path = "macosx";
		} else if ( os.contains("nix") || os.contains("nux") || os.contains("aix") ) {
			path = "linux";
		}
		String fullPath = new File("lib/natives/" + path).getAbsolutePath();
		System.setProperty("java.library.path", fullPath);
		System.setProperty("net.java.games.input.librarypath", fullPath);
		System.setProperty("org.lwjgl.librarypath", fullPath);
		System.setProperty("net.java.games.input.DirectAndRawInputEnvironmentPlugin", fullPath);
		// System.out.println(System.getProperty("org.lwjgl.librarypath"));
	}

	public static void main(String[] args) {
		displayCopyright();
		resetLib();
		// ProgramLoader.loadJar("/test.jar");
		new XendosMain();
	}

	private static void displayCopyright() {
		System.err.println("XendosXP  Copyright (C) 2018  James Burnell");
		System.err.println(
				"This program comes with ABSOLUTELY NO WARRANTY; for details run 'Copyright Notice' program within XendosXP.");
		System.err.println(
				"This is free software, and you are welcome to redistribute it under certain conditions; for details\nrun 'Copyright Notice' program within XendosXP.");
		System.err.println();

	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.enterState(STATE_DESKTOP);
	}

	/**
	 * Used to register a program in Xendos
	 * 
	 * @param c
	 *            - the class of the program which extends AppWindow
	 * @param name
	 *            - the name of the program to be displayed
	 */
	public static void registerProgram(Class<? extends AppWindow> c, String name) {
		programs.put(c, name);
	}

	@Override
	public boolean closeRequested() {
		if ( review && this.getCurrentStateID() == STATE_DESKTOP ) {
			if ( Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE) ) {
				try {
					Desktop.getDesktop().browse(new URI("https://goo.gl/forms/l29hoW5fPPg3GwpC2"));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
		return super.closeRequested();
	}

}
