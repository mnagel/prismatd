package com.avona.games.towerdefence;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.avona.games.towerdefence.waveListeners.WaveListener;

public class WaveTracker implements Serializable {

	private static final long serialVersionUID = 1069096293847665655L;

	public List<WaveListener> waveFullyDeployedListeners = new LinkedList<WaveListener>();
	public List<WaveListener> waveCompletedListeners = new LinkedList<WaveListener>();
	public List<WaveListener> waveBegunListeners = new LinkedList<WaveListener>();

	public WaveTracker(final WaveSender sender) {
		this.sender = sender;
	}

	public int currentWaveNum() {
		return waveNum;
	}

	public boolean hasWaveStarted() {
		return currentWave != null && !currentWave.isFullyDeployed();
	}

	public boolean hasWaveEnded() {
		return currentWave != null && currentWave.isFullyDeployed();
	}

	public boolean haveAllWavesBeenDeployed() {
		return (waveNum + 1) == sender.getNumWaves()
				|| (waveNum > -1 && currentWave == null);
	}

	public boolean hasPendingWaves() {
		return pendingWaves.size() > 0;
	}

	public void startNextWave() {
		if (currentWave != null && !currentWave.isFullyDeployed()) {
			return; // one wave deployment at a time
		}

		++waveNum;

		// Generate new wave
		currentWave = sender.sendWave(waveNum);
		if (currentWave != null) {
			pendingWaves.add(currentWave);

			// trigger events
			for (WaveListener l : waveBegunListeners) {
				l.onWave(currentWave);
			}
		} else {
			checkAllWavesCompleted();
		}
	}

	public void onWaveFullyDeployed(Wave w) {
		Util.log("Wave fully deployed");

		for (WaveListener l : waveFullyDeployedListeners) {
			l.onWave(w);
		}
	}

	public void onWaveCompleted(Wave w) {
		Util.log("Wave completed");
		pendingWaves.remove(w);

		for (WaveListener l : waveCompletedListeners) {
			l.onWave(w);
		}

		checkAllWavesCompleted();
	}

	private void checkAllWavesCompleted() {
		Util.log("Checking completion ...");
		if (haveAllWavesBeenDeployed()) {
			Util.log("All waves have been deployed");
			if (!hasPendingWaves()) {
				Util.log("No more pending waves.");
				sender.onAllWavesCompleted();
			}
		}
	}

	private int waveNum = -1;

	/**
	 * Currently running wave.
	 */
	private Wave currentWave;

	private WaveSender sender;

	private List<Wave> pendingWaves = new LinkedList<Wave>();
}
