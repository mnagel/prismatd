package com.avona.games.towerdefence.wave.waveListeners;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.wave.Wave;

public class GrantInterestPerWave implements WaveListener {
	private static final long serialVersionUID = 1L;
	public float interestRate;
	private Game game;

	public GrantInterestPerWave(Game game, float interestRate) {
		this.game = game;
		this.interestRate = interestRate;
	}

	@Override
	public void onWaveBegun(Wave wave) {
		if (wave.waveNum == 1) {
			return;
		}
		game.money += game.money * interestRate;
	}

	@Override
	public void onWaveFullyDeployed(Wave wave) {

	}

	@Override
	public void onWaveKilled(Wave wave) {

	}
}
