package com.avona.games.towerdefence;

import java.util.List;

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
