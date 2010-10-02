package com.avona.games.towerdefence;

import java.util.LinkedList;
import java.util.List;

import com.avona.games.towerdefence.tower.Tower;

public class EventDistributor implements EventListener {
	private static final long serialVersionUID = 1L;

	public List<EventListener> listeners = new LinkedList<EventListener>();

	@Override
	public void onBuildTower(Tower t) {
		for (EventListener l : listeners)
			l.onBuildTower(t);
	}
}
