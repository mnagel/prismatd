package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import com.avona.games.towerdefence.AsyncInput;
import com.avona.games.towerdefence.FeatureFlags;
import com.avona.games.towerdefence.IAsyncInput;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.mission.MissionList;

public class MainActivity extends Activity {
	protected WakeLock wl;
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
		// getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "td-game");

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

		ml.inputActor.pause();
		ml.surfaceView.onPause();
		wl.release();

		paused = true;
	}

	private void resume() {
		if (ml == null || !paused)
			return;

		ml.inputActor.resume();
		ml.surfaceView.onResume();
		wl.acquire();

		paused = false;
	}

	@Override
	public void onBackPressed() {
		//pause();

		AsyncInput.runnableChooser(
				"What do you want to do?",
				new String[]{
						"Go Back to my game.",
						"Start another mission.",
						"Quit the game."
				},
				new IAsyncInput.MyRunnable() {
					@Override
					public void run(int selectedOption) {
						switch (selectedOption) {
							case 1:
								ml.game.loadMissionInteractive();
							default:
							case 0:
								//resume();
								break;
							case 2:
								finish();
								break;
						}
					}
				}
		);
	}
}
