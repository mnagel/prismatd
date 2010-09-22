package com.avona.games.towerdefence;

import java.util.List;

public interface EnemyParticleCollisionSelector {
	public boolean collideWithParticles(Enemy e, List<Particle> particles,
			float dt);
}
