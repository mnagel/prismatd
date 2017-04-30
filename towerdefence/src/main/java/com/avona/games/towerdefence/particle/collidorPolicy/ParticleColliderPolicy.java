package com.avona.games.towerdefence.particle.collidorPolicy;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.Particle;

import java.io.Serializable;
import java.util.List;

public interface ParticleColliderPolicy extends Serializable {
	void collideParticleWithEnemies(Particle p, List<Enemy> enemies, float dt);
}
