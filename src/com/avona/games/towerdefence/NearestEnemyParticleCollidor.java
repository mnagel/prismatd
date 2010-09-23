package com.avona.games.towerdefence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NearestEnemyParticleCollidor implements EnemyParticleCollidor {

	/**
	 * List of particles that, while targeting a specific enemy, will damage any
	 * enemy on its way.
	 */
	private List<Particle> particles = new LinkedList<Particle>();

	@Override
	public void collideParticlesWithEnemies(final List<Enemy> enemies,
			final float dt) {
		Iterator<Enemy> eiter = enemies.iterator();
		nextEnemy: while (eiter.hasNext()) {
			final Enemy e = eiter.next();
			if (e.isDead()) {
				eiter.remove();
				continue;
			}

			Iterator<Particle> piter = particles.iterator();
			while (piter.hasNext()) {
				final Particle p = piter.next();
	
				if (p.isDead()) {
					// Particle went away some time, remove from our list too.
					piter.remove();
					continue;
				}

				if (p.collidedWith(e, dt)) {
					p.attack(e);
					if (e.isDead()) {
						eiter.remove();
						continue nextEnemy;
					}
	
					if (p.isDead()) {
						// Particle exploded, don't use it any more.
						piter.remove();
						continue;
					}
				}
			}
		}
	}

	@Override
	public void registerEnemyVsParticle(final Enemy e, final Particle p) {
		particles.add(p);
	}
}
