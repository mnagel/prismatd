package com.avona.games.towerdefence;

public class RechargeTimer {
	public double time;
	public double timeRemaining;

	public RechargeTimer(double time) {
		this.time = time;
		this.timeRemaining = 0.0;
	}

	public boolean isReady() {
		return this.timeRemaining <= 0.0;
	}

	public void rearm() {
		this.timeRemaining = this.time;
	}

	public void step(double dt) {
		this.timeRemaining -= dt;
	}
}
