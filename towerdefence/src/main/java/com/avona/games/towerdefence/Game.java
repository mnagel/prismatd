package com.avona.games.towerdefence;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemyEventListeners.EnemyDeathGivesMoney;
import com.avona.games.towerdefence.enemyEventListeners.EnemyDeathUpdatesGameStats;
import com.avona.games.towerdefence.level.Level;
import com.avona.games.towerdefence.level._010_Hello_World;
import com.avona.games.towerdefence.level._020_About_Colors;
import com.avona.games.towerdefence.level._030_Mixing_Colors;
import com.avona.games.towerdefence.level._100_Grass;
import com.avona.games.towerdefence.level._101_Space;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.tower.Tower;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	public List<Enemy> enemies = new LinkedList<Enemy>();
	public List<Tower> towers = new LinkedList<Tower>();
	public List<Particle> particles = new LinkedList<Particle>();
	public List<Transient> transients = new LinkedList<Transient>();

	public TimeTrack gameTime = new TimeTrack();
	public TimedCodeManager timedCodeManager = new TimedCodeManager();

	public EventListener eventListener;

	public Level[] levels;
	public int curLevelIdx;
	public Level level;

	public int killed = 0;
	public int lives;

	public int money;

	/**
	 * Which type of tower to build - if any.
	 */
	public Tower selectedBuildTower = null;

	public boolean draggingTower = false;

	/**
	 * Currently selected, existing tower. We will typically show the properties
	 * of that tower.
	 */
	public LocationObject selectedObject = null;

	private EnemyDeathGivesMoney enemyDeathGivesMoney = new EnemyDeathGivesMoney(
			this);
	private EnemyDeathUpdatesGameStats enemyDeathUpdatesGameStats = new EnemyDeathUpdatesGameStats(
			this);

	public Game(EventListener eventListener) {
		this.eventListener = eventListener;

		levels = new Level[] {
				new _010_Hello_World(this),
				new _020_About_Colors(this), 
				new _030_Mixing_Colors(this), 
				new _100_Grass(this),
				new _101_Space(this)
		};
		loadLevel(0);
	}

	public void loadLevel(int levelIdx) {
		curLevelIdx = levelIdx;
		level = levels[curLevelIdx];

		towers.clear();
		lives = level.getStartLives();
		money = level.getStartMoney();
		selectedBuildTower = level.buildableTowers[0];

		eventListener.onLevelSwitched(level);
	}

	public boolean isLastLevel() {
		return curLevelIdx + 1 == levels.length;
	}

	public void loadNextLevel() {
		if (!isLastLevel()) {
			loadLevel(curLevelIdx + 1);
		} else {
			eventListener.onGameCompleted(this);
		}
	}

	public void looseLife() {
		if (lives <= 0) {
			return;
		}
		--lives;
		if (isGameOver()) {
			// FIXME add some game over logic here...
			Util.log("you should die now...");
			eventListener.onGameOver(this);
		}
	}

	public boolean isGameOver() {
		return lives == 0;
	}

	public void pressForwardButton() {
		if (level.completed) {
			loadNextLevel();
		} else if (level.showOverlay) {
			level.showOverlay = false;
		} else {
			level.waveTracker.startNextWave();
		}
	}

	/**
	 * This method is for debugging purposes only.
	 */
	public void killAllEnemies() {
		for (final Enemy e : enemies) {
			e.kill();
		}
	}

	public boolean canBuildTowerAt(V2 location) {
		return !isPaused()
				&& selectedBuildTower != null
				&& money >= selectedBuildTower.getPrice()
				&& getTowerWithinRadius(location, selectedBuildTower.radius) == null;
	}

	public void addTowerAt(V2 location) {
		// FIXME place this somewhere sensible
		if (level.showOverlay == true) {
			level.showOverlay = false;
			return;
		}
		
		Tower newTower = selectedBuildTower.clone();
		newTower.location = new V2(location);
		money -= newTower.getPrice();
		addTransient(new TransientText(
				String.format("-$%d", newTower.getPrice()),
				1.5f,
				location,
				new RGB(1.0f, 1.0f, 1.0f),
				1.0f));

		towers.add(newTower);
		eventListener.onBuildTower(newTower);
	}

	public void levelUpTower(Tower t) {
		final int price = t.getLevelUpPrice();
		if (money < price)
			return;
		money -= price;
		t.setLevel(t.level + 1);
		addTransient(new TransientText(
				String.format("-$%d", price),
				1.5f,
				t.location,
				new RGB(1.0f, 1.0f, 1.0f),
				1.0f));
	}

	static Random rand = new Random();

	public void onEnemySpawned(Enemy e) {
		e.eventListeners.add(enemyDeathGivesMoney);
		e.eventListeners.add(enemyDeathUpdatesGameStats);
		enemies.add(e);
	}

	public Tower getTowerWithinRadius(V2 location, float range) {
		return (Tower) getLocationWithinRadius(towers, location, range);
	}

	public Enemy getEnemyWithinRadius(V2 location, float range) {
		return (Enemy) getLocationWithinRadius(enemies, location, range);
	}

	@SuppressWarnings("unchecked")
	public static Object getLocationWithinRadius(final List objects,
			final V2 location, final float range) {
		final List<LocationObject> locationObjects = (List<LocationObject>) objects;
		for (final LocationObject lo : locationObjects) {
			if (lo.collidesWith(location, range))
				return lo;
		}
		return null;
	}

	public void pause() {
		gameTime.stopClock();
	}

	public void unpause() {
		gameTime.startClock();
	}

	public boolean isPaused() {
		return !gameTime.isRunning();
	}

	public void updateWorld(final float dt) {
		if (isPaused())
			return;

		gameTime.updateTick(dt);
		timedCodeManager.update(gameTime.tick);

		/**
		 * Step all objects first. This will cause them to move.
		 */
		for (Enemy e : enemies) {
			e.step(dt);
		}
		for (Tower t : towers) {
			t.step(dt);
		}
		for (Transient t : transients) {
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

			final V2 w = level.waypoints[e.waypointId].center;
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

	public void logDebugInfo() {
		StringBuilder sb = new StringBuilder();
		for (Enemy e: enemies) {
			sb.append(e.toString()).append("\n");
		}

		Util.log(sb.toString());
	}

	public void addTransient(Transient newT) {
		Iterator<Transient> it = transients.iterator();
		while(it.hasNext()) {
			Transient t = it.next();
			if (t.isDead()) {
				it.remove();
				continue;
			}
			if (t.getLocation().equals(newT.getLocation())) {
				it.remove();
				continue;
			}
		}
		transients.add(newT);
	}
}
