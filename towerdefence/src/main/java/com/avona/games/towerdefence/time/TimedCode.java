package com.avona.games.towerdefence.time;

public abstract class TimedCode implements Runnable {
	double startTime = 0;

	public abstract double getDelay();

	public abstract void run();

	void activate(double clock) {
		startTime = clock + getDelay();
	}
}
