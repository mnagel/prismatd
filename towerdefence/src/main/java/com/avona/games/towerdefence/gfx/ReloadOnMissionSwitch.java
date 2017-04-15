package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.EmptyEventListener;
import com.avona.games.towerdefence.mission.Mission;

public class ReloadOnMissionSwitch extends EmptyEventListener {

	private static final long serialVersionUID = -4165695670439622610L;

	private PortableGraphicsEngine ge;

	public ReloadOnMissionSwitch(PortableGraphicsEngine ge) {
		this.ge = ge;
	}

	@Override
	public void onMissionSwitched(Mission mission) {
		ge.freeMissionVertices();
		ge.freeMenuVertices();
	}
}
