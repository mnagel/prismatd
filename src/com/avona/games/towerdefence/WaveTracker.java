package com.avona.games.towerdefence;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.avona.games.towerdefence.waveListeners.WaveListener;

public class WaveTracker implements Serializable {

	private static final long serialVersionUID = 1069096293847665655L;

	public List<WaveListener> waveCompletedListeners = new LinkedList<WaveListener>();
	public List<WaveListener> waveBegunListeners = new LinkedList<WaveListener>();

	public WaveTracker(final WaveSender sender) {
		this.sender = sender;
	}

	public int currentWaveNum() {
		return waveNum;
	}

	public boolean hasWaveStarted() {
		return currentWave != null && !currentWave.isCompleted();
	}

	public boolean hasWaveEnded() {
		return currentWave != null && currentWave.isCompleted();
	}

	public void startNextWave() {
		if (currentWave != null && !currentWave.isCompleted()) {
			return; // one wave at a time
		}

		++waveNum;

		// Generate new wave
		currentWave = sender.sendWave(waveNum);

		// trigger events
		for (WaveListener l : waveBegunListeners) {
			l.onWave(waveNum);
		}
	}

	public void waveCompleted() {
		for (WaveListener l : waveCompletedListeners) {
			l.onWave(waveNum);
		}
	}

	private int waveNum = 0;

	/**
	 * Currently running wave.
	 */
	private Wave currentWave;

	private WaveSender sender;

}
