package com.avona.games.towerdefence.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;

import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.mission.MissionList;

public class MainActivity extends Activity {
	protected WakeLock wl;
	boolean paused = true; // onResume will start us
	private MainLoop ml;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Util.log("instance: onCreate");

		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"td-game");

		if (savedInstanceState == null) {
		} else {
			// TODO: Restore.
			Util.log("restoring instance");
		}

		String[] missions = Util.mapMissionNames(MissionList.availableMissions);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Mission");
		builder.setItems(missions, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Util.log("Selected Mission: " + which);
				ml = new MainLoop(MainActivity.this, vibrator, which);
				setContentView(ml.surfaceView);
			}
		});
		builder.show();
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
		pause();

		final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setCancelable(true);
		alertBuilder.setPositiveButton("Continue Playing",
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resume();
					}
				});
		alertBuilder.setNegativeButton("Quit", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		final AlertDialog alertDialog = alertBuilder.create();
		alertDialog
				.setMessage("Do you really want to quit Prisma TD?\nYour progress will be lost!");
		alertDialog.show();
	}
}
