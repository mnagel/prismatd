package com.avona.games.towerdefence;

public abstract class TimedCode {
	
	public double startTime = 0.0f;
	
	public TimedCode(float startDelay, TimeTrack basis) {
		this.startTime = startDelay + basis.clock;
	}
	
	public abstract void execute();
}
