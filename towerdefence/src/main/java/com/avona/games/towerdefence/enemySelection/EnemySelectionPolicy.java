package com.avona.games.towerdefence.enemySelection;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.Tower;

import java.io.Serializable;
import java.util.List;

public interface EnemySelectionPolicy extends Serializable {
	String getName();

	Enemy findSuitableEnemy(Tower tower, List<Enemy> enemies);
}
