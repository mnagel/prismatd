package com.avona.games.towerdefence;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.world.World;

public class Wave extends TimedCode {
	private static final long serialVersionUID = 1L;

	private int level;
	private boolean completed = false;
	private Game game;
	private World world;
	private TimedCodeManager timedCodeManager;
	private int curEnemy = 0;
	private WaveEnemyConfig[] enemies;

	public Wave(Game game, World world, TimedCodeManager timedCodeManager,
			int level, WaveEnemyConfig[] enemies) {
		this.game = game;
		this.world = world;
		this.timedCodeManager = timedCodeManager;
		this.level = level;
		this.enemies = enemies;

		spawnEnemy();
	}

	public int getLevel() {
		return level;
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
			game.onWaveCompleted(level);
		}
	}
}
