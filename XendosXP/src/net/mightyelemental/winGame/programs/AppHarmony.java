package net.mightyelemental.winGame.programs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.Graphics;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

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
public class AppHarmony extends AppWindow {

	private static final long serialVersionUID = 1046548414901267080L;

	private FirebaseAuth	fbAuth;
	private FirebaseApp		fbApp;

	public AppHarmony(float x, float y, float width, float height) {
		super(x, y, width, height, "Harmony");

		try {
			//FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
			FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.getApplicationDefault())
					.setDatabaseUrl("https://xendos-chat.firebaseio.com/").build();

			fbApp = FirebaseApp.initializeApp(options);
			System.out.println(fbApp.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void drawContent(Graphics g, int width, int height) {

	}

	@Override
	public void updateContent(int delta) {

	}

}
