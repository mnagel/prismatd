package com.avona.games.towerdefence;

import javax.vecmath.Point2d;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.vecmath.Vector2d;
import java.util.Random;

public class World {
	public final static double ORIGIN_X = 0;
	public final static double WIDTH = 675;
	public final static double ORIGIN_Y = 0;
	public final static double HEIGHT = 480;

	public static Random rand = new java.util.Random();
	public Point2d initLocation = new Point2d(300, 300);
	
	public VelocityVector getRandomDirection(Point2d location) {
		double x = rand.nextDouble() - 0.5;
		double y = rand.nextDouble() - 0.5;
		Vector2d dir = new Vector2d(x, y);
		dir.normalize();
		double s = rand.nextDouble() * 75;
		return new VelocityVector(dir, s);
	}

	public Point2d getInitialLocation() {
		return initLocation;
	}
	
	public World() {
		waypoints = new LinkedList<Vector2d>();
		addwp(0, 0);
		addwp(300, 300);
		addwp(300, 100);
		addwp(600, 400);
		addwp(0, 350);
		addwp(100, 400);
		addwp(0, 480);
	}
	
	private void addwp(int x, int y) {
		waypoints.add(new Vector2d(x, y));
	}
	
	public List<Vector2d> waypoints;
}
