package com.avona.games.towerdefence;

import java.util.List;

public interface ParticleCollidorPolicy {
	public void collideParticleWithEnemies(Particle p, List<Enemy> enemies,
			float dt);
}
