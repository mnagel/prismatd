package com.avona.games.towerdefence.wave;

import com.avona.games.towerdefence.enemy.Enemy;

import java.io.Serializable;

public class WaveEnemyConfig implements Serializable {

	private static final long serialVersionUID = -7341162462907266565L;

	public Enemy enemy;
	public float delayAfter;

	public WaveEnemyConfig(final Enemy enemy, final float delayAfter) {
		this.enemy = enemy;
		this.delayAfter = delayAfter;
	}
}
