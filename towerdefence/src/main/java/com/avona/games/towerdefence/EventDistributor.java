package com.avona.games.towerdefence;

import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

import java.util.LinkedList;
import java.util.List;

public class EventDistributor implements EventListener {
	private static final long serialVersionUID = 1L;

	public List<EventListener> listeners = new LinkedList<EventListener>();

	@Override
	public void onBuildTower(Tower t) {
		for (EventListener l : listeners)
			l.onBuildTower(t);
	}

	@Override
	public void onMissionCompleted(Mission mission) {
		for (EventListener l : listeners)
			l.onMissionCompleted(mission);
	}

	@Override
	public void onGameCompleted(Game g) {
		for (EventListener l : listeners)
			l.onGameCompleted(g);
	}

	@Override
	public void onGameOver(Game g) {
		for (EventListener l : listeners)
			l.onGameOver(g);
	}

	@Override
	public void onMissionSwitched(Mission mission) {
		for (EventListener l : listeners)
			l.onMissionSwitched(mission);
	}
}
