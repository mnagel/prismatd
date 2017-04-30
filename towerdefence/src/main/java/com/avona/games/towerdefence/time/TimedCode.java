package com.avona.games.towerdefence.time;

import java.io.Serializable;

public abstract class TimedCode implements Serializable {
	private static final long serialVersionUID = 1L;

	public double startTime = 0.0f;

	public abstract void execute();
}
