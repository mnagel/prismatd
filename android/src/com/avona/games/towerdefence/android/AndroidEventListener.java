package com.avona.games.towerdefence.android;

import android.os.Vibrator;

import com.avona.games.towerdefence.EventListener;
import com.avona.games.towerdefence.level.Level;
import com.avona.games.towerdefence.tower.Tower;

public class AndroidEventListener implements EventListener {

	private static final long serialVersionUID = 5134420451781390194L;

	private Vibrator vibrator;

	public AndroidEventListener(Vibrator vibrator) {
		this.vibrator = vibrator;
	}

	@Override
	public void onBuildTower(Tower t) {
		vibrator.vibrate(25);
	}

	@Override
	public void onLevelCompleted(Level l) {
	}
}
