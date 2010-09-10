package com.avona.games.towerdefence.android;

import com.avona.games.towerdefence.Util;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	private MainLoop ml;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Util.log("creating fresh instance");
		} else {
			// TODO Restore game state (using Bundles).
			Util.log("would have restored instance");
		}
		ml = new MainLoop(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ml.inputActor.onPause();
		ml.surfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ml.inputActor.onResume();
		ml.surfaceView.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Store the game state (using Bundles)
		Util.log("supposed to save instance state");
	}
}
