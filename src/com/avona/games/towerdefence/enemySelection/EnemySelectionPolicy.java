package com.avona.games.towerdefence.enemySelection;

import java.io.Serializable;
import java.util.List;

import com.avona.games.towerdefence.Tower.Tower;
import com.avona.games.towerdefence.enemy.Enemy;

public interface EnemySelectionPolicy extends Serializable {
	public Enemy findSuitableEnemy(Tower tower, List<Enemy> enemies);
}
