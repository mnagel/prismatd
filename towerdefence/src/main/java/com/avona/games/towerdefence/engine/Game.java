package com.avona.games.towerdefence.engine;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyDeathGivesMoney;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyDeathUpdatesGameStats;
import com.avona.games.towerdefence.events.IEventListener;
import com.avona.games.towerdefence.mission.CellState;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionList;
import com.avona.games.towerdefence.mission.data._000_Empty_Mission;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.time.TimeTrack;
import com.avona.games.towerdefence.time.TimedCodeManager;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.transients.Transient;
import com.avona.games.towerdefence.transients.TransientText;
import com.avona.games.towerdefence.util.Util;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("UnnecessaryContinue")
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	public List<Enemy> enemies = new LinkedList<>();
	public List<Tower> towers = new LinkedList<>();
	public List<Particle> particles = new LinkedList<>();
	public List<Transient> transients = new LinkedList<>();
	public TimeTrack gameTime = new TimeTrack();
	public TimedCodeManager timedCodeManager = new TimedCodeManager();
	public IEventListener eventListener;
	public Mission mission;
	public int killed = 0;
	public int lives;
	public int money;
	public boolean isTerminated = false;
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
	private EnemyDeathGivesMoney enemyDeathGivesMoney = new EnemyDeathGivesMoney(this);
	private EnemyDeathUpdatesGameStats enemyDeathUpdatesGameStats = new EnemyDeathUpdatesGameStats(this);

	public Game(IEventListener eventListener) {
		this.eventListener = eventListener;
		//noinspection unchecked
		loadMission((Class) _000_Empty_Mission.class);
	}

	private void loadMission(Class<Mission> klass) {
		try {
			Constructor<Mission> ctor = klass.getConstructor(Game.class);
			this.mission = ctor.newInstance(this);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			throw new RuntimeException("died horribly in mission list hackery", e);
		}

		towers.clear();
		lives = mission.getStartLives();
		money = mission.getStartMoney();
		selectedBuildTower = mission.buildableTowers[0];

		eventListener.onMissionSwitched(mission);
	}

	public void loadMission(int missionIdx) {
		Util.log("loadMission");
		Class<Mission> klass = MissionList.availableMissions[missionIdx];
		loadMission(klass);
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

	private boolean isGameOver() {
		return lives == 0;
	}

	/**
	 * This method is for debugging purposes only.
	 */
	public void killAllEnemies() {
		for (final Enemy e : enemies) {
			e.kill();
		}
	}

	public boolean canBuildTowerAt(GridCell where) {
		return !isPaused()
				&& selectedBuildTower != null
				&& money >= selectedBuildTower.getPrice()
				&& where != null
				&& where.state == CellState.FREE;
	}

	public void addTowerAt(GridCell where) {
		addTowerAt(selectedBuildTower.clone2(), where);
	}

	public void addTowerAt(Tower newTower, GridCell where) {
		newTower.location = new V2(where.center);
		money -= newTower.getPrice();
		addTransient(new TransientText(
				String.format(Locale.US, "-$%d", newTower.getPrice()),
				1.5f,
				where.center,
				new RGB(1.0f, 1.0f, 1.0f),
				1.0f));

		where.state = CellState.TOWER;
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
				String.format(Locale.US, "-$%d", price),
				1.5f,
				t.location,
				new RGB(1.0f, 1.0f, 1.0f),
				1.0f));
	}

	public void onEnemySpawned(Enemy e) {
		e.eventListeners.add(enemyDeathGivesMoney);
		e.eventListeners.add(enemyDeathUpdatesGameStats);
		enemies.add(e);
	}

	private <T extends LocationObject> T getObjectWithinRange(final List<T> objects, final V2 location, final float range) {
		for (final T lo : objects) {
			if (lo.collidesWith(location, range))
				return lo;
		}
		return null;
	}

	public Tower getTowerWithinRadius(V2 location, float range) {
		return getObjectWithinRange(towers, location, range);
	}

	public Enemy getEnemyWithinRadius(V2 location, float range) {
		return getObjectWithinRange(enemies, location, range);
	}

	void pause() {
		gameTime.stopClock();
	}

	void unpause() {
		gameTime.startClock();
	}

	public boolean isPaused() {
		return !gameTime.isRunning();
	}

	void updateWorld(final float dt) {
		if (isPaused())
			return;

		gameTime.updateTick(dt);
		timedCodeManager.update(gameTime.tick);

		// Step all objects first. This will cause them to move.
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

		// Look for each tower, if it's ready to shoot and if an enemy ready to shoot.
		// If so, create a new particle. The tower is free to create any particle in its shootTowards() method.

		for (Tower t : towers) {
			Enemy enemy = t.findSuitableEnemy(enemies);
			if (enemy != null) { // policy found some enemy
				final Particle p = t.shootTowards(enemy);
				if (p != null) {
					particles.add(p);
				}
			}
		}

		// Handle enemy damage / deaths.
		Iterator<Enemy> eiter = enemies.iterator();
		while (eiter.hasNext()) {
			final Enemy e = eiter.next();
			if (e.isDead()) {
				eiter.remove();
				continue;
			}

			final V2 lastWp = mission.waypoints[e.waypointId - 1].center;
			final V2 currentWp = e.target;
			float overshoot = lastWp.dist(e.location) - lastWp.dist(currentWp);
			if (overshoot >= 0) {
				// Position exactly on waypoint for correct direction calculation
				e.location = currentWp.clone2();
				e.setWPID(e.waypointId + 1);
				if (e.escaped) {
					eiter.remove();
					continue;
				}

				// Compensate motion lost for overshoot towards next waypoint
				V2 move = new V2(e.target);
				move.sub(e.location);
				move.setLength(overshoot);
				e.location.add(move);
			}
		}
	}

	public void logDebugInfo() {
		StringBuilder sb = new StringBuilder();
		for (Enemy e : enemies) {
			sb.append(e.toString()).append("\n");
		}

		Util.log(sb.toString());
	}

	private void addTransient(Transient newT) {
		Iterator<Transient> it = transients.iterator();
		while (it.hasNext()) {
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
