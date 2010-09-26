package com.avona.games.towerdefence.waveListeners;

import java.io.Serializable;

public interface WaveListener extends Serializable {
	public void onWave(int level);
}
