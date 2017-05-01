package com.avona.games.towerdefence.events;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

public class EmptyEventListener implements IEventListener {

	private static final long serialVersionUID = -5784144234246757063L;

	@Override
	public void onBuildTower(Tower t) {
	}

	@Override
	public void onMenuRebuild() {
	}

	@Override
	public void onMissionCompleted(Mission l) {
	}

	@Override
	public void onGameCompleted(Game g) {
	}

	@Override
	public void onGameOver(Game g) {
	}

	@Override
	public void onMissionSwitched(Mission mission) {
	}
}
