package com.avona.games.towerdefence.events;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

import java.io.Serializable;

public interface IEventListener extends Serializable {

	void onBuildTower(Tower t);

	void onMenuRebuild();

	void onMissionSwitched(Mission mission);

	void onMissionCompleted(Mission mission);

	void onGameCompleted(Game g);

	void onGameOver(Game g);
}
