package com.avona.games.towerdefence;

public class EnemyDeathUpdatesGameStats implements EnemyEventListener {
	private Game game;

	public EnemyDeathUpdatesGameStats(Game game) {
		this.game = game;
	}

	@Override
	public void onDeathEvent(Enemy e) {
		game.killed += 1;
	}

	@Override
	public void onEscapeEvent(Enemy e) {
		game.escaped += 1;
	}

}
