package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.mission.Mission;

public class BlueEnemy extends Enemy {
	private static final long serialVersionUID = 3102974967310386039L;

	public BlueEnemy(int level) {
		super(
				level,
				3 + (level - 1),
				new RGB(
						0,
						0,
						50 * level + 10
				),
				80 + 3 * (level - 1)
		);
	}

	public BlueEnemy(BlueEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new BlueEnemy(this);
	}
}
