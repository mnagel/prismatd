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

	public static WaveEnemyConfig[] create(int count, float delayAfter, Enemy... e) {
		WaveEnemyConfig[] result = new WaveEnemyConfig[count];
		for (int i = 0; i < count; i++) {
			result[i] = new WaveEnemyConfig(e[i % e.length].clone2(), delayAfter);
		}
		return result;
	}
}
