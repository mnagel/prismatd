package com.avona.games.towerdefence.enemySelection;

import java.util.List;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.Tower;

public class NearestEnemyPolicy implements EnemySelectionPolicy {
	private static final long serialVersionUID = 1L;

	@Override
	public Enemy findSuitableEnemy(Tower t, List<Enemy> enemies) {
		Enemy bestEnemy = null;
		float bestEnemyLocationSquaredDist = Float.MAX_VALUE;
		final float squaredRange = t.getRange() * t.getRange();

		for (Enemy e : enemies) {
			final float newEnemyLocationSquaredDist = t.location
					.squaredDist(e.location);
			// Within range?
			if (newEnemyLocationSquaredDist < squaredRange) {
				// Shoot to nearest enemy.
				if (bestEnemyLocationSquaredDist > newEnemyLocationSquaredDist) {
					bestEnemy = e;
					bestEnemyLocationSquaredDist = newEnemyLocationSquaredDist;
				}
			}
		}
		return bestEnemy;
	}
}
