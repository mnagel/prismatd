package com.avona.games.towerdefence;

public class TimeTrack {

	public double clock = 0;
	public double tick = 0;

	private double lastWallClock = 0;
	private boolean running = true;

	public void stopClock() {
		running = false;
	}
	public void startClock() {
		running = true;
	}

	public void update(double wallClock) {
		updateTick(wallClock - lastWallClock);
		lastWallClock = wallClock;
	}

	protected void updateTick(double wallTick) {
		if (running) {
			tick = wallTick;
			clock += wallTick;
		}
	}
}
