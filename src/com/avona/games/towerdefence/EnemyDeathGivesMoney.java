package com.avona.games.towerdefence;

public class EnemyDeathGivesMoney implements EnemyEventListener {
	private Game game;

	public EnemyDeathGivesMoney(Game game) {
		this.game = game;
	}

	@Override
	public void onDeathEvent(Enemy e) {
		game.money += e.worth;
	}

	@Override
	public void onEscapeEvent(Enemy e) {
		// Nothing to do.
	}
}
