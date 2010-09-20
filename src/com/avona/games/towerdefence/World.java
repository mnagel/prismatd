package com.avona.games.towerdefence;

import java.util.ArrayList;

public class World {
	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;

	public World() {
		waypoints.clear();
		addwp(30, 480);
		addwp(30, 400);
		addwp(600, 400);
		addwp(600, 300);
		addwp(500, 300);
		addwp(500, 350);
		addwp(30, 350);
		addwp(30, 200);
		addwp(300, 200);
		addwp(300, 0);
	}

	private void addwp(int x, int y) {
		waypoints.add(new V2(x, y));
	}

	public ArrayList<V2> waypoints = new ArrayList<V2>();
}
