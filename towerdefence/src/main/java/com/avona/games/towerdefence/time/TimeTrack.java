package com.avona.games.towerdefence.time;

import java.io.Serializable;

public class TimeTrack implements Serializable {
	private static final long serialVersionUID = 1L;

	public float clock = 0;
	public float tick = 0;

	private double lastWallClock = 0;
	private boolean running = true;

	public boolean isRunning() {
		return running;
	}

	public void stopClock() {
		running = false;
		tick = 0;
		lastWallClock = 0;
	}

	public void startClock() {
		running = true;
	}

	/**
	 * Progress delay by specifying new wall clock.
	 * <p>
	 * You may alternatively also use updateTick(), but don't use both at the
	 * same delay.
	 *
	 * @param wallClock New wall clock delay.
	 * @see TimeTrack#updateTick(float)
	 */
	public void update(double wallClock) {
		if (lastWallClock != 0) {
			updateTick((float) (wallClock - lastWallClock));
		}
		lastWallClock = wallClock;
	}

	/**
	 * Progress time by specifying time delta.
	 * <p>
	 * You may alternatively also use update(), but don't use both at the same
	 * time.
	 *
	 * @param wallTick
	 * @see TimeTrack#update(double)
	 */
	public void updateTick(float wallTick) {
		if (running) {
			tick = wallTick;
			clock += wallTick;

		} else {
			tick = 0;
		}
	}
}
