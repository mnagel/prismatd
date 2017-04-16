package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.mission.Mission;

public class PurpleEnemy extends Enemy {

	private static final long serialVersionUID = 93877621275472018L;

	public PurpleEnemy(Mission mission, int levelNum) {
		super(
				mission,
				levelNum,
				3 + (levelNum - 1),
				new RGB(
						50 * levelNum + 10,
						0,
						50 * levelNum + 10
				),
				80 + 3 * (levelNum - 1),
				12
		);
	}

	public PurpleEnemy(PurpleEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone() {
		return new PurpleEnemy(this);
	}
}
