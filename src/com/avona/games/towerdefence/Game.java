package com.avona.games.towerdefence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;

public class Game {
	public List<Enemy> enemies = new LinkedList<Enemy>();
	public List<Tower> towers = new LinkedList<Tower>();
	public List<Particle> particles = new LinkedList<Particle>();
	public World world;
	public Mouse mouse;

	public Game() {
		world = new World();
		enemies.add(new Enemy(world, world.getInitialLocation()));
		towers.add(new Tower(new Point2d(0.5, 0.5)));
		towers.add(new Tower(new Point2d(0.3, 0.5)));

		mouse = new Mouse();
	}

	public void addTowerAt(Point2d location) {
		towers.add(new Tower(location));
	}

	public void addEnemyAt(Point2d location) {
		enemies.add(new Enemy(world, location));
	}

	public void updateWorld(final double dt) {
		/**
		 * Step all objects first. This will cause them to move.
		 */
		for (Enemy e : enemies) {
			e.step(dt);
		}
		for (Tower t : towers) {
			t.step(dt);
		}
		for (Particle p : particles) {
			p.step(dt);
		}

		/**
		 * Look for each tower, if it's ready to shoot and if an enemy ready to
		 * shoot. If so, create a new particle. The tower is free to create any
		 * particle in its shootTowards() method.
		 */
		for (Tower t : towers) {
			for (Enemy e : enemies) {
				if (t.inRange(e)) {
					Particle p = t.shootTowards(e, dt);
					if (p != null) {
						particles.add(p);
					}
				}
			}
		}

		/**
		 * Check for any particle collisions and handle damage.
		 */
		Iterator<Particle> piter = particles.iterator();
		while (piter.hasNext()) {
			final Particle p = piter.next();
			Iterator<Enemy> eiter = enemies.iterator();
			while (eiter.hasNext()) {
				final Enemy e = eiter.next();
				if (p.inRange(e)) {
					p.attack(e);
					if (e.isDead()) {
						eiter.remove();
					}
				}
			}
			if (p.isDead()) {
				piter.remove();
			}
		}
	}
}
