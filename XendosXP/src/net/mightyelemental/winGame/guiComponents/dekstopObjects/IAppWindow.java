package net.mightyelemental.winGame.guiComponents.dekstopObjects;

import org.newdawn.slick.Graphics;

public interface IAppWindow extends Runnable {

    /**
     * Get the current frames-per-second of the window.
     * 
     * @return The FPS
     */
    public float getFps();

    /**
     * Whether the FPS should be added to the title bar
     * 
     * @return {@code true} if FPS should be shown
     */
    public boolean showFPS();

    /** Sets the flags to close the window. */
    public void closeWindow();

    /**
     * Get the window title.
     * 
     * @return The title
     */
    public String getTitle();

    /**
     * Used to update the window content. Must be defined for each application.
     * 
     * @param delta the time since the last update in seconds
     */
    public void updateContent(float delta);

    /**
     * Used to draw the window content. Must be defined for each application.
     * 
     * @param g the graphics context
     * @param width the window content width
     * @param height the window content height
     */
    public void drawContent(Graphics g, int width, int height);

    /** Clear the window content */
    public void clearContent();

    /**
     * @param key the key pressed
     * @param c
     */
    public void onKeyPressed(int key, char c);

    /**
     * Get the taskbar button associated with this application window.
     * 
     * @return The associated taskbar item
     */
    public TaskbarApp getTaskbarItem();

    public float getX();

    public float getY();

    /** Get the width of the application window */
    public float getWidth();

    /** Get the height of the application window */
    public float getHeight();

    public boolean contains(float x, float y);

}
