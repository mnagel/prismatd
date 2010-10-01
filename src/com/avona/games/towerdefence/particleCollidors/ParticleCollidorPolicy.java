package com.avona.games.towerdefence.particleCollidors;

import java.io.Serializable;
import java.util.List;

import com.avona.games.towerdefence.Particle.Particle;
import com.avona.games.towerdefence.enemy.Enemy;

public interface ParticleCollidorPolicy extends Serializable {
	public void collideParticleWithEnemies(Particle p, List<Enemy> enemies,
			float dt);
}
