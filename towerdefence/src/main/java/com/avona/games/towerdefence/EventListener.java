package com.avona.games.towerdefence;

import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

import java.io.Serializable;

public interface EventListener extends Serializable {

	void onBuildTower(Tower t);

	void onMissionSwitched(Mission mission);

	void onMissionCompleted(Mission mission);

	void onGameCompleted(Game g);

	void onGameOver(Game g);
}
