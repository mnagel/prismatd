package com.avona.games.towerdefence;

import com.avona.games.towerdefence.level.Level;
import com.avona.games.towerdefence.tower.Tower;

public class EmptyEventListener implements EventListener {

	private static final long serialVersionUID = -5784144234246757063L;

	@Override
	public void onBuildTower(Tower t) {
	}

	@Override
	public void onLevelCompleted(Level l) {
	}

	@Override
	public void onGameCompleted(Game g) {
	}

	@Override
	public void onGameOver(Game g) {
	}

	@Override
	public void onLevelSwitched(Level level) {
	}
}
