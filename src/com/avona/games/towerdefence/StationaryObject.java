package com.avona.games.towerdefence;

import javax.media.opengl.GLAutoDrawable;
import javax.vecmath.Point2d;


public abstract class StationaryObject {
	public Point2d location;

	public Point2d getLocation() {
		return location;
	}

	public void setLocation(Point2d location) {
		this.location = location;
	}
	
	abstract public void display(GLAutoDrawable glDrawable);
}
