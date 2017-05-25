package com.avona.games.towerdefence.wave.waveListeners;

import com.avona.games.towerdefence.wave.Wave;

import java.io.Serializable;

public interface WaveListener extends Serializable {
	void onWaveBegun(Wave wave);

	void onWaveFullyDeployed(Wave wave);

	void onWaveKilled(Wave wave);
}
