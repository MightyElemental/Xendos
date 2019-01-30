package net.mightyelemental.winGame;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.Log;

/**
 * @updated 04/10/2018
 * @author MightyElemental
 * @since 28/10/2014
 * @version Slick2D edition
 */
public class ResourceLoader {

	private static Map<String, Image>	imageLoads	= new HashMap<String, Image>();
	private static Map<String, Sound>	soundLoads	= new HashMap<String, Sound>();
	private static Map<String, Music>	musicLoads	= new HashMap<String, Music>();

	/** Prevents instantiation */
	private ResourceLoader() {
	}

	/**
	 * Loads an image from the 'assets/textures' package
	 * 
	 * @param imagePath
	 *            the path to the image beginning with 'assets/textures'. Remember
	 *            that you can replace slashes '/' with dots '.'
	 * @return Image the newly loaded image
	 */
	public static Image loadImage(String imagePath) {

		if ( !imageLoads.containsKey("null") ) {
			loadNullImage();
		}

		Image loadedImage = imageLoads.get("null");

		if ( imagePath.equals("null") ) { return loadedImage; }

		String location = formatPath(imagePath);
		if ( imageLoads.containsKey(location) ) {
			return imageLoads.get(location);
		} else {
			try {
				// loadedImage = new Image(location);
				File temp = new File(location);
				if ( temp.exists() ) {
					loadedImage = new Image(location);
					System.out.println("Added texture\t'" + location + "'");
				} else {
					Log.warn("Missing texture\t'" + location + "'");
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
			imageLoads.put(location, loadedImage);
		}

		return loadedImage;
	}

	private static String formatPath(String imagePath) {
		String location = imagePath.replaceAll("[.]", "/");
		location += ".png";
		if ( location.startsWith("!!") ) {
			location = location.replaceFirst("!!", ".");
		} else {
			location = "./assets/textures/" + location;
		}
		return location;
	}

	public static boolean imageExists(String imagePath) {
		String location = formatPath(imagePath);
		File temp = new File(location);
		return temp.exists();
	}

	public static void loadNullImage() {
		try {
			imageLoads.put("null", ResourceLoader.generateNullImage());
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public static Image getNullImage() {
		if ( !imageLoads.containsKey("null") ) {
			loadNullImage();
		}
		return imageLoads.get("null");
	}

	public static Image generateNullImage() throws SlickException {
		Image nul = new Image(2, 2);
		Graphics g = nul.getGraphics();
		g.setColor(Color.red);
		g.drawRect(0, 0, 0, 0);
		g.drawRect(1, 1, 0, 0);
		g.setColor(Color.black);
		g.drawRect(0, 1, 0, 0);
		g.drawRect(1, 0, 0, 0);
		g.destroy();
		return nul;
	}

	/**
	 * Loads a music file from the 'assets/sounds/music' package
	 * 
	 * @param musicPath
	 *            the path to the sound file beginning with 'assets/sounds/music'.
	 *            Remember that you can replace slashes '/' with dots '.'
	 * @return Music the newly loaded music file
	 */
	@Deprecated
	public Music loadMusic(String musicPath) {

		Music loadedMusic = null;

		String location = musicPath.replaceAll("[.]", "/");
		location += ".ogg";
		location = "./assets/sounds/music/" + location;
		if ( imageLoads.get(location) != null ) {
			return musicLoads.get(location);
		} else {
			try {

				File temp = new File(this.getClass().getClassLoader().getResource(location).toURI());
				if ( temp.exists() ) {
					loadedMusic = new Music(this.getClass().getClassLoader().getResourceAsStream(location), location);
					System.out.println("Added music\t'" + location + "'");
				} else {
					throw new Exception("Missing music\t'" + location + "'");
				}
			} catch (Exception e) {
				System.err.println("Missing music\t'" + location + "'");
			}
			musicLoads.put(location, loadedMusic);
		}

		return loadedMusic;
	}

	/**
	 * Loads a sound file from the 'assets/sounds' package
	 * 
	 * @param soundPath
	 *            the path to the sound file beginning with 'assets/sounds'.
	 *            Remember that you can replace slashes '/' with dots '.'
	 * @return Sound the newly loaded sound
	 */
	public static Sound loadSound(String soundPath) {

		Sound loadedSound = null;

		String location = soundPath.replaceAll("[.]", "/");
		location += ".ogg";
		location = "./assets/sounds/" + location;
		if ( imageLoads.containsKey(location) ) {
			return soundLoads.get(location);
		} else {
			try {
				File temp = new File(location);
				if ( temp.exists() ) {
					loadedSound = new Sound(location);
					System.out.println("Added sound\t'" + location + "'");
				} else {
					throw new Exception("Missing sound\t'" + location + "' ");
				}
			} catch (Exception e) {
				System.out.println("Missing sound\t'" + location + "'");
			}
			soundLoads.put(location, loadedSound);
		}

		return loadedSound;
	}

	@SuppressWarnings("unchecked")
	public static UnicodeFont loadFont(String fontPath, int size) {
		UnicodeFont font = null;
		try {
			Font f = Font
					.createFont(Font.TRUETYPE_FONT,
							org.newdawn.slick.util.ResourceLoader.getResourceAsStream("assets/fonts/" + fontPath + ".ttf"))
					.deriveFont(Font.PLAIN, size);
			font = new UnicodeFont(f);
			font.addAsciiGlyphs();
			ColorEffect a = new ColorEffect();
			a.setColor(java.awt.Color.black);
			font.getEffects().add(a);
			font.loadGlyphs();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		// font.getEffects().add(new ColorEffect());
		return font;
	}

}
