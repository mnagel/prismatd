package com.avona.games.towerdefence.android;

import javax.vecmath.Point2d;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.World;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class towerdefenceandroidmain extends Activity {

	private TextView text;

	/** Called when the activity is first created. */
	@Override        
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		text = (TextView) findViewById(R.id.TextView01);

		text.setText("hallo michael");

		World w = new World();
		Point2d p = new Point2d();

		Enemy e = new Enemy(w, p);

		e.step(1.0);
		text.setText(text.getText() + "\n e is at" + e.location.toString());
		e.step(1.0);
		text.setText(text.getText() + "\n e is at" + e.location.toString());
		e.step(1.0);
		text.setText(text.getText() + "\n e is at" + e.location.toString());
		e.step(1.0);
		text.setText(text.getText() + "\n e is at" + e.location.toString());

		Game g = new Game();
		g.updateWorld(1.0);

		text.setText(text.getText() + "end game");

	}

}