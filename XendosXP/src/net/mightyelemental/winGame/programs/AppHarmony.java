package net.mightyelemental.winGame.programs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.Graphics;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import net.mightyelemental.winGame.guiComponents.dekstopObjects.AppWindow;

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
