package com.avona.games.towerdefence.waveListeners;

import java.io.Serializable;

import com.avona.games.towerdefence.Wave;

public interface WaveListener extends Serializable {
	public void onWave(Wave wave);
}
