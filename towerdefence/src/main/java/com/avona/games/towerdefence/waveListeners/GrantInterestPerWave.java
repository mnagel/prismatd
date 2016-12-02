package com.avona.games.towerdefence.waveListeners;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Wave;

public class GrantInterestPerWave implements WaveListener {
	private static final long serialVersionUID = 1L;

	private Game game;
	public float interestRate;

	public GrantInterestPerWave(Game game, float interestRate) {
		this.game = game;
		this.interestRate = interestRate;
	}

	@Override
	public void onWave(Wave wave) {
		if (wave.waveNum == 1)
			return;
		game.money += game.money * interestRate;
	}
}
