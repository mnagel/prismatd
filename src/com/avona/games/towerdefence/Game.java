package com.avona.games.towerdefence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Game {
	public List<Enemy> enemies = new LinkedList<Enemy>();
	public List<Tower> towers = new LinkedList<Tower>();
	public List<Particle> particles = new LinkedList<Particle>();

	public TimedCodeManager gameTime;

	public World world;

	public int waveCount = 0;

	public int killed = 0;
	public int escaped = 0;

	public int money = 250;

	/**
	 * Debugging value that counts the number of enemies that have left the game
	 * area.
	 */
	public int leftBuilding = 0;

	/**
	 * Which type of tower to build - if any.
	 */
	public Tower selectedBuildTower = null;

	/**
	 * Currently selected, existing tower. We will typically show the properties
	 * of that tower.
	 */
	public Tower selectedExistingTower = null;

	public Game(TimedCodeManager gameTime) {
		this.gameTime = gameTime;
		world = new World();

		selectedBuildTower = new Tower(1);
	}

	public boolean canBuildTowerAt(V2 location) {
		return money >= selectedBuildTower.price;
	}

	public void addTowerAt(V2 location) {
		Tower t = selectedBuildTower.copy();
		t.location = new V2(location);
		money -= t.price;
		towers.add(t);
	}

	public void spawnWave(int waveCount) {
		this.waveCount++; // TODO keep state somewhere else
		final Game self = this;
		final int wc = this.waveCount;

		for (int i = 0; i < 2 * this.waveCount + 2; i++) {
			TimedCode tc = new TimedCode() {

				@Override
				public void execute() {
					self.spawnEnemy(wc);
				}
			};

			gameTime.addCode(0.4f * i, tc);
		}
	}

	public void spawnEnemy(int level) {
		enemies.add(new Enemy(world, new V2(world.waypoints.get(0)), level,
				this));
	}

	public void showTowerDetails(Tower t) {
		selectedExistingTower = t;
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
			Enemy enemy = t.findSuitableEnemy(enemies);
			if (enemy != null) { // policy found some enemy
				final Particle p = t.shootTowards(enemy);
				if (p != null) {
					particles.add(p);
				}
			}
		}

		/**
		 * Check for any particle collisions and handle damage.
		 */
		Iterator<Enemy> eiter = enemies.iterator();
		nextEnemy: while (eiter.hasNext()) {
			final Enemy e = eiter.next();
			piter = particles.iterator();
			while (piter.hasNext()) {
				final Particle p = piter.next();

				if (p.collidedWith(e, dt)) {
					p.attack(e);
					if (e.isDead()) {
						killed += 1;
						eiter.remove();
						continue nextEnemy; // enemy dead, no more particles to
											// check
					}
				}

				if (p.isDead()) {
					piter.remove();
					continue; // particle exploded, dont use it any more
				}
			}

			final V2 w = world.waypoints.get(e.waypointId);
			if (Collision.movingCircleCollidedWithCircle(e.location,
					e.velocity, e.radius, w, V2.ZERO, 1, dt)) {
				e.setWPID(e.waypointId + 1);
				if (e.escaped) {
					escaped += 1;
					eiter.remove();
					continue;
				}
			}

			if (e.location.x < World.ORIGIN_X || e.location.y < World.ORIGIN_Y
					|| e.location.x > World.WIDTH
					|| e.location.y > World.HEIGHT) {
				if (!e.left) {
					e.left = true;
					leftBuilding += 1;
					Util.log("enemy " + e + " has left the building");
				}
			} else if (e.left) {
				e.left = false;
				leftBuilding -= 1;
				Util.log("enemy " + e + " has reentered the building!");
			}
		}
	}
}
