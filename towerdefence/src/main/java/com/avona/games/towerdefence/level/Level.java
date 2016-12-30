package com.avona.games.towerdefence.level;

import java.io.Serializable;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Util;
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
	public String overlayBackgroundName;

	public boolean showOverlay = true;

	public String levelName;

	public final float WAYPOINT_WIDTH = 4.0f;

	public final V2[] waypoints;
	public final Tower[] buildableTowers;
	private final WaveEnemyConfig[][] enemyWaves;

	public WaveTracker waveTracker = new WaveTracker(this);
	public boolean completed = false;

	protected Game game;

	public Level(final Game game) {
		this.game = game;
		this.gameBackgroundName = getGameBackgroundName();
		this.menuBackgroundName = getMenuBackgroundName();
		this.overlayBackgroundName = getOverlayBackgroundName();
		this.levelName = getLevelName();
		this.waypoints = loadWaypoints();
		this.enemyWaves = loadEnemyWaves();
		this.buildableTowers = loadBuildableTowers();

		// very sad to have this here, but loadBuildableTowers() is user-defined per level :(
		for (int i = 0; i < buildableTowers.length; i++) {
			V2 menuPos = new V2(1, i);
			Tower t = buildableTowers[i];
			t.location = menuPos;
		}
	}

	@Override
	public Wave sendWave(final int waveNum) {
		if (waveNum >= enemyWaves.length)
			return null;

		return new Wave(waveNum, game, this, game.timedCodeManager,
				enemyWaves[waveNum]);
	}

	@Override
	public void onAllWavesCompleted() {
		Util.log("All waves completed -> level completed");
		completed = true;
		game.eventListener.onLevelCompleted(this);
	}

	@Override
	public int getNumWaves() {
		return enemyWaves.length;
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

	protected abstract String getLevelName();

	protected abstract String getGameBackgroundName();

	protected abstract String getMenuBackgroundName();

	protected abstract String getOverlayBackgroundName();

	/**
	 * @return The amount of money the player starts with.
	 */
	public abstract int getStartMoney();

	/**
	 * @return The number of lives the player starts with.
	 */
	public abstract int getStartLives();
}
