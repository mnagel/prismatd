package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;

public class WhiteEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public WhiteEnemy(int level) {
		super(
				level,
				40 + level,
				new RGB(50.0f * (float) Math.pow(1.3, level), 50.0f * (float) Math.pow(1.3, level), 50.0f * (float) Math.pow(1.3, level)),
				80 + 3 * (float) Math.pow(1.1, level)
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
