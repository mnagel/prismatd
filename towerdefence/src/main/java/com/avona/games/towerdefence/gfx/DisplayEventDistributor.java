package com.avona.games.towerdefence.gfx;

import java.util.LinkedList;
import java.util.List;

public class DisplayEventDistributor implements DisplayEventListener {
	private List<DisplayEventListener> listeners = new LinkedList<>();

	public void add(DisplayEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void onNewScreenContext() {
		for (DisplayEventListener l : listeners)
			l.onNewScreenContext();
	}

	@Override
	public void onReshapeScreen() {
		for (DisplayEventListener l : listeners)
			l.onReshapeScreen();
	}
}
