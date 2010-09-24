package com.avona.games.towerdefence.enemySelection;

import java.util.List;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Tower;

public interface EnemySelectionPolicy {
	public Enemy findSuitableEnemy(Tower tower, List<Enemy> enemies);
}
