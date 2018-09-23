package net.mightyelemental.winGame.states;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.mightyelemental.winGame.OSSettings;
import net.mightyelemental.winGame.ResourceLoader;
import net.mightyelemental.winGame.XendosMain;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUIPanel;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.FileObject;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.StartWindow;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.TaskbarApp;
import net.mightyelemental.winGame.programs.AppTest;

public class StateDesktop extends BasicGameState {

	public StateDesktop() {
	}

	private List<GUIComponent>	guiComponents	= new ArrayList<GUIComponent>();
	private List<AppWindow>		windowList		= new ArrayList<AppWindow>();
	private List<String>		taskbarAppOrder	= new ArrayList<String>();

	private Image		background, taskbar;
	private Rectangle	selection;
	private StartWindow	startWin;

	private String selectedUID = "";

	@Override
	public void init(GameContainer gc, StateBasedGame delta) throws SlickException {
		background = ResourceLoader.loadImage("desktop.background-bliss");
		taskbar = ResourceLoader.loadImage("desktop.taskbar");

		startWin = new StartWindow();
		guiComponents.add(new GUIComponent(0, gc.getHeight() - 43, 105, 43, "#START").setTransparent(true));

		int i = 0;
		for ( Class<? extends AppWindow> c : XendosMain.programs.keySet() ) {
			// String title = c.getSimpleName().replaceFirst("App", "");
			String title = XendosMain.programs.get(c);
			guiComponents.add(new FileObject(15, 15 + (OSSettings.FILE_DISPLAY_SIZE + 25) * i, "File_" + title, title)
					.setLinkedClass(c).setColor(Color.magenta));
			i++;
		}
		startWin.init(gc, delta);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setFont(OSSettings.NORMAL_FONT);
		background.draw();
		g.setColor(Color.white);
		// g.drawString("<Some GUI goes here>", gc.getWidth() / 2 -
		// (g.getFont().getWidth("<Some GUI goes here>") / 2),
		// gc.getHeight() / 2);

		if ( selection != null ) {
			g.setColor(new Color(0f, 0f, 0.5f, 0.3f));
			g.fill(selection);
			g.setColor(new Color(0f, 0f, 0.7f, 1f));
			g.draw(selection);
		}
		taskbar.draw(0, gc.getHeight() - taskbar.getHeight());
		for ( GUIComponent c : guiComponents ) {
			c.draw(gc, sbg, g);
		}
		drawWindows(gc, sbg, g);
	}

	private void drawWindows(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		for ( int i = 0; i < windowList.size(); i++ ) {
			// if ( windowList.size() > 0 ) {
			windowList.get(i).draw(gc, sbg, g);
			// }
		}
		if ( getComponent("#START") != null && getComponent("#START").isSelected() ) {
			startWin.draw(gc, sbg, g);
			startWin.isMinimised = true;
		} else {
			startWin.isMinimised = false;
		}
	}

	public GUIComponent getComponent(String uid) {
		for ( int i = 0; i < guiComponents.size(); i++ ) {
			if ( guiComponents.get(i).getUID().equals(uid.toUpperCase()) ) return guiComponents.get(i);
		}
		return null;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		updateWindows(gc, sbg, delta);
	}

	public void updateWindows(GameContainer gc, StateBasedGame sbg, int delta) {
		for ( int i = 0; i < windowList.size(); i++ ) {
			// windowList.get(i).update(gc, sbg, delta);
			windowList.get(i).updateMinimize(delta);
			if ( windowList.get(i).toClose && windowList.get(i).isMinimised ) {
				deleteWindow(windowList.get(i));
			}
		}
		for ( int i = 0; i < guiComponents.size(); i++ ) {
			if ( guiComponents.get(i) instanceof TaskbarApp ) {
				if ( ((TaskbarApp) guiComponents.get(i)).linkedWindow == null
						|| ((TaskbarApp) guiComponents.get(i)).linkedWindow.toClose ) {
					guiComponents.remove(i);
				}
			}
		}
	}

