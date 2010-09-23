package com.avona.games.towerdefence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OnlyTargetEnemyParticleCollidor implements EnemyParticleCollidor {

	/**
	 * List of particles that target a specific enemy and will only damage that
	 * particular enemy.
	 */
	private List<Particle> particles = new LinkedList<Particle>();

	@Override
	public void collideParticlesWithEnemies(final List<Enemy> enemies,
			final float dt) {
		Iterator<Particle> piter = particles.iterator();
		while (piter.hasNext()) {
			final Particle p = piter.next();

			if (p.isDead()) {
				// Particle went away some time, remove from our list too.
				piter.remove();
				continue;
			}

			final Enemy e = p.target;
			if (p.collidedWith(e, dt)) {
				p.attack(e);
				if (p.isDead()) {
					// Particle exploded, don't use it any more.
					piter.remove();
					continue;
				}
			}
		}
	}

	@Override
	public void registerEnemyVsParticle(final Enemy e, final Particle p) {
		particles.add(p);
	}
}
