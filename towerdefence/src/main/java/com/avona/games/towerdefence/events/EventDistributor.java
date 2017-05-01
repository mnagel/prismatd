package com.avona.games.towerdefence.events;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

import java.util.LinkedList;
import java.util.List;

public class EventDistributor implements IEventListener {
	private static final long serialVersionUID = 1L;

	public List<IEventListener> listeners = new LinkedList<>();

	@Override
	public void onBuildTower(Tower t) {
		for (IEventListener l : listeners)
			l.onBuildTower(t);
	}

	@Override
	public void onMenuRebuild() {
		for (IEventListener l : listeners)
			l.onMenuRebuild();
	}

	@Override
	public void onMissionCompleted(Mission mission) {
		for (IEventListener l : listeners)
			l.onMissionCompleted(mission);
	}

	@Override
	public void onGameCompleted(Game g) {
		for (IEventListener l : listeners)
			l.onGameCompleted(g);
	}

	@Override
	public void onGameOver(Game g) {
		for (IEventListener l : listeners)
			l.onGameOver(g);
	}

	@Override
	public void onMissionSwitched(Mission mission) {
		for (IEventListener l : listeners)
			l.onMissionSwitched(mission);
	}
}
