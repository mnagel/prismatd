package com.avona.games.towerdefence;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class World {
	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;

	public static Random rand = new java.util.Random();
	public V2 initLocation = new V2(300, 300);

	public VelocityVector getRandomDirection(V2 location) {
		float x = rand.nextFloat() - 0.5f;
		float y = rand.nextFloat() - 0.5f;
		V2 dir = new V2(x, y);
		dir.normalize();
		float s = rand.nextFloat() * 75;
		return new VelocityVector(dir, s);
	}

	public V2 getInitialLocation() {
		return initLocation;
	}

	public World() {
		waypoints = new LinkedList<V2>();
		addwp(40, 40);
		addwp(300, 300);
		addwp(300, 100);
		addwp(600, 400);
		addwp(0, 350);
		addwp(100, 400);
		addwp(0, 480);
	}

	private void addwp(int x, int y) {
		waypoints.add(new V2(x, y));
	}

	public List<V2> waypoints;
}
