package com.avona.games.towerdefence;

import java.util.LinkedList;
import java.util.List;

public class EventDistributor implements EventListener {

	public List<EventListener> listeners = new LinkedList<EventListener>();

	@Override
	public void onBuildTower(Tower t) {
		for (EventListener l : listeners)
			l.onBuildTower(t);
	}
}
