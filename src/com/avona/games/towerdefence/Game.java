package com.avona.games.towerdefence;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.LimeLizard;
import com.avona.games.towerdefence.enemy.VioletViper;
import com.avona.games.towerdefence.enemyEventListeners.EnemyDeathGivesMoney;
import com.avona.games.towerdefence.enemyEventListeners.EnemyDeathUpdatesGameStats;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.tower.EmeraldPrismaTower;
import com.avona.games.towerdefence.tower.RubyPrismaTower;
import com.avona.games.towerdefence.tower.SapphirePrismaTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.waveListeners.WaveListener;
import com.avona.games.towerdefence.world.World;
import com.avona.games.towerdefence.world._010_Hello_World;
import com.avona.games.towerdefence.world._020_About_Colors;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	public List<Enemy> enemies = new LinkedList<Enemy>();
	public List<Tower> towers = new LinkedList<Tower>();
	public List<Particle> particles = new LinkedList<Particle>();
	public List<WaveListener> waveCompletedListeners = new LinkedList<WaveListener>();
	public List<WaveListener> waveBegunListeners = new LinkedList<WaveListener>();

	public TimeTrack gameTime;
	public TimedCodeManager timedCodeManager;

	public EventListener eventListener;

	public World world;

	/**
	 * Currently running wave.
	 */
	public Wave currentWave;

	public int killed = 0;
	public int lifes;
	
	public void looseLife() {
		this.lifes--;
		if (this.lifes <= 0) {
			gameOver();
		}
	}
	
	public void gameOver() {
		// FIXME add some game over logic here...
		Util.log("you should die now...");
	}

	public int money;

	/**
	 * Which type of tower to build - if any.
	 */
	public Tower selectedBuildTower = null;

	public boolean draggingTower = false;
	
	public void LoadLevel(World w) {
		this.world = w;
		
		this.world.initWaypoints();
		this.lifes = this.world.getStartLifes();
		this.money = this.world.getStartMoney();
		// FIXME use this call...
		// something = this.world.listBuildableTowers();
	}

	/**
	 * Currently selected, existing tower. We will typically show the properties
	 * of that tower.
	 */
	public LocationObject selectedObject = null;

	private EnemyDeathGivesMoney enemyDeathGivesMoney = new EnemyDeathGivesMoney(
			this);
	private EnemyDeathUpdatesGameStats enemyDeathUpdatesGameStats = new EnemyDeathUpdatesGameStats(
			this);

	public Game(TimeTrack gameTime, TimedCodeManager timedCodeManager,
			EventListener eventListener) {
		this.gameTime = gameTime;
		this.timedCodeManager = timedCodeManager;
		this.eventListener = eventListener;
		
		World[] levels = new World[] {
				new _010_Hello_World(), 
				new _020_About_Colors()
		};
		
		LoadLevel(levels[rand.nextInt(levels.length)]);

		selectedBuildTower = new EmeraldPrismaTower(timedCodeManager, 1);
	}

	public boolean canBuildTowerAt(V2 location) {
		return money >= selectedBuildTower.price;
	}

	public void addTowerAt(V2 location) {
		Tower newTower = selectedBuildTower.copy();
		newTower.location = new V2(location);
		money -= newTower.price;
		towers.add(newTower);
		eventListener.onBuildTower(newTower);

		// While we have no true tower selection, pick a new tower by random.
		final int val = rand.nextInt(3);
		if (val == 1) {
			selectedBuildTower = new EmeraldPrismaTower(timedCodeManager, 1);
		} else if (val == 2) {
			selectedBuildTower = new RubyPrismaTower(timedCodeManager, 1);
		} else {
			selectedBuildTower = new SapphirePrismaTower(timedCodeManager, 1);
		}
	}

	public void startWave() {
		int level = 1;
		
		if (currentWave != null) {
			if (!currentWave.isCompleted()) {
				return; // one wave at a time
			}
		}
		
		if (currentWave != null) {
			level = currentWave.getLevel() + 1;
		}
		
		// generate new wave
		this.currentWave = this.world.sendWave(level, this);
		
		// trigger events
		for (WaveListener l : waveBegunListeners) {
			l.onWave(level);
		}
	}

	public void onWaveCompleted(int level) {
		// world-specific handlers
		this.world.onWaveCompleted(level);
		
		// game-specific handlers
		for (WaveListener l : waveCompletedListeners) {
			l.onWave(level);
		}
	}

	static Random rand = new Random();

	public void spawnEnemy(int level) {
		final V2 location = world.waypoints.get(0).copy();
		Enemy e;
		if (rand.nextBoolean()) {
			e = new LimeLizard(world, location, level);
		} else {
			e = new VioletViper(world, location, level);
		}
		e.eventListeners.add(enemyDeathGivesMoney);
		e.eventListeners.add(enemyDeathUpdatesGameStats);
		enemies.add(e);
	}

	public Tower closestTowerWithinRadius(V2 location, float range) {
		return (Tower) closestStationaryWithinRadius(towers, location, range);
	}

	public Enemy closestEnemyWithinRadius(V2 location, float range) {
		return (Enemy) closestStationaryWithinRadius(enemies, location, range);
	}

	@SuppressWarnings("unchecked")
	// does not seem to be cast/type/...-able
	public static Object closestStationaryWithinRadius(final List objects,
			final V2 location, final float range) {
		for (final Object o : objects) {
			if (((LocationObject) o).collidesWith(location, range))
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

			// Advance particle.
			p.step(dt);
			if (p.isDead()) {
				piter.remove();
				continue;
			}

			// Collide particle with enemies.
			p.collideWithEnemies(enemies, dt);
			if (p.isDead()) {
				piter.remove();
				continue;
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
		 * Handle enemy damage / deaths.
		 */
		Iterator<Enemy> eiter = enemies.iterator();
		while (eiter.hasNext()) {
			final Enemy e = eiter.next();
			if (e.isDead()) {
				eiter.remove();
				continue;
			}

			final V2 w = world.waypoints.get(e.waypointId);
			if (Collision.movingCircleCollidedWithCircle(e.location,
					e.velocity, e.radius, w, V2.ZERO, 1, dt)) {
				e.setWPID(e.waypointId + 1);
				if (e.escaped) {
					eiter.remove();
					continue;
				}
			}
		}
	}
}
