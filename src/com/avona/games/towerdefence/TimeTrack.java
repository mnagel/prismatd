package com.avona.games.towerdefence;

public class TimeTrack {

	public double clock = 0;
	public double tick = 0;

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
	 * Progress time by specifying new wall clock.
	 * 
	 * You may alternatively also use updateTick(), but don't use both at the
	 * same time.
	 * 
	 * @see TimeTrack#updateTick(double)
	 * @param wallClock
	 *            New wall clock time.
	 */
	public void update(double wallClock) {
		if (lastWallClock != 0)
			updateTick(wallClock - lastWallClock);
		lastWallClock = wallClock;
	}

	/**
	 * Progress time by specifying time delta.
	 * 
	 * You may alternatively also use update(), but don't use both at the same
	 * time.
	 * 
	 * @see TimeTrack#update(double)
	 * @param wallTick
	 */
	public void updateTick(double wallTick) {
		if (running) {
			tick = wallTick;
			clock += wallTick;
		} else {
			tick = 0;
		}
	}
}
