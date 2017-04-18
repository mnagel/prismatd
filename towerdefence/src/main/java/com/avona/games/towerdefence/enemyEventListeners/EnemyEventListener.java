package com.avona.games.towerdefence.enemyEventListeners;

import com.avona.games.towerdefence.enemy.Enemy;

import java.io.Serializable;

public interface EnemyEventListener extends Serializable {
	void onDeathEvent(Enemy e);

	void onEscapeEvent(Enemy e);
}
