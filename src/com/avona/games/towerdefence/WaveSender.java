package com.avona.games.towerdefence;

public interface WaveSender {
	/**
	 * Gets called as soon as a new wave of enemies should be spawned.
	 * 
	 * @param waveLevel
	 * @return The Wave object tracking the progress of the new wave.
	 */
	Wave sendWave(int waveLevel);
}
