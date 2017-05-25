package com.avona.games.towerdefence.enemy.eventListeners;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.Particle;

import java.io.Serializable;

public interface EnemyEventListener extends Serializable {
	void onHurtEvent(Enemy e, Particle cause);

	void onDeathEvent(Enemy e);

	void onEscapeEvent(Enemy e);
}
