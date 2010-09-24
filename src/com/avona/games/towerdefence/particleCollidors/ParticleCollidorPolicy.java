package com.avona.games.towerdefence.particleCollidors;

import java.util.List;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Particle;

public interface ParticleCollidorPolicy {
	public void collideParticleWithEnemies(Particle p, List<Enemy> enemies,
			float dt);
}
