package com.avona.games.towerdefence.enemySelection;

import java.io.Serializable;
import java.util.List;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.Tower;

public interface EnemySelectionPolicy extends Serializable {
	public Enemy findSuitableEnemy(Tower tower, List<Enemy> enemies);
}