	@Override
	public int getID() {
		return XendosMain.STATE_DESKTOP;
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		boolean selectFlag = true;
		for ( int i = windowList.size() - 1; i >= 0; i-- ) {
			if ( windowList.get(i).isDraggable() ) {
				windowList.get(i).mouseDragged(newx - oldx, newy - oldy);
				selectFlag = false;
				break;
			}
		}
		if ( selectFlag && selectedUID.equals("") ) {
			selection = new Rectangle(this.oldx, this.oldy, newx - this.oldx, newy - this.oldy);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		selection = null;
		for ( int i = windowList.size() - 1; i >= 0; i-- ) {
			AppWindow aw = windowList.get(i);
			if ( aw.isDraggable() ) {
				aw.onMouseReleased(button);
				break;
			}
			for ( GUIComponent c : aw.guiObjects ) {
				if ( c instanceof GUIPanel ) {
					for ( GUIComponent com : ((GUIPanel) c).getGuiObjects() ) {
						// System.out.println(com.getUID() + "|" + com.isSelected());
						if ( com.isSelected() ) {
							onComponentReleased(button, com);
						}
					}
				} else if ( c.isSelected() ) {
					onComponentReleased(button, c);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(int button, int x, int y) {
		oldx = x;
		oldy = y;
		selectedUID = "";
		for ( GUIComponent c : guiComponents ) {
			if ( c.contains(x, y) ) {
				c.setSelected(!c.isSelected());
				if ( c instanceof TaskbarApp ) {
					AppWindow aw = ((TaskbarApp) c).linkedWindow;
					foregroundWindow(aw);
				}
				onComponentPressed(button, c);
				selectedUID = c.getUID();
				// if (c.getUID().equals("#START")) {
				// boolean sel = c.isSelected();
				// c.setSelected(!sel);
				// } else {
				// c.setSelected(true);
				// }
				if ( c instanceof FileObject ) {
					FileObject fo = (FileObject) c;
					if ( fo.getLinkedClass() != null && fo.lastClicked() + 500 > System.currentTimeMillis() ) {
						try {
							this.createNewWindow(800, 600, (Class<? extends AppWindow>) fo.getLinkedClass());
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}
						break;
					}
				}
				c.onMousePressed(button);
			}
		}
		for ( AppWindow aw : windowList ) {
			if ( aw.contains(x, y) && !aw.isMinimised ) for ( GUIComponent c : aw.guiObjects ) {
				if ( c instanceof GUIPanel ) {
					for ( GUIComponent com : ((GUIPanel) c).getGuiObjects() ) {
						if ( com.contains(x - aw.getX(), y - aw.getY() - 26) ) {// TODO: Fix position issue
							onComponentPressed(button, com);// TODO something is wrong...
							break;
						}
					}
				} else if ( c.contains(x - aw.getX(), y - aw.getY() - 26) ) {
					onComponentPressed(button, c);
					break;
				}
			}
		}
		if ( y > 720 - 43 ) return;
		for ( int i = windowList.size() - 1; i >= 0; i-- ) {
			if ( windowList.get(i).contains(x, y) ) {
				boolean success = windowList.get(i).onMousePressed(button, x, y);
				if ( success ) {
					break;
				} else {
					// System.out.println("WINDOW");
					foregroundWindow(windowList.get(i));// TODO FIX THIS
					if ( !windowList.get(i).isMinimised ) {
						break;
					}
				}
			}
		}
	}

	private int oldx, oldy;

	public void foregroundWindow(AppWindow aw) {
		windowList.remove(aw);
		windowList.add(aw);
		// System.out.println(aw.getLinkedTaskbarApp().getUID());
	}

	public void onComponentPressed(int button, GUIComponent c) {
		if ( c.getLinkedWindow() != null ) {
			c.getLinkedWindow().onComponentPressed(button, c);
			c.onMousePressed(button);
			// c.setSelected(true);
		}
	}

	public void onComponentReleased(int button, GUIComponent c) {
		if ( c.getLinkedWindow() != null ) {
			c.onMouseReleased(button);
			// c.setSelected(false);
		}
	}

	public void createNewWindow(int width, int height, Class<? extends AppWindow> c)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		int x = 1280 / 2 - width / 2;
		int y = 720 / 2 - height / 2;
		// AppWindow wa = new AppSquareRotator(x, y, 800, 600);
		AppWindow wa = c.getConstructor(new Class<?>[] { float.class, float.class, float.class, float.class }).newInstance(x, y,
				width, height);
		windowList.add(wa);
		TaskbarApp t = new TaskbarApp(110, wa, taskbarAppOrder.size());
		guiComponents.add(t);
		taskbarAppOrder.add(t.getUID());
	}

	public void deleteWindow(AppWindow aw) {
		windowList.remove(aw);
		taskbarAppOrder.remove(aw.getLinkedTaskbarApp().getUID());
		for ( AppWindow a : windowList ) {
			TaskbarApp t = a.getLinkedTaskbarApp();
			t.setIndex(taskbarAppOrder.indexOf(t.getUID()));
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		for ( GUIComponent g : guiComponents ) {
			if ( g.getUID().equals(selectedUID) ) {
				// System.out.println(selectedUID);
				g.onKeyPressed(key, c);
				break;
			}
		}
		for ( int i = windowList.size() - 1; i >= 0; i-- ) {
			AppWindow aw = windowList.get(i);
			if ( aw.isMinimised ) continue;
			aw.keyPressed(key, c);
			break;
		}
		if ( key == Input.KEY_I ) {
			try {
				createNewWindow(800, 600, AppTest.class);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		if ( key == Input.KEY_DELETE ) {
			for ( AppWindow w : windowList ) {
				deleteWindow(w);
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		for ( int i = windowList.size() - 1; i >= 0; i-- ) {
			AppWindow aw = windowList.get(i);
			if ( aw.isMinimised ) continue;
			aw.keyReleased(key, c);
			break;
		}
	}

}
