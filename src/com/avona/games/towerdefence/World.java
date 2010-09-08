package com.avona.games.towerdefence;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import java.util.Random;

public class World {
	public static Random rand = new java.util.Random();
	public Point2d initLocation = new Point2d();
	
	public VelocityVector getDirection(Point2d location) {
		double x = rand.nextDouble() - 0.5;
		double y = rand.nextDouble() - 0.5;
		Vector2d dir = new Vector2d(x, y);
		dir.normalize();
		double s = rand.nextDouble();
		return new VelocityVector(dir, s);
	}

	public Point2d getInitialLocation() {
		return initLocation;
	}
}
