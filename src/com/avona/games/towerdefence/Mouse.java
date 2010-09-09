package com.avona.games.towerdefence;

import javax.vecmath.Point2d;

public class Mouse extends StationaryObject {
	public boolean onScreen = true;

	public Mouse() {
		radius = 7;
		location = new Point2d();
	}

	@Override
	public void step(double dt) {
		// TODO Auto-generated method stub
	}
}
