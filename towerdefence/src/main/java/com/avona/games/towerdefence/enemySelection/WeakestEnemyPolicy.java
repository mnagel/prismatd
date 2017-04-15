package com.avona.games.towerdefence.enemySelection;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.Tower;

import java.util.List;

public class WeakestEnemyPolicy implements EnemySelectionPolicy {
	@Override
	public String getName() {
		return "Weakest Enemy";
	}

	@Override
	public Enemy findSuitableEnemy(Tower tower, List<Enemy> enemies) {
		Enemy bestEnemy = null;
		float bestEnemyHealth = Float.MAX_VALUE;
		final float squaredRange = tower.getRange() * tower.getRange();

		for (Enemy e : enemies) {
			final float newEnemyLocationSquaredDist = tower.location
					.squaredDist(e.location);
			// Within range?
			if (newEnemyLocationSquaredDist < squaredRange) {
				// Shoot to nearest enemy.
				if (bestEnemyHealth > e.life.length()) {
					bestEnemy = e;
					bestEnemyHealth = e.life.length();
				}
			}
		}
		return bestEnemy;
	}
}
