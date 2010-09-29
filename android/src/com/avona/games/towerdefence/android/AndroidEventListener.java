package com.avona.games.towerdefence.android;

import Tower.Tower;
import android.os.Vibrator;

import com.avona.games.towerdefence.EventListener;

public class AndroidEventListener implements EventListener {

	private Vibrator vibrator;

	public AndroidEventListener(Vibrator vibrator) {
		this.vibrator = vibrator;
	}

	@Override
	public void onBuildTower(Tower t) {
		vibrator.vibrate(25);
	}
}
