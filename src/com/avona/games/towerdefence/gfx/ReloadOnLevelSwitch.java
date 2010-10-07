package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.EmptyEventListener;
import com.avona.games.towerdefence.level.Level;

public class ReloadOnLevelSwitch extends EmptyEventListener {

	private static final long serialVersionUID = -4165695670439622610L;

	private PortableGraphicsEngine ge;

	public ReloadOnLevelSwitch(PortableGraphicsEngine ge) {
		this.ge = ge;
	}

	@Override
	public void onLevelSwitched(Level level) {
		ge.freeLevelVertices();
		ge.freeMenuVertices();
		ge.freeOverlayVertices();
	}
}
