package com.avona.games.towerdefence;

import javax.vecmath.Point2d;
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
}
