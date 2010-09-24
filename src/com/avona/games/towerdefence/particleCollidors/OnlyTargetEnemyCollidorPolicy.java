package com.avona.games.towerdefence.particleCollidors;

import java.util.List;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Particle;

public class OnlyTargetEnemyCollidorPolicy implements ParticleCollidorPolicy {

	@Override
	public void collideParticleWithEnemies(final Particle p,
			final List<Enemy> enemies, final float dt) {
		final Enemy e = p.target;
		if (p.collidedWith(e, dt)) {
			p.attack(e);
		}
	}
}
