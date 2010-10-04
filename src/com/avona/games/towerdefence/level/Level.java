package com.avona.games.towerdefence.level;

import java.io.Serializable;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.Wave;
import com.avona.games.towerdefence.WaveEnemyConfig;
import com.avona.games.towerdefence.WaveSender;
import com.avona.games.towerdefence.WaveTracker;
import com.avona.games.towerdefence.tower.Tower;

public abstract class Level implements Serializable, WaveSender {
	private static final long serialVersionUID = 1L;

	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;

	public String gameBackgroundName;
	public String menuBackgroundName;

	public final float WAYPOINT_WIDTH = 4.0f;

	public final V2[] waypoints;
	public final Tower[] buildableTowers; 
	private final WaveEnemyConfig[][] enemyWaves;

	public WaveTracker waveTracker = new WaveTracker(this);

	protected Game game;

	public Level(final Game game) {
		this.game = game;
		this.waypoints = loadWaypoints();
		this.enemyWaves = loadEnemyWaves();
		this.buildableTowers = loadBuildableTowers();
	}

	@Override
	public Wave sendWave(final int wave) {
		if (wave < enemyWaves.length) {
			return new Wave(game, this, game.timedCodeManager, enemyWaves[wave]);
		} else {
			onFinishedLastWave();
			return null;
		}
	}
	protected void onFinishedLastWave() {
		// TODO inform listeners about "Level ended" event.
	}

	/**
	 * @return The amount of money the player starts with.
	 */
	protected abstract V2[] loadWaypoints();

	protected abstract WaveEnemyConfig[][] loadEnemyWaves();

	/**
	 * @return A list of towers that can be built in this level.
	 */
	protected abstract Tower[] loadBuildableTowers();

	/**
	 * @return The amount of money the player starts with.
	 */
	public abstract int getStartMoney();

	/**
	 * @return The number of lives the player starts with.
	 */
	public abstract int getStartLives();
}
