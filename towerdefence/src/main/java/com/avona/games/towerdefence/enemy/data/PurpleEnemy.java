package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;

public class PurpleEnemy extends Enemy {

	private static final long serialVersionUID = 93877621275472018L;

	public PurpleEnemy(int level) {
		super(
				level,
				40 + level,
				new RGB(25.0f * (float) Math.pow(1.3, level), 0, 25.0f * (float) Math.pow(1.3, level)),
				80 + 3 * (float) Math.pow(1.1, level)
		);
	}

	public PurpleEnemy(PurpleEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new PurpleEnemy(this);
	}
}
