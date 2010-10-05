package com.avona.games.towerdefence;

import java.io.Serializable;

import com.avona.games.towerdefence.level.Level;
import com.avona.games.towerdefence.tower.Tower;

public interface EventListener extends Serializable {

	public void onBuildTower(Tower t);

	public void onLevelCompleted(Level l);

	public void onGameCompleted(Game g);
}
