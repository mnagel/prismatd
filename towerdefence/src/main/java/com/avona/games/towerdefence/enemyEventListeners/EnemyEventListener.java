package com.avona.games.towerdefence.enemyEventListeners;

import com.avona.games.towerdefence.enemy.Enemy;

import java.io.Serializable;

public interface EnemyEventListener extends Serializable {
	public void onDeathEvent(Enemy e);

	public void onEscapeEvent(Enemy e);
}
