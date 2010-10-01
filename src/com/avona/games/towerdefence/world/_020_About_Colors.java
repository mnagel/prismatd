package com.avona.games.towerdefence.world;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Wave;

public class _020_About_Colors extends World {

	private static final long serialVersionUID = -2476503319147078452L;

	@Override
	public int getStartLifes() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 100;
	}

	@Override
	public void initWaypoints() {
		addWaypoint(500, 300);
		addWaypoint(500, 350);
		addWaypoint(30, 350);
		addWaypoint(30, 200);
		addWaypoint(300, 200);
		addWaypoint(300, 0);
		addWaypoint(30, 480);
		addWaypoint(30, 400);
		addWaypoint(600, 400);
		addWaypoint(600, 300);
	}

	@Override
	public Object listBuildableTowers() {
		return null;
	}

	@Override
	public void onWaveCompleted(int wave) {
		return;
	}

	@Override
	public Wave sendWave(int wave, Game g) {
		// FIXME need convenience constructor for wave
		// to allow for dynamic creation (set monster classes, set monster count, ...)
		// here...
		return new Wave(g, g.timedCodeManager, wave);
	}
}