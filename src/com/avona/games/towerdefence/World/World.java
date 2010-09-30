package com.avona.games.towerdefence.World;

import java.io.Serializable;
import java.util.ArrayList;

import com.avona.games.towerdefence.V2;

public abstract class World implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;

	public World() {
		waypoints.clear();
	}

	public void addWaypoint(int x, int y) {
		waypoints.add(new V2(x, y));
	}

	public ArrayList<V2> waypoints = new ArrayList<V2>();
}
