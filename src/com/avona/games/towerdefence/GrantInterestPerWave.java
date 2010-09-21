package com.avona.games.towerdefence;

public class GrantInterestPerWave implements WaveListener {
	private Game game;
	public float interestRate;
	
	public GrantInterestPerWave(Game game, float interestRate) {
		this.game = game;
		this.interestRate = interestRate;
	}

	@Override
	public void onWave(int level) {
		if (level == 1)
			return;
		game.money += game.money * interestRate;
	}
}
