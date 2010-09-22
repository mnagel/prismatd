package com.avona.games.towerdefence;

import java.util.Iterator;
import java.util.List;

public class OnlyTargetEnemyParticleCollidor implements
		EnemyParticleCollisionSelector {

	@Override
	public boolean collideWithParticles(Enemy e,
			final List<Particle> particles, final float dt) {
		Iterator<Particle> piter = particles.iterator();
		while (piter.hasNext()) {
			final Particle p = piter.next();
			
			if (p.target != e) {
				continue;
			}

			if (p.collidedWith(e, dt)) {
				p.attack(e);
				if (e.isDead()) {
					return true;
				}

				if (p.isDead()) {
					piter.remove();
					continue; // particle exploded, don't use it any more
				}
			}
		}
		return false;
	}
}
