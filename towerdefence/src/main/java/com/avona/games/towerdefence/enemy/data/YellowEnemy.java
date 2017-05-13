package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.mission.Mission;

public class YellowEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public YellowEnemy(Mission mission, int levelNum) {
		super(
				mission,
				levelNum,
				3 + (levelNum - 1),
				new RGB(
						1 / 2.0f * 50 * levelNum + 10,
						1 / 2.0f * 50 * levelNum + 10,
						0
				),
				2 * (80 + 3 * (levelNum - 1))
		);
	}

	public YellowEnemy(YellowEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new YellowEnemy(this);
	}
}