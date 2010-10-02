package com.avona.games.towerdefence;

import java.io.Serializable;

import com.avona.games.towerdefence.enemy.Enemy;

public class WaveEnemyConfig {

	public Enemy enemy;
	public float delay;

	public WaveEnemyConfig(final Enemy enemy, final float delay) {
		this.enemy = enemy;
		this.delay = delay;
	}
}
