package com.avona.games.towerdefence;

import com.avona.games.towerdefence.util.Util;

public class TickRater {
	static final float INITIAL_TICKRATE = 60.0f;

	public float tickRate = INITIAL_TICKRATE;

	public TimeTrack time;

	private boolean hadInitialZeroTick = false;

	public TickRater(TimeTrack time) {
		this.time = time;
	}

	public void updateTickRate() {
		if (!time.isRunning()) {
			tickRate = INITIAL_TICKRATE;
			return;
		}

		if (time.tick < 0.00001f) {
			if (this.hadInitialZeroTick) {
				Util.log("ingame zero tick: " + time.tick);
			} else {
				this.hadInitialZeroTick = true;
			}
			return;
		}
		if (time.tick > 1.0f) {
			tickRate = 0.99f; // less than 1 fps, dont bother
			return;
		}
		tickRate = (1.0f - time.tick) * tickRate + 1.0f;
	}
}
