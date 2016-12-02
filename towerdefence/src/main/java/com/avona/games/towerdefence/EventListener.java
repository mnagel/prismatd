package com.avona.games.towerdefence;

import java.io.Serializable;

import com.avona.games.towerdefence.level.Level;
import com.avona.games.towerdefence.tower.Tower;

public interface EventListener extends Serializable {

	public void onBuildTower(Tower t);

	public void onLevelSwitched(Level level);

	public void onLevelCompleted(Level level);

	public void onGameCompleted(Game g);

	public void onGameOver(Game g);
}
