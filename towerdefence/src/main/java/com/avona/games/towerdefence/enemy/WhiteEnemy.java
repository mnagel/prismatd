package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.mission.Mission;

public class WhiteEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public WhiteEnemy(Mission mission, int levelNum) {
		super(
				mission,
				levelNum,
				3 + (levelNum - 1),
				new RGB(
						5 * 50 * levelNum + 10,
						5 * 50 * levelNum + 10,
						5 * 50 * levelNum + 10
				),
				(80 + 3 * (levelNum - 1)) / 3
		);
	}

	public WhiteEnemy(WhiteEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new WhiteEnemy(this);
	}
}
