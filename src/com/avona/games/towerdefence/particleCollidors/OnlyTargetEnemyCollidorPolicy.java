package com.avona.games.towerdefence.particleCollidors;

import java.util.List;

import com.avona.games.towerdefence.Particle.Particle;
import com.avona.games.towerdefence.enemy.Enemy;

public class OnlyTargetEnemyCollidorPolicy implements ParticleCollidorPolicy {
	private static final long serialVersionUID = 1L;

	@Override
	public void collideParticleWithEnemies(final Particle p,
			final List<Enemy> enemies, final float dt) {
		final Enemy e = p.target;
		if (p.collidedWith(e, dt)) {
			p.attack(e);
		}
	}
}
