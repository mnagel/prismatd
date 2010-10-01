package com.avona.games.towerdefence.World;

import java.io.Serializable;
import java.util.ArrayList;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.Wave;

public abstract class World implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;
	
	public ArrayList<V2> waypoints = new ArrayList<V2>();

	public World() {
		waypoints.clear();
	}

	protected void addWaypoint(int x, int y) {
		waypoints.add(new V2(x, y));
	}
	
	// gets called on construction -- needs to add waypoints to the list of waypoints
	public abstract void initWaypoints();
	// should return the amount of money the player starts with
	public abstract int getStartMoney();
	// should return the number of of lifes the player starts with
	public abstract int getStartLifes();
	
	// FIXME should return a list of towers that can be build in this level
	public abstract Object listBuildableTowers();
	
	// gets called when a new wave should be spawned
	public abstract Wave sendWave(int wave, Game g);
	// allows to execute some code when a wave completes
	public abstract void onWaveCompleted(int wave);
}
