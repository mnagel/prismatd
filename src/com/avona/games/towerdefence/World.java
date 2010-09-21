package com.avona.games.towerdefence;

import java.util.ArrayList;

public class World {
	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;

	public World() {
		waypoints.clear();
		addWaypoint(30, 480);
		addWaypoint(30, 400);
		addWaypoint(600, 400);
		addWaypoint(600, 300);
		addWaypoint(500, 300);
		addWaypoint(500, 350);
		addWaypoint(30, 350);
		addWaypoint(30, 200);
		addWaypoint(300, 200);
		addWaypoint(300, 0);
	}

	public void addWaypoint(int x, int y) {
		waypoints.add(new V2(x, y));
	}

	public ArrayList<V2> waypoints = new ArrayList<V2>();
}
