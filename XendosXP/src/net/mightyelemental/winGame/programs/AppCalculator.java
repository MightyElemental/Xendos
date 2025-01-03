package net.mightyelemental.winGame.programs;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import net.mightyelemental.winGame.guiComponents.GUIButton;
import net.mightyelemental.winGame.guiComponents.GUIComponent;
import net.mightyelemental.winGame.guiComponents.GUIEntryPanel;
import net.mightyelemental.winGame.guiComponents.GUIPanel;
import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;
import net.mightyelemental.winGame.states.StateDesktop;

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
public class AppCalculator extends AppWindow {

	GUIEntryPanel entryPan = new GUIEntryPanel(390 - 2, 200, "#display", this);

	private double previousAnswer;

	public static ScriptEngineManager	manager	= new ScriptEngineManager();
	public static ScriptEngine			engine	= manager.getEngineByName("js");

	public AppCalculator(StateDesktop context, float x, float y, float width, float height) {
		super(context, x, y, 400, 600, "Calculator");
		GUIPanel p = new GUIPanel(400 - 12, 600 - 260, this).setGridLayout(4, 5).setColor(Color.gray);
		// this.addGUIObject(new GUIButton(190, 20, "#x", this).setText("EXE"), 5, 210);
		GUIComponent[] g = { new GUIButton(0, 0, "#-", this).setText("-"), new GUIButton(0, 0, "#*", this).setText("x"),
				new GUIButton(0, 0, "#/", this).setText("/") };
		this.addGUIObject(entryPan, 5, 5);
		this.addGUIObject(p, 5, 210);

		p.addGUIObject(new GUIButton(0, 0, "#clear", this).setText("CLEAR"), 0, 0);
		p.addGUIObject(new GUIButton(0, 0, "#del", this).setText("DEL"), 0, 0);
		p.addGUIObject(new GUIButton(190, 20, "#exe", this).setText("EXE"), 0, 0);
		p.addGUIObject(new GUIButton(0, 0, "#+", this).setText("+"), 0, 0);

		for ( int j = 0; j < 3; j++ ) {
			for ( int i = 0; i < 3; i++ ) {
				int num = (3 - j) * 3 - 2 + i;
				p.addGUIObject(new GUIButton(190, 20, "#num_" + num, this).setText("" + num), 0, 0);
			}
			p.addGUIObject(g[j], 0, 0);
			g[j] = null;
		}
		p.addGUIObject(new GUIButton(0, 0, "#.", this).setText("."), 0, 0);
		p.addGUIObject(new GUIButton(190, 20, "#num_" + 0, this).setText("" + 0), 0, 0);

		this.setSleepTime(50);
	}

	@Override
	public void drawContent(Graphics g, int width, int height) {
		this.clearContent();
	}

	@Override
	public void updateContent(float delta) {
		// TODO Auto-generated method stub

	}

	/** Handle what each button does */
	@Override
	public void onComponentPressed(int button, GUIComponent c) {
		String nid = c.getNID();
		if ( entryPan.getLatestEntry() == null ) {
			entryPan.addEntry(" ");
		}
		if ( entryPan.getLatestEntry().isFinalized() ) {
			entryPan.addEntry("");
		}
		if ( nid.startsWith("NUM_") ) {
			if ( !(entryPan.getLatestEntry().getText().length() == 0 && nid.equals("NUM_0")) ) {
				entryPan.getLatestEntry().getBuilder().append(nid.replaceFirst("NUM_", ""));
			}
		} else if ( nid.equals("EXE") ) {
			try {
				String evalText = entryPan.getLatestEntry().getText();
				evalText = evalText.replaceAll("ANS", previousAnswer + "");
				Object o = engine.eval(evalText);
				if ( o != null ) {
					entryPan.getLatestEntry().setFinalized();
					entryPan.addEntry(o.toString(), true, true);
					previousAnswer = Double.parseDouble(o.toString());
				}
			} catch (ScriptException e) {
				entryPan.getLatestEntry().setFinalized();
				entryPan.addEntry("Invalid operation", true, true);
				// e.printStackTrace();
			}
		} else if ( nid.equals("DEL") ) {
			if ( entryPan.getLatestEntry().getBuilder().length() > 0 ) {
				entryPan.getLatestEntry().getBuilder().deleteCharAt(entryPan.getLatestEntry().getBuilder().length() - 1);
			}
		} else if ( nid.equals("CLEAR") ) {
			entryPan.clearEntries();
		} else if ( !nid.equals("DISPLAY") ) {
			if ( entryPan.getLatestEntry().getText().isEmpty() ) {
				entryPan.getLatestEntry().getBuilder().append("ANS");
			}
			entryPan.getLatestEntry().getBuilder().append(nid);
		}
	}

}
