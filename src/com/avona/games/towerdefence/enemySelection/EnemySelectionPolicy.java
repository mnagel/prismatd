package com.avona.games.towerdefence.enemySelection;

import java.io.Serializable;
import java.util.List;

import Tower.Tower;

import com.avona.games.towerdefence.Enemy.Enemy;

public interface EnemySelectionPolicy extends Serializable {
	public Enemy findSuitableEnemy(Tower tower, List<Enemy> enemies);
}
