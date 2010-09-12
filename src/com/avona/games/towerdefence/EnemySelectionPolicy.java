package com.avona.games.towerdefence;

import java.util.List;

public interface EnemySelectionPolicy {
	public Enemy findSuitableEnemy(Tower tower, List<Enemy> enemies);
}
