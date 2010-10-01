package com.avona.games.towerdefence.android;

import android.os.Vibrator;

import com.avona.games.towerdefence.EventListener;
import com.avona.games.towerdefence.tower.Tower;

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
