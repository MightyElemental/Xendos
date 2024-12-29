package net.mightyelemental.winGame.programs;

import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.OSSettings;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;
import net.mightyelemental.winGame.states.StateDesktop;
import net.mightyelemental.winGame.util.ErrorType;

/**
 * @author James
 */
public class AppPopupMessage extends AppWindow {

    private ErrorType type;
    private String    content;

    public AppPopupMessage(StateDesktop context, String title, String content, ErrorType type) {
        super(context, 0, 0, OSSettings.NORMAL_FONT.getWidth(content) + 10, 80, title);
        this.type = type;
        this.content = content;
        this.setSleepTime(1000);
    }

    @Override
    public void drawContent(Graphics g, int width, int height) {
        g.setFont(OSSettings.NORMAL_FONT);
        g.drawString(content, 0, 0);
    }

    @Override
    public void updateContent(float delta) {

    }

}
