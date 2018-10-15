package net.mightyelemental.winGame.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.newdawn.slick.util.Log;

import net.mightyelemental.winGame.OSSettings;

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
public class ProgramLoader {

	public static synchronized void loadJar(String path) {
		String[] s = path.split("\\\\");
		try {
			JarFileLoader loader = new JarFileLoader(new URL[] {});
			loader.addFile(path);
			// System.out.println("Found Jar: " + Arrays.asList(loader.getURLs()));
			// System.out.println(new
			// File(loader.getURLs()[0].toString()).canRead()+"|"+loader.getURLs()[0].toString());
			Class<?> c = Class.forName("Main", true, loader);
			Method method = c.getMethod("init", int.class);
			method.setAccessible(true); /* promote the method to public access */
			// System.out.println(Arrays.asList(method.getParameters()));
			method.invoke(c.newInstance(), OSSettings.VERSION);
			// System.out.println("Successfully added [" + path + "]");
			Log.info("Successfully added " + s[s.length - 1]);
		} catch (IOException | IllegalArgumentException | NoSuchMethodException | SecurityException | ClassNotFoundException
				| IllegalAccessException | InvocationTargetException | InstantiationException e) {
			Log.error("Failed to add " + s[s.length - 1]);
		}
	}

	/**
	 * @author Emiflake
	 * @since 23/09/2018
	 * @param strPath
	 *            - the path to a library to be loaded
	 * 
	 */
	public static synchronized void loadLib(String strPath) {
		File path = new File(strPath);
		loadLib(path);
	}

	/**
	 * @author Emiflake
	 * @since 23/09/2018
	 * @param strPath
	 *            - the path to a library to be loaded
	 * 
	 */
	public static synchronized void loadLib(File path) {
		try {
			URL url = path.toURI().toURL();
			URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(classLoader, url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
