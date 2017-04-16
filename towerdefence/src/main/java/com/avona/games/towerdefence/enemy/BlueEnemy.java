package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.mission.Mission;

public class BlueEnemy extends Enemy {
	private static final long serialVersionUID = 3102974967310386039L;

	public BlueEnemy(Mission mission, int levelNum) {
		super(
				mission,
				levelNum,
				3 + (levelNum - 1),
				new RGB(
						0,
						0,
						50 * levelNum + 10
				),
				80 + 3 * (levelNum - 1), 12
		);
	}

	public BlueEnemy(BlueEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone() {
		return new BlueEnemy(this);
	}
}
