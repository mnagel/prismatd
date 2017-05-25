package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;

public class RedEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public RedEnemy(int level) {
		super(
				level,
				40 + level,
				new RGB(50.0f * (float) Math.pow(1.3, level), 0, 0),
				80 + 3 * (float) Math.pow(1.1, level)
		);
	}

	public RedEnemy(RedEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new RedEnemy(this);
	}
}
