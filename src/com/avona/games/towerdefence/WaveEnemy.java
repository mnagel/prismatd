package com.avona.games.towerdefence;

import com.avona.games.towerdefence.enemy.Enemy;

public class WaveEnemy {
	public Enemy enemy;
	public float delay;

	public WaveEnemy(final Enemy enemy, final float delay) {
		this.enemy = enemy;
		this.delay = delay;
	}
}
