package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;

public class YellowEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public YellowEnemy(int level) {
		super(
				level,
				40 + level,
				new RGB(20.0f * (float) Math.pow(1.3, level), 20.0f * (float) Math.pow(1.3, level), 0),
				140
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
