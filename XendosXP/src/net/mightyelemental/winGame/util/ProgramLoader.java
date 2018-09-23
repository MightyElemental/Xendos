package net.mightyelemental.winGame.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

import net.mightyelemental.winGame.OSSettings;

public class ProgramLoader {

	public static synchronized void loadJar(String path) {
		try {
			JarFileLoader loader = new JarFileLoader(new URL[] {});
			loader.addFile(path);
			System.out.println("Found Jar: " + Arrays.asList(loader.getURLs()));
			// System.out.println(new
			// File(loader.getURLs()[0].toString()).canRead()+"|"+loader.getURLs()[0].toString());
			Class<?> c = Class.forName("Main", true, loader);
			Method method = c.getMethod("init", int.class);
			method.setAccessible(true); /* promote the method to public access */
			// System.out.println(Arrays.asList(method.getParameters()));
			method.invoke(c.newInstance(), OSSettings.VERSION);
			System.out.println("Successfully added [" + path + "]");
		} catch (IOException | IllegalArgumentException | NoSuchMethodException | SecurityException | ClassNotFoundException
				| IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}

}
