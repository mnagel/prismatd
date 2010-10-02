package com.avona.games.towerdefence;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.level.Level;

public class Wave extends TimedCode {
	private static final long serialVersionUID = 1L;

	private boolean completed = false;
	private Game game;
	private Level world;
	private TimedCodeManager timedCodeManager;
	private int curEnemy = 0;
	private WaveEnemyConfig[] enemies;

	public Wave(Game game, Level world, TimedCodeManager timedCodeManager,
			WaveEnemyConfig[] enemies) {
		this.game = game;
		this.world = world;
		this.timedCodeManager = timedCodeManager;
		this.enemies = enemies;

		spawnEnemy();
	}

	public boolean isCompleted() {
		return completed;
	}

	private void spawnEnemy() {
		final V2 location = world.waypoints.get(0).copy();
		WaveEnemyConfig we = enemies[curEnemy];
		Enemy e = we.enemy.copy();
		e.setInitialLocation(location);
		game.onEnemySpawned(e);

		++curEnemy;
		timedCodeManager.addCode(we.delay, this);
	}

	@Override
	public void execute() {
		if (curEnemy < enemies.length) {
			spawnEnemy();
		} else {
			completed = true;
			world.waveTracker.waveCompleted();
		}
	}
}
