package com.avona.games.towerdefence;

import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

import java.io.Serializable;

public interface EventListener extends Serializable {

	public void onBuildTower(Tower t);

	public void onMissionSwitched(Mission mission);

	public void onMissionCompleted(Mission mission);

	public void onGameCompleted(Game g);

	public void onGameOver(Game g);
}
