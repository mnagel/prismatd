package com.avona.games.towerdefence.wave;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyEventListener;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.time.TimedCode;

public class Wave implements EnemyEventListener {
	private static final long serialVersionUID = 1L;

	public int waveNum;

	private boolean fullyDeployed = false;
	// TODO make independent of Game
	private Game game;
	private int curEnemy = 0;
	private WaveEnemyConfig[] enemies;
	private int activeEnemies = 0;

	public Wave(Game game, int waveNum, WaveEnemyConfig[] enemies) {
		this.game = game;
		this.waveNum = waveNum;
		this.enemies = enemies;

		spawnNextEnemy();
	}

	public boolean isFullyDeployed() {
		return fullyDeployed;
	}

	private void spawnNextEnemy() {
		spawnEnemy();

		if (moreEnemiesSpawnable()) {
			scheduleNextEnemySpawn();
		} else {
			onWaveFullyDeployed();
		}
	}

	private void spawnEnemy() {
		final V2 location = game.mission.waypoints[0].center.clone2();
		final V2 target = game.mission.waypoints[1].center.clone2();
		WaveEnemyConfig we = enemies[curEnemy];
		Enemy e = we.enemy.clone2();
		e.eventListeners.add(this);
		e.setInitialLocation(location, target);
		++curEnemy;
		++activeEnemies;
		game.onEnemySpawned(e);
	}

	private boolean moreEnemiesSpawnable() {
		return curEnemy < enemies.length;
	}

	private void onWaveFullyDeployed() {
		fullyDeployed = true;
		game.waveTracker.onWaveFullyDeployed(this);
		checkEnemiesDone();
	}

	private void scheduleNextEnemySpawn() {
		final WaveEnemyConfig we = enemies[curEnemy];
		game.timedCodeManager.addCode(
				new TimedCode() {
					@Override
					public double getDelayInS() {
						return we.delayAfter;
					}

					@Override
					public void run() {
						spawnNextEnemy();
					}
				}
		);
	}

	private void checkEnemiesDone() {
		if (!fullyDeployed) {
			return;
		}
		if (activeEnemies > 0) {
			return;
		}

		game.waveTracker.onWaveCompleted(this);
	}

	@Override
	public void onHurtEvent(Enemy e, Particle cause) {
		// do nothing
	}

	@Override
	public void onDeathEvent(Enemy e) {
		--activeEnemies;
		checkEnemiesDone();
	}

	@Override
	public void onEscapeEvent(Enemy e) {
		--activeEnemies;
		checkEnemiesDone();
	}

	@Override
	public String toString() {
		return "Wave #" + waveNum;
	}
}
