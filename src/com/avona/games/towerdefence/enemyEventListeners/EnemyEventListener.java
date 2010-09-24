package com.avona.games.towerdefence.enemyEventListeners;

import com.avona.games.towerdefence.Enemy;

public interface EnemyEventListener {
	public void onDeathEvent(Enemy e);

	public void onEscapeEvent(Enemy e);
}
