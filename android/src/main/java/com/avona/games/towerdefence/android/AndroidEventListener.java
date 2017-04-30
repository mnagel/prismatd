package com.avona.games.towerdefence.android;

import android.os.Vibrator;
import com.avona.games.towerdefence.events.EmptyEventListener;
import com.avona.games.towerdefence.tower.Tower;

public class AndroidEventListener extends EmptyEventListener {

	private static final long serialVersionUID = 5134420451781390194L;

	private Vibrator vibrator;

	public AndroidEventListener(Vibrator vibrator) {
		this.vibrator = vibrator;
	}

	@Override
	public void onBuildTower(Tower t) {
		vibrator.vibrate(25);
	}
}
