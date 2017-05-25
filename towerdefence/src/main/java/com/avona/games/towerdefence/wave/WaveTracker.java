package com.avona.games.towerdefence.wave;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.util.Util;
import com.avona.games.towerdefence.wave.waveListeners.WaveListener;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class WaveTracker implements Serializable {

	private static final long serialVersionUID = 1069096293847665655L;

	public List<WaveListener> listeners = new LinkedList<>();
	private int waveNum = -1;
	/**
	 * Currently running wave.
	 */
	private Wave currentWave;
	// TODO make independent of Game
	private Game game;
	private List<Wave> pendingWaves = new LinkedList<>();

	public WaveTracker(Game game) {
		this.game = game;
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

	private boolean haveAllWavesBeenDeployed() {
		return (waveNum + 1) == game.mission.getWaveCount()
				|| (waveNum > -1 && currentWave == null);
	}

	private boolean hasPendingWaves() {
		return pendingWaves.size() > 0;
	}

	public void startNextWave() {
		if (currentWave != null && !currentWave.isFullyDeployed()) {
			Util.log("Cannot deploy multiple waves concurrently!");
			return; // one wave deployment at a delay
		}

		++waveNum;

		// Generate new wave
		currentWave = game.sendWave(waveNum);
		if (currentWave != null) {
			pendingWaves.add(currentWave);

			Util.log(currentWave + " starting");
			// trigger events
			for (WaveListener l : listeners) {
				l.onWaveBegun(currentWave);
			}
		} else {
			Util.log("No more waves to deploy!");
			checkAllWavesCompleted();
		}
	}

	void onWaveFullyDeployed(Wave w) {
		Util.log(w + " fully deployed");

		for (WaveListener l : listeners) {
			l.onWaveFullyDeployed(w);
		}
	}

	void onWaveCompleted(Wave w) {
		Util.log(w + " completed");
		pendingWaves.remove(w);

		for (WaveListener l : listeners) {
			l.onWaveKilled(w);
		}

		checkAllWavesCompleted();
	}

	private void checkAllWavesCompleted() {
		Util.log("checkAllWavesCompleted...");
		if (haveAllWavesBeenDeployed()) {
			Util.log("All waves have been deployed");
			if (!hasPendingWaves()) {
				Util.log("No more pending waves.");
				game.onAllWavesCompleted();
			}
		}
	}
}
