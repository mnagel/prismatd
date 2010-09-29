package com.avona.games.towerdefence.enemyEventListeners;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Game;

public class EnemyDeathUpdatesGameStats implements EnemyEventListener {
	private static final long serialVersionUID = 1L;

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
