package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.mission.Mission;

public class RedEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public RedEnemy(Mission mission, int levelNum) {
		super(
				mission,
				levelNum,
				3 + (levelNum - 1),
				new RGB(
						50 * levelNum + 10,
						0,
						0
				),
				80 + 3 * (levelNum - 1)
		);
	}

	public RedEnemy(RedEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone() {
		return new RedEnemy(this);
	}
}
