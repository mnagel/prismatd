package com.avona.games.towerdefence.level;

import java.io.Serializable;
import java.util.ArrayList;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.WaveSender;
import com.avona.games.towerdefence.WaveTracker;

public abstract class Level implements Serializable, WaveSender {
	private static final long serialVersionUID = 1L;

	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;

	protected Game game;

	public String gameBackgroundName;
	public String menuBackgroundName;

	public float WAYPOINT_WIDTH = 4.0f;

	public final ArrayList<V2> waypoints = new ArrayList<V2>();

	public WaveTracker waveTracker = new WaveTracker(this);

	public Level(final Game game) {
		this.game = game;
	}

	protected void addWaypoint(final int x, final int y) {
		waypoints.add(new V2(x, y));
	}

	/**
	 * gets called on construction -- needs to add waypoints to the list of
	 * waypoints
	 */
	public abstract void initWaypoints();

	/**
	 * @return The amount of money the player starts with.
	 */
	public abstract int getStartMoney();

	/**
	 * @return The number of lives the player starts with.
	 */
	public abstract int getStartLives();

	/**
	 * @return A list of towers that can be built in this level.
	 */
	public abstract Object listBuildableTowers();
}
