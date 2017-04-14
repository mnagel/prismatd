package com.avona.games.towerdefence;

import java.io.Serializable;

import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

public interface EventListener extends Serializable {

	public void onBuildTower(Tower t);

	public void onMissionSwitched(Mission mission);

	public void onMissionCompleted(Mission mission);

	public void onGameCompleted(Game g);

	public void onGameOver(Game g);
}
