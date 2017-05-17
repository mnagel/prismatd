package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.mission.Mission;

public class RedEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public RedEnemy(int level) {
		super(
				level,
				3 + (level - 1),
				new RGB(
						50 * level + 10,
						0,
						0
				),
				80 + 3 * (level - 1)
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
