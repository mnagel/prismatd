package com.avona.games.towerdefence.enemy.eventListeners;

import com.avona.games.towerdefence.enemy.Enemy;

import java.io.Serializable;

public interface EnemyEventListener extends Serializable {
	void onDeathEvent(Enemy e);

	void onEscapeEvent(Enemy e);
}