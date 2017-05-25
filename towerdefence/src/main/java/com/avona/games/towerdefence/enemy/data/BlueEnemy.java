package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;

public class BlueEnemy extends Enemy {
	private static final long serialVersionUID = 3102974967310386039L;

	public BlueEnemy(int level) {
		super(
				level,
				40 + level,
				new RGB(0, 0, 50.0f * (float) Math.pow(1.3, level)),
				80 + 3 * (float) Math.pow(1.1, level)
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
