package com.avona.games.towerdefence.engine;

import com.avona.games.towerdefence.balancing.BalancingHelper;
import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyDeathGivesMoney;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyDeathUpdatesGameStats;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyEventListener;
import com.avona.games.towerdefence.events.EventDistributor;
import com.avona.games.towerdefence.mission.*;
import com.avona.games.towerdefence.mission.data._000_Empty_Mission;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.time.TimeTrack;
import com.avona.games.towerdefence.time.TimedCode;
import com.avona.games.towerdefence.time.TimedCodeManager;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.transients.Transient;
import com.avona.games.towerdefence.transients.TransientText;
import com.avona.games.towerdefence.util.Util;
import com.avona.games.towerdefence.wave.Wave;
import com.avona.games.towerdefence.wave.WaveTracker;
import com.avona.games.towerdefence.wave.waveListeners.WaveListener;
import com.google.common.base.Joiner;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("UnnecessaryContinue")
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	public List<Enemy> enemies = new LinkedList<>();
	public List<Tower> towers = new LinkedList<>();
	public List<Particle> particles = new LinkedList<>();
	public List<Transient> transients = new LinkedList<>();
	public TimeTrack gameTime = new TimeTrack();
	public TimedCodeManager timedCodeManager = new TimedCodeManager();
	public EventDistributor eventDistributor;
	public Mission mission;
	public MissionStatus missionStatus = MissionStatus.ALLOCATED;

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
	public WaveTracker waveTracker = new WaveTracker(this);
	private List<EnemyEventListener> enemyEventListeners = new ArrayList<>();

	public Game(EventDistributor eventDistributor) {
		this.eventDistributor = eventDistributor;
		loadMission(_000_Empty_Mission.class);


		enemyEventListeners.add(new EnemyDeathGivesMoney(this));
		enemyEventListeners.add(new EnemyDeathUpdatesGameStats(this));
		enemyEventListeners.add(new EnemyEventListener() {
			@Override
			public void onHurtEvent(Enemy e, Particle cause) {
				addTransient(
						new TransientText(
								"*",
								0.5f,
								cause.location,
								cause.strength,
								1.0f
						)
				);
			}

			@Override
			public void onDeathEvent(Enemy e) {
				addTransient(
						new TransientText(
								"x",
								0.5f,
								e.location,
								e.maxLife,
								1.0f
						)
				);
			}

			@Override
			public void onEscapeEvent(Enemy e) {
				addTransient(
						new TransientText(
								"o",
								0.5f,
								e.location,
								e.maxLife,
								1.0f
						)
				);
			}
		});
	}

	public Wave sendWave(final int waveNumber) {
		if (waveNumber >= mission.enemyWaves.length) {
			return null;
		}

		return new Wave(this, waveNumber, mission.enemyWaves[waveNumber]);
	}

	public void onAllWavesCompleted() {
		missionStatus = MissionStatus.WON;
		eventDistributor.onMissionCompleted(this.mission);
	}

	private void loadMission(Class<? extends Mission> klass) {
		Util.log("loadMission: " + klass.getSimpleName());
		try {
			Constructor<? extends Mission> ctor = klass.getConstructor();
			mission = ctor.newInstance();
			missionStatus = MissionStatus.ACTIVE;
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			throw new RuntimeException("died horribly in mission list hackery", e);
		}

		towers.clear();
		enemies.clear();
		particles.clear();
		transients.clear();
		timedCodeManager.clear();

		lives = mission.getStartLives();
		money = mission.getStartMoney();

		selectedBuildTower = null;
		selectedObject = null;

		waveTracker = new WaveTracker(this);
		// printStats WaveListener
		waveTracker.listeners.add(new WaveListener() {
			@Override
			public void onWaveBegun(Wave wave) {

			}

			@Override
			public void onWaveFullyDeployed(Wave wave) {

			}

			@Override
			public void onWaveKilled(Wave wave) {
				Util.log(String.format(
						Locale.US,
						"STATS: %s killed.\t$%d\tlives:%d\tfleet:%s",
						wave, Game.this.money, Game.this.lives, new BalancingHelper().rateFleet(Game.this.towers)
						)
				);
			}
		});

		eventDistributor.onMissionSwitched(mission);

		if (mission instanceof GameManipulatingMission) {
			((GameManipulatingMission) mission).manipulateGame(this);
		}

		unpause();
	}

	public void loadMission(int missionIdx) {
		Class<Mission> klass = MissionList.availableMissions[missionIdx];
		loadMission(klass);
	}

	public void reloadMission() {
		loadMission(this.mission.getClass());
	}

	public void looseLife() {
		if (lives <= 0) {
			return;
		}
		--lives;
		if (lives <= 0) {
			pause();
			missionStatus = MissionStatus.LOST;
			eventDistributor.onGameOver(this);

			Util.log(String.format(
					Locale.US,
					"STATS: game lost. \t$%d\tlives:%d\tfleet:%s",
					Game.this.money, Game.this.lives, new BalancingHelper().rateFleet(Game.this.towers)
					)
			);
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

	public boolean canBuildTowerAt(GridCell where) {
		return !isPaused()
				&& selectedBuildTower != null
				&& money >= selectedBuildTower.getPrice()
				&& where != null
				&& where.state == CellState.FREE;
	}

	public void addTowerAt(GridCell where) {
		Tower t = selectedBuildTower.clone2();
		addTowerAt(t, where);
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
		eventDistributor.onBuildTower(newTower);
	}

	public void sellTower(Tower towerToSell) {
		GridCell where = mission.getCellAt(towerToSell.location);
		int cashBack = towerToSell.getSellPrice();
		money += cashBack;
		addTransient(new TransientText(
				String.format(Locale.US, "+$%d", cashBack),
				1.5f,
				where.center,
				new RGB(1.0f, 1.0f, 1.0f),
				1.0f));

		where.state = CellState.FREE;
		towers.remove(towerToSell);

		// TODO: pending re-arms in timedCodeManager really should be removed
		// but this requires to "tag" the inserted codes to be able to remove them later...
		// and the re-arm alone does not corrupt any game state

		selectedObject = null;
		eventDistributor.onMenuRebuild();
	}

	public void levelUpTower(Tower t) {
		final int price = t.getUpgradePrice();
		if (!t.canUpgrade() || money < price) {
			return;
		}
		money -= price;
		t.upgrade();
		eventDistributor.onMenuRebuild(); // button might need to be disabled
		addTransient(new TransientText(
				String.format(Locale.US, "-$%d", price),
				1.5f,
				t.location,
				new RGB(1.0f, 1.0f, 1.0f),
				1.0f));
	}

	public void onEnemySpawned(Enemy e) {
		e.eventListeners.addAll(enemyEventListeners);
		enemies.add(e);
	}

	private <T extends LocationObject> T getObjectWithinRange(final List<T> objects, final V2 location, final float range) {
		for (final T lo : objects) {
			if (lo.collidesWith(location, range)) {
				return lo;
			}
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

	boolean isPaused() {
		return !gameTime.isRunning();
	}

	void updateWorld(final float dt) {
		if (isPaused()) {
			return;
		}

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
			final Tower tf = t;
			if (t.isReadyToShoot) {
				Enemy enemy = t.findSuitableEnemy(enemies);
				if (enemy != null) { // policy found some enemy
					Particle p = t.shootTowards(enemy);
					particles.add(p);
					t.isReadyToShoot = false;
					timedCodeManager.addCode(new TimedCode() {
						@Override
						public double getDelay() {
							return tf.getReloadTime();
						}

						@Override
						public void run() {
							tf.isReadyToShoot = true;
						}
					});
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

				if (e.waypointId + 1 < mission.waypoints.length) {
					V2 wp = mission.waypoints[e.waypointId + 1].center;
					e.setTargetWaypoint(e.waypointId + 1, wp);
				} else {
					e.escape();
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
		String s = Joiner.on("\n").join(enemies);
		Util.log(s);
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
