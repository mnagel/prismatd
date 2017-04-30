package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.os.Vibrator;
import com.avona.games.towerdefence.input.AsyncInput;
import com.avona.games.towerdefence.util.FeatureFlags;
import com.avona.games.towerdefence.input.IAsyncInput;
import com.avona.games.towerdefence.util.Util;
import com.avona.games.towerdefence.mission.MissionList;

public class MainActivity extends Activity {
	boolean paused = true; // onResume will start us
	private AndroidMainLoop ml;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		AsyncInput.setInstance(new AndroidIAsyncInput(this));

		super.onCreate(savedInstanceState);
		Util.log("instance: onCreate");

		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// TODO: Restore from savedInstanceState

		// Check if the system supports OpenGL ES 2.0
		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		if (configurationInfo.reqGlEsVersion < 0x20000) {
			throw new RuntimeException("This device does not support GL ES 2.0");
		}

		//noinspection ConstantConditions
		if (FeatureFlags.AUTOSTART_MISSION != -1) {
			Util.log("AUTOSTART_MISSION: " + FeatureFlags.AUTOSTART_MISSION);
			ml = new AndroidMainLoop(MainActivity.this, vibrator, FeatureFlags.AUTOSTART_MISSION);
			setContentView(ml.surfaceView);
		} else {
			String[] missions = MissionList.getAvailableMissionNames();
			AsyncInput.runnableChooser("Select Mission", missions, new IAsyncInput.MyRunnable() {
				@Override
				public void run(int selectedOption) {
					Util.log("Selected Mission: " + selectedOption);
					ml = new AndroidMainLoop(MainActivity.this, vibrator, selectedOption);
					setContentView(ml.surfaceView);
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Util.log("instance: onPause");
		pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Util.log("instance: onResume");
		resume();
	}

	private void pause() {
		if (paused)
			return;

		ml.rootInputActor.pause();
		ml.surfaceView.onPause();

		paused = true;
	}

	private void resume() {
		if (ml == null || !paused)
			return;

		ml.rootInputActor.resume();
		ml.surfaceView.onResume();

		paused = false;
	}

	@Override
	public void onBackPressed() {
		ml.rootInputActor.pressedOtherKey('b');
	}
}
