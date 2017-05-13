package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.mission.Mission;

public class GreenEnemy extends Enemy {
	private static final long serialVersionUID = 3102974967310386039L;

	public GreenEnemy(Mission mission, int levelNum) {
		super(
				mission,
				levelNum,
				3 + (levelNum - 1),
				new RGB(
						0,
						50 * levelNum + 10,
						0
				),
				80 + 3 * (levelNum - 1)
		);
	}

	public GreenEnemy(GreenEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new GreenEnemy(this);
	}
}