package com.avona.games.towerdefence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Game {
	public List<Enemy> enemies = new LinkedList<Enemy>();
	public List<Tower> towers = new LinkedList<Tower>();
	public List<Particle> particles = new LinkedList<Particle>();
	public World world;

	public int killed = 0;
	public int escaped = 0;

	private Tower rangeShowingTower = null;

	public Game() {
		world = new World();
	}

	public void addTowerAt(V2 location) {
		towers.add(new Tower(location));
	}

	public void spawnEnemy() {
		enemies.add(new Enemy(world, new V2(world.waypoints.get(0))));
	}

	public void showTowersRange(Tower t) {
		if (t == rangeShowingTower)
			return;

		if (rangeShowingTower != null)
			rangeShowingTower.showRange = false;
		rangeShowingTower = t;
		if (rangeShowingTower != null)
			rangeShowingTower.showRange = true;
	}

	public Tower closestTowerWithinRadius(V2 location, float range) {
		return (Tower) closestStationaryWithinRadius(towers, location, range);
	}

	@SuppressWarnings("unchecked")
	// does not seem to be cast/type/...-able
	public static Object closestStationaryWithinRadius(final List objects,
			final V2 location, final float range) {
		for (final Object o : objects) {
			if (((StationaryObject) o).collidesWith(location, range))
				return o;
		}
		return null;
	}

	public void updateWorld(final float dt) {
		/**
		 * Step all objects first. This will cause them to move.
		 */
		for (Enemy e : enemies) {
			e.step(dt);
		}
		for (Tower t : towers) {
			t.step(dt);
		}

		Iterator<Particle> piter = particles.iterator();
		while (piter.hasNext()) {
			final Particle p = piter.next();
			p.step(dt);
			if (p.isDead()) {
				piter.remove();
			}
		}

		/**
		 * Look for each tower, if it's ready to shoot and if an enemy ready to
		 * shoot. If so, create a new particle. The tower is free to create any
		 * particle in its shootTowards() method.
		 */

		for (Tower t : towers) {
			Enemy bestEnemy = null;
			float bestEnemyLocationSquaredDist = Float.MAX_VALUE;

			for (Enemy e : enemies) {
				if (t.inRange(e)) {
					final float newEnemyLocationSquaredDist = t.location
							.squaredDist(e.location);

					// shoot to nearest enemy
					if (bestEnemy == null
							|| bestEnemyLocationSquaredDist > newEnemyLocationSquaredDist) {
						bestEnemy = e;
						bestEnemyLocationSquaredDist = newEnemyLocationSquaredDist;
					} // TODO allow for different policies here...
				}
			}

			if (bestEnemy != null) { // policy found some enemy
				final Particle p = t.shootTowards(bestEnemy);
				if (p != null) {
					particles.add(p);
				}
			}
		}

		/**
		 * Check for any particle collisions and handle damage.
		 */
		Iterator<Enemy> eiter = enemies.iterator();
		while (eiter.hasNext()) {
			final Enemy e = eiter.next();
			piter = particles.iterator();
			while (piter.hasNext()) {
				final Particle p = piter.next();

				if (p.collidedWith(e, dt)) {
					p.attack(e);
					if (e.isDead()) {
						killed += 1;
						eiter.remove();
						break; // enemy dead, no more particles to check
					}
				}

				if (p.isDead()) {
					piter.remove();
					continue; // particle exploded, dont use it any more
				}
			}

			final V2 w = world.waypoints.get(e.waypointId);
			if (Collision.movingCircleCollidesWithCircle(e.location, e.velocity
					.asVector(), e.radius, w, V2.ZERO, 1, dt)) {
				e.setWPID(e.waypointId + 1);
				if (e.escaped) {
					escaped += 1;
					eiter.remove();
					break;
				}
			}
		}
	}
}
