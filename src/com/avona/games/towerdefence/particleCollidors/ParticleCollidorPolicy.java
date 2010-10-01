package com.avona.games.towerdefence.particleCollidors;

import java.io.Serializable;
import java.util.List;

import com.avona.games.towerdefence.Enemy.Enemy;
import com.avona.games.towerdefence.Particle.Particle;

public interface ParticleCollidorPolicy extends Serializable {
	public void collideParticleWithEnemies(Particle p, List<Enemy> enemies,
			float dt);
}
