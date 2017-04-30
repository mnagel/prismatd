package com.avona.games.towerdefence.tower.enemySelection;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.Tower;

import java.util.List;

public class NearestEnemyPolicy implements EnemySelectionPolicy {
	private static final long serialVersionUID = 1L;

	public String getName() {
		return "Nearest Enemy";
	}

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
