package com.avona.games.towerdefence.enemyEventListeners;

import java.io.Serializable;

import com.avona.games.towerdefence.enemy.Enemy;

public interface EnemyEventListener extends Serializable {
	public void onDeathEvent(Enemy e);

	public void onEscapeEvent(Enemy e);
}
