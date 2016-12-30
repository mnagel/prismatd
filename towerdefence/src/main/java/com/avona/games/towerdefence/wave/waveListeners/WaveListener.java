package com.avona.games.towerdefence.wave.waveListeners;

import com.avona.games.towerdefence.wave.Wave;

import java.io.Serializable;

public interface WaveListener extends Serializable {
	void onWave(Wave wave);
}
