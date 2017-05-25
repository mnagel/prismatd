package com.avona.games.towerdefence.enemy.eventListeners;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.particle.Particle;

public class EnemyDeathUpdatesGameStats implements EnemyEventListener {
	private static final long serialVersionUID = 1L;

	private Game game;

	public EnemyDeathUpdatesGameStats(Game game) {
		this.game = game;
	}

	@Override
	public void onHurtEvent(Enemy e, Particle cause) {
		// do nothing
	}

	@Override
	public void onDeathEvent(Enemy e) {
		game.killed += 1;
	}

	@Override
	public void onEscapeEvent(Enemy e) {
		game.looseLife();
	}

}
