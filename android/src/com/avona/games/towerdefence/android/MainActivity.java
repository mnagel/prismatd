package com.avona.games.towerdefence.android;

import com.avona.games.towerdefence.Util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

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
		
		pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "td-game");
		
		setContentView(R.layout.main);

		ml = new MainLoop(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ml.inputActor.onPause();
		ml.surfaceView.onPause();
		wl.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ml.inputActor.onResume();
		ml.surfaceView.onResume();
		wl.acquire();
	}
	
	protected PowerManager pm;
	protected WakeLock wl;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Store the game state (using Bundles)
		Util.log("supposed to save instance state");
	}
}
